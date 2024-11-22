package edu.byuh.cis.cs300.grid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import edu.byuh.cis.cs300.grid.logic.GameMode;

public class TitleScreen extends AppCompatActivity {

    private ImageView iv;

    /**
     * Initializes the activity and sets the custom view for the title screen.
     *
     * @param b The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    /**
     * Handles touch events on the title screen.
     *
     * The touch events are handled by identifying the region of the screen
     * that was touched. The touch regions are
     * 1. Top-left corner (About the Game) displays an alert dialog to
     *    explain the rules of the game.
     * 2. Top-right corner (Settings) navigates to the preferences screen.
     * 3. First bottom (PvE button - One Player Mode) starts the game in
     *    one-player mode.
     * 4. Second bottom (PvP button - Two Player Mode) starts the game in
     *    two-player mode.
     *
     * @param m the MotionEvent object containing full information about
     *           the event.
     * @return true if the event was handled; false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX(); //x-coordinate of the touch
            float y = m.getY(); //y-coordinate of the touch
            float w = iv.getWidth(); // Width of the screen or image view
            float h = iv.getHeight(); // Height of the screen or image view
            
            // Top-left corner (About the Game)
            if (x < w / 4 && y < h / 4) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About the Game");
                builder.setMessage("Slide is a strategic board game with two modes: one-player (PvE) and two-player (PvP local). You play against the computer or your friend, which uses a strong greedy strategy. The game is played on a 5x5 board where you and the computer or your friend take turns placing tokens from the top (1-5) or the left (A-E). Tokens slide existing ones over or down, possibly pushing them off the board. The goal is to get five tokens in a row horizontally, vertically, or diagonally. You play as moons, and the computer or your friend plays as suns. Enjoy the challenge!");
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                builder.show();
            }
            // Top-right corner (Settings)
            else if (x > (3 * w) / 4 && y < h / 4) {
                Intent prefsIntent = new Intent(this, Prefs.class);
                startActivity(prefsIntent);
            }
            //first Bottom (PvE button - One Player Mode)
            else if (x > (w / 4) && x < (3 * w / 4) && y > (3 * h / 4) && y < (7 * h / 8)) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                mainActivityIntent.putExtra("GAME_MODE", GameMode.ONE_PLAYER); //for PvE
                startActivity(mainActivityIntent);
            }
            //second Bottom (PvP button - Two Player Mode)
            else if (x > (w / 4) && x < (3 * w / 4) && y > (7 * h / 8) && y < h) {
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                mainActivityIntent.putExtra("GAME_MODE", GameMode.TWO_PLAYER); //for PvP
                startActivity(mainActivityIntent);
            }
        }
        return true;
    }

}