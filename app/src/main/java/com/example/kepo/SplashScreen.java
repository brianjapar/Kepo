package com.example.kepo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPref sharedPref = new SharedPref(getApplicationContext());
                if(sharedPref.isLoggedIn()){
                    HashMap<String,String> user = sharedPref.getUserDetails();
                    String username = user.get(SharedPref.USERNAME);
                    String user_id = user.get(SharedPref.USER_ID);
                    String name = user.get(SharedPref.NAME);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class)
                            .putExtra("username", username)
                            .putExtra("user_id", user_id)
                            .putExtra("name", name));
                }else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                finish();
            }
        }, 1500L); //1500 L = 1.5 detik


    }
}