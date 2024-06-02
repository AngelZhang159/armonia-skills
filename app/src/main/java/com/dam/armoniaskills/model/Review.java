package com.dam.armoniaskills.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Review implements Parcelable {

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
	private String content;
	private int stars;
	private UUID skillId;
	private UUID sellerId;
	private UUID buyerId;
	private String imageUrl;
	private String username;


	public Review(String content, int stars, UUID skillId, UUID sellerId, UUID buyerId, String imageUrl, String username) {
		this.content = content;
		this.stars = stars;
		this.skillId = skillId;
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.imageUrl = imageUrl;
		this.username = username;
	}

	protected Review(Parcel in) {
		content = in.readString();
		stars = in.readInt();
		imageUrl = in.readString();
		username = in.readString();
		skillId = UUID.fromString(in.readString());
		sellerId = UUID.fromString(in.readString());
		buyerId = UUID.fromString(in.readString());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(@NonNull Parcel dest, int flags) {
		dest.writeString(content);
		dest.writeInt(stars);
		dest.writeString(imageUrl);
		dest.writeString(username);
		dest.writeString(skillId.toString());
		dest.writeString(sellerId.toString());
		dest.writeString(buyerId.toString());
	}

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

	public UUID getSkillId() {
		return skillId;
	}

	public void setSkillId(UUID skillId) {
		this.skillId = skillId;
	}

	public UUID getSellerId() {
		return sellerId;
	}

	public void setSellerId(UUID sellerId) {
		this.sellerId = sellerId;
	}

	public UUID getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(UUID buyerId) {
		this.buyerId = buyerId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
