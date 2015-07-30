package com.lordnash.bacha.interactivestory.model;

import org.w3c.dom.Text;

/**
 * Created by Bacha on 7/18/2015.
 */
public class choice {
    private String mText;
    private int mNextPage;

    public choice(String text ,int nextPage){
        mText = text;
        mNextPage = nextPage;

    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(int mNextPage) {
        this.mNextPage = mNextPage;
    }
}
