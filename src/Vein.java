import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Vein extends Actionable{

    public Vein(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(position, images, id, actionPeriod);
    }


    //Actionable
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.position);

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                                   ORE_CORRUPT_MIN + rand.nextInt(
                                           ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                                           imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
        Factory.createActivityAction(this, world, imageStore),
                          this.actionPeriod);
    }
}
