public class Animation implements Action{
    private final Animated entity;
    private final int repeatCount;

    public Animation(
            Animated entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        this.executeAnimationAction(scheduler);
    }

    public void executeAnimationAction(
            EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity,
            Factory.createAnimationAction(entity,
                                                Math.max(repeatCount - 1,
                                                         0)),
                                                         entity.getAnimationPeriod());
        }
    }
}
