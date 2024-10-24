package edu.byuh.cis.cs300.grid.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs300.grid.logic.GameEngine;
import edu.byuh.cis.cs300.grid.logic.GameHandler;
import edu.byuh.cis.cs300.grid.logic.TickListener;

/**
 * GridView represents the user interface for the game grid. It handles
 * the drawing of the grid and tokens, as well as user interactions like
 * pressing buttons and token movements.
 */
public class GridView extends View implements TickListener {

    private GridLines grid;
    private boolean firstRun;
    private GridButton[] buttons;
    private List<GuiToken> tokens;
    private GameEngine engine;
    private GameHandler handler;

    /**
     * Constructor for GridView.
     * 
     * @param context the application context
     */
    public GridView(Context context) {
        super(context);
        firstRun = true;
        buttons = new GridButton[10];
        tokens = new ArrayList<>();
        engine = new GameEngine();
        handler = new GameHandler();
        handler.registerListener(this);
    }

    /**
     * This method is called when the view is drawn on the screen.
     * It initializes the grid and buttons on the first run and redraws
     * the grid and tokens on each subsequent draw.
     *
     * @param c the Canvas on which the background will be drawn
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.drawColor(Color.WHITE);
        if (firstRun) {
            init();
            firstRun = false;
        }

        grid.draw(c);
        for (GuiToken token : tokens) {
            token.draw(c);
        }
        for (GridButton b : buttons) {
            b.draw(c);
        }
    }

    /**
     * Handles user touch events. It processes button presses,
     * moves tokens, and handles token movements.
     *
     * @param m the MotionEvent object containing full information about the event
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (GuiToken.isAnyTokenMoving()) {
            return true; // Ignore touch events if tokens are still moving
        }

        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            boolean missed = true;
            for (GridButton b : buttons) {
                if (b.contains(x, y)) {
                    b.press();
                    GuiToken token = new GuiToken(engine.getCurrentPlayer(), b, getResources());
                    engine.submitMove(b.getLabel());
                    tokens.add(token);
                    handler.registerListener(token);
                    handleTokenMovement(token, b);
                    missed = false;
                }
            }
            if (missed) {
                Toast t = Toast.makeText(getContext(), "Please touch a button", Toast.LENGTH_SHORT);
                t.show();
            }
        } else if (m.getAction() == MotionEvent.ACTION_UP) {
            for (GridButton b : buttons) {
                b.release();
            }
        }
        invalidate();
        return true;
    }

    /**
     * This method is called at regular intervals (ticks) to update the game state.
     * It forces the view to be redrawn on each tick.
     */
    @Override
    public void onTick() {
        invalidate();
    }

    /**
     * Initializes the grid layout and positions the buttons on the grid.
     * This method is only called during the first drawing of the view.
     */
    private void init() {
        float w = getWidth();
        float h = getHeight();
        float unit = w / 16f;
        float gridX = unit * 2.5f;
        float cellSize = unit * 2.3f;
        float gridY = unit * 9;
        grid = new GridLines(gridX, gridY, cellSize);
        float buttonTop = gridY - cellSize;
        float buttonLeft = gridX - cellSize;

        buttons[0] = new GridButton('1', this, buttonLeft + cellSize * 1, buttonTop, cellSize);
        buttons[1] = new GridButton('2', this, buttonLeft + cellSize * 2, buttonTop, cellSize);
        buttons[2] = new GridButton('3', this, buttonLeft + cellSize * 3, buttonTop, cellSize);
        buttons[3] = new GridButton('4', this, buttonLeft + cellSize * 4, buttonTop, cellSize);
        buttons[4] = new GridButton('5', this, buttonLeft + cellSize * 5, buttonTop, cellSize);

        buttons[5] = new GridButton('A', this, buttonLeft, buttonTop + cellSize * 1, cellSize);
        buttons[6] = new GridButton('B', this, buttonLeft, buttonTop + cellSize * 2, cellSize);
        buttons[7] = new GridButton('C', this, buttonLeft, buttonTop + cellSize * 3, cellSize);
        buttons[8] = new GridButton('D', this, buttonLeft, buttonTop + cellSize * 4, cellSize);
        buttons[9] = new GridButton('E', this, buttonLeft, buttonTop + cellSize * 5, cellSize);
    }

    /**
     * Handles the movement of tokens based on button presses. It checks
     * whether a button belongs to the top row or the left column and moves
     * the tokens accordingly.
     *
     * @param newToken the newly created token to be moved
     * @param button the button that was pressed to create the token
     */
    private void handleTokenMovement(GuiToken newToken, GridButton button) {
        List<GuiToken> neighbors = new ArrayList<>();
        neighbors.add(newToken);
        char col = button.getLabel();

        if (button.isTopButton()) {
            for (char row = 'A'; row <= 'E'; row++) {
                GuiToken token = findTokenAtPosition(row, col);
                if (token != null) {
                    neighbors.add(token);
                } else {
                    break;
                }
            }
            for (GuiToken token : neighbors) {
                token.moveDown();
                token.position.row++;
            }
        } else if (button.isLeftButton()) {
            for (char column = '1'; column <= '5'; column++) {
                GuiToken token = findTokenAtPosition(button.getLabel(), column);
                if (token != null) {
                    neighbors.add(token);
                } else {
                    break;
                }
            }
            for (GuiToken token : neighbors) {
                token.moveRight();
                token.position.column++;
            }
        }
    }

    /**
     * Searches for a token at a given grid position.
     *
     * @param row the row character (A-E) of the grid
     * @param column the column character (1-5) of the grid
     * @return the GridToken at the given position, or null if no token is found
     */
    private GuiToken findTokenAtPosition(char row, char column) {
        for (GuiToken token : tokens) {
            if (token.position.row == row && token.position.column == column) {
                return token;
            }
        }
        return null;
    }
}
