import java.util.List;
import java.util.Random;

import processing.core.PImage;

abstract class Entity
{
    protected final Random rand = new Random();

    protected static final String VEIN_KEY = "vein";

    protected final String ORE_ID_PREFIX = "ore -- ";
    protected final int ORE_CORRUPT_MIN = 9000;
    protected final int ORE_CORRUPT_MAX = 20000;
    protected final String ORE_KEY = "ore";

    protected final String BLOB_KEY = "blob";
    protected final String BLOB_ID_SUFFIX = " -- blob";
    protected final int BLOB_PERIOD_SCALE = 4;
    protected final int BLOB_ANIMATION_MIN = 50;
    protected final int BLOB_ANIMATION_MAX = 150;

    protected final String QUAKE_KEY = "quake";
    protected final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    protected Point position;
    protected final List<PImage> images;
    protected int imageIndex;
    protected String id;

    public Entity(Point position, List<PImage> images, String id)
    {
        this.position = position;
        this.images = images;
        this.id = id;
        this.imageIndex = 0;
    }
    
    public PImage getCurrentEntityImage()
    {
        return (images.get(imageIndex));
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point point)
    {
        this.position = point;
    }

}
