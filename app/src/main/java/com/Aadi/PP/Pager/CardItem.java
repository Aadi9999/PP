package com.Aadi.PP.Pager;

public class CardItem {

    private int mPictureResource;
    private int mTitleResource;

    public CardItem(int title, int picture){
        mTitleResource = title;
        mPictureResource = picture;
    }

    public int getPicture() {
        return mPictureResource;
    }

    public int getTitle() {
        return mTitleResource;
    }

}