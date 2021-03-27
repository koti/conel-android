package com.example.conelshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText textInputEditTextEmail;
    Button fpemail;
    TextView textViewLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        textInputEditTextEmail = findViewById(R.id.email);
        progressBar = findViewById(R.id.progress);
        fpemail= findViewById(R.id.fpemail);
        textViewLogin = findViewById(R.id.loginText);


        textViewLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });



        fpemail.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
            final String email;
            email = String.valueOf(textInputEditTextEmail.getText());

            if(!email.equals("") ) {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    public void run() {
                        String[] field = new String[1];
                        field[0] = "email";

                        String[] data = new String[1];
                        data[0] = email;

                        PutData putData = new PutData("http://192.168.0.9/AndroidAppDatabaseConnection/login.php", "POST", field, data);

                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                progressBar.setVisibility(View.GONE);
                                String result = putData.getResult();
                                if(result.equals("Reset Email Sent!")) {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
            }
        }
    });



    }
}