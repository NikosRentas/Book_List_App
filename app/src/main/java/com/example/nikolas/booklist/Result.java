package com.example.nikolas.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Item>> {

    private static final int LOADER_ID = 1;
    private ItemAdapter mAdapter;
    private ImageView mEmptyStateImageView;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_layout);

        // Change activity label name
        setTitle("Books for " + MainActivity.getBookName());

        // Create list view and empty views
        ListView listView = (ListView) findViewById(R.id.list_view);
        mEmptyStateImageView = (ImageView) findViewById(R.id.empty_image_view);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        listView.setEmptyView(mEmptyStateImageView);
        listView.setEmptyView(mEmptyStateTextView);

        // Create adapter with empty ArrayList
        mAdapter = new ItemAdapter(this, new ArrayList<Item>());

        // Set the adapter on the list view
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Item currentEarthquake = mAdapter.getItem(position);

                Uri siteOfBook = null;
                if (currentEarthquake != null) {
                    siteOfBook = Uri.parse(currentEarthquake.getmPreviewLink());
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, siteOfBook);
                startActivity(intent);
            }
        });

        // Check internet connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager and initialize the loader
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateImageView.setImageResource(R.drawable.nonet);
        }
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
        return new ItemLoader(this, MainActivity.getUserURL());
    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_books);
        // Clear adapter
        mAdapter.clear();

        // Add data to list
        if (data != null && !data.isEmpty()) mAdapter.addAll(data);

    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {
        mAdapter.clear();
    }
}
