package com.example.syrup.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Edit extends AppCompatActivity {
    //properties
    private ImageView changeProfilePic;
    private EditText nameField;
    private EditText surnameField;

    private Button changeProflePicButton;
    private Button editProfile;
    private Button back;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String name;
    private String surname;

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

        //setting properties
        changeProfilePic = (ImageView)findViewById(R.id.changeProfilePicImageView);

        nameField = (EditText)findViewById(R.id.name);
        surnameField = (EditText)findViewById(R.id.surname);

        changeProflePicButton = (Button)findViewById(R.id.changeProfile);
        editProfile = (Button)findViewById(R.id.editProfile);
        back = (Button)findViewById(R.id.backList);




        //setting firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        final Map<String, Object> dataToSave = new HashMap<String, Object>();

        //setting the buttons
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( name != null && surname != null ) {
                    name = nameField.getText().toString().trim();
                    surname = surnameField.getText().toString().trim();

                    dataToSave.put(NAME, name);
                    dataToSave.put(SURNAME, name);

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
                    Toast.makeText(Edit.this,"Name changed", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(Edit.this,"Please enter name..", Toast.LENGTH_LONG).show();
                }
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
