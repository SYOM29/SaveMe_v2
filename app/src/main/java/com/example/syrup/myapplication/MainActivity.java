package com.example.syrup.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.Object;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    //variable declaration
    private ImageView ambulance;
    private ImageView fire;
    private ImageView police;
    private ImageView siren;
    private ImageView SOSButton;
    private Toast toast;
    private MediaRecorder mRecorder;
    private String mFileName = null;
    private static final String LOG_TAG = "Record_log";
    private ProgressDialog mProgress;

    private StorageReference storageReference;

    //constants
    private final int REQUEST_CALL = 1234;

    //onCreate
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackground(new ColorDrawable(Color.parseColor("#0c5774")));

        //setting navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setting the variables
        ambulance = (ImageView)findViewById(R.id.ambulanceButton);
        siren = (ImageView)findViewById(R.id.sirenButton);
        fire = (ImageView)findViewById(R.id.fireButton);
        police = (ImageView)findViewById(R.id.policeButton);
        SOSButton = (ImageView)findViewById(R.id.SOSButton);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";
        mProgress = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();

        //setting listeners
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String number = "112";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                //asking permission
                ActivityCompat.requestPermissions( MainActivity.this, new String[] {android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String number = "110";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                //asking permission
                ActivityCompat.requestPermissions( MainActivity.this, new String[] {android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);

            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String number = "155";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                //asking permission
                ActivityCompat.requestPermissions( MainActivity.this, new String[] {android.Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

        siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.siren);
                player.start();
            }
        });

        SOSButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startRecording();
                   toast = Toast.makeText(MainActivity.this, "Recording is started...",Toast.LENGTH_LONG);
                   toast.show();


                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stopRecording();
                    toast = Toast.makeText(MainActivity.this, "Recording is stopped...",Toast.LENGTH_LONG);
                    toast.show();

                }
                return true;

            }


        });
    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void stopRecording()
    {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadAudio();
    }
    private void uploadAudio()
    {
        mProgress.setMessage("Uploading Audio...");
        mProgress.show();

        StorageReference filepath = storageReference.child("Audio").child("new_audio.mp3");
        Uri uri = Uri.fromFile(new File(mFileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mProgress.dismiss();
                toast = Toast.makeText(MainActivity.this, "Recording is uploaded succesfully...",Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void startRecording()
    {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mRecorder.prepare();
        }catch (IOException e)
        {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_page)
        {

        }
        else if (id == R.id.contact_list) {

        }
        else if (id == R.id.locations)
        {
            Intent goLocations = new Intent(MainActivity.this, locations.class);
            startActivity(goLocations);
        }
        else if (id == R.id.recordings)
        {

        }
        else if (id == R.id.settings)
        {
            Intent goSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(goSettings);
        }
        else if (id == R.id.logout)
        {
            Intent logout = new Intent(MainActivity.this, Login.class);
            startActivity(logout);
        }
        else if (id == R.id.exit)
        {

            //exiting the app
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
