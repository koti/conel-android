package com.example.conelshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import static android.app.ProgressDialog.show;

public class Register extends AppCompatActivity {


    TextInputEditText textInputEditTextfirstname,textInputEditTextlastname,textInputEditTextEmail,textInputEditTextPassword;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputEditTextfirstname=findViewById(R.id.firstname);
        textInputEditTextlastname=findViewById(R.id.lastname);
        textInputEditTextEmail=findViewById(R.id.email);
        textInputEditTextPassword=findViewById(R.id.password);
        buttonSignUp=findViewById(R.id.buttonSignUp);
        textViewLogin=findViewById(R.id.loginText);
        progressBar=findViewById(R.id.progress);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname,lastname,email,password;
                firstname= String.valueOf(textInputEditTextfirstname.getText());
                lastname= String.valueOf(textInputEditTextlastname.getText());
                email= String.valueOf(textInputEditTextEmail.getText());
                password= String.valueOf(textInputEditTextPassword.getText());


                if (!firstname.equals("") && !lastname.equals("") && !email.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[4];
                            field[0] = "firstname";
                            field[1] = "lastname";
                            field[2] = "email";
                            field[3] = "password";

                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = firstname;
                            data[1] = lastname;
                            data[2] = email;
                            data[3] = password;


                            PutData putData = new PutData("http://192.168.0.8/AndroidAppDatabaseConnection/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Login.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"All fields required",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
