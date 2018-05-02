package com.example.syrup.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Edit extends AppCompatActivity {

    public boolean updateAll() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Button returnList = (Button)findViewById(R.id.backList);
        returnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Opens Contact List Edit Page

                Intent edit = new Intent( Edit.this, MainActivity.class);
                startActivity( edit);
            }
        });
    }
}
