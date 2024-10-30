package edu.byuh.cis.cs300.grid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.byuh.cis.cs300.grid.logic.GameEngine;
import edu.byuh.cis.cs300.grid.logic.GameHandler;
import edu.byuh.cis.cs300.grid.logic.Player;
import edu.byuh.cis.cs300.grid.logic.TickListener;

public class GridView extends View implements TickListener {

    private GridLines grid;
    private boolean firstRun;
    private GridButton[] buttons;
    private List<GuiToken> tokens;
    private GameEngine engine;
    private GameHandler gameHandler;

    /**
     * Constructor for GridView
     * @param context The context of the application
     */
    public GridView(Context context) {
        super(context);
        firstRun = true;
        buttons = new GridButton[10];
        tokens = new ArrayList<>();
        engine = new GameEngine();
        gameHandler = new GameHandler();
        gameHandler.registerListener(this);
    }

    /**
     * Draw the grid and tokens on the canvas
     * @param c The Canvas object supplied by onDraw
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

        float screenHeight = getHeight();
        Iterator<GuiToken> iterator = tokens.iterator();
        while (iterator.hasNext()) {
            GuiToken token = iterator.next();
            if (token.isInvisible(screenHeight)) {
                token.remove();
                iterator.remove();
                gameHandler.deregisterListener(token);
            } else {
                token.draw(c);
            }
        }

        for (GridButton button : buttons) {
            button.draw(c);
        }
    }

    /**
     * Handle touch events on the grid
     * @param m The MotionEvent object
     * @return true if the event was handled; false otherwise
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            if (!GuiToken.anyMovers()) {
                float x = m.getX();
                float y = m.getY();
                boolean missed = true;
                for (GridButton b : buttons) {
                    if (b.contains(x, y)) {
                        b.press();
                        GuiToken token = new GuiToken(engine.getCurrentPlayer(), b, getResources());
                        engine.submitMove(b.getLabel());
                        tokens.add(token);
                        gameHandler.registerListener(token);
                        setupAnimation(b, token);
                        missed = false;

                        Player winner = engine.checkForWin();
                        if (winner != Player.BLANK) {
                            showWinnerDialog(winner);
                        }
                    }
                }
                if (missed) {
                    Toast t = Toast.makeText(getContext(), "Please touch a button", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        } else if (m.getAction() == MotionEvent.ACTION_UP) {
            for (GridButton b : buttons) {
                b.release();
            }
        }
        return true;
    }

    /**
     * Called on each tick to update the grid view
     */
    @Override
    public void onTick() {
        invalidate();
    }

    /**
     * Show a dialog to announce the winner
     * @param winner The player who won the game
     */
    private void showWinnerDialog(Player winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("GAME IS DONE!");
    
        // Customize the message based on the winner
        String message;
        if (winner == Player.O) {
            message = "Padthai wins! Play again?";
        } else if (winner == Player.X) {
            message = "Thai Temple wins! Play again?";
        } else {
            message = "It's a Tie! Play again?";
        }
    
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                engine.clear();
                tokens.clear();
                invalidate();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            ((Activity) getContext()).finish();
        });
        builder.show();
    }

    /**
     * Initialize the grid and buttons
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
     * Setup the animation for the token
     * @param b The GridButton that was pressed
     * @param tok The GuiToken to animate
     */
    private void setupAnimation(GridButton b, GuiToken tok) {
        List<GuiToken> neighbors = new ArrayList<>();
        neighbors.add(tok);
        if (b.isTopButton()) {
            char col = b.getLabel();
            for (char row = 'A'; row <= 'E'; row++) {
                GuiToken other = findTokenAtPosition(row, col);
                if (other != null) {
                    neighbors.add(other);
                } else {
                    break;
                }
            }
            for (GuiToken token : neighbors) {
                token.startMovingDown();
            }
        } else {
            char row = b.getLabel();
            for (char col = '1'; col <= '5'; col++) {
                GuiToken other = findTokenAtPosition(row, col);
                if (other != null) {
                    neighbors.add(other);
                } else {
                    break;
                }
            }
            for (GuiToken token : neighbors) {
                token.startMovingRight();
            }
        }
    }

    /**
     * Find a token at the given grid position
     * @param row The row to check
     * @param col The column to check
     * @return The GuiToken at the given position, or null if none found
     */
    private GuiToken findTokenAtPosition(char row, char col) {
        for (GuiToken token : tokens) {
            if (token.matches(row, col)) {
                return token;
            }
        }
        return null;
    }
}