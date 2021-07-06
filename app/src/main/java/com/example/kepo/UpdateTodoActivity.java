package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.request.CreateTodoRequest;
import com.example.kepo.request.UpdateTodoRequest;
import com.example.kepo.response.CreateTodoResponse;
import com.example.kepo.response.UpdateTodoResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTodoActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private EditText etTitle,etDescription;
    private TextView tvChar,tvMessage;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    int characters=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_todo);

        ibBack = findViewById(R.id.ib_back);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        floatingActionButton = findViewById(R.id.btn_edit);
        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");
        String todoID = intent.getStringExtra("todo_id");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String last_edited = intent.getStringExtra("last_edited");
        String flag = intent.getStringExtra("flag");

        tvChar=findViewById(R.id.tv_char);
        tvMessage=findViewById(R.id.tv_message);
        progressBar = findViewById(R.id.progress_bar);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etDescription.getText().toString();
                characters = text.length();
                tvChar.setText( characters+"/100");
                if(characters>100){
                    tvChar.setTextColor(Color.RED);
                    tvMessage.setText("Your description exceded the maximum words");
                }else{
                    tvChar.setTextColor(Color.LTGRAY);
                    tvMessage.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTitle.setText(title);
        etDescription.setText(description);
        if(flag.equals("Detail")){
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    Intent intent2 = new Intent(UpdateTodoActivity.this, DetailTodoActivity.class)
                            .putExtra("username", passedUsername)
                            .putExtra("user_id", passedUserid)
                            .putExtra("name", passedName)
                            .putExtra("todo_id",todoID)
                            .putExtra("title",title)
                            .putExtra("description",description)
                            .putExtra("last_edited",last_edited)
                            .putExtra("flag","");
                    startActivity(intent2);
                }
            });
        }else{
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    Intent intent2 = new Intent(UpdateTodoActivity.this, DetailTodoActivity.class)
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


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                if(characters>100){
                    Toast.makeText(UpdateTodoActivity.this,"Your description exceded the maximum words",Toast.LENGTH_SHORT).show();
                }else if (title.isEmpty() || description.isEmpty() ) {
                    tvMessage.setText("Text field cannot be empty");
//                    Toast.makeText(UpdateTodoActivity.this,"Text field cannot be empty",Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    UpdateTodoRequest updateTodoRequest = toRequest(title,description);
                    Call<UpdateTodoResponse> updateTodoResponseCall = ApiClient.getUserService().updateTodo(updateTodoRequest,passedUserid,todoID);
                    Log.d("CEK USER ID & TODO ID", passedUserid + " "+todoID);
                    updateTodoResponseCall.enqueue(new Callback<UpdateTodoResponse>(){
                        @Override
                        public void onResponse(Call<UpdateTodoResponse> call, Response<UpdateTodoResponse> response) {
                            if(response.isSuccessful()){
                                UpdateTodoResponse updateTodoResponse = response.body();
                                Log.i("update Todo response: ",updateTodoResponse.toString());
                                Toast.makeText(UpdateTodoActivity.this,"Update Todo Success!", Toast.LENGTH_LONG).show();

                                Intent intent2 = new Intent(UpdateTodoActivity.this, DetailTodoActivity.class)
                                        .putExtra("username", passedUsername)
                                        .putExtra("user_id", passedUserid)
                                        .putExtra("name", passedName)
                                        .putExtra("todo_id",todoID)
                                        .putExtra("title",title)
                                        .putExtra("description",description)
                                        .putExtra("last_edited",last_edited)
                                        .putExtra("flag","");
                                startActivity(intent2);
                            }else{
                                Toast.makeText(UpdateTodoActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onFailure(Call<UpdateTodoResponse> call, Throwable t) {
                            Toast.makeText(UpdateTodoActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private UpdateTodoRequest toRequest(String title, String description){
        UpdateTodoRequest updateTodoRequest = new UpdateTodoRequest();

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date date = new Date();
        String dateStr = timeStampFormat.format(date);

        updateTodoRequest.setTitle(title);
        updateTodoRequest.setDescription(description);
        updateTodoRequest.setLast_edited("Last Edited : "+dateStr);
        return updateTodoRequest;
    }
}