package com.dam.armoniaskills.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.dam.armoniaskills.model.StatusCompraEnum;

import java.sql.Timestamp;
import java.util.UUID;

public class ComprasVentasDTO implements Parcelable {

	public static final Creator<ComprasVentasDTO> CREATOR = new Creator<ComprasVentasDTO>() {
		@Override
		public ComprasVentasDTO createFromParcel(Parcel in) {
			return new ComprasVentasDTO(in);
		}

		@Override
		public ComprasVentasDTO[] newArray(int size) {
			return new ComprasVentasDTO[size];
		}
	};
	private UUID id;
	private String username;
	private Timestamp date;
	private String imageURL;
	private String skillName;
	private UUID skillId;
	private StatusCompraEnum status;
	private double price;

	public ComprasVentasDTO(UUID id, String username, Timestamp date, String imageURL, String skillName, UUID skillId, StatusCompraEnum status, double price) {
		this.id = id;
		this.username = username;
		this.date = date;
		this.imageURL = imageURL;
		this.skillName = skillName;
		this.skillId = skillId;
		this.status = status;
		this.price = price;
	}

	protected ComprasVentasDTO(Parcel in) {
		username = in.readString();
		imageURL = in.readString();
		skillName = in.readString();
		price = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(imageURL);
		dest.writeString(skillName);
		dest.writeDouble(price);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public UUID getSkillId() {
		return skillId;
	}

	public void setSkillId(UUID skillId) {
		this.skillId = skillId;
	}

	public StatusCompraEnum getStatus() {
		return status;
	}

	public void setStatus(StatusCompraEnum status) {
		this.status = status;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
