import java.util.List;

import processing.core.PImage;

abstract class Animated extends Actionable{

    protected int animationPeriod;

    public Animated(Point position, List<PImage> images, String id, int actionPeriod, int animationPeriod)
    {
        super(position, images, id, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    @Override
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        
            scheduler.scheduleEvent(this,
            Factory.createActivityAction(this, world, imageStore),
                              actionPeriod);
                              scheduler.scheduleEvent(this,
                              Factory.createAnimationAction(this, this._repeatCount()),
                getAnimationPeriod());
    }

    protected abstract int _repeatCount();
}
