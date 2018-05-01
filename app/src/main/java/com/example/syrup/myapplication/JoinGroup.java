package com.example.syrup.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JoinGroup extends AppCompatActivity implements View.OnClickListener{

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference mDocRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private final String GROUPCODE = "GroupCode";

    private TextView textView;
    private Button enter;
    private Button createGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mDocRef = FirebaseFirestore.getInstance().collection("users").document(currentUser.getUid());

        mDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            String groupCode = documentSnapshot.getString(GROUPCODE);
                            if ( !groupCode.equals("") )
                            {
                                Intent goMainPage = new Intent(JoinGroup.this, MainActivity.class);
                                startActivity(goMainPage);
                                return;
                            }

                        }
                        else
                            Toast.makeText(JoinGroup.this, "not exist", Toast.LENGTH_SHORT).show();
                    }
                });


        enter        = findViewById(R.id.enter);
        createGroup  = findViewById(R.id.createGroup);
        textView     = findViewById(R.id.pass);

    }

    @Override
    public void onClick(View view) {
        if ( view == textView )
        {
            Intent goMainPage = new Intent(JoinGroup.this, MainActivity.class);
            startActivity(goMainPage);
        }

        else if ( view == enter )
        {
            //TODO
            //it will check database for an existing code
            //if one is exist, user will join to it
        }

        else if ( view == createGroup )
        {
            //TODO
            //it will create a new group with a random code
        }

    }
}
