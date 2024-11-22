package edu.byuh.cis.cs300.grid;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

public class Prefs extends AppCompatActivity {

    /**
     * Initializes the activity and displays the settings fragment in the UI.
     *
     * @param b The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.settings_activity);
        if (b == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Handles the user selecting an item from the options menu.
     *
     * @param mi The selected item.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (mi.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }

    /**
     * Retrieves the music preference setting from shared preferences.
     *
     * @param c The context used to access shared preferences.
     * @return True if the music preference is enabled, false otherwise.
     */
    public static boolean getMusicPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MUSIC_PREF", false);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        /**
         * Called during the creation of the fragment to inflate the preference hierarchy.
         *
         * @param b The bundle containing the saved state of the fragment.
         * @param s The key for the preference hierarchy to be inflated.
         */
        @Override
        public void onCreatePreferences(Bundle b, String s) {
            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            SwitchPreference music = new SwitchPreference(context);
            music.setTitle("Music");
            music.setSummaryOn("Background music will play");
            music.setSummaryOff("Background music will NOT play");
            music.setDefaultValue(false);
            music.setKey("MUSIC_PREF");
            screen.addPreference(music);
            setPreferenceScreen(screen);
        }
    }
}