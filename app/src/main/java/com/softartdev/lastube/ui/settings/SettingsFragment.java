package com.softartdev.lastube.ui.settings;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.softartdev.lastube.SessionManager;
import com.softartdev.lastube.ui.login.LoginActivity;

public class SettingsFragment extends ListFragment {

    final String[] setList = new String[]{"LogIn", "LogOut"};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListAdapter adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, setList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position == 0) {
            startActivity(new Intent(l.getContext(), LoginActivity.class));
        } else if (position == 1) {
            SessionManager session = new SessionManager(l.getContext());
            session.logoutUser();
        }
    }
}
