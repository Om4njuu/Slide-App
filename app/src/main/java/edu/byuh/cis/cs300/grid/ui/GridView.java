package edu.byuh.cis.cs300.grid.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import edu.byuh.cis.cs300.grid.R;
import edu.byuh.cis.cs300.grid.logic.Player;

public class GridView extends View {

    private GridLines grid;
    private GridButton gridButton;
    private boolean firstRun;
    private ArrayList<GuiToken> tokens;
    private boolean isXTurn;
    private GameHandler handler;

    /**
     * Constructor for creating the GridView.
     *
     * @param context the context in which the view is being created.
     */
    public GridView(Context context) {
        super(context);
        firstRun = true;
        tokens = new ArrayList<>();
        isXTurn = true;
        handler = new GameHandler();
    }

    /**
     * Draws the grid, buttons, and tokens on the canvas.
     *
     * @param c the Canvas object used to draw the grid, buttons, and tokens.
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

        //draw the grid and buttons
        grid.draw(c);
        gridButton.draw(c);

        //draw all tokens in the arraylist
        for (GuiToken token : tokens) {
            token.draw(c);
        }
    }

    /**
     * Handle touch events for pressing and releasing buttons.
     *
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

                //loop through all buttons to see if one is touched
                for (GridButton btn : gridButton.buttons) {
                    if (btn.contains(x, y)) {
                        //if a button is pressed, create a token and add it to the list
                        createToken(btn);
                        invalidate();  // Redraw the view
                        buttonPressed = true;
                        break;
                    }
                }

                //if no button was pressed, show a toast message
                if (!buttonPressed) {
                    Toast.makeText(getContext(), "Touch the button", Toast.LENGTH_SHORT).show();
                }
                break;

            case MotionEvent.ACTION_UP:
                //release all buttons when the user lifts their finger
                for (GridButton btn : gridButton.buttons) {
                    btn.release();
                }
                invalidate();  //redraw the view
                break;
        }
        return true;
    }

    /**
     * Creates a token behind the pressed button and adds it to the list of tokens.
     * Alternates between X and O tokens.
     *
     * @param btn The button that was pressed.
     */
    private void createToken(GridButton btn) {
        float tokenSize = btn.bounds.width() * 0.8f; // Scale down the token size
        float tokenX = btn.bounds.left + (btn.bounds.width() - tokenSize) / 2;
        float tokenY = btn.bounds.top + (btn.bounds.height() - tokenSize) / 2;
        RectF tokenBounds = new RectF(tokenX, tokenY, tokenX + tokenSize, tokenY + tokenSize);

        Bitmap tokenImage;
        Player player;
        if (isXTurn) {
            tokenImage = BitmapFactory.decodeResource(getResources(), R.drawable.player_x);
            player = Player.X;
        } else {
            tokenImage = BitmapFactory.decodeResource(getResources(), R.drawable.player_o);
            player = Player.O;
        }

        tokenImage = Bitmap.createScaledBitmap(tokenImage, (int) tokenSize, (int) tokenSize, true);
        GuiToken newToken = new GuiToken(tokenImage, tokenBounds, player);
        tokens.add(newToken);

        //set target and velocity based on which button was pressed
        if (btn.isTopRow()) {
            newToken.setTarget(tokenX, tokenY + btn.bounds.height()); // Move down
            newToken.setVelocity(0, 10);
        } else if (btn.isLeftColumn()) {
            newToken.setTarget(tokenX + btn.bounds.width(), tokenY); // Move right
            newToken.setVelocity(10, 0);
        }

        //toggle turn between X and O
        isXTurn = !isXTurn;

        //start the movement handler when a token is added
        if (tokens.size() == 1) {
            handler.sendEmptyMessage(0); //start handling movements
        }
    }

    private class GameHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            for (GuiToken token : tokens) {
                token.move(); //move each token
            }

            invalidate(); //redraw
            sendEmptyMessageDelayed(0, 16); //call this method again after 16ms (~60 FPS)
        }
    }
}