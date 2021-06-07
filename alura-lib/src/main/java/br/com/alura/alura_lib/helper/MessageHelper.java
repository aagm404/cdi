package br.com.alura.alura_lib.helper;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

/*
 * Classe criada para adicionar mensagens personalizadas ao contexto do JSF
 */

public class MessageHelper {
	
	private FacesContext context;
	private Flash flash;

	@Inject
	public MessageHelper (FacesContext context, Flash flash) {
		this.context = context;
		this.flash = flash;
	}

	public void addMessage(String message) {
		addMessage(null, message);
	}

	private void addMessage(String clientId, String message) {
		context.addMessage(clientId, new FacesMessage (message));
	}
	
	public MessageHelper onFlash() {
		flash.setKeepMessages(true);
		return this;
	}
}
