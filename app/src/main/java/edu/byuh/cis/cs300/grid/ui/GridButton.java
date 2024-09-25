package edu.byuh.cis.cs300.grid.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.ArrayList;

import edu.byuh.cis.cs300.grid.R;

/**
 * A class that represents a button on the grid. Each button has a label and can be drawn on the screen.
 */
public class GridButton {
    private Bitmap img;
    private RectF bounds;
    private char label;
    private ArrayList<GridButton> buttons;

    /**
     * Constructor for creating buttons along the grid's border.
     * @param res the Resources object to load images.
     * @param gridX the x coordinate of the grid.
     * @param gridY the y coordinate of the grid.
     * @param cellSize the size of each button.
     */
    public GridButton(Resources res, float gridX, float gridY, float cellSize) {
        buttons = new ArrayList<>();
        img = BitmapFactory.decodeResource(res, R.drawable.unpressed_button);
        img = Bitmap.createScaledBitmap(img, (int)cellSize, (int)cellSize, true);

        //Create buttons for numeric and alphabetic labels.
        for (int i = 0; i < 5; i++) {
            buttons.add(new GridButton((char)('1' + i), gridX + cellSize * i, gridY - cellSize, cellSize, res));
        }

        for (int i = 0; i < 5; i++) {
            buttons.add(new GridButton((char)('A' + i), gridX - cellSize, gridY + cellSize * i, cellSize, res));
        }
    }

    /**
     * Constructor for creating individual buttons.
     * @param label the label of the button (either a number or letter).
     * @param x the x coordinate for the button.
     * @param y the y coordinate for the button.
     * @param size the size of the button.
     * @param res the Resources object to load the image.
     */
    private GridButton(char label, float x, float y, float size, Resources res) {
        this.label = label;
        this.bounds = new RectF(x, y, x + size, y + size);
        this.img = BitmapFactory.decodeResource(res, R.drawable.unpressed_button);
        this.img = Bitmap.createScaledBitmap(this.img, (int)size, (int)size, false);
    }

    /**
     * Draws the buttons onto the canvas.
     * @param c the Canvas object used to draw the buttons.
     */
    public void draw(Canvas c) {
        for (GridButton btn : buttons) {
            c.drawBitmap(btn.img, btn.bounds.left, btn.bounds.top, null);
        }
    }

    /**
     * Sets the location of the button.
     * @param x the new x coordinate.
     * @param y the new y coordinate.
     */
    public void setLocation(float x, float y) {
        bounds.offsetTo(x, y);
    }
}