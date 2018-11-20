package com.example.antonis.rgbstripcontrol;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    RequestQueue requestQueue;
    String storedBaseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_modes);

        colorModesList = findViewById(R.id.color_modes_list);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        Intent intent = getIntent();

        storedBaseUrl = intent.getStringExtra("URL");

        List<ColorModesListAdapter.ColorModeListItem> colorModeListItems = new ArrayList<>();
        colorModeListItems.add(new ColorModesListAdapter.ColorModeListItem("Night Light", -1, new int[]{255, 255, 255}));

        colorModesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendPostColorHttpRequest(storedBaseUrl, ((ColorModesListAdapter.ColorModeListItem) adapterView.getItemAtPosition(i)).rgb);
            }
        });

        colorModesList.setAdapter(new ColorModesListAdapter(this, colorModeListItems));
    }

    void sendPostColorHttpRequest(String baseUrl, int[] rgb) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + rgb[0] + "&" + rgb[1] + "&" + rgb[2],
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//						Log.i("POST Request", "Got response");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.i("POST Request", "Error: " + error);
                    }
                });
        requestQueue.add(stringRequest);
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
        newFragment.setDefaultUrl(storedBaseUrl);
//		newFragment.setOnSubmitSettingsListener(onSubmitSettingsListener);
        newFragment.show(getSupportFragmentManager(), "dialog");
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
