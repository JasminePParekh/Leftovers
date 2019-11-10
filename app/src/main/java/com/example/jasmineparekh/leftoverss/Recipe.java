package com.example.jasmineparekh.leftoverss;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable{
    private String name;
    private String imageUrl;
    private String sourceUrl;
    private String[] ingredients;
    private String pushId;

    public Recipe(){

    };

    public Recipe(String name, String imageUrl, String sourceUrl, String ingredients) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
        this.ingredients = ingredients.replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\\\","").split("\",\"");
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        sourceUrl = in.readString();
        ingredients = in.createStringArray();
        pushId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(sourceUrl);
        dest.writeStringArray(ingredients);
        dest.writeString(pushId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
