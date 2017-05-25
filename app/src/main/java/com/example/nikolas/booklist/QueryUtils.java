package com.example.nikolas.booklist;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils extends AppCompatActivity{
    private static final String LOG_TAG = QueryUtils.class.getName();
    private static final String ITEMS = "items";
    private static final String VOLUME_INFO = "volumeInfo";
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String PUBLISHED_DATE = "publishedDate";
    private static final String AVERAGE_RATING = "averageRating";
    private static final String PREVIEW_LINK = "previewLink";
    private static final String LANGUAGE = "language";
    private static final String SEARCH_INFO = "searchInfo";
    private static final String TEXT_SNIPPET = "textSnippet";

    public static List<Item> fetchData(String requestUrl) {
        URL url = createURL(requestUrl);

        String responseJSON = null;
        try {
            responseJSON = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        List<Item> itemList = extractFeatureFromJSON(responseJSON);

        return itemList;
    }

    private static List<Item> extractFeatureFromJSON(String responseJSON) {
        if (TextUtils.isEmpty(responseJSON)) {
            return null;
        }

        List<Item> listItem = new ArrayList<>();

        try {
            JSONObject rootJSONObject = new JSONObject(responseJSON);
            JSONArray rootJSONArray = rootJSONObject.getJSONArray(ITEMS);

            for (int i = 0; i < rootJSONArray.length(); i++) {
                JSONObject currentObject = rootJSONArray.getJSONObject(i);

                // Get the volumeInfo object
                JSONObject volumeInfoObject = currentObject.getJSONObject(VOLUME_INFO);

                // Get title of the book
                String title = volumeInfoObject.getString(TITLE);

                // Get authors of the book
                ArrayList<String> authorsList = new ArrayList<>();
                JSONArray authors = volumeInfoObject.optJSONArray(AUTHORS);
                if (authors == null) {  // In the case of no author
                    authorsList.add(0, "");
                } else {
                    for (int j = 0; j < authors.length(); j++) {
                        authorsList.add(authors.getString(j));
                    }
                }

                // Get published Date of the book
                String publishedDate = volumeInfoObject.getString(PUBLISHED_DATE);

                // Get average Rating of the book
                // check if exist
                Double averageRating = 0.0;
                if (volumeInfoObject.has(AVERAGE_RATING))
                    averageRating = volumeInfoObject.getDouble(AVERAGE_RATING);

                // Get previewLink of the book
                String previewLink = volumeInfoObject.getString(PREVIEW_LINK);

                // Get language of the book
                String language = volumeInfoObject.getString(LANGUAGE);

                // Get textSnippet of the book
                JSONObject textSnippetObject = currentObject.getJSONObject(SEARCH_INFO);
                String textSnippet = textSnippetObject.getString(TEXT_SNIPPET);

                Item item = new Item(title, language, textSnippet, publishedDate, averageRating, previewLink, authorsList);
                listItem.add(item);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error at extractFeatureFromJSON function", e);
        }
        return listItem;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String responseJSON = "";

        if (url == null) {
            return responseJSON;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                responseJSON = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response HTTP code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving data from JSON results");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return responseJSON;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder outputData = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                outputData.append(line);
                line = bufferedReader.readLine();
            }
        }
        return outputData.toString();
    }

    private static URL createURL(String requestUrl) {
        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }
}
