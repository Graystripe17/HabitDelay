package com.cinnamint.habitdelay;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rvalerio.fgchecker.AppChecker;

import static com.rvalerio.fgchecker.Utils.hasUsageStatsPermission;

public class MainActivity extends AppCompatActivity {

    Context c;
    static String mostRecentApp;
    public String DEBUG = "DEBUG";

    void requestUsageStatsPermission() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
            Toast.makeText(c, "Habit Delay needs Usage Access permissions", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        c = getApplicationContext();
        mostRecentApp = "";

        requestUsageStatsPermission();

        AppChecker appChecker = new AppChecker();
        appChecker
            .when("com.google.android.youtube", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        if (!mostRecentApp.equals("youtube")) {
                            Toast.makeText(c, "Youtube Opened", Toast.LENGTH_LONG).show();
                            mostRecentApp = "youtube";

                            openTimer();

                        }
                    }
                }
            )
            .when("com.snapchat.android", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        Toast.makeText(c, "Snapchat Opened", Toast.LENGTH_LONG).show();
                    }
                }
            )
            .when("com.android.camera", new AppChecker.Listener() {
                        @Override
                        public void onForeground(String packageName) {
                            Toast.makeText(c, "Camera Opened", Toast.LENGTH_LONG).show();

                            if (!mostRecentApp.equals("camera") && !mostRecentApp.equals("camera_not_completed")) {
                                mostRecentApp = "camera_not_completed";

                                Log.d(DEBUG, mostRecentApp);

                                openTimer();
                            } else {
                                Log.d(DEBUG, mostRecentApp);
                            }
                        }
                    }
            )
            .whenOther(new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        mostRecentApp = "none";
                        Log.d(DEBUG, mostRecentApp);
                    }
                }
            )
        .timeout(1000)
        .start(this);

    }

    public void openTimer() {
        Intent cd = new Intent(c, CountdownActivity.class);
        // cd.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(cd);
        finish();
    }

}
