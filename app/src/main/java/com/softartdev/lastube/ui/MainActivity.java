package com.softartdev.lastube.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softartdev.lastube.LastfmAPI;
import com.softartdev.lastube.R;
import com.softartdev.lastube.SessionManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Collection;
import java.util.HashMap;

import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class MainActivity extends SingleFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton imageButton;
    TextView usernameTextView, playsTextView;
    LinearLayout navLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onLaunch();

        userInfoFragment();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imageButton = (ImageButton) headerView.findViewById(R.id.avatarButton);
        if (imageButton != null){
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userInfoFragment();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
        }
        usernameTextView = (TextView) headerView.findViewById(R.id.nickText);
        playsTextView = (TextView) headerView.findViewById(R.id.textView);
        //headerView.setBackgroundResource(R.mipmap.ic_launcher);
        navLayout = (LinearLayout) headerView.findViewById(R.id.navLayout);

        new headerTask().execute();
    }

    public void userInfoFragment(){
        Fragment fragment = new UserInfoFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    class headerTask extends AsyncTask<Void,Void,Void>{
        String username, key, imageURL, nameString, playsString, recentCoverURL;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // get user data from session
            final SessionManager session = new SessionManager(getContext());
            HashMap<String, String> user = session.getUserDetails();
            // username
            username = user.get(SessionManager.KEY_USERNAME);
            // key
            key = LastfmAPI.key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = User.getInfo(username, key);
            imageURL = user.getImageURL();
            nameString = user.getName();
            playsString = String.valueOf(user.getPlaycount()) + " scrobbles";

            Collection<Track> scrobblesEntries = User.getRecentTracks(username, key).getPageResults();
            Track[] scrobblesArray = scrobblesEntries.toArray(new Track[scrobblesEntries.size()]);
            recentCoverURL = scrobblesArray[0].getImageURL(ImageSize.EXTRALARGE);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!imageURL.equals("")){
                Picasso.get().load(imageURL).into(imageButton);
            }
            usernameTextView.setText(nameString);
            playsTextView.setText(playsString);
            if (!recentCoverURL.equals("")){
                Picasso.get().load(recentCoverURL).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            navLayout.setBackground(new BitmapDrawable(bitmap));
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d("TAG", "Prepare Load");
                    }
                });
            }
        }
    }
}
