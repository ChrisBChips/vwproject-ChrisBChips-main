import java.util.List;

import processing.core.PImage;

public class Blacksmith extends Entity{

    public Blacksmith(
            String id,
            Point position,
            List<PImage> images)
    {
        super(position, images, id);
    }
}
