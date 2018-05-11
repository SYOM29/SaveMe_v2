package com.example.syrup.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syrup.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This class creates login page for the user
 * @author SYRUP group: Siyovush Kadyrov, Emre Tolga Ayan, Atakan Bora Karacalioglu, Can Aybalik, Sertac Cebeci, Noman Aslam
 * @version 1.0
 */
public class Login extends AppCompatActivity implements View.OnClickListener {


    //properties
    private TextView textViewSignUp;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewSignUp = findViewById(R.id.signUp);
        loginButton = findViewById(R.id.loginButton);
        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        textViewSignUp.setOnClickListener(this);
        loginButton.setOnClickListener(this);


    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //successful
                            //start activity
                            Toast.makeText(Login.this,
                                    "login successfully", Toast.LENGTH_SHORT).show();
                            Intent goMainPage = new Intent(Login.this, MainActivity.class);
                            startActivity(goMainPage);
                        } else
                            Toast.makeText(Login.this,
                                    "login failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onClick(View view) {

        if (view == textViewSignUp)
        {
            Intent edit = new Intent( Login.this,SignIn.class);
            startActivity(edit);

        } else if (view == loginButton) {
            userLogin();
        }
    }
}
