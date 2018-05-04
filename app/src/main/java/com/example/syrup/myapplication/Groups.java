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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Groups extends AppCompatActivity implements View.OnClickListener {

    private EditText groupCodeText;
    private Button enterGroupCode;
    private String groupCode;
    private FirebaseFirestore firebaseFirestore;
    private final String TAG = "groupCode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        groupCodeText = findViewById(R.id.groupCode);
        enterGroupCode = findViewById(R.id.enterCode);


        firebaseFirestore = FirebaseFirestore.getInstance();
        enterGroupCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        groupCode = groupCodeText.getText().toString().trim();
        String groups = "groups";

        final DocumentReference docRef = firebaseFirestore.collection(groups).document(groupCode);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                    }

                    else {
                    }

                }
            }

        });
    }


}
