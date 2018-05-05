package com.example.syrup.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //properties
    private ImageView account;
    private ImageView GPSUpdate;
    private ImageView help;
    private ImageView about;

    private TextView accountText;
    private TextView GPSUpdateText;
    private TextView helpText;
    private TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //creating navigation drawer menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //assigning values to properties
        account = (ImageView)findViewById(R.id.accountImageView);
        GPSUpdate = (ImageView)findViewById(R.id.GPSImageView);
        help = (ImageView)findViewById(R.id.helpImageView);
        about = (ImageView)findViewById(R.id.aboutImageView);

        accountText = (TextView) findViewById(R.id.editProfileTextView);
        GPSUpdateText = (TextView)findViewById(R.id.autoGPSUpdateTextView);
        helpText = (TextView)findViewById(R.id.helpTextView);
        aboutText = (TextView)findViewById(R.id.aboutTextView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_page)
        {
            Intent goMain = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(goMain);
        }
        else if (id == R.id.contact_list) {

        }
        else if (id == R.id.locations)
        {
            Intent goLocations = new Intent(SettingsActivity.this, locations.class);
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
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