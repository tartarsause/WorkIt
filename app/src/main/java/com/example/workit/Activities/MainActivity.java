package com.example.workit.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workit.R;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeViewFragment.DataPassListener
                    {

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private String emailString;
    private String userNameString;
    private String locationInput;
    private FirebaseAuth mAuth;

    private boolean viewIsAtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Toast.makeText(getApplicationContext(), "Login Extras are:" + bundle , Toast.LENGTH_LONG).show();
            userNameString = bundle.getString("Name");
            emailString = bundle.getString("EMAIL");
        }
        else {
            Toast.makeText(getApplicationContext(), "Login Extras are empty", Toast.LENGTH_LONG).show();
            userNameString = "Bob Tester";
            emailString = "test@test.com";
        }

        // Views
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mNameTextView = findViewById(R.id.userNameTextView);
        mEmailTextView =  findViewById(R.id.userEmailTextView);
        userNameString = "Bob Tester";
        emailString = "test@test.com";
//        mNameTextView.setText("Bob Tester");
//        mEmailTextView.setText("test@test.com");

        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayView(R.id.nav_home); //initial startup view
    }

    @Override
    public void passData(String data) {
        Map_view_fragment map_view_fragment = new Map_view_fragment();
        Bundle args = new Bundle();
        args.putString(Map_view_fragment.DATA_RECEIVE, data);
        map_view_fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main_frame, map_view_fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the Home fragment
            displayView(R.id.nav_home); //display the Home fragment
        } else {
            moveTaskToBack(true);  //If view is in Home fragment, exit application
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    public void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_home:
                fragment = new HomeViewFragment();
                title = "Home";
                viewIsAtHome = true;
                break;

            case R.id.nav_map:
                fragment = new Map_view_fragment();
                title = "Map";
                viewIsAtHome = false;
                break;

            case  R.id.nav_history:
                title="History";
                viewIsAtHome = false;
                break;

            case R.id.nav_settings:
                title = "Settings";
                viewIsAtHome = false;
                break;

            case R.id.nav_share:
                title = "Share";
                viewIsAtHome = false;
                break;

            case R.id.nav_send:
                title = "Send";
                viewIsAtHome = false;
                break;
            }

            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main_frame, fragment);
                ft.commit();
            }

            //set toolbar title
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
