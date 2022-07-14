import java.util.List;

import processing.core.PImage;

public class Obstacle extends Entity{

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
        super(position, images, id);
    }
}
