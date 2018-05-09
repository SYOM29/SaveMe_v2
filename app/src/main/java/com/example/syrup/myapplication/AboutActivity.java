package com.example.syrup.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //properties
    private ImageView logo;

    private TextView projectName;
    private TextView projectName2;
    private TextView groupName;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setting the properties
        logo = (ImageView)findViewById(R.id.logoImageView);
        projectName = (TextView)findViewById(R.id.projectNameTextView);
        projectName2 = (TextView)findViewById(R.id.projectNameTextView2);
        groupName = (TextView)findViewById(R.id.groupNameTextView);
        text = (TextView)findViewById(R.id.aboutTextView);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_page)
        {
            Intent goMain = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(goMain);
        }
        else if (id == R.id.contact_list) {
            Intent goLocations = new Intent(AboutActivity.this, Groups.class);
            startActivity(goLocations);
        }
        else if (id == R.id.locations)
        {
            Intent goLocations = new Intent(AboutActivity.this, locations.class);
            startActivity(goLocations);
        }
        else if (id == R.id.recordings)
        {
            Intent goRecordings = new Intent(AboutActivity.this, RecordingsActivity.class);
            startActivity(goRecordings);
        }
        else if (id == R.id.settings)
        {
            Intent goSettings = new Intent(AboutActivity.this, SettingsActivity.class);
            startActivity(goSettings);
        }
        else if (id == R.id.logout)
        {
            Intent logout = new Intent(AboutActivity.this, Login.class);
            startActivity(logout);
        }
        else if (id == R.id.exit)
        {

            //exiting the app
            final AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
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
