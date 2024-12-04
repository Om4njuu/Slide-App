package edu.byuh.cis.cs300.grid.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs300.grid.Prefs;
import edu.byuh.cis.cs300.grid.R;
import edu.byuh.cis.cs300.grid.logic.Player;
import edu.byuh.cis.cs300.grid.logic.TickListener;

public class GuiToken implements TickListener {
    private Player player;
    private RectF bounds;
    private PointF velocity;
    private Bitmap image;
    private GridPosition gp;
    private static int movers = 0;
    private int stepCounter;
    private int slideSpeed;
    private boolean falling = false;
    private Context context;

    public class GridPosition {
        public char row;
        public char col;
    }

    /**
     * Create a new GuiToken object
     * @param p The Player (X or O) who created the token
     * @param parent which button was tapped to create the token
     * @param res the Resources object (used for loading image)
     */
    public GuiToken(Player p, GridButton parent, Resources res, Context context) {
        this.context = context;
        slideSpeed = Prefs.getSpeedPref(context);
        gp = new GridPosition();
        if (parent.isTopButton()) {
            gp.row = 'A' - 1;
            gp.col = parent.getLabel();
        } else {
            gp.row = parent.getLabel();
            gp.col = '1' - 1;
        }

        this.bounds = new RectF(parent.getBounds());
        velocity = new PointF();
        player = p;

        // Load images based on theme preference
        String theme = Prefs.getThemePref(context);
        if (theme.equals("Thai")) {
            if (player == Player.X) {
                image = BitmapFactory.decodeResource(res, R.drawable.player_x_th);
            } else {
                image = BitmapFactory.decodeResource(res, R.drawable.player_o_th);
            }
        } else if (theme.equals("Space")) {
            if (player == Player.X) {
                image = BitmapFactory.decodeResource(res, R.drawable.player_x);
            } else {
                image = BitmapFactory.decodeResource(res, R.drawable.player_o);
            }
        } else {
            if (player == Player.X) {
                image = BitmapFactory.decodeResource(res, R.drawable.player_x_nl);
            } else {
                image = BitmapFactory.decodeResource(res, R.drawable.player_o_nl);
            }
        }

        image = Bitmap.createScaledBitmap(image, (int)bounds.width(), (int)bounds.height(), true);
    }

    /**
     * Draw the token at the correct location, using the correct
     * image (X or O)
     * @param c The Canvas object supplied by onDraw
     */
    public void draw(Canvas c) {
        c.drawBitmap(image, bounds.left, bounds.top, null);
    }

    /**
     * Move the token by its current velocity.
     * Stop when it reaches its destination location.
     */
    public void move() {
        if (velocity.x != 0 || velocity.y != 0) {
            if (stepCounter >= slideSpeed) {
                if (gp.row > 'E' || gp.col > '5') {
                    velocity.set(0, 1);
                    falling = true;
                } else {
                    velocity.set(0, 0);
                    movers--;
                }
            } else {
                stepCounter++;
                bounds.offset(velocity.x, velocity.y);
            }
        }
        if (falling) {
            velocity.y *= 20;
            bounds.offset(velocity.x, velocity.y);
        }
    }

    /**
     * Helper method for tokens created by the top row of buttons
     */
    public void startMovingDown() {
        startMoving(0, bounds.width() / slideSpeed);
        gp.row++;
    }

    /**
     * Helper method for tokens created by the left column of buttons
     */
    public void startMovingRight() {
        startMoving(bounds.width() / slideSpeed, 0);
        gp.col++;
    }

    /**
     * Start moving the token with the given velocity
     * @param vx The velocity in the x direction
     * @param vy The velocity in the y direction
     */
    private void startMoving(float vx, float vy) {
        velocity.set(vx, vy);
        movers++;
        stepCounter = 0;
    }

    /**
     * Is animation currently happening?
     * @return true if the token is currently moving (i.e. has a non-zero velocity); false otherwise.
     */
    public boolean isMoving() {
        return (velocity.x > 0 || velocity.y > 0);
    }

     /**
     * Check if any tokens are currently moving
     * @return true if any tokens are moving; false otherwise
     */
    public static boolean anyMovers() {
        return movers > 0;
    }

    /**
     * Called on each tick to update the token's position
     */
    @Override
    public void onTick() {
        move();
    }

    /**
     * Check if the token matches the given grid position
     * @param row The row to check
     * @param col The column to check
     * @return true if the token matches the given position; false otherwise
     */
    public boolean matches(char row, char col) {
        return (gp.row == row && gp.col == col);
    }

    /**
     * Check if the token is off the screen
     * @param screenHeight The height of the screen
     * @return true if the token is off the screen; false otherwise
     */
    public boolean isInvisible(float screenHeight) {
        return bounds.top > screenHeight;
    }

    /**
     * Remove the token from the game
     */
    public void remove() {
        if (isMoving()) {
            movers--;
        }
    }
}