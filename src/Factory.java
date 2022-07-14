import java.util.List;

import processing.core.PImage;

public final class Factory
{
    public final static int ORE_REACH = 1;

    public static final int PROPERTY_KEY = 0;

    public static final String BGND_KEY = "background";

    public static final String MINER_KEY = "miner";

    public static final String OBSTACLE_KEY = "obstacle";

    public static final String ORE_KEY = "ore";

    public static final String SMITH_KEY = "blacksmith";

    public static final String VEIN_KEY = "vein";
    
    public static Action createAnimationAction(Animated entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    public static Action createActivityAction(
            Actionable entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    public static Blacksmith createBlacksmith(
            String id, Point position, List<PImage> images)
    {
        return new Blacksmith(id, position, images);
    }

    public static MinerFull createMinerFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new MinerFull(id, position, images,
                          resourceLimit, actionPeriod,
                          animationPeriod);
    }

    public static MinerNotFull createMinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
                          resourceLimit, actionPeriod, animationPeriod);
    }

    public static Obstacle createObstacle(
            String id, Point position, List<PImage> images)
    {
        return new Obstacle(id, position, images);
    }

    public static Ore createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Ore(id, position, images, actionPeriod);
    }

    public static OreBlob createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new OreBlob(id, position, images,
                          actionPeriod, animationPeriod);
    }

    public static Quake createQuake(
            Point position, List<PImage> images, String id)
    {
        return new Quake(position, images, id, 1100, 100);
    }

    public static Vein createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }

    public static Buffed createBuffed(String id, Point position,  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Buffed(id, position, images, actionPeriod, animationPeriod);
    }

    public static Amogus createAmogus(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        return new Amogus(id, position, images, actionPeriod, animationPeriod);
    }
}