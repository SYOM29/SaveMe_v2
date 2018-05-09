package com.example.syrup.myapplication;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class GroupsShowPage extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    private static final String TAG = "groups";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_show_page);



        firebaseFirestore  = FirebaseFirestore.getInstance();
        firebaseAuth       = FirebaseAuth.getInstance();
        currentUser        = firebaseAuth.getCurrentUser();

        listAllGroups();

    }

    public void listAllGroups()
    {
        firebaseFirestore.collection("users").document(currentUser.getUid())
                .collection("groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String documentID = document.getId();
                                //TODO

                                LinearLayout parentLayout = (LinearLayout)findViewById(R.id.layout);
                                LayoutInflater layoutInflater = getLayoutInflater();
                                View view;

                                view = layoutInflater.inflate(R.layout.text_layout, parentLayout, false);

                                // In order to get the view we have to use the new view with text_layout in it
                                TextView textView = (TextView)view.findViewById(R.id.text);
                                textView.setText( "" + document.getId() );

                                // Add the text view to the parent layout
                                parentLayout.addView(textView);
                                textView.setTextSize(35);
                                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                            }
                        }

                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void makeOpearations()
    {

    }


}
