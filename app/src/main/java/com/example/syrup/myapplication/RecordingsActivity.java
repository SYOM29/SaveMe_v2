package com.example.syrup.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * This class creates recordings page of the program where user is able to see and download his/her groups recordings
 * @author SYRUP group: Siyovush Kadyrov, Emre Tolga Ayan, Atakan Bora Karacalioglu, Can Aybalik, Sertac Cebeci, Noman Aslam
 * @version 1.0
 */
public class RecordingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private LinearLayout parentLinearLayout;
    private StorageReference storageReference;
    private StorageReference myRef;
    private FloatingActionButton download;
    private TextView recordingName;
    private String mFileName = null;

    /**
     * This method creates environment for user to download his/her recordings
     * @Override
     * @param savedInstanceState
     * @return void
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //parentLinearLayout = (LinearLayout) findViewById(R.id.content_recordings);
        storageReference = FirebaseStorage.getInstance().getReference();
        myRef = storageReference.child("users/nullnull/recordings").child("File 1");

        //test
        download = (FloatingActionButton)findViewById(R.id.downloadFloatingActionButton);
        recordingName = (TextView)findViewById(R.id.recordingTextView);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    File localFile = File.createTempFile("file1", "mp3");
                    myRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //playing the audio
                                    MediaPlayer player = MediaPlayer.create(RecordingsActivity.this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "file1.mp3"));
                                    player.start();

                                    //downloading the audio
                                    Uri uri = Uri.fromFile(new File( mFileName));
                                    mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRecordings";
                                    mFileName += "/" + "file1.mp3";
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                }
                catch ( IOException e )
                {

                }
            }
        });



    }
    /**
     * This method deletes a view
     * @param v
     * @return void
     */
    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    /**
     * This method is used to open menu
     * @Override
     * @param
     * @return void
     */
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method is used to navigate in menu
     * @Override
     * @param item
     * @return void
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_page)
        {
            Intent goMain = new Intent(RecordingsActivity.this, MainActivity.class);
            startActivity(goMain);
        }
        else if (id == R.id.contact_list) {
            Intent goLocations = new Intent(RecordingsActivity.this, Groups.class);
            startActivity(goLocations);
        }
        else if (id == R.id.locations)
        {
            Intent goLocations = new Intent(RecordingsActivity.this, locations.class);
            startActivity(goLocations);
        }
        else if (id == R.id.recordings)
        {

        }
        else if (id == R.id.settings)
        {
            Intent goSettings = new Intent(RecordingsActivity.this, SettingsActivity.class);
            startActivity(goSettings);
        }
        else if (id == R.id.logout)
        {
            //Signing out from Firebase
            FirebaseAuth.getInstance().signOut();

            //Intent
            Intent logout = new Intent(RecordingsActivity.this, Login.class);
            startActivity(logout);
        }
        else if (id == R.id.exit)
        {

            //exiting the app
            final AlertDialog.Builder builder = new AlertDialog.Builder(RecordingsActivity.this);
            builder.setMessage("Are you sure you want to quit?");
            builder.setCancelable(true);
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
