import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

public class Buffed extends Movable {
    
    public Buffed(
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
        Predicate<Point> passThrough = (Point) -> !world.isOccupied(Point) && world.withinBounds(Point);
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
        world.findNearest(this.position, new OreBlob(BLOB_ID_SUFFIX, position, images, 75, 75));

        if (fullTarget.isPresent() && moveTo(world,
                                                 fullTarget.get(), scheduler))
        {

            this.scheduleActions(scheduler, world, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                          Factory.createActivityAction(this, world, imageStore),
                          this.actionPeriod);
        }
    }

    protected int _repeatCount() { return 0; }
}
