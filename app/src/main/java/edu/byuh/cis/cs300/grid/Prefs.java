package edu.byuh.cis.cs300.grid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import java.util.Locale;

public class Prefs extends AppCompatActivity {

    /**
     * Initializes the activity and displays the settings fragment in the UI.
     *
     * @param b The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle b) {
        setLocale();
        setTheme(R.style.Theme_Grid_Prefs);
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
     * Handles the user selecting an item from the options menu.
     *
     * @param mi The selected item.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (mi.getItemId() == android.R.id.home) {
            onBackPressed();
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

    /**
     * Retrieves the slide sound preference setting from shared preferences.
     *
     * @param c The context used to access shared preferences.
     * @return True if the slide sound preference is enabled, false otherwise.
     */
    public static boolean getSoundEffectsPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("SOUND_EFFECTS_PREF", true);
    }

    /**
     * Retrieves the speed preference setting from shared preferences.
     *
     * @param c The context used to access shared preferences.
     * @return The speed preference setting as an integer, from 5 to 17.
     */
    public static int getSpeedPref(Context c) {
        String speedPrefSlide = PreferenceManager.getDefaultSharedPreferences(c).getString("SPEED_PREF", "11");
        return Integer.parseInt(speedPrefSlide);
    }

    /**
     * Retrieves the theme preference setting from shared preferences.
     *
     * @param c The context used to access shared preferences.
     * @return The theme preference setting as a string, either "Thai", "Space", or "Flower".
     */
    public static String getThemePref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString("THEME_PREF", "Space");
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        /**
         * Called when the Fragment is created to inflate the preference hierarchy.
         *
         * Creates a PreferenceScreen with four Preference objects: a SwitchPreference for
         * enabling/disabling music, a SwitchPreference for enabling/disabling slide sounds,
         * a ListPreference for setting the game speed, and a ListPreference for setting the
         * game theme.
         *
         * Also sets up a listener for changes to the theme preference and shows a dialog
         * asking the user to restart the app when the theme preference changes.
         *
         * @param b The saved instance state bundle.
         * @param s The key of the preference screen root.
         */
        @Override
        public void onCreatePreferences(Bundle b, String s) {
            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            SwitchPreference music = new SwitchPreference(context);
            music.setTitle(R.string.music_pref);
            music.setSummaryOn(R.string.music_on);
            music.setSummaryOff(R.string.music_off);
            music.setDefaultValue(false);
            music.setKey("MUSIC_PREF");
            screen.addPreference(music);

            SwitchPreference slideSound = new SwitchPreference(context);
            slideSound.setTitle(R.string.sound_effects_pref);
            slideSound.setSummaryOn(R.string.sound_effects_on);
            slideSound.setSummaryOff(R.string.sound_effects_off);
            slideSound.setDefaultValue(true);
            slideSound.setKey("SOUND_EFFECTS_PREF");
            screen.addPreference(slideSound);

            ListPreference speed = new ListPreference(context);
            speed.setTitle(R.string.speed_pref_title);
            speed.setSummary(R.string.speed_pref_summary);
            speed.setKey("SPEED_PREF");
            String[] values = {"5", "11", "17"};
            speed.setEntries(R.array.speed_entries);
            speed.setEntryValues(values);
            speed.setDefaultValue("11");
            screen.addPreference(speed);

            ListPreference theme = new ListPreference(context);
            theme.setTitle(R.string.theme_pref_title);
            theme.setSummary(R.string.theme_pref_summary);
            theme.setKey("THEME_PREF");
            String[] themes = {"Space", "Thai", "Flower"};
            theme.setEntries(R.array.theme_entries);
            theme.setEntryValues(themes);
            theme.setDefaultValue("Space");
            screen.addPreference(theme);

            setPreferenceScreen(screen);

            //listen for changes in the theme preference
            theme.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedTheme = (String) newValue;
                showRestartDialog(context, selectedTheme);
                return true;
            });
        }

        /**
         * Displays a dialog prompting the user to restart the app.
         *
         * This dialog is shown when the theme preference is changed,
         * informing the user that a restart is required for the new
         * theme to take effect. The dialog provides options for the
         * user to either restart the app immediately or dismiss the
         * dialog without taking any action.
         *
         * @param context The context in which the dialog should be displayed.
         * @param selectedTheme The newly selected theme that requires a restart.
         */
        private void showRestartDialog(Context context, String selectedTheme) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.restart_title)
                    .setMessage(R.string.restart_message)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        restartApp(context);
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        }

        /**
         * Restarts the app immediately.
         *
         * This method is called when the user chooses to restart the app
         * immediately after changing the theme preference. It starts the
         * title screen activity with the flags to clear the current task
         * and start a new one. It also calls the Runtime#exit method to
         * ensure that the app is completely shut down before the new
         * activity is started.
         *
         * @param context The context in which the app should be restarted.
         */
        private void restartApp(Context context) {
            Intent intent = new Intent(context, TitleScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }
}