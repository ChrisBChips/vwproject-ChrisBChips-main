import java.util.List;

import processing.core.PImage;

public class Quake extends Animated{


    public Quake(
            Point position,
            List<PImage> images,
            String id,
            int animationPeriod,
            int actionPeriod)
    {
        super(position, images, id, animationPeriod, actionPeriod);
    }






    //Actionable
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    protected int _repeatCount() { return QUAKE_ANIMATION_REPEAT_COUNT; }




}
