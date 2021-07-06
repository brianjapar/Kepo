package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kepo.apihelper.ApiClient;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvLogout;
    private ImageButton ibBack;
    private TextView tvUsername,tvName;
    private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvLogout=findViewById(R.id.tv_logout);
        ibBack = findViewById(R.id.ib_back);
        tvName=findViewById(R.id.tv_name);
        tvUsername=findViewById(R.id.tv_username);
        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");
        if(intent.getExtras() !=null){
            tvName.setText(passedName);
            tvUsername.setText(passedUsername);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(ProfileActivity.this, HomeActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }


    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder
                .setMessage("Are you sure want to logout?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileActivity.this,SplashScreen.class);
                        SharedPref sharedPref = new SharedPref(getApplicationContext());
                        sharedPref.logout();
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}