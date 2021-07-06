package com.example.kepo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kepo.adapter.TodolistAdapter;
import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.modal.Todolist;
import com.example.kepo.request.DeleteTodoRequest;
import com.example.kepo.response.DeleteTodoResponse;
import com.example.kepo.response.GetTodoDetailResponse;
import com.example.kepo.response.GetUserTodosResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTodoActivity extends AppCompatActivity implements View.OnLongClickListener {

    private FloatingActionButton floatingActionButton;
    private ImageButton ibBack;
    private RecyclerView rvTodolist;
    private TodolistAdapter todolistAdapter;
    private TextView tvEmpty;
    private ProgressBar progressBar;
    private ArrayList<Todolist> todolists;
    private ArrayList<Todolist> selectionTodolists;
    private List<String> todos = new ArrayList<>();
    public static boolean isActionMode = false;
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_todo);

        ibBack = findViewById(R.id.ib_back);
        floatingActionButton = findViewById(R.id.btn_add);
        todolists = new ArrayList<>();
        selectionTodolists = new ArrayList<>();
        Intent intent = getIntent();
        String passedName = intent.getStringExtra("name");
        String passedUsername = intent.getStringExtra("username");
        String passedUserid = intent.getStringExtra("user_id");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(GetTodoActivity.this, CreateTodolistActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
                isActionMode=false;
                selectionTodolists.clear();
                counter=0;
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Intent intent2 = new Intent(GetTodoActivity.this, HomeActivity.class)
                        .putExtra("username", passedUsername)
                        .putExtra("user_id", passedUserid)
                        .putExtra("name", passedName);
                startActivity(intent2);
                isActionMode=false;
                selectionTodolists.clear();
                counter=0;
            }
        });
        rvTodolist = findViewById(R.id.rv_todolist);
        tvEmpty = findViewById(R.id.tv_empty);
        progressBar = findViewById(R.id.progress_bar);

        IClickable listener = new IClickable() {
            @Override
            public void onTimeClick(Todolist todolist) {
                progressBar.setVisibility(View.VISIBLE);
                Call<GetTodoDetailResponse> getTodoDetailResponseCall = ApiClient.getUserService().getTodoDetail(passedUserid,todolist.getTodo_id());
                getTodoDetailResponseCall.enqueue(new Callback<GetTodoDetailResponse>(){

                    @Override
                    public void onResponse(Call<GetTodoDetailResponse> call, Response<GetTodoDetailResponse> response) {
                        if(response.isSuccessful()){
                            GetTodoDetailResponse getTodoDetailResponse = response.body();

                            Log.i("User Todo Detresponse: ",getTodoDetailResponse.toString());
                            Intent intent = new Intent(GetTodoActivity.this, DetailTodoActivity.class)
                                    .putExtra("user_id",passedUserid)
                                    .putExtra("username",passedUsername)
                                    .putExtra("name",passedName)
                                    .putExtra("title",getTodoDetailResponse.getData().getTitle())
                                    .putExtra("description",getTodoDetailResponse.getData().getDescription())
                                    .putExtra("todo_id",getTodoDetailResponse.getData().getTodo_id())
                                    .putExtra("last_edited",getTodoDetailResponse.getData().getLast_edited())
                                    .putExtra("flag","");

                            startActivity(intent);

                        }else{
                            Toast.makeText(GetTodoActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(Call<GetTodoDetailResponse> call, Throwable t) {
                        Toast.makeText(GetTodoActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void checkBoxClick(Todolist todolist,View v,int position){
                MakeSelection(v,position,passedUserid,passedUsername,passedName);
            }
        };

        todolists = new ArrayList<>();
        todolistAdapter = new TodolistAdapter(todolists,listener,this);
        rvTodolist.setAdapter(todolistAdapter);
        rvTodolist.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);
        Call<GetUserTodosResponse> getUserTodosResponseCall = ApiClient.getUserService().getTodo(passedUserid);
        getUserTodosResponseCall.enqueue(new Callback<GetUserTodosResponse>(){

            @Override
            public void onResponse(Call<GetUserTodosResponse> call, Response<GetUserTodosResponse> response) {
                if(response.isSuccessful()){
                    GetUserTodosResponse getUserTodosResponse = response.body();
                    List<GetUserTodosResponse.ListTodo> listTodos = getUserTodosResponse.getData().getListTodo();
                    for (GetUserTodosResponse.ListTodo todo : listTodos) {
                        Todolist newTodo = new Todolist();
                        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
                        DateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        String new_date="";
                        try {
                            new_date=dateFormat.format(df.parse(todo.getLast_edited()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        newTodo.setTitle(todo.getTitle());
                        newTodo.setTodo_id(todo.getTodo_id());
                        newTodo.setLastEdited(new_date);
                        todolistAdapter.addTodolist(newTodo);
                    }
                    if(getUserTodosResponse.getData().getListTodo().isEmpty()){
                        tvEmpty.setVisibility(View.VISIBLE);
                    }else{
                        tvEmpty.setVisibility(View.GONE);
                    }
                    Log.i("User Todo response: ",getUserTodosResponse.toString());

                }else{
                    Toast.makeText(GetTodoActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetUserTodosResponse> call, Throwable t) {
                Toast.makeText(GetTodoActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private DeleteTodoRequest toRequest(List<String> todos){
        DeleteTodoRequest deleteTodoRequest = new DeleteTodoRequest();

        deleteTodoRequest.setTodos(todos);

        return deleteTodoRequest;
    }

    public void MakeSelection(View v,int position,String passedUserid,String passedUsername,String passedName){
        if(((CheckBox)v).isChecked()){
            selectionTodolists.add(todolists.get(position));
            counter++;

        }else{
            selectionTodolists.remove(todolists.get(position));
            counter--;
        }
        UpdateCounter(v,position,passedUserid,passedUsername,passedName,selectionTodolists);
    }


    public void UpdateCounter(View v, int position, String passedUserid, String passedUsername, String passedName, List<Todolist> todolist){
        List<String> todoIds = new ArrayList<>();
        for (Todolist todo : todolist) {
            todoIds.add(todo.getTodo_id());
        }
        todos.addAll(todoIds);
        Snackbar.make(v,counter+" item(s)",Snackbar.LENGTH_LONG)
                .setAction("DELETE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GetTodoActivity.this);

                        alertDialogBuilder.setTitle("Delete Todo");
                        alertDialogBuilder
                                .setMessage("Are you sure want to delete all this todos?")
                                .setCancelable(false)
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.i("Todos: ", todos.toString());
                                        DeleteTodoRequest deleteTodoRequest = toRequest(todos);
                                        progressBar.setVisibility(View.VISIBLE);
                                        Call<DeleteTodoResponse> deleteTodoResponseCall = ApiClient.getUserService().deleteTodo(deleteTodoRequest,passedUserid);
                                        Log.d("CEK USER ID : ", passedUserid);
                                        deleteTodoResponseCall.enqueue(new Callback<DeleteTodoResponse>(){
                                            @Override
                                            public void onResponse(Call<DeleteTodoResponse> call, Response<DeleteTodoResponse> response) {
                                                Log.i("TES", response.toString());
                                                if(response.isSuccessful()){
                                                    DeleteTodoResponse deleteTodoResponse = response.body();
                                                    Log.i("DELETE Todo response: ",deleteTodoResponse.toString());
                                                    Toast.makeText(GetTodoActivity.this,"Delete Todo Success!", Toast.LENGTH_LONG).show();
                                                    Intent intent = getIntent();
                                                    Intent intent2 = new Intent(GetTodoActivity.this, GetTodoActivity.class)
                                                            .putExtra("username", passedUsername)
                                                            .putExtra("user_id", passedUserid)
                                                            .putExtra("name", passedName);
                                                    startActivity(intent2);
                                                }else{
                                                    Toast.makeText(GetTodoActivity.this,"Todo Not Found!", Toast.LENGTH_LONG).show();

                                                }
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            @Override
                                            public void onFailure(Call<DeleteTodoResponse> call, Throwable t) {
                                                Toast.makeText(GetTodoActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        todolistAdapter.RemoveItem(selectionTodolists);
                                        isActionMode=false;
                                        counter=0;
                                        todolistAdapter.updateList(selectionTodolists);
                                        selectionTodolists.clear();
//                                        todolists.remove(position);
//                                        todolistAdapter.notifyItemRemoved(position);
//                                        todolistAdapter.notifyItemRangeChanged(position,todolists.size());
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
                })
                .setActionTextColor(Color.RED)
                .show();
    }

    @Override
    public boolean onLongClick(View v) {
        isActionMode=true;
        todolistAdapter.notifyDataSetChanged();
        return true;
    }


}