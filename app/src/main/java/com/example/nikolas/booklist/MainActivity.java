package com.example.nikolas.booklist;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";
    private static String bookName;
    private static String userURL;
    private TextView textViewError;
    ObjectAnimator errorTextAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Result.class);

                // Fetch data to create the url query
                EditText editText = (EditText) findViewById(R.id.search_edit_text);
                bookName = editText.getText().toString();

                Uri baseUri = Uri.parse(REQUEST_URL);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("q", bookName);
                uriBuilder.appendQueryParameter("maxResults", "30");
                userURL = uriBuilder.toString();

                textViewError = (TextView) findViewById(R.id.error_text_view);
                if (!bookName.isEmpty()) {
                    textViewError.setVisibility(View.INVISIBLE);
                    startActivity(intent);
                } else {
                    // Hide keyboard to show the message
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    // Set error text view visible
                    textViewError.setVisibility(View.VISIBLE);
                    // Text blink animation
                    errorTextAnimate = ObjectAnimator.ofInt(textViewError, "textColor", Color.RED, Color.TRANSPARENT);
                    errorTextAnimate.setDuration(1000);
                    errorTextAnimate.setEvaluator(new ArgbEvaluator());
                    errorTextAnimate.setRepeatCount(ValueAnimator.INFINITE);
                    errorTextAnimate.setRepeatMode(ValueAnimator.REVERSE);
                    errorTextAnimate.start();
                    Log.e(LOG_TAG, "Empty search box!");
                }
            }
        });
    }

    public static String getBookName() { return bookName; }

    public static String getUserURL() {
        return userURL;
    }
}
