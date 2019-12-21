package com.example.antonis.rgbstripcontrol;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class SettingsFragment extends DialogFragment {

    String urlStr;
    EditText urlText;
    Button submitSettingsButton;
    SharedPreferences storedSettings;

//    private OnSubmitSettingsListener onSubmitSettingsListener;

//    public static interface OnSubmitSettingsListener {
//        void OnSubmitSettings(String ipAddress);
//    }

    public void setDefaultUrl(String urlStr) {
        this.urlStr = urlStr;
    }

//    public void setOnSubmitSettingsListener(OnSubmitSettingsListener onSubmitSettingsListener) {
//        this.onSubmitSettingsListener = onSubmitSettingsListener;
//    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        storedSettings = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        urlText = fragmentView.findViewById(R.id.text_ip_address);
        submitSettingsButton = fragmentView.findViewById(R.id.button_submit_settings);

        getDialog().setTitle("Settings");


        String storedIpAddress = storedSettings.getString("URL", urlStr);

        if (storedIpAddress != null)
            urlText.setText(storedIpAddress);

        submitSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!urlText.getText().toString().equals("")) {
//                    onSubmitSettingsListener.OnSubmitSettings(urlText.getText().toString());
                    SharedPreferences.Editor editor = storedSettings.edit();
                    editor.putString("URL", urlText.getText().toString());
                    editor.apply();
                }
                dismiss();
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
