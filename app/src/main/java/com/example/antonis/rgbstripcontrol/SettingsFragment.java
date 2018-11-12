package com.example.antonis.rgbstripcontrol;


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

		ipAddress = fragmentView.findViewById(R.id.text_ip_address);
		submitSettingsButton = fragmentView.findViewById(R.id.button_submit_settings);

		getDialog().setTitle("Settings");
		if (ipAddressStr != null)
			ipAddress.setText(ipAddressStr);

		submitSettingsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!ipAddress.getText().toString().equals(""))
					onSubmitSettingsListener.OnSubmitSettings(ipAddress.getText().toString());
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
