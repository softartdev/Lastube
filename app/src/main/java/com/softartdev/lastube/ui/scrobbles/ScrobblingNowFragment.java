package com.softartdev.lastube.ui.scrobbles;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softartdev.lastube.LastfmAPI;
import com.softartdev.lastube.R;
import com.softartdev.lastube.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashMap;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class ScrobblingNowFragment extends Fragment {
    View view;
    String username, password, key;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmen_now_scrobbling, container, false);
        /**
         * Content main
         * */
        context = inflater.getContext();
        // get user data from session
        final SessionManager session = new SessionManager(inflater.getContext());
        HashMap<String, String> user = session.getUserDetails();
        // username
        username = user.get(SessionManager.KEY_USERNAME);
        // password
        password = user.get(SessionManager.KEY_PASSWORD);
        // key
        key = LastfmAPI.key;
        // inflating info fragment
        new ScrobblingNowTask().execute();

        return view;
    }

    class ScrobblingNowTask extends AsyncTask<Void,Void,Void>{
        TextView infoTrackTextView, infoAlbumTextView, infoWikiTextView;
        String infoStringTrack, infoStringArtist, infoStringName, infoImageURL, infoAlbumString, infoWikiString;
        ImageView imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            infoTrackTextView = (TextView) view.findViewById(R.id.infoTextTrack);
            infoAlbumTextView = (TextView) view.findViewById(R.id.infoTextAlbum);
            infoWikiTextView = (TextView) view.findViewById(R.id.infoTextWiki);
            infoWikiTextView.setMovementMethod(LinkMovementMethod.getInstance());
            imageView = (ImageView)view.findViewById(R.id.infoImage);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Collection<Track> scrobblesEntries = User.getRecentTracks(username, key).getPageResults();
            Track[] scrobblesArray = scrobblesEntries.toArray(new Track[scrobblesEntries.size()]);
            Track track = scrobblesArray[0];
            infoStringArtist = track.getArtist();
            infoStringName = track.getName();
            infoStringTrack = infoStringArtist + " - " + infoStringName;
            infoAlbumString = track.getAlbum();
            infoWikiString = Artist.getInfo(infoStringArtist, key).getWikiSummary();
            infoImageURL = scrobblesArray[0].getImageURL(ImageSize.EXTRALARGE);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            infoTrackTextView.setText(infoStringTrack);
            infoAlbumTextView.setText(infoAlbumString);
            infoWikiTextView.setText(Html.fromHtml(infoWikiString));
            // inflating image
            if (imageView != null && !infoImageURL.equals("")) {
                Picasso.with(context)
                        .load(infoImageURL)
                        .placeholder(R.drawable.ic_menu_scrobbling_now)
                        .error(R.drawable.ic_menu_top_albums)
                        .into(imageView);
            }
        }
    }
}
