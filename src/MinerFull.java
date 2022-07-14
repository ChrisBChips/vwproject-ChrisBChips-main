import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

public class MinerFull extends Movable {
    private final int resourceLimit;
    
    public MinerFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, id, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        MinerNotFull miner = Factory.createMinerNotFull(this.id, this.resourceLimit,
                                          this.position, this.actionPeriod,
                                          this.animationPeriod,
                                          this.images);

                                          world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }










    //Movable
    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        Predicate<Point> passThrough = (Point) -> !world.isOccupied(Point) && world.withinBounds(Point);
        BiPredicate<Point, Point> withinReach = (one, two) -> (one.adjacent(two));
        PathingStrategy path = new AStarPathingStrategy();
        List<Point> pointList = path.computePath(this.position, destPos, passThrough, withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
        return pointList.size() == 0 ? this.position : pointList.get(0);
    }

    /*
    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        int vert = Integer.signum(destPos.y - this.position.y);

        Point newPos = new Point(this.position.x + horiz, this.position.y + vert);

        if (world.isOccupied(newPos)) {

            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = new Point(this.position.x + horiz, this.position.y);
                if (horiz == 0 || world.isOccupied(newPos))
                {
                    newPos = this.position;
                }
            }

        }

        return newPos;
    }

    */

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (position.adjacent(target.getPosition())) {
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
        Optional<Entity> fullTarget =
        world.findNearest(this.position, new Blacksmith(BLOB_ID_SUFFIX, position, images));

        if (fullTarget.isPresent() && moveTo(world,
                                                 fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                          Factory.createActivityAction(this, world, imageStore),
                          this.actionPeriod);
        }
    }

    protected int _repeatCount() { return 0; }
}
