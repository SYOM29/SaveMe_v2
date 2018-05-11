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
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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


/**
 * This class creates main page of the program
 * Main page consists of widgets: ambulance, police, siren, fire and SOSButton
 * @author SYRUP group: Siyovush Kadyrov, Emre Tolga Ayan, Atakan Bora Karacalioglu, Can Aybalik, Sertac Cebeci, Noman Aslam
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    //variables
    //Widgets
    private ImageView ambulance;
    private ImageView fire;
    private ImageView police;
    private ImageView siren;
    private ImageView SOSButton;

    //other properties
    private Toast toast;
    private MediaRecorder mRecorder;
    private String mFileName = null;
    private ProgressDialog mProgress;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private String phones = "";
    private SirenService sirenService;
    private int counter = 0;

    //Firebase
    private StorageReference storageReference;
    private static FirebaseUser currentUser;
    private static FirebaseAuth firebaseAuth;
    private static String name;
    private static String surname;

    private static DocumentReference mDocRef;
    private static DocumentReference mDocRef2;

    //constants
    private static final String LOG_TAG = "Record_log";

    File dir;

    /**
     * This method is called when this activity is called
     * creates Navigation Drawer menu, widgets
     * sets ClickListeners for the widgets
     * @SuppressLint("ClickableViewAccessibility")
     * @Override
     * @param savedInstanceState
     * @return void
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting Firebase
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
        ambulance = (ImageView) findViewById(R.id.ambulanceButton);
        siren = (ImageView) findViewById(R.id.sirenButton);
        fire = (ImageView) findViewById(R.id.fireButton);
        police = (ImageView) findViewById(R.id.policeButton);
        SOSButton = (ImageView) findViewById(R.id.SOSButton);

        //sirenService
        sirenService = new SirenService(MainActivity.this);

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


        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRecordings");
        dir.mkdir();

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyRecordings";
        mFileName += "/" + System.currentTimeMillis() + "Recorded_audio.mp3";
        mProgress = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();


        //setting listeners for widgets
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "112";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "110";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);

            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "155";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });

        siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter % 2 == 1)
                {
                    startService(new Intent(MainActivity.this, SirenService.class));
                } else if (counter % 2 == 0)
                {
                    stopService(new Intent(MainActivity.this, SirenService.class));
                }
            }
        });


        SOSButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                toast = Toast.makeText(MainActivity.this, "Recording is started...", Toast.LENGTH_LONG);
                toast.show();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Thread thread = new Thread();
                    try {

                        sleep(6 * 1000);
                        stopRecording();
                    } catch (Exception ex) {

                    }
                    thread.start();

                    toast = Toast.makeText(MainActivity.this, "Recording is stopped...", Toast.LENGTH_LONG);
                    toast.show();


                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    String strnum = getNums(); //Contact List Numbers
                    Uri smsToUri = Uri.parse("smsto:" + strnum);
                    Intent intent = new Intent(
                            android.content.Intent.ACTION_SENDTO, smsToUri);

                    String link = "http://maps.google.com/maps?q=" + latitude + "," + longitude + "\n" + "For recording please go to the recordings page"; //User's Location
                    // message = message.replace("%s", StoresMessage.m_storeName);
                    intent.putExtra("sms_body", link);
                    startActivity(intent);


                }
                return true;
            }
        });
    }

    /**
     * This method is used to resume recording
     * @param
     * @Override
     * @return void
     */
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * This method is used to pause recording
     * @param
     * @Override
     * @return void
     */
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    /**
     * This method is used to open Navigation Drawer Menu
     * @param
     * @Override
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
     * This method is used to stop recording
     * @param
     * @Override
     * @return void
     */
    private void stopRecording()
    {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadAudio();
    }

    /**
     * This method is used to upload recording
     * @param
     * @Override
     * @return void
     */
    private void uploadAudio()
    {
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

    /**
     * This method is used to start recording
     * @param
     * @Override
     * @return void
     */
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

    /**
     * This method is used to handle the shake event in main activity
     * @param
     * @Override
     * @return void
     */
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

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            String strnum = "+905316410668"; //Contact List Numbers
            Uri smsToUri = Uri.parse("smsto:" + strnum);
            Intent intent = new Intent(
                    android.content.Intent.ACTION_SENDTO, smsToUri);

            String link = "http://maps.google.com/maps?q=" + latitude + "," + longitude; //User's Location
            // message = message.replace("%s", StoresMessage.m_storeName);
            intent.putExtra("sms_body", link);
            startActivity(intent);

        }

    }

    /**
     * This method is used to get the names of the users from firebase
     * @param
     * @Override
     * @return void
     */
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

    /**
     * This method is used to get the names of the users from firebase
     * @param item
     * @SuppressWarnings("StatementWithEmptyBody")
     * @Override
     * @return void
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_page)
        {

        }
        else if (id == R.id.contact_list) {
            Intent goLocations = new Intent(MainActivity.this, Groups.class);
            startActivity(goLocations);

        }
        else if (id == R.id.locations)
        {
            Intent goLocations = new Intent(MainActivity.this, locations.class);
            startActivity(goLocations);
        }
        else if (id == R.id.recordings)
        {
            Intent goRecordings = new Intent(MainActivity.this, RecordingsActivity.class);
            startActivity(goRecordings);
        }
        else if (id == R.id.settings)
        {
            Intent goSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(goSettings);
        }
        else if (id == R.id.logout)
        {
            //Signing out from Firebase
            FirebaseAuth.getInstance().signOut();

            //Intent
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

    /**
     * This method is used to get the numbers of the users to send SMS notification
     * @param
     * @Override
     * @return String
     */
    public String getNums()
    {
        final FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("users").document(currentUser.getUid())
                .collection("groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (final QueryDocumentSnapshot document1 : task.getResult()) {

                                String documentID = document1.getId();
                                //TODO
                                firebaseFirestore.collection("groups").document(documentID)
                                        .collection("usersInGroup")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (final QueryDocumentSnapshot document2 : task.getResult()) {


                                                        //TODO
                                                        mDocRef2 = FirebaseFirestore.getInstance().collection("groups")
                                                                .document(document1.getId()).collection( "usersInGroup")
                                                        .document(document2.getId());

                                                        mDocRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                if ( documentSnapshot.exists()) {
                                                                String phone = documentSnapshot.getString(document2.getId());

                                                                phones = phone +  ";" + phones ;
                                                                }
                                                            }
                                                        });
                                                    }
                                                }

                                                else {
                                                    Log.d("UserInfo", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }
                        }

                        else {
                            Log.d("UserInfo", "Error getting documents: ", task.getException());
                        }
                    }
                });

        Toast.makeText(MainActivity.this,
                phones, Toast.LENGTH_SHORT).show();
        return phones;
    }
}
