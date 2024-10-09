package edu.byuh.cis.cs300.grid.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

import edu.byuh.cis.cs300.grid.logic.Player;

/**
 * Class representing a graphical token, either X or O.
 */
public class GuiToken {
    private Bitmap image;
    private RectF bounds;
    private float velocityX;
    private float velocityY;
    private float targetX;
    private float targetY;
    private Player player;

    /**
     * Constructor for creating a token (either X or O).
     *
     * @param image  the Bitmap image for the token (X or O).
     * @param bounds the bounding rectangle for the token.
     * @param player the player owning the token (X or O).
     */
    public GuiToken(Bitmap image, RectF bounds, Player player) {
        this.image = image;
        this.bounds = bounds;
        this.player = player;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Draw the token on the canvas.
     *
     * @param c the Canvas object used for drawing.
     */
    public void draw(Canvas c) {
        c.drawBitmap(image, bounds.left, bounds.top, null);
    }

    /**
     * Set the target position (x, y) for the token to move towards.
     *
     * @param targetX the x-coordinate of the target destination.
     * @param targetY the y-coordinate of the target destination.
     */
    public void setTarget(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * Get the x-coordinate of the target destination cell.
     *
     * @return the x-coordinate of the target.
     */
    public float getTargetX() {
        return targetX;
    }

    /**
     * Get the y-coordinate of the target destination cell.
     *
     * @return the y-coordinate of the target.
     */
    public float getTargetY() {
        return targetY;
    }

    /**
     * Set the velocity for the token.
     *
     * @param velocityX the x-velocity of the token.
     * @param velocityY the y-velocity of the token.
     */
    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /**
     * Move the token by offsetting its bounds based on its velocity.
     * If the token reaches its target destination, stop its movement.
     */
    public void move() {
        //offset the bounds by velocity
        bounds.offset(velocityX, velocityY);

        //check if the token is close enough to the target
        if (Math.abs(bounds.left - targetX) < 5 && Math.abs(bounds.top - targetY) < 5) {
            //snap the token to the target position and stop movement
            bounds.offsetTo(targetX, targetY);
            velocityX = 0;
            velocityY = 0;
        }
    }
}