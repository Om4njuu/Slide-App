package edu.byuh.cis.cs300.grid.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byuh.cis.cs300.grid.logic.GameEngine;
import edu.byuh.cis.cs300.grid.logic.GameHandler;
import edu.byuh.cis.cs300.grid.logic.GameMode;
import edu.byuh.cis.cs300.grid.logic.Player;
import edu.byuh.cis.cs300.grid.Prefs;
import edu.byuh.cis.cs300.grid.logic.TickListener;
import edu.byuh.cis.cs300.grid.R;

public class GridView extends AppCompatImageView implements TickListener {

    private GameMode gameMode;
    private GridLines grid;
    private boolean firstRun;
    private GridButton[] buttons;
    private List<GuiToken> tokens;
    private GameEngine engine;
    private GameHandler gameHandler;
    private MediaPlayer song;
    private boolean spaceDir;
    private MediaPlayer fallSound;
    private MediaPlayer slideSound;
    private MediaPlayer buttonSound;
    static int[] backgrounds = new int[] {0,1,2,3,4,5};


    /**
     * Pauses the background music if music is enabled.
     */
    public void pauseMusic() {
        if (Prefs.getMusicPref(getContext())) {
            song.pause();
        }
    }

    /**
     * Resumes the background music if music is enabled in preferences.
     */
    public void resumeMusic() {
        if (Prefs.getMusicPref(getContext())) {
            song.start();
        }
    }

    /**
     * Unloads the background music, releasing all associated resources.
     */
    public void unloadMusic() {
        song.release();
    }

    /**
     * Constructor for GridView
     * @param context The context of the application
     * @param gameMode The game mode (ONE_PLAYER or TWO_PLAYER)
     */
    public GridView(Context context, GameMode gameMode) {
        super(context);
        this.gameMode = gameMode;
        firstRun = true;
        buttons = new GridButton[10];
        tokens = new ArrayList<>();
        engine = new GameEngine();
        gameHandler = new GameHandler();
        gameHandler.registerListener(this);
        song = MediaPlayer.create(getContext(), R.raw.space_music);
        fallSound = MediaPlayer.create(getContext(), R.raw.space_fall);
        slideSound = MediaPlayer.create(getContext(), R.raw.space_slide);
        buttonSound = MediaPlayer.create(getContext(), R.raw.space_button);
        song.setLooping(true);
        setImageResource(R.drawable.bg1);
        setScaleType(ScaleType.FIT_XY);
        backgrounds[0] = R.drawable.bg1;
        backgrounds[1] = R.drawable.bg2;
        backgrounds[2] = R.drawable.bg3;
        backgrounds[3] = R.drawable.bg4;
        backgrounds[4] = R.drawable.bg5;
        backgrounds[5] = R.drawable.bg6;
        if (Prefs.getMusicPref(context)) {
            song.start();
        }
        //showGameModeDialog();
    }

    /**
     * Draw the grid and tokens on the canvas
     * @param c The Canvas object supplied by onDraw
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
//        c.drawColor(Color.WHITE);
        if (firstRun) {
            init();
            firstRun = false;
        }

        grid.draw(c);

        float screenHeight = getHeight();
        tokens.removeIf(token -> {
            if (token.isInvisible(screenHeight)) {
                token.remove();
                gameHandler.deregisterListener(token);
                fallSound.start();
                return true;
            }
            return false;
        });

        tokens.forEach(token -> token.draw(c));
        Arrays.stream(buttons).forEach(button -> button.draw(c));
    }

    /**
     * Handles touch events on the GridView.
     *
     * @param m the MotionEvent object containing full information about the event.
     * @return true if the event was handled, false otherwise.
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
                        buttonSound.start();
                        GuiToken token = new GuiToken(engine.getCurrentPlayer(), b, getResources());
                        engine.submitMove(b.getLabel());
                        tokens.add(token);
                        gameHandler.registerListener(token);
                        setupAnimation(b, token);
                        missed = false;

                        Player winner = engine.checkForWin();
                        if (winner != Player.BLANK) {
                            showWinnerDialog(winner);
                        } else {
                            handleComputerTurn();
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
     * Handles the computer's turn in a one-player game mode. This method is invoked
     * when it's the computer's turn to play as Player O. It calculates the computer's
     * move, animates the move, and checks for a winner.
     */
    private void handleComputerTurn() {
        if (gameMode == GameMode.ONE_PLAYER && engine.getCurrentPlayer() == Player.O) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000); //delay to simulate thinking time
                    char suggestedMove = engine.suggestNextMove();
                    GridButton selectedButton = null;
                    for (GridButton button : buttons) {
                        if (button.getLabel() == suggestedMove) {
                            selectedButton = button;
                            break;
                        }
                    }
                    if (selectedButton != null) {
                        GridButton finalSelectedButton = selectedButton;
                        post(() -> {
                            finalSelectedButton.press();
                            buttonSound.start();
                            GuiToken token = new GuiToken(engine.getCurrentPlayer(), finalSelectedButton, getResources());
                            engine.submitMove(finalSelectedButton.getLabel());
                            tokens.add(token);
                            gameHandler.registerListener(token);
                            setupAnimation(finalSelectedButton, token);
                            Player winner = engine.checkForWin();
                            if (winner != Player.BLANK) {
                                showWinnerDialog(winner);
                            } else {
                                postDelayed(() -> finalSelectedButton.release(), 200); // delay before releasing the button
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
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

        //customize the message based on the winner
        String message;
        if (winner == Player.O) {
            message = "Sun wins! Play again?";
        } else if (winner == Player.X) {
            message = "Moon wins! Play again?";
        } else {
            message = "It's a Tie! Play again?";
        }

        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                engine.clear();
                tokens.clear();
                engine.setCurrentPlayer(Player.X);
                setImageResource(backgrounds[(int) (Math.random() * 6)]);
                setScaleType(ScaleType.FIT_XY);
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
     * This method is called when the view is first created
     * and sets up the grid and buttons for the game
     * board.
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
                slideSound.start();
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
                slideSound.start();
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