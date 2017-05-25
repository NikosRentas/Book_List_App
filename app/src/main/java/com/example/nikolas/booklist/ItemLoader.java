package com.example.nikolas.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ItemLoader extends AsyncTaskLoader<List<Item>> {
    private String mURL;

    public ItemLoader(Context context, String mURL) {
        super(context);
        this.mURL = mURL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Item> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        List<Item> itemList = QueryUtils.fetchData(mURL);

        return itemList;
    }
}
