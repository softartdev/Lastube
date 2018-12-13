package com.softartdev.lastube.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softartdev.lastube.LastfmAPI;
import com.softartdev.lastube.R;
import com.softartdev.lastube.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.umass.lastfm.Tag;
import de.umass.lastfm.User;

public class UserInfoFragment extends Fragment {
    View view;
    String username, key;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        context = inflater.getContext();
        // get user data from session
        final SessionManager session = new SessionManager(inflater.getContext());
        HashMap<String, String> user = session.getUserDetails();
        // username
        username = user.get(SessionManager.KEY_USERNAME);
        // key
        key = LastfmAPI.key;
        // inflating info fragment
        new UserInfoTask().execute();
        return view;
    }

    class UserInfoTask extends AsyncTask<Void,Void,Void> {
        ImageView imageView;
        TextView nameTextView, countryTextView, dateTextView, playsTextView, lovesTextView, tagsTextView;
        String nameString, countryString, imageURL, playsString, lovesString;
        Collection<Tag> tags;
        Date date;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageView = (ImageView) view.findViewById(R.id.avatar);
            nameTextView = (TextView) view.findViewById(R.id.nick);
            countryTextView = (TextView) view.findViewById(R.id.country);
            dateTextView = (TextView) view.findViewById(R.id.reg_date);
            playsTextView = (TextView) view.findViewById(R.id.playcount);
            lovesTextView = (TextView) view.findViewById(R.id.loved_tracks_count);
            tagsTextView = (TextView) view.findViewById(R.id.top_tags_count);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = User.getInfo(username, key);
            imageURL = user.getImageURL();
            nameString = user.getName();
            countryString = user.getCountry();
            date = user.getRegisteredDate();
            playsString = String.valueOf(user.getPlaycount());
            lovesString = String.valueOf(User.getLovedTracks(username, key).getPageResults().size());
            tags = User.getTopTags(username, key);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nameTextView.setText(nameString);
            countryTextView.setText(countryString);
            dateTextView.setText(new SimpleDateFormat("d.M.y", Locale.getDefault()).format(date));
            playsTextView.setText(playsString);
            lovesTextView.setText(lovesString);
            for (Tag tag : tags){
                tagsTextView.append(tag.getName() + "\n");
            }
            // inflating image
            if (imageView != null && !imageURL.equals("")) {
                Picasso.get()
                        .load(imageURL)
                        .placeholder(R.drawable.ic_menu_scrobbling_now)
                        .error(R.drawable.ic_menu_top_albums)
                        .into(imageView);
            }
        }
    }
}
