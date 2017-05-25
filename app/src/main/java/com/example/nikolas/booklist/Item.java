package com.example.nikolas.booklist;

import java.util.ArrayList;

public class Item {
    private String mTitle;
    private String mLanguage;
    private String mTextSnippet;
    private String mPublishedDate;
    private Double mAverageRating;
    private String mPreviewLink;
    private ArrayList<String> mAuthorsList;

    public Item(String mTitle, String mLanguage, String mTextSnippet, String mPublishedDate, Double mAverageRating, String mPreviewLink, ArrayList<String> mAuthorsList) {
        this.mTitle = mTitle;
        this.mLanguage = mLanguage;
        this.mTextSnippet = mTextSnippet;
        this.mPublishedDate = mPublishedDate;
        this.mAverageRating = mAverageRating;
        this.mPreviewLink = mPreviewLink;
        this.mAuthorsList = mAuthorsList;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public String getmTextSnippet() {
        return mTextSnippet;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public Double getmAverageRating() {
        return mAverageRating;
    }

    public String getmPreviewLink() {
        return mPreviewLink;
    }

    public ArrayList<String> getmAuthorsList() {
        return mAuthorsList;
    }
}


