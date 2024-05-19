package com.dam.armoniaskills.model;

import java.util.Date;
import java.util.UUID;

public class ChatDTO {

	private UUID chatId;
	private String nombreUsuario;
	private String ultimoMensaje;
	private Date ultimaHora;
	private String fotoPerfil;
	private String nombreSkill;

	public ChatDTO(UUID chatId, String nombreUsuario, String ultimoMensaje, Date ultimaHora, String fotoPerfil, String nombreSkill) {
		this.chatId = chatId;
		this.nombreUsuario = nombreUsuario;
		this.ultimoMensaje = ultimoMensaje;
		this.ultimaHora = ultimaHora;
		this.fotoPerfil = fotoPerfil;
		this.nombreSkill = nombreSkill;
	}

	public UUID getChatId() {
		return chatId;
	}

	public void setChatId(UUID chatId) {
		this.chatId = chatId;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getUltimoMensaje() {
		return ultimoMensaje;
	}

	public void setUltimoMensaje(String ultimoMensaje) {
		this.ultimoMensaje = ultimoMensaje;
	}

	public Date getUltimaHora() {
		return ultimaHora;
	}

	public void setUltimaHora(Date ultimaHora) {
		this.ultimaHora = ultimaHora;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public String getNombreSkill() {
		return nombreSkill;
	}

	public void setNombreSkill(String nombreSkill) {
		this.nombreSkill = nombreSkill;
	}
}
