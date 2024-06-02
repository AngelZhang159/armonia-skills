package com.dam.armoniaskills.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class ChatDTO {

	private UUID chatId;
	private String nombreUsuario;
	private String ultimoMensaje;
	private Timestamp ultimaHora;
	private String fotoPerfil;
	private String nombreSkill;
	private UUID otroUsuarioId;

	public ChatDTO(UUID chatId, String nombreUsuario, String ultimoMensaje, Timestamp ultimaHora, String fotoPerfil, String nombreSkill, UUID otroUsuarioId) {
		this.chatId = chatId;
		this.nombreUsuario = nombreUsuario;
		this.ultimoMensaje = ultimoMensaje;
		this.ultimaHora = ultimaHora;
		this.fotoPerfil = fotoPerfil;
		this.nombreSkill = nombreSkill;
		this.otroUsuarioId = otroUsuarioId;
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

	public Timestamp getUltimaHora() {
		return ultimaHora;
	}

	public void setUltimaHora(Timestamp ultimaHora) {
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

	public UUID getOtroUsuarioId() {
		return otroUsuarioId;
	}

	public void setOtroUsuarioId(UUID otroUsuarioId) {
		this.otroUsuarioId = otroUsuarioId;
	}
}
