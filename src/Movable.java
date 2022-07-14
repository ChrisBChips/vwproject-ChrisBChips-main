import java.util.List;

import processing.core.PImage;

public abstract class Movable extends Animated {

    public Movable(Point position,
    List<PImage> images,
    String id,
    int actionPeriod,
    int animationPeriod)
    {
        super(position, images, id, actionPeriod, animationPeriod);
    }
    public abstract Point nextPosition(WorldModel world, Point destPos);
    public abstract boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);
}
