package edu.byuh.cis.cs300.grid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import edu.byuh.cis.cs300.grid.logic.GameMode;
import edu.byuh.cis.cs300.grid.ui.GridView;

public class MainActivity extends AppCompatActivity {

    private GridView view;

    /**
     * Initializes the activity and sets the custom view for displaying the game grid.
     *
     * @param b The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Intent mainActivityIntent = getIntent();
        GameMode gameMode = (GameMode) getIntent().getSerializableExtra("GAME_MODE");
        view = new GridView(this, gameMode);
        setContentView(view);
    }

    /**
     * Pauses the music when the activity is paused, such as when the screen is turned off or another
     * activity is started.
     */
    @Override
    public void onPause() {
        super.onPause();
        view.pauseMusic();
    }

    /**
     * Resumes the music when the activity is resumed, such as when the screen is turned on or
     * the user returns to the app from another activity.
     */
    @Override
    public void onResume() {
        super.onResume();
        view.resumeMusic();
    }

    /**
     * Destroys the activity and unloads the music from memory.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //un-load the music from memory
        view.unloadMusic();
    }
}
