package edu.byuh.cis.cs300.grid.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byuh.cis.cs300.grid.R;
import edu.byuh.cis.cs300.grid.logic.Player;
import edu.byuh.cis.cs300.grid.logic.TickListener;

/**
 * This class represents a token in a grid-based game. It can be moved and drawn on a canvas.
 */
public class GuiToken implements TickListener {
    private Player player;
    private RectF bounds;
    private PointF velocity;
    private PointF destination;
    private float tolerance;
    private Bitmap image;
    GridPosition position;
    private static int movers = 0;

    /**
     * Nested class representing the grid position of the token.
     */
    public static class GridPosition {
        public char row;
        public char column;

        /**
         * Constructor to create a GridPosition with a specific row and column.
         *
         * @param row    the row character
         * @param column the column character
         */
        public GridPosition(char row, char column) {
            this.row = row;
            this.column = column;
        }
    }

    /**
     * Constructor for the GuiToken.
     *
     * @param p      the player (either X or O)
     * @param parent the GridButton that the token belongs to
     * @param res    the resources used to load images
     */
    public GuiToken(Player p, GridButton parent, Resources res) {
        this.bounds = new RectF(parent.getBounds());
        velocity = new PointF();
        destination = new PointF();
        tolerance = bounds.height() / 10f;
        player = p;
        if (player == Player.X) {
            image = BitmapFactory.decodeResource(res, R.drawable.player_x);
        } else {
            image = BitmapFactory.decodeResource(res, R.drawable.player_o);
        }
        image = Bitmap.createScaledBitmap(image, (int) bounds.width(), (int) bounds.height(), true);
        if (parent.isTopButton()) {
            moveDown();
            position = new GridPosition((char) ('A' - 1), parent.getLabel());
        } else {
            moveRight();
            position = new GridPosition(parent.getLabel(), (char) ('1' - 1));
        }
    }

    /**
     * Draw the token on the provided canvas.
     *
     * @param c the canvas on which to draw the token
     */
    public void draw(Canvas c) {
        c.drawBitmap(image, bounds.left, bounds.top, null);
    }

    /**
     * Move the token towards its destination.
     */
    public void move() {
        if (velocity.x != 0 || velocity.y != 0) {
            float dx = destination.x - bounds.left;
            float dy = destination.y - bounds.top;
            if (PointF.length(dx, dy) < tolerance) {
                bounds.offsetTo(destination.x, destination.y);
                velocity.set(0, 0);
                movers--; // decrement when the token stops moving
            } else {
                bounds.offset(velocity.x, velocity.y);
            }
        }
    }

    /**
     * Check if any tokens are currently moving.
     *
     * @return true if any token is moving, false otherwise
     */
    public static boolean isAnyTokenMoving() {
        return movers > 0;
    }

    /**
     * Move the token downwards by setting its destination below its current position.
     */
    public void moveDown() {
        setGoal(bounds.left, bounds.top + bounds.height());
    }

    /**
     * Move the token to the right by setting its destination to the right of its current position.
     */
    public void moveRight() {
        setGoal(bounds.left + bounds.width(), bounds.top);
    }

    /**
     * Check if the token is currently moving.
     *
     * @return true if the token is moving, false otherwise
     */
    public boolean isMoving() {
        return (velocity.x > 0 || velocity.y > 0);
    }

    /**
     * Set the goal (destination) for the token's movement.
     *
     * @param x the x-coordinate of the destination
     * @param y the y-coordinate of the destination
     */
    private void setGoal(float x, float y) {
        destination.set(x, y);
        float dx = destination.x - bounds.left;
        float dy = destination.y - bounds.top;
        if (velocity.x == 0 && velocity.y == 0) {
            movers++; // increment when the token starts moving
        }
        velocity.x = dx / 5f;
        velocity.y = dy / 5f;
    }

    /**
     * Called every tick of the game loop, causing the token to move if necessary.
     */
    @Override
    public void onTick() {
        move();
    }
}
