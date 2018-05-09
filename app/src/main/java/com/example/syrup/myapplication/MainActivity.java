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
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
import android.view.Gravity;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Object;
import java.util.Objects;

import static java.lang.Thread.sleep;;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    //variable declaration
    //foldksljfgsdflgşkdfsgklsfdjhflşkh 19.36
    //khfkpfkpfkgh
    private ImageView ambulance;
    private ImageView fire;
    private ImageView police;
    private ImageView siren;
    private ImageView SOSButton;
    private int counter = 0;
    private Toast toast;
    private MediaRecorder mRecorder;
    private String mFileName = null;
    private static final String LOG_TAG = "Record_log";
    private ProgressDialog mProgress;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private boolean running ;
    private static FirebaseUser currentUser;
    private static FirebaseAuth firebaseAuth;
    private static String name;
    private static String surname;

    private static DocumentReference mDocRef;


    File dir;

    private StorageReference storageReference;

    //onCreate
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        mDocRef = FirebaseFirestore.getInstance().collection("users")
                .document(currentUser.getUid());

        getName();
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

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                handleShakeEvent(count);
            }
        });


        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyRecordings");
        dir.mkdir();

         mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() +"/MyRecordings";
         mFileName += "/"+ System.currentTimeMillis() + "Recorded_audio.mp3";
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
                 if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                 {
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
                if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
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
                if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(intent);
            }
        });

        siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            }
        });


        SOSButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v,MotionEvent event )
            {
                toast = Toast.makeText(MainActivity.this, "Recording is started...", Toast.LENGTH_LONG);
                toast.show();
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startRecording();

                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Thread thread = new Thread();
                    try {

                        sleep(6 * 1000);
                        stopRecording();
                    } catch (Exception ex) {

                    }
                    thread.start();

                    toast = Toast.makeText(MainActivity.this, "Recording is stopped...",Toast.LENGTH_LONG);
                    toast.show();

                }
return true;
            }


        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
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
        counter++;
        mProgress.setMessage("Uploading Audio...");
        mProgress.show();


        StorageReference filepath = storageReference.child("users").child(surname + name).child("profile").child(System.currentTimeMillis() + "new_audio.mp3");
        StorageReference filepath2 = storageReference.child("users").child(surname + name).child("recordings").child(System.currentTimeMillis() + "new_audio.mp3");
        StorageReference filepath3 = storageReference.child("users").child(surname + name).child("contactList").child(System.currentTimeMillis() + "new_audio.mp3");

        Uri uri = Uri.fromFile(new File( mFileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mProgress.dismiss();
                toast = Toast.makeText(MainActivity.this, "Recording is uploaded succesfully to your profile...",Toast.LENGTH_LONG);
                toast.show();
            }
        });
        filepath2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mProgress.dismiss();
                toast = Toast.makeText(MainActivity.this, "Recording is uploaded succesfully to your recordings...",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        filepath3.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mProgress.dismiss();
                toast = Toast.makeText(MainActivity.this, "Recording is uploaded succesfully to your contact list...",Toast.LENGTH_LONG);

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



    public void handleShakeEvent(int count)
    {
        if(count >= 5)
        {

            startRecording();
            toast = Toast.makeText(MainActivity.this, "Recording is started...",Toast.LENGTH_LONG);
            toast.show();


            Thread thread = new Thread();
            try {

                sleep(6 * 1000);
                stopRecording();
            }
            catch (Exception ex)
            {

            }
            thread.start();


            toast = Toast.makeText(MainActivity.this, "Recording is stopped...",Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public static void getName()
    {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    name = documentSnapshot.getString("Name");
                     surname = documentSnapshot.getString("Surname");
                }
            }
        });
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
