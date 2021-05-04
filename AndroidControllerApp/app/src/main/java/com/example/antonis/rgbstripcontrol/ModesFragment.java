package com.example.antonis.rgbstripcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;


public class ModesFragment extends Fragment {

    private static final String STORED_URL = "stored_url";

    ListView colorModesList;
    String defaultUrl;
    Integer defaultRainbowTransitionSpeedMs;
    SharedPreferences storedSettings;
    SharedPreferences storedRainbowSettings;
    RequestHandler requestHandler;

    public ModesFragment() {
        // Required empty public constructor
    }

    public static ModesFragment newInstance() {
        return new ModesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modes, container, false);

        colorModesList = view.findViewById(R.id.color_modes_list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        defaultUrl = getString(R.string.default_url);
        defaultRainbowTransitionSpeedMs = 1;
        storedSettings = getActivity().getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
        storedRainbowSettings = getActivity().getSharedPreferences(getString(R.string.rainbow_settings), Context.MODE_PRIVATE);

        requestHandler = new RequestHandler(Volley.newRequestQueue(getActivity()));

        List<ColorModesListAdapter.ColorModeListItem> colorModeListItems = new ArrayList<>();
        colorModeListItems.add(new ColorModesListAdapter.ColorModeListItem("Rainbow effect", R.drawable.ic_rainbow_48dp, new int[]{255, 255, 255}, new ColorModesListAdapter.ColorModeListItemOnClickI() {
            @Override
            public void onClick() {
                requestHandler.addRainbowEffectHttpRequest(storedSettings.getString("URL", defaultUrl), Integer.parseInt(storedRainbowSettings.getString(getString(R.string.rainbow_transition_speed), defaultRainbowTransitionSpeedMs.toString())));
            }
        }, new ColorModesListAdapter.ColorModeListItemOnClickI() {
            @Override
            public void onClick() {
                showSettingsDialog(getString(R.string.rainbow_settings),getString(R.string.rainbow_transition_speed));
            }
        }));

        colorModesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((ColorModesListAdapter.ColorModeListItem) adapterView.getItemAtPosition(i)).onClickI.onClick();
            }
        });

        colorModesList.setAdapter(new ColorModesListAdapter(getActivity(), colorModeListItems));
    }

    private void showSettingsDialog(String sharedPreferencesName, String settingName) {
        android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();

        // Create and show the dialog.
        SettingsFragment newFragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString("sharedPreferencesName", sharedPreferencesName);
        args.putString("settingName", settingName);
        args.putString("settingDefaultValue", defaultRainbowTransitionSpeedMs.toString());
        args.putFloat("settingEditTextWeight", 0.2f);
        newFragment.setArguments(args);
        newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
    }
}
