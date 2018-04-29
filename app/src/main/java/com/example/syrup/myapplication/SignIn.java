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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;



    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        buttonRegister    = findViewById(R.id.register);

        editTextName      = findViewById(R.id.name);
        editTextSurname   = findViewById(R.id.surname);
        editTextEmail     = findViewById(R.id.email);
        editTextPassword  = findViewById(R.id.password);
        editTextPassword2 = findViewById(R.id.password2);


        buttonRegister.setOnClickListener(this);


        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void registerUser() {
        String name      = editTextName.getText().toString().trim();
        String surname   = editTextSurname.getText().toString().trim();
        String email     = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "enter name", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(surname))
        {
            Toast.makeText(this, "enter surname", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if (TextUtils.isEmpty(password2))
        {
            Toast.makeText(this, "enter password again", Toast.LENGTH_SHORT).show();
            //stopping the function execution
            return;
        }

        if ( !password.equals(password2) )
        {
            Toast.makeText(this, "passwords does not match", Toast.LENGTH_SHORT).show();
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
                            //successful


                            //start activity
                            Toast.makeText(SignIn.this,
                                    "resigtered successfully", Toast.LENGTH_SHORT).show();
                            Intent goMainPage = new Intent(SignIn.this, MainActivity.class);
                            startActivity(goMainPage);
                        }
                        else
                            Toast.makeText(SignIn.this,
                                    "could not register", Toast.LENGTH_SHORT).show();
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
