package com.batch16.collegemate.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.batch16.collegemate.R;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_settings, container, false);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings,new SettingsFrag())
                .commit();

        return root;
    }
    public static class SettingsFrag extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preference, rootKey);
        }
    }

}

