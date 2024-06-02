package com.dam.armoniaskills.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Categoria implements Parcelable {
	public static final Creator<Categoria> CREATOR = new Creator<Categoria>() {
		@Override
		public Categoria createFromParcel(Parcel in) {
			return new Categoria(in);
		}

		@Override
		public Categoria[] newArray(int size) {
			return new Categoria[size];
		}
	};
	private int imagen;
	private String titulo;

	public Categoria(int imagen, String titulo) {
		this.imagen = imagen;
		this.titulo = titulo;
	}

	protected Categoria(Parcel in) {
		imagen = in.readInt();
		titulo = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(@NonNull Parcel parcel, int i) {
		parcel.writeInt(imagen);
		parcel.writeString(titulo);
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
