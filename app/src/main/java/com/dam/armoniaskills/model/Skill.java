package com.dam.armoniaskills.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.UUID;

public class Skill implements Parcelable {

    private String title;
    private String description;
    private String category;
    private String price;
    private String location;
    private UUID userID;
    private List<String> imageList;

    public Skill(String title, String description, String category, String price, String location, UUID userID, List<String> imageList) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.location = location;
        this.imageList = imageList;
        this.userID = userID;
    }

    protected Skill(Parcel in) {
        title = in.readString();
        description = in.readString();
        category = in.readString();
        price = in.readString();
        location = in.readString();
        imageList = in.createStringArrayList();
        userID = UUID.fromString(in.readString());
    }

    public static final Creator<Skill> CREATOR = new Creator<Skill>() {
        @Override
        public Skill createFromParcel(Parcel in) {
            return new Skill(in);
        }

        @Override
        public Skill[] newArray(int size) {
            return new Skill[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(price);
        dest.writeString(location);
        dest.writeStringList(imageList);
        dest.writeString(userID.toString());
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
