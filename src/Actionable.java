import java.util.List;

import processing.core.PImage;

abstract class Actionable extends Entity {
    
    protected final int actionPeriod;

    public Actionable(Point position, List<PImage> images, String id, int actionPeriod)
    {
        super(position, images, id);
        this.actionPeriod = actionPeriod;
    }

    public abstract void executeActivity(WorldModel w, ImageStore i, EventScheduler e);
    
    public void scheduleActions(EventScheduler scheduler, 
                    WorldModel world,
                    ImageStore imageStore)
    {
            scheduler.scheduleEvent(this,
            Factory.createActivityAction(this, world, imageStore),
                              actionPeriod);
    }
}
