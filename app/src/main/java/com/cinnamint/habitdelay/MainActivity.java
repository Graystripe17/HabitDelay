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
import android.view.View;
import android.widget.Toast;

import com.rvalerio.fgchecker.AppChecker;

import static com.rvalerio.fgchecker.Utils.hasUsageStatsPermission;

public class MainActivity extends AppCompatActivity {

    Context c;
    static String mostRecentApp;

    void requestUsageStatsPermission() {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !hasUsageStatsPermission(this)) {
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

                            Intent cd = new Intent(c, CountdownActivity.class);
                            cd.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(cd);
                        }
                    }
                }
            )
            .when("com.snapchat.android", new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        // do something when com.my.app is in the foreground
                    }
                }
            )
            .when("com.android.camera", new AppChecker.Listener() {
                        @Override
                        public void onForeground(String packageName) {
                            if (!mostRecentApp.equals("camera")) {
                                Toast.makeText(c, "Camera Opened", Toast.LENGTH_LONG).show();
                                mostRecentApp = "camera_not_completed";


                                Intent cd = new Intent(c, CountdownActivity.class);
                                cd.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(cd);
                            }
                        }
                    }
            )
            .whenOther(new AppChecker.Listener() {
                    @Override
                    public void onForeground(String packageName) {
                        mostRecentApp = "none";
                    }
                }
            )
        .timeout(1000)
        .start(this);

    }

}
