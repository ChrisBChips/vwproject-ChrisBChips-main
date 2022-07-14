import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private final int TIMER_ACTION_PERIOD = 100;

    private final int VIEW_WIDTH = 640;
    private final int VIEW_HEIGHT = 480;
    private final int TILE_WIDTH = 32;
    private final int TILE_HEIGHT = 32;
    private final int WORLD_WIDTH_SCALE = 2;
    private final int WORLD_HEIGHT_SCALE = 2;

    private final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private final String IMAGE_LIST_FILE_NAME = "imagelist";
    private final String DEFAULT_IMAGE_NAME = "background_default";
    private final int DEFAULT_IMAGE_COLOR = 0x808080;

    private final String LOAD_FILE_NAME = "world.sav";

    private final static String FAST_FLAG = "-fast";
    private final static String FASTER_FLAG = "-faster";
    private final static String FASTEST_FLAG = "-fastest";
    private final static double FAST_SCALE = 0.5;
    private final static double FASTER_SCALE = 0.25;
    private final static double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
        imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.load(in, world);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Actionable){
            Actionable actionable = (Actionable) entity;
            actionable.scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public void mousePressed()
    {
        Point pressed = mouseToPoint(mouseX, mouseY);
        for (int i = -2; i<= 2; i++){
            for (int j = -2; j<= 2; j++){

                // Visualization
                Point current = new Point(pressed.x+j, pressed.y+i);
                if (world.withinBounds(current)){
                    world.setBackgroundCell(current, new Background("floor", imageStore.getImageList("floor")));
                }
                
                // Effect
                if (world.isOccupied(current) && (world.getOccupant(current).get() instanceof MinerNotFull || 
                    world.getOccupant(current).get() instanceof MinerFull))
                {
                    Movable miner = (Movable) world.getOccupant(current).get();
                    Buffed buffed = Factory.createBuffed("buff", current, imageStore.getImageList("buff"), 813 , 100);
                    world.removeEntity(miner);
                    scheduler.unscheduleAllEvents(miner);
                    world.addEntity(buffed);
                    buffed.scheduleActions(scheduler, world, imageStore);
                }
            }
        }

        // New Entity
        if (world.isOccupied(pressed)){
            world.removeEntityAt(pressed);
        }
        Amogus amogus = Factory.createAmogus("amogus", pressed, imageStore.getImageList("amogus"), 1000, 100);
        world.addEntity(amogus);
        amogus.scheduleActions(scheduler, world, imageStore);

        redraw();
    }

   private Point mouseToPoint(int x, int y)
   {
      return new Point((x/TILE_WIDTH)+view.getCol(), (y/TILE_HEIGHT)+view.getRow());
   }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
