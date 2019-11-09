package com.example.antonis.rgbstripcontrol;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class ColorModesActivity extends AppCompatActivity {

    ListView colorModesList;
    SharedPreferences storedSettings;
    RequestQueue requestQueue;
    RequestHandler requestHandler;
    String storedUrl;
    String defaultUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_modes);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        colorModesList = findViewById(R.id.color_modes_list);
        storedSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        storedUrl = storedSettings.getString("URL", defaultUrl);
        defaultUrl = getResources().getString(R.string.default_url);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestHandler = new RequestHandler(requestQueue);
        Intent intent = getIntent();

        List<ColorModesListAdapter.ColorModeListItem> colorModeListItems = new ArrayList<>();
        colorModeListItems.add(new ColorModesListAdapter.ColorModeListItem("Rainbow effect", -1, new int[]{255, 255, 255}, new ColorModesListAdapter.ColorModeListItemOnClickI() {
            @Override
            public void onClick() {
                requestHandler.addRainbowEffectHttpRequest(storedUrl, 1);
            }
        }));

        colorModesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((ColorModesListAdapter.ColorModeListItem) adapterView.getItemAtPosition(i)).onClickI.onClick();
            }
        });

        colorModesList.setAdapter(new ColorModesListAdapter(this, colorModeListItems));
    }

    private void showSettingsDialog(/*SettingsFragment.OnSubmitSettingsListener onSubmitSettingsListener*/) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        SettingsFragment newFragment = new SettingsFragment();
        newFragment.setDefaultUrl(storedUrl);
//		newFragment.setOnSubmitSettingsListener(onSubmitSettingsListener);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        storedUrl = storedSettings.getString("URL", defaultUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
            showSettingsDialog(
//					new SettingsFragment.OnSubmitSettingsListener() {
//				@Override
//				public void OnSubmitSettings(String ipAddress) {
//					baseUrl = ipAddress;
//				}
//			}
            );

        return super.onOptionsItemSelected(item);
    }
}
