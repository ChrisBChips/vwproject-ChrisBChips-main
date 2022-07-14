import java.util.List;
import processing.core.PImage;

public final class Background
{
    private String id;
    private List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }

    public String getId() {
        return id;
    }

    public void setBackground(
            WorldModel world, Point pos)
    {
        if (world.withinBounds(pos)) {
            world.setBackgroundCell(pos, this);
        }
    }

    public PImage getCurrentBackgroundImage() 
    {
        return (images.get(imageIndex));
    }
}
