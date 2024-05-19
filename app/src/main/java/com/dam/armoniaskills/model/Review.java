package com.dam.armoniaskills.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Review implements Parcelable {

    private String content;

    private int stars;

    private UUID skillID;

    private UUID sellerID;

    private UUID buyerID;

    public Review(String content, int stars, UUID skillID, UUID sellerID, UUID buyerID) {
        this.content = content;
        this.stars = stars;
        this.skillID = skillID;
        this.sellerID = sellerID;
        this.buyerID = buyerID;
    }

    protected Review(Parcel in) {
        content = in.readString();
        stars = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeInt(stars);
        dest.writeString(String.valueOf(skillID));
        dest.writeString(String.valueOf(sellerID));
        dest.writeString(String.valueOf(buyerID));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public UUID getSkillID() {
        return skillID;
    }

    public void setSkillID(UUID skillID) {
        this.skillID = skillID;
    }

    public UUID getSellerID() {
        return sellerID;
    }

    public void setSellerID(UUID sellerID) {
        this.sellerID = sellerID;
    }

    public UUID getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(UUID buyerID) {
        this.buyerID = buyerID;
    }

    @Override
    public String toString() {
        return "Review{" +
                "content='" + content + '\'' +
                ", stars=" + stars +
                ", skillID=" + skillID +
                ", sellerID=" + sellerID +
                ", buyerID=" + buyerID +
                '}';
    }
}
