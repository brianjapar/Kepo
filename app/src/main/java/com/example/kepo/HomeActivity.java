package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView btnMyTodo,btnSearchTodo,btnSearchUser,btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnMyTodo = findViewById(R.id.btn_myTodo);
        btnProfile=findViewById(R.id.btn_profile);
        btnSearchTodo=findViewById(R.id.btn_searchTodo);
        btnSearchUser=findViewById(R.id.btn_searchUser);

        tvName = findViewById(R.id.tv_name);
        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");
        SharedPref sharedPref = new SharedPref(getApplicationContext());
        HashMap<String,String> user = sharedPref.getUserDetails();
        String username = user.get(SharedPref.USERNAME);
        String user_id = user.get(SharedPref.USER_ID);
        String name = user.get(SharedPref.NAME);
        if(intent.getExtras() !=null){
            tvName.setText("Welcome, "+name);
        }

        btnMyTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(HomeActivity.this, GetTodoActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
            }
        });

        btnSearchTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(HomeActivity.this, SearchTodoActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(HomeActivity.this, SearchUserActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(HomeActivity.this, ProfileActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
            }
        });
    }
}