/**
 * This class tracks user's coordinates
 * User's coordinates can be sent to contact list via SMS or open Google Maps to show current position
 * @author SYRUP group: Siyovush Kadyrov, Emre Tolga Ayan, Atakan Bora Karacalioglu, Can Aybalik, Sertac Cebeci, Noman Aslam
 * @version 1.0
 */
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
 * Gets user's location by request
 */
public class locations extends AppCompatActivity {
    //properties
    private boolean check;
    private Button b;
    private TextView t;
    private Button view;
    private Button sms;
    private LocationManager locationManager;
    private LocationListener listener;
    private Snackbar warning;
    private double myLongitude;
    private double myLatitude;

    /**
     * Creates locations page environment
     * @Override
     * @param savedInstanceState
     * @return void
     */
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
            /**
             * Updates coordinates on location changed
             * @param location user's location
             */
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


        /**
         * Send coordinates to contacts via SMS
         */
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( check) {
                    MainActivity main = new MainActivity();
                    String strnum = main.getNums(); //Contact List Numbers
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

        /**
         * Views your location on Google Maps
         */
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


    /**
     * check the given permission by user
     * @param requestCode request code
     * @param permissions permissions
     * @param grantResults results
     */
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

        /**
         * Requests coordinates from user
         */
        b.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
    }
}