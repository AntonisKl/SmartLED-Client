package com.example.antonis.rgbstripcontrol;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    SharedPreferences storedSettings;
    ColorPickerView colorPicker;
    int[] argb;
    String defaultUrl;
    String storedUrl;
    RequestQueue requestQueue;
    Button colorModesButton;
    ImageButton offButton;
    BrightnessSlideBar brightnessSlideBar;
    ImageButton redButton, greenButton, blueButton, cyanButton, yellowButton, purpleButton, whiteButton;

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
        newFragment.setDefaultUrl(defaultUrl);
//		newFragment.setOnSubmitSettingsListener(onSubmitSettingsListener);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

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

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        colorPicker = findViewById(R.id.view_color_picker);
        initPresetColorButtons();

        argb = new int[4];

        defaultUrl = getResources().getString(R.string.default_url);
        storedSettings = getPreferences(Context.MODE_PRIVATE);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storedUrl = storedSettings.getString("URL", defaultUrl);
                sendPostColorHttpRequest(storedUrl, new int[]{0, 0, 0, 0});
            }
        });

        colorPicker.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope, boolean fromUser) {
                if (!Arrays.equals(argb, colorEnvelope.getArgb())) {
                    argb = colorEnvelope.getArgb();
                    storedUrl = storedSettings.getString("URL", defaultUrl);
                    Log.i("argb", "a=" + argb[0] + ", r=" + argb[1] + ", g=" + argb[2] + ", b=" + argb[3]);
                    sendPostColorHttpRequest(storedUrl, argb);
//                    ColorPickerPreferenceManager.getInstance().saveColorPickerData(colorPicker);
                }
            }
        });

        colorModesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ColorModesActivity.class);
                intent.putExtra("URL", storedSettings.getString("URL", defaultUrl));
                startActivity(intent);
            }
        });

        brightnessSlideBar = findViewById(R.id.brightnessSlide);
        colorPicker.attachBrightnessSlider(brightnessSlideBar);
    }

    void sendPostColorHttpRequest(String baseUrl, int[] argb) {
        // calculate color with brightness

        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl + "setColor?r=" + argb[1] + "&g=" + argb[2] + "&b=" + argb[3],
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

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    void initPresetColorButtons() {
        offButton = findViewById(R.id.button_off);
        colorModesButton = findViewById(R.id.button_color_modes);
        redButton = findViewById(R.id.button_red);
        greenButton = findViewById(R.id.button_green);
        blueButton = findViewById(R.id.button_blue);
        cyanButton = findViewById(R.id.button_cyan);
        yellowButton = findViewById(R.id.button_yellow);
        purpleButton = findViewById(R.id.button_purple);
        whiteButton = findViewById(R.id.button_white);
        redButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 255, 0, 0}));
        greenButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 0, 255, 0}));
        blueButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 0, 0, 255}));
        cyanButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 0, 255, 255}));
        yellowButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 255, 255, 0}));
        purpleButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 255, 0, 255}));
        whiteButton.setOnClickListener(createColorButtonOnClickListner(new int[]{0, 255, 255, 255}));
    }

    View.OnClickListener createColorButtonOnClickListner(final int[] argb) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storedUrl = storedSettings.getString("URL", defaultUrl);
                sendPostColorHttpRequest(storedUrl, argb);
            }
        };
    }
}
