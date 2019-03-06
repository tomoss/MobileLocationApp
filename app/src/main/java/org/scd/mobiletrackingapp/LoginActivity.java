package org.scd.mobiletrackingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.scd.mobiletrackingapp.model.ResUser;
import org.scd.mobiletrackingapp.remote.RetrofitClient;
import org.scd.mobiletrackingapp.remote.UserService;

import org.scd.mobiletrackingapp.model.dto.UserLoginDTO;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(validateLogin(email, password)){
                    //do login
                    doLogin(email, password);
                }
            }
        });
    }

    private boolean validateLogin(String email, String password){
        if(email == null || email.trim().length() == 0){
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String email,final String password){


        UserLoginDTO userLogin = new UserLoginDTO(email,password);
        final String credentials = Credentials.basic(email,password);

        String BASE_URL = "http://192.168.137.1:8080/";
        RetrofitClient client = new RetrofitClient(BASE_URL);
        client.getServices().login(userLogin, credentials).enqueue(new Callback<ResUser>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        ResUser resUser = (ResUser) response.body();
                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        intent.putExtra("credentials",credentials);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "The email or password are incorrect", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
