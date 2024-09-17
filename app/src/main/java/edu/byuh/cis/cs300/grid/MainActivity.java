package edu.byuh.cis.cs300.grid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActView view;

    /**
     * Initializes the activity and sets the custom view for displaying the game grid.
     *
     * @param b The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        view = new ActView(this);
        setContentView(view);
    }
}
