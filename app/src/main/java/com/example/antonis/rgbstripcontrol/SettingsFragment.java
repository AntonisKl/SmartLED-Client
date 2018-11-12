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

    String ipAddressStr;
    EditText ipAddress;
    Button submitSettingsButton;
    SharedPreferences storedSettings;

    private OnSubmitSettingsListener onSubmitSettingsListener;

    public static interface OnSubmitSettingsListener {
        void OnSubmitSettings(String ipAddress);
    }

    public void setDefaultIpAddress(String ipAddress) {
        ipAddressStr = ipAddress;
    }

    public void setOnSubmitSettingsListener(OnSubmitSettingsListener onSubmitSettingsListener) {
        this.onSubmitSettingsListener = onSubmitSettingsListener;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        storedSettings = getActivity().getPreferences(Context.MODE_PRIVATE);
        ipAddress = fragmentView.findViewById(R.id.text_ip_address);
        submitSettingsButton = fragmentView.findViewById(R.id.button_submit_settings);

        getDialog().setTitle("Settings");


        String storedIpAddress = storedSettings.getString("IP", ipAddressStr);

        if (storedIpAddress != null)
            ipAddress.setText(storedIpAddress);

        submitSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ipAddress.getText().toString().equals("")) {
                    onSubmitSettingsListener.OnSubmitSettings(ipAddress.getText().toString());
                    SharedPreferences.Editor editor = storedSettings.edit();
                    editor.putString("IP", ipAddress.getText().toString());
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
