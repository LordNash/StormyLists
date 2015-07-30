package com.lordnash.bacha.interactivestory.model;

import java.text.ChoiceFormat;

/**
 * Created by Bacha on 7/18/2015.
 */
public class Page {
    private int mImageId;
    private String mText;
    private choice mChoise1;
    private choice mChoise2;
    private boolean mIsFinal =false;

    public Page(int ImageId,String text){
        mImageId = ImageId;
        mText = text;
        mChoise1 = null;
        mChoise2 = null;
        mIsFinal = true;
    }

    public boolean isIsFinal() {
        return mIsFinal;
    }

    public void setIsFinal(boolean mIsFinal) {
        this.mIsFinal = mIsFinal;
    }

    public Page(int ImageId,String text ,choice ch1,choice ch2){
        mImageId = ImageId;
        mText = text;
        mChoise1 = ch1;
        mChoise2 = ch2;
    }

    public int getImageId() {
        return mImageId;
    }

    public choice getChoise1() {
        return mChoise1;
    }

    public String getText() {
        return mText;
    }

    public void setImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public void setChoise1(choice mChoise1) {
        this.mChoise1 = mChoise1;
    }

    public choice getChoise2() {
        return mChoise2;
    }

    public void setChoise2(choice mChoise2) {
        this.mChoise2 = mChoise2;
    }
}
