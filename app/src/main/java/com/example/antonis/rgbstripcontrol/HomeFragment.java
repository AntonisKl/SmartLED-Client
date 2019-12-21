package com.example.antonis.rgbstripcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    ColorPickerView colorPicker;
    String defaultUrl;
    SharedPreferences storedSettings;
    RequestHandler requestHandler;
    ImageButton offButton;
    BrightnessSlideBar brightnessSlideBar;
    ImageButton redButton, greenButton, blueButton, cyanButton, yellowButton, purpleButton, whiteButton;
    int[] argb;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        colorPicker = view.findViewById(R.id.view_color_picker);
        brightnessSlideBar = view.findViewById(R.id.brightnessSlide);
        initPresetColorButtons(view);

        argb = new int[4];

        colorPicker.attachBrightnessSlider(brightnessSlideBar);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        defaultUrl = getString(R.string.default_url);
        storedSettings = getActivity().getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);

        requestHandler = new RequestHandler(Volley.newRequestQueue(getActivity()));

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestHandler.addSetColorHttpRequest(storedSettings.getString("URL", defaultUrl), new int[]{0, 0, 0, 0});
            }
        });

        colorPicker.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope, boolean fromUser) {
                if (!Arrays.equals(argb, colorEnvelope.getArgb())) {
                    argb = colorEnvelope.getArgb();
                    requestHandler.addSetColorHttpRequest(storedSettings.getString("URL", defaultUrl), argb);
                }
            }
        });
    }

    void initPresetColorButtons(View view) {
        offButton = view.findViewById(R.id.button_off);
        redButton = view.findViewById(R.id.button_red);
        greenButton = view.findViewById(R.id.button_green);
        blueButton = view.findViewById(R.id.button_blue);
        cyanButton = view.findViewById(R.id.button_cyan);
        yellowButton = view.findViewById(R.id.button_yellow);
        purpleButton = view.findViewById(R.id.button_purple);
        whiteButton = view.findViewById(R.id.button_white);
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
                requestHandler.addSetColorHttpRequest(storedSettings.getString("URL", defaultUrl), argb);
            }
        };
    }
}