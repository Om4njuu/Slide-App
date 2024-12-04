package edu.byuh.cis.cs300.grid;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import edu.byuh.cis.cs300.grid.logic.GameMode;
import android.media.MediaPlayer;

public class TitleScreen extends AppCompatActivity {

    private ImageView iv;
    private MediaPlayer mainMenuSong;

    /**
     * Initializes the activity and displays the splash screen in the UI.
     *
     * @param b The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setLocale();
        iv = new ImageView(this);
        applyTheme();
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);

        // Initialize the main menu song
        mainMenuSong = MediaPlayer.create(this, R.raw.main_menu_music);
        mainMenuSong.setLooping(true);

        // Start the main menu song if music is enabled in preferences
        if (Prefs.getMusicPref(this)) {
            mainMenuSong.start();
        }
    }

    /**
     * Sets the locale to the device locale.
     */
    private void setLocale() {
        Locale deviceLocale = Resources.getSystem().getConfiguration().locale;
        Locale.setDefault(deviceLocale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(deviceLocale);
        createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * Apply the theme-based splash screen.
     */
    private void applyTheme() {
        String theme = Prefs.getThemePref(this);
        Locale deviceLocale = Resources.getSystem().getConfiguration().locale;
        String languageCode = deviceLocale.getLanguage();

        String resourceName = theme.toLowerCase() + "_splash_" + languageCode;
        int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());

        if (resId == 0) {
            //fallback to default splash screen if the specific one is not found
            resId = R.drawable.space_splash_en;
        }

        iv.setImageResource(resId);
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
            float w = iv.getWidth(); //width of the screen or image view
            float h = iv.getHeight(); //height of the screen or image view
            
            //top-left corner (About the Game)
            if (x < w / 4 && y < h / 4) {
                setLocale();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.about);
                String theme = Prefs.getThemePref(this);
                Locale deviceLocale = Resources.getSystem().getConfiguration().locale;
                String languageCode = deviceLocale.getLanguage();
                String resourceName = theme.toLowerCase() + "_about_message";
                int resId = getResources().getIdentifier(resourceName, "string", getPackageName());
                builder.setMessage(resId != 0 ? getString(resId) : getString(R.string.space_about_message));
                builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            }
            //top-right corner (Settings)
            else if (x > (3 * w) / 4 && y < h / 4) {
                setLocale(); // Ensure locale is set before launching the activity
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

    /**
     * Pauses the main menu music when the activity is paused.
     * This is important so that the music doesn't continue playing when the
     * user navigates away from the main menu or the screen is turned off.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mainMenuSong.isPlaying()) {
            mainMenuSong.pause();
        }
    }

    /**
     * Resumes the main menu music if music is enabled in preferences.
     *
     * This method is called when the activity is resumed, such as when the screen is turned
     * on or the user returns to the app from another activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (Prefs.getMusicPref(this)) {
            mainMenuSong.start();
        }
    }

    /**
     * Destroys the activity and releases the main menu music from memory.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainMenuSong != null) {
            mainMenuSong.release();
            mainMenuSong = null;
        }
    }
}