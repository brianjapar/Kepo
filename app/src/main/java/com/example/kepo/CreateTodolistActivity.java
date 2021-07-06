package com.example.kepo;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.request.CreateTodoRequest;
import com.example.kepo.response.CreateTodoResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTodolistActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private EditText etTitle,etDescription;
    private TextView tvChar,tvMessage;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    int characters=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todolist);

        ibBack = findViewById(R.id.ib_back);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        floatingActionButton = findViewById(R.id.btn_add);
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
                if( characters>100){
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
        
        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");
        
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(CreateTodolistActivity.this, GetTodoActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();

                if(characters>100){
                    Toast.makeText(CreateTodolistActivity.this,"Your description exceded the maximum words",Toast.LENGTH_SHORT).show();
                }else if (title.isEmpty() || description.isEmpty() ) {
                    tvMessage.setText("Text field cannot be empty");
//                    Toast.makeText(CreateTodolistActivity.this,"Text field cannot be empty",Toast.LENGTH_SHORT).show();
                }else{
                    CreateTodoRequest createTodoRequest = toRequest(title,description);
                    progressBar.setVisibility(View.VISIBLE);
                    Call<CreateTodoResponse> createTodoResponseCall = ApiClient.getUserService().createTodo(createTodoRequest,passedUserid);
                    createTodoResponseCall.enqueue(new Callback<CreateTodoResponse>(){

                        @Override
                        public void onResponse(Call<CreateTodoResponse> call, Response<CreateTodoResponse> response) {
                            if(response.isSuccessful()){
                                CreateTodoResponse createTodoResponse = response.body();
                                Log.i("Todo response: ",createTodoResponse.toString());
                                Toast.makeText(CreateTodolistActivity.this,"Todo Created Successfull!", Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                Intent intent2 = new Intent(CreateTodolistActivity.this, GetTodoActivity.class)
                                        .putExtra("username", passedUsername)
                                        .putExtra("user_id", passedUserid)
                                        .putExtra("name", passedName);
                                startActivity(intent2);
                            }else{
                                Toast.makeText(CreateTodolistActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onFailure(Call<CreateTodoResponse> call, Throwable t) {
                            Toast.makeText(CreateTodolistActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private CreateTodoRequest toRequest(String title, String description){
        CreateTodoRequest createTodoRequest = new CreateTodoRequest();

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        Date date = new Date();
        String dateStr = timeStampFormat.format(date);

        createTodoRequest.setTitle(title);
        createTodoRequest.setDescription(description);
        createTodoRequest.setLastEdited("Last Edited : "+dateStr);
        return createTodoRequest;
    }
}