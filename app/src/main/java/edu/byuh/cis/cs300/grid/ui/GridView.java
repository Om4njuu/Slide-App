package edu.byuh.cis.cs300.grid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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

    /**
     * Handle touch events for pressing and releasing buttons.
     * @param event the MotionEvent object containing touch information.
     * @return true to indicate that the touch event was handled.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                boolean buttonPressed = false;

                //loop all buttons to see if one is touched
                for (GridButton btn : gridButton.buttons) {
                    if (btn.contains(x, y)) {
                        //if button is pressed, release all others and press this one
                        for (GridButton b : gridButton.buttons) {
                            b.release();
                        }
                        btn.press();
                        invalidate(); // Redraw the view
                        buttonPressed = true;
                        break;
                    }
                }
                //if no button was pressed or pressed somewhere else on screen, show a toast message
                if (!buttonPressed) {
                    Toast.makeText(getContext(), "Touch the button", Toast.LENGTH_SHORT).show();
                }
                break;

            case MotionEvent.ACTION_UP:
                //release all buttons when the user lifts the finger
                for (GridButton btn : gridButton.buttons) {
                    btn.release();
                }
                invalidate(); //redraw the view
                break;
        }
        return true;
    }
}
