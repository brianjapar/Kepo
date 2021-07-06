package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepo.adapter.TodolistAdapter;
import com.example.kepo.adapter.UserAdapter;
import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.modal.Todolist;
import com.example.kepo.modal.User;
import com.example.kepo.request.DeleteTodoRequest;
import com.example.kepo.request.SearchUserRequest;
import com.example.kepo.response.DeleteTodoResponse;
import com.example.kepo.response.GetUserTodosResponse;
import com.example.kepo.response.SearchUserResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserActivity extends AppCompatActivity {

    private ImageButton ibBack,ibSearch;
    private EditText etText;
    private ProgressBar progressBar;
    private RecyclerView rvUser;
    private TextView tvEmpty;
    private UserAdapter userAdapter;
    private ArrayList<User> users;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        ibBack = findViewById(R.id.ib_back);
        ibSearch=findViewById(R.id.ib_search);
        etText = findViewById(R.id.et_text);
        progressBar = findViewById(R.id.progress_bar);
        rvUser = findViewById(R.id.rv_user);
        tvResult = findViewById(R.id.tv_result);
        tvEmpty = findViewById(R.id.tv_empty);

        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SearchUserActivity.this, HomeActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);

            }
        });

        IClickableUser listener = new IClickableUser() {
            @Override
            public void onClickUser(User user) {
                Intent intent2 = new Intent(SearchUserActivity.this, DetailUserScreenActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName)
                        .putExtra("usernameItem",user.getUsername())
                        .putExtra("user_idItem",user.getUser_id())
                        .putExtra("nameItem",user.getName());

                startActivity(intent2);
            }
        };
        users = new ArrayList<>();
        userAdapter = new UserAdapter(users,listener,this);
        rvUser.setAdapter(userAdapter);
        rvUser.setLayoutManager(new LinearLayoutManager(this));

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAdapter.RemoveItem();
                String text = etText.getText().toString();
                if(text.isEmpty()){
                    Toast.makeText(SearchUserActivity.this,"Text field cannot be empty",Toast.LENGTH_SHORT).show();
                }
                if(!text.isEmpty()){
                    tvResult.setText("Result for '"+text+"'");
                    progressBar.setVisibility(View.VISIBLE);
                    SearchUserRequest searchUserRequest = toRequest(passedUserid,text);
                    Call<SearchUserResponse> searchUserResponseCall = ApiClient.getUserService().searchUser(searchUserRequest);
                    Log.d("CEK USER ID : ", passedUserid);
                    searchUserResponseCall.enqueue(new Callback<SearchUserResponse>(){
                        @Override
                        public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {

                            if(response.isSuccessful()){
                                SearchUserResponse searchUserResponse = response.body();
                                Log.i("Search user response: ",searchUserResponse.toString());
                                if(searchUserResponse.getData()==null){
                                    Toast.makeText(SearchUserActivity.this,"User Not Found!", Toast.LENGTH_LONG).show();
                                }else{
                                    List<SearchUserResponse.Data> listUser = searchUserResponse.getData();
                                    for (SearchUserResponse.Data user : listUser) {
                                        User newUser = new User();
                                        newUser.setName(user.getName());
                                        newUser.setUsername(user.getUsername());
                                        newUser.setUser_id(user.getUser_id());
                                        userAdapter.addUser(newUser);
                                    }
                                }
                                if(searchUserResponse.getData().isEmpty()){
                                    tvEmpty.setVisibility(View.VISIBLE);
                                }else{
                                    tvEmpty.setVisibility(View.GONE);
                                }
                            }else{
                                Toast.makeText(SearchUserActivity.this,"User Not Found!", Toast.LENGTH_LONG).show();

                            }
                            Log.i("Username", passedUsername);
                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                            Toast.makeText(SearchUserActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    private SearchUserRequest toRequest(String user_id, String searchQuery){
        SearchUserRequest searchUserRequest = new SearchUserRequest();

        searchUserRequest.setUser_id(user_id);
        searchUserRequest.setSearchQuery(searchQuery);

        return searchUserRequest;
    }
}