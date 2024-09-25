package edu.byuh.cis.cs300.grid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Custom view that contains and draws a grid along with interactive buttons.
 */
public class GridView extends View {

    private GridLines grid;
    private GridButton gridButton;
    private boolean firstRun;

    public GridView(Context context) {
        super(context);
        firstRun = true;
    }

    /**
     * Draws the grid and buttons on the canvas.
     * @param c the Canvas object used to draw the grid and buttons.
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawColor(Color.WHITE);

        if (firstRun) {
            //initialize grid and buttons on first draw
            float w = c.getWidth();
            float unit = w / 16f;
            float gridX = unit * 2.5f;
            float cellSize = unit * 2.3f;
            float gridY = unit * 9;

            grid = new GridLines(gridX, gridY, cellSize);
            gridButton = new GridButton(getResources(), gridX, gridY, cellSize);

            firstRun = false;
        }
        grid.draw(c); //draw grid
        gridButton.draw(c); //draw buttons
    }
}
