import java.util.Optional;

import processing.core.PApplet;
import processing.core.PImage;


public final class WorldView
{
    private final PApplet screen;
    private final WorldModel world;
    private final int tileWidth;
    private final int tileHeight;
    private final Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(viewport.getCol() + colDelta, 0,
                           this.world.getNumCols() - viewport.getNumCols());
        int newRow = clamp(viewport.getRow() + rowDelta, 0,
                           this.world.getNumRows() - viewport.getNumRows());

        this.viewport.shift(newCol, newRow);
    }

    public static void drawBackground(WorldView view) {
        for (int row = 0; row < view.viewport.getNumRows(); row++) {
            for (int col = 0; col < view.viewport.getNumCols(); col++) {
                Point worldPoint = view.viewport.viewportToWorld(col, row);
                Optional<PImage> image =
                        view.world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    view.screen.image(image.get(), col * view.tileWidth,
                                      row * view.tileHeight);
                }
            }
        }
    }

    public void drawEntities() {
        for (Entity entity : world.getEntities()) {
            Point pos = entity.getPosition();

            if (viewport.contains(pos)) {
                Point viewPoint = viewport.worldToViewport(pos.x, pos.y);
                screen.image(entity.getCurrentEntityImage(),
                                  viewPoint.x * tileWidth,
                                  viewPoint.y * tileHeight);
            }
        }
    }

    public void drawViewport() {
        drawBackground(this);
        this.drawEntities();
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public int getRow(){ return viewport.getRow(); }
    public int getCol(){ return viewport.getCol(); }
}
