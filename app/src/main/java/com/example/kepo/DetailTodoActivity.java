package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.request.CreateTodoRequest;
import com.example.kepo.response.CreateTodoResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTodoActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private TextView tvTitle,tvDescription,tvEdited;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_todo);

        ibBack = findViewById(R.id.ib_back);
        tvTitle = findViewById(R.id.tv_title);
        tvEdited = findViewById(R.id.tv_edited);
        tvDescription = findViewById(R.id.tv_description);
        floatingActionButton = findViewById(R.id.btn_edit);
        progressBar = findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        String passedUserid = intent.getStringExtra("user_id");
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String todoID = intent.getStringExtra("todo_id");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String last_edited = intent.getStringExtra("last_edited");
        String flag = intent.getStringExtra("flag");
        String UserItemUsername = intent.getStringExtra("usernameItem");
        String UserItemUser_id = intent.getStringExtra("user_idItem");
        String UserItemName = intent.getStringExtra("nameItem");

        if(flag.equals("Detail")){
            floatingActionButton.setVisibility(View.GONE);

            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(DetailTodoActivity.this, DetailUserScreenActivity.class)
                            .putExtra("username", passedUsername)
                            .putExtra("user_id", passedUserid)
                            .putExtra("name", passedName)
                            .putExtra("usernameItem",UserItemUsername)
                            .putExtra("user_idItem",UserItemUser_id)
                            .putExtra("nameItem",UserItemName);
                    startActivity(intent2);
                }
            });
        }else if(flag.equals("DetailSearchTodo")){
            floatingActionButton.setVisibility(View.GONE);

            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(DetailTodoActivity.this, SearchTodoActivity.class)
                            .putExtra("username", passedUsername)
                            .putExtra("user_id", passedUserid)
                            .putExtra("name", passedName)
                            .putExtra("usernameItem",UserItemUsername)
                            .putExtra("user_idItem",UserItemUser_id)
                            .putExtra("nameItem",UserItemName);
                    startActivity(intent2);
                }
            });
        }else if(flag.equals("DetailSearchTodoUser")){
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(DetailTodoActivity.this, SearchTodoActivity.class)
                            .putExtra("username", passedUsername)
                            .putExtra("user_id", passedUserid)
                            .putExtra("name", passedName)
                            .putExtra("usernameItem",UserItemUsername)
                            .putExtra("user_idItem",UserItemUser_id)
                            .putExtra("nameItem",UserItemName);
                    startActivity(intent2);
                }
            });
        }else{
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(DetailTodoActivity.this, GetTodoActivity.class)
                            .putExtra("username", passedUsername)
                            .putExtra("user_id", passedUserid)
                            .putExtra("name", passedName);
                    startActivity(intent2);
                }
            });
        }


        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String new_date="";
        try {
            new_date=dateFormat.format(df.parse(last_edited));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvEdited.setText(new_date);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(DetailTodoActivity.this, UpdateTodoActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName)
                        .putExtra("todo_id",todoID)
                        .putExtra("title",title)
                        .putExtra("description",description)
                        .putExtra("last_edited",last_edited)
                        .putExtra("flag",flag);
                startActivity(intent2);
            }
        });
    }
}