package com.example.syrup.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity {
    //properties
    private ImageView changeProfilePic;
    private EditText nameField;
    private EditText phoneField;

    private Button changeProflePicButton;
    private Button editName;
    private Button editPhome;
    private Button back;

    //constants
    private final String NAME = "Name";
    private final String SURNAME = "Surname";
    private final String EMAIL = "Email";
    private final String GROUPCODE = "GroupCode";
    private final String TAG = "userInfo";
    private final int REQUEST_CODE = 1234;

    public boolean updateAll() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //setting firebase
        //FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Map<String, Object> dataToSave = new HashMap<String, Object>();

        //setting properties
        changeProfilePic = (ImageView)findViewById(R.id.changeProfilePicImageView);

        nameField = (EditText)findViewById(R.id.name);
        phoneField = (EditText)findViewById(R.id.phone);

        changeProflePicButton = (Button)findViewById(R.id.changeProfile);
        editName = (Button)findViewById(R.id.editName);
        editPhome = (Button)findViewById(R.id.editPhone);
        back = (Button)findViewById(R.id.backList);


        //setting the buttons
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dataToSave.put(NAME, name);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Goes back to the previous activity
                finishActivity(REQUEST_CODE);
            }
        });
    }
}
