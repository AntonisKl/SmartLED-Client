package com.example.antonis.rgbstripcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private String defaultUrl;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openFragment(HomeFragment.newInstance());
                    return true;
                case R.id.navigation_modes:
                    openFragment(ModesFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views and variables
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // default fragment
        openFragment(HomeFragment.newInstance());
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showSettingsDialog() {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();

        // Create and show the dialog.
        SettingsFragment newFragment = new SettingsFragment();
        newFragment.setDefaultUrl(defaultUrl);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                showSettingsDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
