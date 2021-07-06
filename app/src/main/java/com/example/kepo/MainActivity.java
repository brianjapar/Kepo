package com.example.kepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kepo.apihelper.ApiClient;
import com.example.kepo.databinding.ActivityMainBinding;
import com.example.kepo.request.LoginRequest;
import com.example.kepo.response.LoginResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private SharedPref sharedPref;
    private ProgressBar progressBar;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty() ) {
                    showBottomSheet("Please input username and Password");
                }else{
                    login();
                }

            }
        });
    }

    private LoginRequest toUser(String username, String password){
        LoginRequest user = new LoginRequest();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public void login(){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        LoginRequest user = toUser(username,password);

        progressBar.setVisibility(View.VISIBLE);
        Call<LoginResponse> loginResponseCall = ApiClient.getUserService().userLogin(user);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    LoginResponse loginResponse = response.body();
                    Log.i("Login response: ",loginResponse.toString());
                    if(null==loginResponse.getData()){
                        showBottomSheet(loginResponse.getMessage());
                    }else if(loginResponse.getMessage().equals("Username or password is incorrect")){
                        showBottomSheet("Username or password is incorrect");
                    }else if(loginResponse.getMessage().equals("Something wrong occured while logging in")){
//                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        showBottomSheet("User not found");
                    }else if(loginResponse.getMessage().equals("Login success")){
                       sharedPref = new SharedPref(MainActivity.this);
                       LoginResponse.Data data = response.body().getData();
                       sharedPref.createLoginSession(data);

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class)
                                .putExtra("username", loginResponse.getData().getUsername())
                                .putExtra("user_id", loginResponse.getData().getUser_id())
                                .putExtra("name", loginResponse.getData().getName());
                        startActivity(intent);
                    }
                }else{
//                    Toast.makeText(MainActivity.this,"User not found", Toast.LENGTH_LONG).show();
                    showBottomSheet("User not found");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showBottomSheet(String text){
        TextView tvBsText;
        BottomSheetDialog bottomSheetDialog= new BottomSheetDialog(MainActivity.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_sheet_layout,(LinearLayout)findViewById(R.id.bs_container));

        bottomSheetView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        tvBsText = bottomSheetView.findViewById(R.id.tv_bsText);
        tvBsText.setText(text);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }
}