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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.batch16.collegemate.Functions.IntroActivity;
import com.batch16.collegemate.LoginActivity;
import com.batch16.collegemate.MainActivity;
import com.batch16.collegemate.R;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
    Button logout,intro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_settings, container, false);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings,new SettingsFrag())
                .commit();
        logout=root.findViewById(R.id.logout);
        intro=root.findViewById(R.id.intro);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance()
                        .signOut();
                startActivity(new Intent(MainActivity.ctx, LoginActivity.class));
                getActivity().finish();
            }
        });
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IntroActivity.class));
            }
        });
        return root;
    }
    public static class SettingsFrag extends PreferenceFragmentCompat {
        Preference pref;
        EditTextPreference uname;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preference, rootKey);
            uname=findPreference("Username");
            uname.setText(MainActivity.sp.getString("UserName","Not Set"));
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

