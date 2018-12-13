package com.softartdev.lastube.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softartdev.lastube.ListItem;
import com.softartdev.lastube.R;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {
    ImageView itemImageView;
    TextView itemTitleTextView, itemSubtitleTextView, itemDescriptionTextView;

    public static final String EXTRA_ITEM_PARCELABLE = "PARCELABLE LIST ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemImageView = (ImageView) findViewById(R.id.info_image);
        itemTitleTextView = (TextView) findViewById(R.id.info_title);
        itemSubtitleTextView = (TextView) findViewById(R.id.info_subtitle);
        itemDescriptionTextView = (TextView) findViewById(R.id.info_description);
        itemDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());

        ListItem listItem = getIntent().getParcelableExtra(EXTRA_ITEM_PARCELABLE);

        itemTitleTextView.setText(listItem.getTitle());
        itemSubtitleTextView.setText(listItem.getSubtitle());
        // TODO: add WebView
        itemDescriptionTextView.setText(Html.fromHtml(listItem.getWiki()));
        String url = listItem.getExtralargeURL();
        if (itemImageView != null && !url.equals("")) {
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.ic_menu_scrobbling_now)
                    .error(R.drawable.ic_menu_top_albums)
                    .into(itemImageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
