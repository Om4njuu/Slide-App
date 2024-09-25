package edu.byuh.cis.cs300.grid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        view = new GridView(this);
        setContentView(view);
    }
}
