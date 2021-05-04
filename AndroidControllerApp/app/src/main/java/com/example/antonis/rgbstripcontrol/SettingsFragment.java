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
import android.widget.LinearLayout;
import android.widget.TextView;


public class SettingsFragment extends DialogFragment {

    String sharedPreferencesName;
    String settingName;
    String settingDefaultValue;
    Float settingEditTextWeight;
    TextView settingNameTextView;
    EditText settingEditText;
    Button submitSettingsButton;
    SharedPreferences storedSettings;

//    private OnSubmitSettingsListener onSubmitSettingsListener;

//    public static interface OnSubmitSettingsListener {
//        void OnSubmitSettings(String ipAddress);
//    }

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

        sharedPreferencesName = getArguments().getString("sharedPreferencesName");
        settingName = getArguments().getString("settingName");
        settingDefaultValue = getArguments().getString("settingDefaultValue");
        settingEditTextWeight = getArguments().getFloat("settingEditTextWeight", -1);
        storedSettings = getContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        settingNameTextView = fragmentView.findViewById(R.id.setting_name);
        settingEditText = fragmentView.findViewById(R.id.setting_value);
        submitSettingsButton = fragmentView.findViewById(R.id.button_submit_settings);

        getDialog().setTitle("Settings");

        settingNameTextView.setText(settingName);
        if (settingEditTextWeight != -1) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) settingEditText.getLayoutParams();
            params.weight = settingEditTextWeight;
            settingEditText.setLayoutParams(params);
        }

        String storedIpAddress = storedSettings.getString(settingName, settingDefaultValue);

        if (storedIpAddress != null)
            settingEditText.setText(storedIpAddress);

        submitSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!settingEditText.getText().toString().equals("")) {
//                    onSubmitSettingsListener.OnSubmitSettings(urlText.getText().toString());
                    SharedPreferences.Editor editor = storedSettings.edit();
                    editor.putString(settingName, settingEditText.getText().toString());
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
