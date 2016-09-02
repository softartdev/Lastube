package com.softartdev.lastube.ui.top;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softartdev.lastube.LastfmAPI;
import com.softartdev.lastube.ListItem;
import com.softartdev.lastube.R;
import com.softartdev.lastube.ui.CustomRecyclerFragment;

import java.util.ArrayList;
import java.util.Collection;

import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

public class TopTracksFragment extends CustomRecyclerFragment {
    ArrayList<ListItem> mListData;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new TopTrackTask(inflater, container).execute();
        return view;
    }

    class TopTrackTask extends AsyncTask<Void,Void,Void>{
        String username, key;
        LayoutInflater inflater;
        ViewGroup container;
        Context context;
        RecyclerView mItemRecyclerView;

        public TopTrackTask(LayoutInflater inflater, ViewGroup container) {
            this.inflater = inflater;
            this.container = container;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = inflater.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("SoftArtDevPref", 0);
            username = sharedPreferences.getString("username", null);
            key = LastfmAPI.key;
            mListData = new ArrayList<ListItem>();
            view = inflater.inflate(R.layout.fragment_item_list, container, false);
            mItemRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
            mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Collection<Track> tracksTopEntries = User.getTopTracks(username, key);
            for (Track track : tracksTopEntries){
                ListItem listItem = new ListItem();
                listItem.setExtralargeURL(track.getImageURL(ImageSize.EXTRALARGE));
                listItem.setWiki(Artist.getInfo(track.getArtist(), key).getWikiSummary());
                listItem.setUrl(track.getImageURL(ImageSize.MEDIUM));
                listItem.setTitle(track.getName());
                listItem.setSubtitle(track.getArtist());
                listItem.setDescription(String.valueOf(track.getPlaycount()));
                mListData.add(listItem);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            CustomRecyclerAdapter mAdapter = new CustomRecyclerAdapter(mListData);
            mItemRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
