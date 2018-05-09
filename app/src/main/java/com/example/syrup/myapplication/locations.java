package com.example.syrup.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 */
public class locations extends AppCompatActivity {


    boolean check;
    Button b;
    TextView t;
    Button view;
    Button sms;
    LocationManager locationManager;
    LocationListener listener;
    Snackbar warning;
    double myLongitude;
    double myLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        b = (Button) findViewById( R.id.b);
        t = (TextView) findViewById( R.id.t);
        view = (Button) findViewById( R.id.view);
        sms = (Button) findViewById( R.id.sms);
        check = false;
        warning = Snackbar.make(view, "You need to request location first!", Snackbar.LENGTH_SHORT);



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                t.setText(location.getLongitude() + " , " + location.getLatitude());
                myLongitude = location.getLongitude();
                myLatitude = location.getLatitude();
                check = true;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();





        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( check) {
                    String strnum = "+905459414494"; //Contact List Numbers
                    Uri smsToUri = Uri.parse("smsto:" + strnum);
                    Intent intent = new Intent(
                            android.content.Intent.ACTION_SENDTO, smsToUri);

                    String link = "http://maps.google.com/maps?q=" + myLatitude + "," + myLongitude; //User's Location
                    // message = message.replace("%s", StoresMessage.m_storeName);
                    intent.putExtra("sms_body", link);
                    startActivity(intent);
                }
                else {
                    warning.show();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( check) {

                    String url = "http://maps.google.com/maps?q=" + myLatitude + "," + myLongitude;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                else {
                    warning.show();
                }

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
                t.setText("Getting your coordinates...");
            }
        });
    }
}
