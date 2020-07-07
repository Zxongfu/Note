package com.example.note;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.note.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat  {


        private static final String TAG = "SettingsFragment";
        private SwitchPreferenceCompat mSwitchPreference;
        SharedPref mSharedPref;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            mSharedPref =new SharedPref();
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            mSwitchPreference =  findPreference(getResources().getString(R.string.sync_mode));
            if (mSharedPref.loadNightModeState()) {
                mSwitchPreference.setChecked(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }else {
                mSwitchPreference.setChecked(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            mSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    if ((Boolean) newValue){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        mSharedPref.setNightModeState((Boolean) newValue);
                        Toast.makeText(getActivity(), ""+mSwitchPreference.getSwitchTextOn(), Toast.LENGTH_SHORT).show();
                    }else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        mSharedPref.setNightModeState((Boolean) newValue);
                        Toast.makeText(getActivity(), ""+mSwitchPreference.getSwitchTextOff(), Toast.LENGTH_SHORT).show();
                    }
                    getActivity().recreate();
                    return (Boolean) newValue;
                }
            });
        }

    }
}