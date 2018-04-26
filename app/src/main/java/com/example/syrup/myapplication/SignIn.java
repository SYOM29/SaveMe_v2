package com.example.syrup.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.syrup.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        buttonRegister = findViewById(R.id.register);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);


        buttonRegister.setOnClickListener(this);


        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void registerUser() {
        String email    = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        //if validations are ok ->

        progressDialog.setMessage("registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful() )
                        {
                            //succesful
                            //start activity
                            Toast.makeText(SignIn.this,
                                    "resigtered successfully", Toast.LENGTH_SHORT).show();
                            Intent goMainPage = new Intent(SignIn.this, MainActivity.class);
                            startActivity(goMainPage);
                        }
                        else
                            Toast.makeText(SignIn.this,
                                    "resigtered failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if ( view == buttonRegister )
        {
            registerUser();
        }


    }
}
