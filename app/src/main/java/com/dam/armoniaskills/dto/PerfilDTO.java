package com.dam.armoniaskills.dto;

import com.dam.armoniaskills.model.Review;
import com.dam.armoniaskills.model.Skill;

import java.util.List;

public class PerfilDTO {
	private String nombre;
	private String foto;
	private String telefono;
	private List<Review> reviews;
	private List<Skill> skills;

	public PerfilDTO(String nombre, String foto, String telefono, List<Review> reviews, List<Skill> skills) {
		this.nombre = nombre;
		this.foto = foto;
		this.telefono = telefono;
		this.reviews = reviews;
		this.skills = skills;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
}
