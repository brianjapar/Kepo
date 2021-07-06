package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepo.adapter.SearchTodolistAdapter;
import com.example.kepo.adapter.UserAdapter;
import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.modal.SearchTodolistUserDetail;
import com.example.kepo.modal.Todolist;
import com.example.kepo.modal.User;
import com.example.kepo.modal.UserTodolistDetail;
import com.example.kepo.request.SearchTodoRequest;
import com.example.kepo.request.SearchUserRequest;
import com.example.kepo.response.GetTodoDetailResponse;
import com.example.kepo.response.GetUserTodosResponse;
import com.example.kepo.response.SearchTodoResponse;
import com.example.kepo.response.SearchUserResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTodoActivity extends AppCompatActivity {

    private ImageButton ibBack,ibSearch;
    private EditText etText;
    private ProgressBar progressBar;
    private RecyclerView rvTodo;
    private TextView tvEmpty;
    private SearchTodolistAdapter searchTodolistAdapter;
    private ArrayList<SearchTodolistUserDetail> todolists;
    private TextView tvResult;
    private CheckBox cbUser,cbTodo;
    int checkboxUser=0;
    int checkboxTodo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_todo);

        ibBack = findViewById(R.id.ib_back);
        ibSearch=findViewById(R.id.ib_search);
        etText = findViewById(R.id.et_text);
        progressBar = findViewById(R.id.progress_bar);
        rvTodo = findViewById(R.id.rv_todolist);
        tvResult = findViewById(R.id.tv_result);
        cbUser = findViewById(R.id.cb_byUser);
        cbTodo = findViewById(R.id.cb_byTodo);
        tvEmpty = findViewById(R.id.tv_empty);

        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SearchTodoActivity.this, HomeActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);

            }
        });

        IClickableSearchTodo listener = new IClickableSearchTodo() {
            @Override
            public void onTimeClick(SearchTodolistUserDetail todolist) {
                Call<GetTodoDetailResponse> getTodoDetailResponseCall = ApiClient.getUserService().getTodoDetail(todolist.getUser_id(),todolist.getTodo_id());
                getTodoDetailResponseCall.enqueue(new Callback<GetTodoDetailResponse>(){

                    @Override
                    public void onResponse(Call<GetTodoDetailResponse> call, Response<GetTodoDetailResponse> response) {
                        if(response.isSuccessful()){
                            GetTodoDetailResponse getTodoDetailResponse = response.body();
                            Log.i("User Todo Detresponse: ",getTodoDetailResponse.toString());
                            if(getTodoDetailResponse.getData().getUsername().equals(passedUsername)){
                                Intent intent = new Intent(SearchTodoActivity.this, DetailTodoActivity.class)
                                        .putExtra("user_id",passedUserid)
                                        .putExtra("username",passedUsername)
                                        .putExtra("name",passedName)
                                        .putExtra("title",getTodoDetailResponse.getData().getTitle())
                                        .putExtra("description",getTodoDetailResponse.getData().getDescription())
                                        .putExtra("todo_id",getTodoDetailResponse.getData().getTodo_id())
                                        .putExtra("last_edited",getTodoDetailResponse.getData().getLast_edited())
                                        .putExtra("flag","DetailSearchTodoUser")
                                        .putExtra("usernameItem",todolist.getUsername())
                                        .putExtra("user_idItem",todolist.getUser_id())
                                        .putExtra("nameItem",todolist.getName());

                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(SearchTodoActivity.this, DetailTodoActivity.class)
                                        .putExtra("user_id",passedUserid)
                                        .putExtra("username",passedUsername)
                                        .putExtra("name",passedName)
                                        .putExtra("title",getTodoDetailResponse.getData().getTitle())
                                        .putExtra("description",getTodoDetailResponse.getData().getDescription())
                                        .putExtra("todo_id",getTodoDetailResponse.getData().getTodo_id())
                                        .putExtra("last_edited",getTodoDetailResponse.getData().getLast_edited())
                                        .putExtra("flag","DetailSearchTodo")
                                        .putExtra("usernameItem",todolist.getUsername())
                                        .putExtra("user_idItem",todolist.getUser_id())
                                        .putExtra("nameItem",todolist.getName());

                                startActivity(intent);
                            }

                        }else{
                            Toast.makeText(SearchTodoActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<GetTodoDetailResponse> call, Throwable t) {
                        Toast.makeText(SearchTodoActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        todolists = new ArrayList<>();
        searchTodolistAdapter = new SearchTodolistAdapter(todolists,listener,this);
        rvTodo.setAdapter(searchTodolistAdapter);
        rvTodo.setLayoutManager(new LinearLayoutManager(this));




        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTodolistAdapter.RemoveItem();
                String text = etText.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (text.isEmpty()) {
                    Toast.makeText(SearchTodoActivity.this,"Text field cannot be empty",Toast.LENGTH_SHORT).show();
                }if(cbTodo.isChecked()){
                    checkboxTodo =1;
                }if(cbUser.isChecked()){
                    checkboxUser = 1;
                }if(checkboxTodo == 0 && checkboxUser==0){
                    Toast.makeText(SearchTodoActivity.this,"You must choose either to search by user todo,or both",Toast.LENGTH_SHORT).show();
                }if(!text.isEmpty()){
                    tvResult.setText("Result for '"+text+"'");
                    SearchTodoRequest searchTodoRequest = toRequest(text,checkboxUser,checkboxTodo);
                    Call<SearchTodoResponse> searchTodoResponseCall = ApiClient.getUserService().searchTodo(searchTodoRequest);
                    Log.d("CEK USER ID : ", passedUserid);
                    searchTodoResponseCall.enqueue(new Callback<SearchTodoResponse>(){
                        @Override
                        public void onResponse(Call<SearchTodoResponse> call, Response<SearchTodoResponse> response) {

                            if(response.isSuccessful()){
                                SearchTodoResponse searchTodoResponse = response.body();
                                Log.i("Search Todo response: ",searchTodoResponse.toString());
                                if(searchTodoResponse.getData()==null){
                                    Toast.makeText(SearchTodoActivity.this,"User Not Found!", Toast.LENGTH_LONG).show();
                                }else{
                                    List<SearchTodoResponse.Data> listTodo = searchTodoResponse.getData();
                                    for (SearchTodoResponse.Data todo : listTodo) {
                                        SearchTodolistUserDetail newList = new SearchTodolistUserDetail();
                                        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                        DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        String new_date="";
                                        try {
                                            new_date=dateFormat.format(df.parse(todo.getLast_edited()));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        newList.setName(todo.getUsername());
                                        newList.setLastEdited(new_date);
                                        newList.setDescription(todo.getDescription());
                                        newList.setTitle(todo.getTitle());
                                        newList.setUser_id(todo.getUser_id());
                                        newList.setTodo_id(todo.getTodo_id());
                                        searchTodolistAdapter.addTodolist(newList);
                                    }
                                }
                                if(searchTodoResponse.getData().isEmpty()){
                                    tvEmpty.setVisibility(View.VISIBLE);
                                }else{
                                    tvEmpty.setVisibility(View.GONE);
                                }
                            }else{
                                Toast.makeText(SearchTodoActivity.this,"Todolist Not Found!", Toast.LENGTH_LONG).show();

                            }

                            progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onFailure(Call<SearchTodoResponse> call, Throwable t) {
                            Toast.makeText(SearchTodoActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(SearchTodoActivity.this,"Text field cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private SearchTodoRequest toRequest(String searchQuery, int filterUser, int filterTodo){
        SearchTodoRequest searchTodoRequest = new SearchTodoRequest();

        searchTodoRequest.setFilterTodo(filterTodo);
        searchTodoRequest.setFilterUser(filterUser);
        searchTodoRequest.setSearchQuery(searchQuery);

        return searchTodoRequest;
    }


}