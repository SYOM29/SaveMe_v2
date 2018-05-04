package com.example.syrup.myapplication;

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

import java.util.HashMap;
import java.util.Map;

public class Groups extends AppCompatActivity implements View.OnClickListener {

    private EditText groupCodeText;
    private Button enterGroupCode;
    private String groupCode;
    private FirebaseFirestore firebaseFirestore;
    private final String TAG = "groupCode";
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupCodeText = findViewById(R.id.groupCode);
        enterGroupCode = findViewById(R.id.enterCode);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth      = FirebaseAuth.getInstance();
        enterGroupCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        groupCode = groupCodeText.getText().toString().trim();
        String groups = "groups";

        final DocumentReference docRef = firebaseFirestore.collection(groups).document(groupCode);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists())
                    {
                        String email;
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();



                        Map<String, Object> groupsCollection = new HashMap<String, Object>();
                        groupsCollection.put( currentUser.getEmail() , currentUser.getUid() );

                        //create groups collection
                        firebaseFirestore.collection("groups").document( groupCode)
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
                    }

                    else
                    {
                        Toast.makeText(Groups.this,
                                "Group could not found", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        });
    }


}
