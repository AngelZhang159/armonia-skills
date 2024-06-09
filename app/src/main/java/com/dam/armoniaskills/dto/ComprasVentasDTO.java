package com.dam.armoniaskills.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.dam.armoniaskills.model.StatusCompraEnum;

import java.sql.Timestamp;
import java.util.UUID;

public class ComprasVentasDTO implements Parcelable {

	private UUID id;
	private String username;
	private Timestamp date;
	private String imageURL;
	private String skillName;
	private UUID skillId;
	private StatusCompraEnum status;

	public ComprasVentasDTO(UUID compraVentaId, String nombreUsuario, Timestamp date, String fotoPerfil, String nombreSkill, UUID skillId, StatusCompraEnum status) {
		this.id = compraVentaId;
		this.username = nombreUsuario;
		this.date = date;
		this.imageURL = fotoPerfil;
		this.skillName = nombreSkill;
		this.skillId = skillId;
		this.status = status;
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

	protected ComprasVentasDTO(Parcel in) {
		username = in.readString();
		imageURL = in.readString();
		skillName = in.readString();
	}

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(@NonNull Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(imageURL);
		dest.writeString(skillName);
	}
}
