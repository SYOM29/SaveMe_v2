package com.example.syrup.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.syrup.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private final String NAME = "Name";
    private final String SURNAME = "Surname";
    private final String EMAIL = "Email";
    private final String GROUPCODE = "GroupCode";
    private final String TAG = "userInfo";
    private Button buttonRegister;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference mDocRef;


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
        
        firebaseFirestore = FirebaseFirestore.getInstance();
        mDocRef = FirebaseFirestore.getInstance().document("users/userInfo");
    }

    private void registerUser() {
        final String name      = editTextName.getText().toString().trim();
        final String surname   = editTextSurname.getText().toString().trim();
        final String email     = editTextEmail.getText().toString().trim();
        final String password  = editTextPassword.getText().toString().trim();
        final String password2 = editTextPassword2.getText().toString().trim();
        final String groupCode = generateRandomCode();
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
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            Map<String, Object> dataToSave = new HashMap<String, Object>();
                            dataToSave.put(NAME, name);
                            dataToSave.put(SURNAME, surname);
                            dataToSave.put(EMAIL, email);

                            //add user infos
                            firebaseFirestore.collection("users").document( currentUser.getUid())
                                .set(dataToSave)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully saved!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                            Map<String, Object> groups = new HashMap<String, Object>();
                            groups.put( name + "'s group " , groupCode );

                            //add groups under user
                            firebaseFirestore.collection("users").document( currentUser.getUid())
                                    .collection("groups").document(groupCode)
                                    .set(groups)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully saved!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            Map<String, Object> groupsCollection = new HashMap<String, Object>();
                            groupsCollection.put( email , currentUser.getUid() );

                            //create groups collection
                            firebaseFirestore.collection("groups").document( groupCode)
                                    .collection("usersInGroup").document(currentUser.getUid())
                                    .set(groupsCollection)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully saved!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            Map<String, Object> justAText = new HashMap<String, Object>();
                            justAText.put("123","123");
                            groupsCollection.put( email , currentUser.getUid() );

                            firebaseFirestore.collection("groups").document( groupCode)
                                    .set(justAText)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully saved!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

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

    public String generateRandomCode()
    {
        String charPool = "1234567890qwertyuopasdfghjklizxcvbnm";
        String result = "";

        for ( int i = 0; i < 6; i++)
        {
            result = result + charPool.charAt( (int)( Math.random() * 36 ) );
        }
        return result;
    }
}
