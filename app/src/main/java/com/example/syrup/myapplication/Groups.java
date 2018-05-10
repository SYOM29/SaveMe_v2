package com.example.syrup.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Groups extends AppCompatActivity implements View.OnClickListener {

    private EditText groupCodeText;
    private Button enterGroupCode;
    private Button myGroups;
    private String groupCode;
    private FirebaseFirestore firebaseFirestore;
    private final String TAG = "groupCode";
    private FirebaseAuth firebaseAuth;
    private static String phone;
    private static String anyPhone;
    private MyContainer myContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupCodeText  = findViewById(R.id.groupCode );
        enterGroupCode = findViewById(R.id.enterCode );
        myGroups       = findViewById(R.id.goToGroups);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth      = FirebaseAuth.getInstance();
        enterGroupCode.setOnClickListener(this);
        myGroups.setOnClickListener(this);
        getPhoneNum();



    }

    public String getPhoneNum()
    {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        DocumentReference docRef = firebaseFirestore.collection("users").document(currentUser.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String aPhone;
                        aPhone = (String)document.get("Phone");
                        Log.d(TAG, "phone : " + aPhone );

                        myContainer = new MyContainer(phone);
                        phone = aPhone;
                        setPhone(aPhone);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return getPhone();
    }

    public static String setPhone( String thePhone)
    {
        anyPhone = thePhone;
        phone = thePhone;
        return phone;

    }

    public static String getPhone()
    {
        return phone;
    }


    @Override
    public void onClick(View view) {
        if (view == enterGroupCode) {
            groupCode = groupCodeText.getText().toString().trim();
            String groups = "groups";

            final DocumentReference docRef = firebaseFirestore.collection(groups).document(groupCode);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            String email;
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();




                            Log.d(TAG, "phoneNum : " + myContainer.getStr() );

                            Log.d(TAG, "anyPhone : " + anyPhone );
                            Map<String, Object> groupsCollection = new HashMap<String, Object>();
                            groupsCollection.put(currentUser.getUid(), phone );

                            //add user to places in database
                            firebaseFirestore.collection("groups").document(groupCode)
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
                            firebaseFirestore.collection("users").document(currentUser.getUid())
                                    .collection("groups").document(groupCode)
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

                            Toast.makeText(Groups.this,
                                    "joined successfully", Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(Groups.this,
                                    "Group could not found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            });
        }

        else if ( view == myGroups )
        {
            Intent goGroups = new Intent(Groups.this, GroupsShowPage.class);
            startActivity(goGroups);
        }
    }


    public class MyContainer
    {
        String str;

        MyContainer( String string )
        {

            str = string;
        }

        public String getStr()
        {
            return str;
        }
    }

}
