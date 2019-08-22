package com.Aadi.PP;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class SettingsAc extends Fragment {
    private Switch darkModeSwitch;
    private SharedPreferences sharePrefObje;
    private RelativeLayout EditProfile;
    private TextView Logout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.settings, container, false);

        EditProfile = rootView.findViewById(R.id.editprofile);
        Logout = rootView.findViewById(R.id.Logout);
        darkModeSwitch = rootView.findViewById(R.id.darkModeSwitch);

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileAc.class);
                startActivity(intent);

                return;
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout From SportConnect")
                        .setMessage("Would you like to Log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sharePrefObje = getActivity().getSharedPreferences("PREFERENCENAME", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePrefObje.edit();
                                editor.clear();
                                editor.apply();
                                //updateUserStatus("offline");
                                Intent inte = new Intent(getActivity(), PhoneoneActivity.class);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(inte);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        if(new DarkModePrefManager(getActivity()).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        setDarkModeSwitch();

        //function for enabling dark mode



        return rootView;
    }

    private void setDarkModeSwitch(){

        darkModeSwitch.setChecked(new DarkModePrefManager(getActivity()).isNightMode());
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(getActivity());
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        });
    }



}