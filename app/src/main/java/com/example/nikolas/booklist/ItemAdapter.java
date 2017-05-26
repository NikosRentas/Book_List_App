package com.example.nikolas.booklist;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private static final int LANGUAGE_ABBREVIATION = -1;

    public ItemAdapter(@NonNull Context context, @NonNull List<Item> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Viewholder viewholder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            viewholder = new Viewholder();
            viewholder.titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
            viewholder.authorTextView =(TextView) convertView.findViewById(R.id.author_text_view);
            viewholder.dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
            viewholder.langTextView =(TextView) convertView.findViewById(R.id.language_text_view);
            viewholder.snippetTextView =(TextView) convertView.findViewById(R.id.textSnippet_text_view);
            viewholder.bookImageView = (ImageView) convertView.findViewById(R.id.book_image_view);
            viewholder.ratingTextView= (TextView) convertView.findViewById(R.id.rating_text_view);
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }

        Item currentItem = getItem(position);

        // Title text view
        viewholder.titleTextView.setText(currentItem.getmTitle());

        // Author text view
        String concatenateAuthors = "";
        if (currentItem.getmAuthorsList().get(0).equals("")) {
            viewholder.authorTextView.setText("Author unknow");
        } else {
            for (int i = 0; i < currentItem.getmAuthorsList().size(); i++) {
                if (i > 0 && i < currentItem.getmAuthorsList().size() - 1) {
                    concatenateAuthors += currentItem.getmAuthorsList().get(i) + ", ";
                } else {
                    concatenateAuthors += currentItem.getmAuthorsList().get(i);
                }
            }
            viewholder.authorTextView.setText(concatenateAuthors);
        }

        // Date text view
        viewholder.dateTextView.setText("Publish date: " + currentItem.getmPublishedDate());

        // Language text view
        int lang = getLanguage(currentItem.getmLanguage());
        if (lang != LANGUAGE_ABBREVIATION) {
            viewholder.langTextView.setText(lang);
        } else {
            viewholder.langTextView.setText(currentItem.getmLanguage());
        }

        // Snippet text view
        viewholder.snippetTextView.setText(currentItem.getmTextSnippet());

        // Book image view
        viewholder.bookImageView.setImageResource(R.drawable.notebook);

        // Rating text view
        GradientDrawable ratingCircle = (GradientDrawable) viewholder.ratingTextView.getBackground();
        viewholder.ratingTextView.setText(String.valueOf(currentItem.getmAverageRating()));
        int color = getCircleColor(currentItem.getmAverageRating());
        ratingCircle.setColor(color);

        return convertView;
    }

    private int getCircleColor(double averageRating) {
        int colorResourceID;
        int colorInteger = (int) Math.floor(averageRating);
        switch (colorInteger) {
            case 0:
                colorResourceID = R.color.rating_0;
                break;
            case 1:
                colorResourceID = R.color.rating_1;
                break;
            case 2:
                colorResourceID = R.color.rating_2;
                break;
            case 3:
                colorResourceID = R.color.rating_3;
                break;
            case 4:
                colorResourceID = R.color.rating_4;
                break;
            case 5:
                colorResourceID = R.color.rating_5;
                break;
            default:
                colorResourceID = R.color.rating_0;
                break;
        }
        return ContextCompat.getColor(getContext(), colorResourceID);
    }

    private int getLanguage(String abbr) {
        if (abbr.equalsIgnoreCase("en")) {
            return R.string.en;
        } else if (abbr.equalsIgnoreCase("gr")) {
            return R.string.gr;
        } else if (abbr.equalsIgnoreCase("it")) {
            return R.string.it;
        } else if (abbr.equalsIgnoreCase("gre")) {
            return R.string.gre;
        } else if (abbr.equalsIgnoreCase("ru")) {
            return R.string.ru;
        } else if (abbr.equalsIgnoreCase("fr")) {
            return R.string.fr;
        }

        return LANGUAGE_ABBREVIATION;
    }

    static class Viewholder {
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView dateTextView;
        private TextView langTextView;
        private TextView snippetTextView;
        private ImageView bookImageView;
        private TextView ratingTextView;
    }
}
