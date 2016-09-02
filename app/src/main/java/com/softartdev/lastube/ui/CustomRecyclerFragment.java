package com.softartdev.lastube.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softartdev.lastube.ListItem;
import com.softartdev.lastube.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomRecyclerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    class CustomRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleView;
        TextView subtitleView;
        TextView descriptionView;
        ImageView imageView;
        ListItem item;

        public CustomRecyclerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleView = (TextView) itemView.findViewById(R.id.title);
            subtitleView = (TextView) itemView.findViewById(R.id.subtitle);
            descriptionView = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.thumbImage);
        }

        public void bindItem (ListItem listItem){
            item = listItem;
            titleView.setText(item.getTitle());
            subtitleView.setText(item.getSubtitle());
            descriptionView.setText(item.getDescription());

            String url = item.getUrl();
            if (url != null && !url.equals("")) {
                Picasso.with(itemView.getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_menu_scrobbling_now)
                        .error(R.drawable.ic_menu_top_albums)
                        .into(imageView);
            }
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), item.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(view.getContext(), InfoActivity.class);
            intent.putExtra(InfoActivity.EXTRA_ITEM_PARCELABLE, item);
            startActivity(intent);
        }
    }

    public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerHolder>{
        private ArrayList<ListItem> mListData;

        public CustomRecyclerAdapter(ArrayList<ListItem> listItems) {
            mListData = listItems;
        }

        @Override
        public CustomRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_row, parent, false);
            return new CustomRecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomRecyclerHolder holder, int position) {
            ListItem listItem = mListData.get(position);
            holder.bindItem(listItem);
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }
    }
}
