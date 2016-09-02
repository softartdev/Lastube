package com.softartdev.lastube.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.softartdev.lastube.R;
import com.softartdev.lastube.SessionManager;
import com.softartdev.lastube.ui.login.LoginActivity;
import com.softartdev.lastube.ui.scrobbles.ScrobblesFragment;
import com.softartdev.lastube.ui.scrobbles.ScrobblingNowFragment;
import com.softartdev.lastube.ui.settings.SettingsFragment;
import com.softartdev.lastube.ui.top.TopAlbumsFragment;
import com.softartdev.lastube.ui.top.TopArtistsFragment;
import com.softartdev.lastube.ui.top.TopTracksFragment;

import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.MemoryCache;

public abstract class SingleFragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Application Context
    static Context mContext;
    // Session Manager Class
    SessionManager session;

    public void onLaunch(){
        //TODO: set not null cache
        //Caller.getInstance().setCache(null);//it's a very important line, without it nothing works
        Caller.getInstance().setCache(new MemoryCache());

        // Session class instance
        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;// Application Context
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // update the main content by replacing fragments
        Fragment fragment = new UserInfoFragment();

        if (id == R.id.nav_scrobbling_now) {
            fragment = new ScrobblingNowFragment();
        } else if (id == R.id.nav_scrobbles) {
            fragment = new ScrobblesFragment();
        } else if (id == R.id.nav_top_artists) {
            fragment = new TopArtistsFragment();
        } else if (id == R.id.nav_top_albums) {
            fragment = new TopAlbumsFragment();
        } else if (id == R.id.nav_top_tracks) {
            fragment = new TopTracksFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_logout) {
            session.logoutUser();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Context getContext() {
        return mContext;
    }
}
