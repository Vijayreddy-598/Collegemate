package com.batch16.collegemate.ui;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.batch16.collegemate.MainActivity;
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
        Preference pref,pref1;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preference, rootKey);
            pref=findPreference("silentmode");
            pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final boolean val = (Boolean) newValue;
                    //Toast.makeText(getContext(), "val = "+val, Toast.LENGTH_SHORT).show();
                    if(val == Boolean.TRUE){

                    }
                    return true;
                }
            });


        }


    }

}

