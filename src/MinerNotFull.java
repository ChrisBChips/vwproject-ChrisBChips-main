import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

public class MinerNotFull extends Movable{
    private final int resourceLimit;
    private int resourceCount;

    public MinerNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int actionPeriod,
            int animationPeriod)
    {
        super(position, images, id, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.resourceCount = 0;
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            MinerFull miner = Factory.createMinerFull(this.id, this.resourceLimit,
                                           this.position, this.actionPeriod,
                                           this.animationPeriod,
                                           this.images);

                                           world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
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
            resourceCount += 1;
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
        Optional<Entity> notFullTarget =
        world.findNearest(this.position, new Ore(BLOB_ID_SUFFIX, position, images, BLOB_ANIMATION_MAX));

        if (!notFullTarget.isPresent() || !moveTo(world,
                                                         notFullTarget.get(),
                                                         scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
            Factory.createActivityAction(this, world, imageStore),
                          this.actionPeriod);
        }
    }



    protected int _repeatCount() { return 0; }



}
