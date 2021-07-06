package com.example.kepo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.kepo.response.LoginResponse;

import java.util.HashMap;

public class SharedPref {

    public static final String USER_ID = "user_id";
    public static final String USERNAME ="username";
    public static final String NAME ="name";
    private static final String LOGGED_IN = "logged_in";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;


    public SharedPref(Context context){
        pref = context.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static void createLoginSession(LoginResponse.Data loginResponse){
        editor.putBoolean(LOGGED_IN,true);
        editor.putString(USERNAME,loginResponse.getUsername());
        editor.putString(NAME,loginResponse.getName());
        editor.putString(USER_ID,loginResponse.getUser_id());
        editor.commit();
    }


    public HashMap<String, String>getUserDetails(){
        HashMap<String, String> user = new HashMap<String,String>();
        user.put(USER_ID,pref.getString(USER_ID,null));
        user.put(USERNAME,pref.getString(USERNAME,null));
        user.put(NAME,pref.getString(NAME,null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(LOGGED_IN,false);
    }




}
