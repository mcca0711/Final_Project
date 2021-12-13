package com.example.final_project;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "nasaday";
    private static final String DATE = "date";

    private Context context;

    public SharedPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //saving data in shared preference
    public void setDate(String key){
        editor.putString(DATE,key);
        editor.commit();
    }

    //fetching data from shared preference
    public String getDate(){
        return sharedPreferences.getString(DATE,null);
    }

}
