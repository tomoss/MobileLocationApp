package org.scd.mobiletrackingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.scd.mobiletrackingapp.model.ResUser;
import org.scd.mobiletrackingapp.model.UserRegister;
import org.scd.mobiletrackingapp.remote.RetrofitClient;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    EditText edtFirstName;
    EditText edtLastName;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtFirstName = (EditText) findViewById(R.id.edtFirstname);
        edtLastName = (EditText) findViewById(R.id.edtLastname);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String firstName = edtFirstName.getText().toString();
                String lastName = edtLastName.getText().toString();

                if(validateRegister(email, password,firstName,lastName)){
                    //do login
                    doRegister(email, password,firstName,lastName);
                }

            }
        });
    }

    private boolean validateRegister(String email, String password, String firstName, String lastName){
        if(email == null || email.trim().length() == 0){
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(firstName == null || firstName.trim().length() == 0){
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(lastName == null || lastName.trim().length() == 0){
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doRegister(String email, String password, String firstName, String lastName){



        UserRegister userRegister = new UserRegister();
        userRegister.setEmail(email);
        userRegister.setPassword(password);
        userRegister.setFirstName(firstName);
        userRegister.setLastName(lastName);

        String BASE_URL = "http://192.168.100.2:8080/";
        RetrofitClient client = new RetrofitClient(BASE_URL);

        client.getServices().register(userRegister).enqueue(new Callback<ResUser>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Succes !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
