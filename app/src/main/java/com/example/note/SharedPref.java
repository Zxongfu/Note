package com.example.note;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.note.base.BaseApplication;

public class SharedPref  {
    private SharedPreferences mPreferences;

    public SharedPref() {
        mPreferences =BaseApplication.getContext().getSharedPreferences("filName", Context.MODE_PRIVATE);
    }
    //save night mode state
    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor =mPreferences.edit() ;
        editor.putBoolean("NightMode",state);
        editor.apply();
    }
    //load night mode state
    public Boolean loadNightModeState(){
        return mPreferences.getBoolean("NightMode",false);
    }
}
