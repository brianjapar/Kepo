package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepo.adapter.TodolistAdapter;
import com.example.kepo.adapter.UserDetailTodolistAdapter;
import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.modal.Todolist;
import com.example.kepo.modal.UserTodolistDetail;
import com.example.kepo.response.GetTodoDetailResponse;
import com.example.kepo.response.GetUserTodosResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserScreenActivity extends AppCompatActivity {

    private TextView tvUsername,tvName,tvTodos;
    private RecyclerView rvTodolist;
    private ImageButton ibBack;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private ArrayList<UserTodolistDetail> todolists;
    private UserDetailTodolistAdapter userDetailTodolistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_screen);

        tvUsername = findViewById(R.id.tv_username);
        tvName = findViewById(R.id.tv_name);
        rvTodolist = findViewById(R.id.rv_todolist);
        ibBack = findViewById(R.id.ib_back);
        progressBar = findViewById(R.id.progress_bar);
        tvTodos = findViewById(R.id.tv_todos);
        tvEmpty = findViewById(R.id.tv_empty);

        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");
        String UserItemUsername = intent.getStringExtra("usernameItem");
        String UserItemUser_id = intent.getStringExtra("user_idItem");
        String UserItemName = intent.getStringExtra("nameItem");
        tvUsername.setText(UserItemUsername);
        tvName.setText(UserItemName);


        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailUserScreenActivity.this, SearchUserActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);

            }
        });

        IClickableUserTodo listener = new IClickableUserTodo() {
            @Override
            public void onTimeClick(UserTodolistDetail todolist) {
                Call<GetTodoDetailResponse> getTodoDetailResponseCall = ApiClient.getUserService().getTodoDetail(UserItemUser_id,todolist.getTodo_id());
                getTodoDetailResponseCall.enqueue(new Callback<GetTodoDetailResponse>(){

                    @Override
                    public void onResponse(Call<GetTodoDetailResponse> call, Response<GetTodoDetailResponse> response) {
                        if(response.isSuccessful()){
                            GetTodoDetailResponse getTodoDetailResponse = response.body();

                            Log.i("User Todo Detresponse: ",getTodoDetailResponse.toString());
                            Intent intent = new Intent(DetailUserScreenActivity.this, DetailTodoActivity.class)
                                    .putExtra("user_id",passedUserid)
                                    .putExtra("username",passedUsername)
                                    .putExtra("name",passedName)
                                    .putExtra("title",getTodoDetailResponse.getData().getTitle())
                                    .putExtra("description",getTodoDetailResponse.getData().getDescription())
                                    .putExtra("todo_id",getTodoDetailResponse.getData().getTodo_id())
                                    .putExtra("last_edited",getTodoDetailResponse.getData().getLast_edited())
                                    .putExtra("flag","Detail")
                                    .putExtra("usernameItem",UserItemUsername)
                                    .putExtra("user_idItem",UserItemUser_id)
                                    .putExtra("nameItem",UserItemName);

                            startActivity(intent);

                        }else{
                            Toast.makeText(DetailUserScreenActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<GetTodoDetailResponse> call, Throwable t) {
                        Toast.makeText(DetailUserScreenActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        todolists = new ArrayList<>();
        userDetailTodolistAdapter = new UserDetailTodolistAdapter(todolists,listener,this);
        rvTodolist.setAdapter(userDetailTodolistAdapter);
        rvTodolist.setLayoutManager(new LinearLayoutManager(this));


        progressBar.setVisibility(View.VISIBLE);
        Call<GetUserTodosResponse> getUserTodosResponseCall = ApiClient.getUserService().getTodo(UserItemUser_id);
        getUserTodosResponseCall.enqueue(new Callback<GetUserTodosResponse>(){

            @Override
            public void onResponse(Call<GetUserTodosResponse> call, Response<GetUserTodosResponse> response) {
                if(response.isSuccessful()){

                    GetUserTodosResponse getUserTodosResponse = response.body();
                    if(getUserTodosResponse.getData().getListTodo()==null){
                        Toast.makeText(DetailUserScreenActivity.this,"No Data!", Toast.LENGTH_LONG).show();
                    }else{
                        List<GetUserTodosResponse.ListTodo> listTodos = getUserTodosResponse.getData().getListTodo();
                        for (GetUserTodosResponse.ListTodo todo : listTodos) {
                            UserTodolistDetail newTodo = new UserTodolistDetail();
                            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                            DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            String new_date="";
                            try {
                                new_date=dateFormat.format(df.parse(todo.getLast_edited()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            newTodo.setName(UserItemName);
                            newTodo.setTitle(todo.getTitle());
                            newTodo.setTodo_id(todo.getTodo_id());
                            newTodo.setLastEdited(new_date);
                            userDetailTodolistAdapter.addTodolist(newTodo);
                        }

                        Log.i("User Todo response: ",getUserTodosResponse.toString());
                        Log.i("Username", passedUsername);

                    }

                }else{
                    Toast.makeText(DetailUserScreenActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);
                tvTodos.setText("Todos: "+userDetailTodolistAdapter.getItemCount());
                if(userDetailTodolistAdapter.getItemCount()==0){
                    tvEmpty.setVisibility(View.VISIBLE);
                }else{
                    tvEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<GetUserTodosResponse> call, Throwable t) {
                Toast.makeText(DetailUserScreenActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}