package com.softartdev.lastube;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable {
    String title;
    String subtitle;
    String description;
    String url;
    String extralargeURL;
    String wiki;

    public ListItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtralargeURL() {
        return extralargeURL;
    }

    public void setExtralargeURL(String extralargeURL) {
        this.extralargeURL = extralargeURL;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(extralargeURL);
        parcel.writeString(wiki);
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    public ListItem(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        description = in.readString();
        url = in.readString();
        extralargeURL = in.readString();
        wiki = in.readString();
    }
}
