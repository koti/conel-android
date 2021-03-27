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

public class CreateNewPassword extends AppCompatActivity {


    TextInputEditText textInputEditTextPassword,textInputEditConfPassword;
    Button submitbutton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);


        textInputEditTextPassword=findViewById(R.id.newpassword);
        textInputEditConfPassword=findViewById(R.id.confirmnewpass);
        submitbutton=findViewById(R.id.submitbutton);
        progressBar=findViewById(R.id.progress);


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newpassword,confirmnewpass;
                newpassword= String.valueOf(textInputEditTextPassword.getText());
                confirmnewpass= String.valueOf(textInputEditConfPassword.getText());


                if (!newpassword.equals("") && !confirmnewpass.equals("") ) {
                    if (newpassword==confirmnewpass) {
                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[2];
                                field[0] = "newpassword";
                                field[1] = "confirmnewpass";

                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = newpassword;
                                data[1] = confirmnewpass;

                                PutData putData = new PutData("", "POST", field, data);

                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        if (result.equals("Password Changed Succesfully")) {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                            }
                        });
                    }else  Toast.makeText(getApplicationContext(),"Passwords do not match!Try again.",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),"All fields required",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}