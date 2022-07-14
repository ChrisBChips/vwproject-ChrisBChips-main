public class Activity implements Action {
    private final Actionable entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(
            Actionable entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        this.executeActivityAction(scheduler);
    }

    public void executeActivityAction(
            EventScheduler scheduler)
    {
    
                entity.executeActivity(world,
                                         imageStore, scheduler);

    }
}
