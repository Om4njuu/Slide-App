package edu.byuh.cis.cs300.grid.ui;

import java.util.stream.IntStream;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * This class is responsible for drawing a 5x5 grid on the screen.
 */
public class GridLines {

    private final int dim = 5;
    private float lineWidth;
    private Paint paint;
    private RectF bounds;
    private float cellWidth;

    /**
     * initializes the grid
     * @param x the leftmost x coordinate
     * @param y the topmost y coordinate
     * @param cellWidth how wide each cell should be
     * @param lineWidth how thick the lines should be
     * @param color the color of the lines
     * @param style the style of the lines
     * @param dim the number of cells in each row and column
     * @param bounds the bounding box of the grid
     * @param paint the Paint object used to draw the lines
     */
    public GridLines(float x, float y, float cellWidth) {
        this.cellWidth = cellWidth;
        lineWidth = cellWidth/20;
        bounds = new RectF(x, y, x+cellWidth*dim, y+cellWidth*dim);
        paint = new Paint();
        paint.setStrokeWidth(lineWidth);
        paint.setColor(Color.WHITE);
        paint.setStyle(Style.STROKE);
    }

    /**
     * check if the user tapped anywhere inside the grid
     * @param x the x position of the user's finger
     * @param y the y position of the user's finger
     * @return true if tapped inside; otherwise false
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    /**
     * simple "getter" that returns the topmost y coordinate of the grid
     * @return
     */
    public float getTop() {
        return bounds.top;
    }

    /**
     * draw the grid onto the screen
     * @param c the Canvas object, provided by the View
     */
    public void draw(Canvas c) {
        IntStream.rangeClosed(0, dim).forEach(i -> {
            c.drawLine(bounds.left, bounds.top + cellWidth * i, bounds.right, bounds.top + cellWidth * i, paint);
            c.drawLine(bounds.left + cellWidth * i, bounds.top, bounds.left + cellWidth * i, bounds.bottom, paint);
        });
    }
}

