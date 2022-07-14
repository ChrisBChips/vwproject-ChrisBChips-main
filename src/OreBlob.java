import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

public class OreBlob extends Movable{


    public OreBlob(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, id, actionPeriod, animationPeriod);
    }


    //Movable
    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        /*
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.getPosition().x, this.position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get() instanceof Ore)))
            {
                newPos = this.position;
            }
        }

        return newPos;

    */
        Predicate<Point> passThrough = (Point) -> (!world.isOccupied(Point) || world.getOccupant(Point).get() instanceof Ore) && world.withinBounds(Point);
        BiPredicate<Point, Point> withinReach = (one, two) -> (one.adjacent(two));
        PathingStrategy path = new AStarPathingStrategy();
        List<Point> pointList = path.computePath(this.position, destPos, passThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        return pointList.size() == 0 ? this.position : pointList.get(0);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }



    //Actionable
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.position, new Vein(BLOB_ID_SUFFIX, position, images, BLOB_ANIMATION_MAX));
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveTo(world, blobTarget.get(), scheduler)) {
                Quake quake = Factory.createQuake(tgtPos,
                imageStore.getImageList(QUAKE_KEY), QUAKE_KEY);

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
        Factory.createActivityAction(this, world, imageStore),
                      nextPeriod);
    }

    protected int _repeatCount() { return 0; }
}
