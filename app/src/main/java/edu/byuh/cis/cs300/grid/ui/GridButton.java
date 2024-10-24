package edu.byuh.cis.cs300.grid.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import edu.byuh.cis.cs300.grid.R;

public class GridButton {

    private RectF bounds;
    private Bitmap unpressedButton, pressedButton;
    private char label;
    private boolean pressed;

    /**
     * Constructor for GridButton. Initializes button attributes such as label, bounds,
     * and bitmaps for pressed and unpressed states.
     * 
     * @param name   the character label of the button
     * @param parent the parent view to fetch resources from
     * @param x      the x-coordinate of the button's top-left corner
     * @param y      the y-coordinate of the button's top-left corner
     * @param width  the width and height of the square button
     */
    public GridButton(char name, View parent, float x, float y, float width) {
        label = name;
        bounds = new RectF(x, y, x+width, y+width);
        unpressedButton = BitmapFactory.decodeResource(parent.getResources(), R.drawable.unpressed_button);
        unpressedButton = Bitmap.createScaledBitmap(unpressedButton, (int)width, (int)width, true);
        pressedButton = BitmapFactory.decodeResource(parent.getResources(), R.drawable.pressed_button);
        pressedButton = Bitmap.createScaledBitmap(pressedButton, (int)width, (int)width, true);
        pressed = false;
    }

    /**
     * Draw the button at its correct place on the screen.
     * 
     * @param c the Canvas object to draw to
     */
    public void draw(Canvas c) {
        if (pressed) {
            c.drawBitmap(pressedButton, bounds.left, bounds.top, null);
        } else {
            c.drawBitmap(unpressedButton, bounds.left, bounds.top, null);
        }
    }

    /**
     * Tests whether or not a given (x, y) point is inside the button or not.
     * 
     * @param x the x-coordinate to test against the button bounds
     * @param y the y-coordinate to test against the button bounds
     * @return true if (x, y) is inside the bounds; false otherwise
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    /**
     * Change the button to a "pressed" state.
     */
    public void press() {
        pressed = true;
    }

    /**
     * Change the button to an "unpressed" state.
     */
    public void release() {
        pressed = false;
    }

    /**
     * Getter method for the button's label.
     * 
     * @return the character label of the button
     */
    public char getLabel() {
        return label;
    }

    /**
     * Determines if the button is located in the top row.
     * 
     * @return true if the button is on the top row; false otherwise
     */
    public boolean isTopButton() {
        return (label >= '1' && label <= '5');
    }

    /**
     * Determines if the button is located in the left column.
     * 
     * @return true if the button is on the left column; false otherwise
     */
    public boolean isLeftButton() {
        return (label >= 'A' && label <= 'E');
    }

    /**
     * Getter for the current dimensions of the button.
     * 
     * @return a RectF object representing the button's bounds
     */
    public RectF getBounds() {
        return bounds;
    }
}
