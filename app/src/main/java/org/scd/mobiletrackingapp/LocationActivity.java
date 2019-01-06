package org.scd.mobiletrackingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_location);

        txtEmail = (TextView) findViewById(R.id.txtEmail);

        Bundle extras = getIntent().getExtras();
        String email;

        if(extras != null){
            email = extras.getString("email");
            txtEmail.setText("Welcome "+email);
        }
    }
}
