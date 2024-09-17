package edu.byuh.cis.cs300.grid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActView view;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        view = new ActView(this);
        setContentView(view);
    }
}
