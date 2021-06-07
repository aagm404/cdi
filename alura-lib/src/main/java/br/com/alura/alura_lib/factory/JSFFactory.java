package br.com.alura.alura_lib.factory;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.alura.alura_lib.jsf.annotation.ScopeMap;
import br.com.alura.alura_lib.jsf.annotation.ScopeMap.Scope;

public class JSFFactory {
	
	FacesContext context;

	@Inject
	public JSFFactory (FacesContext context) {
		this.context = context;
	}
	
	@Produces
	/*
	 * Importamos a anotacao abaixo do qualificador que criamos em
	 * br.com.alura.alura_lib.jsf.annotation.ScopeMap
	 * 0 - RequestMap
	 * 1 - SessionMap
	 * 2 - ApplicationMap
	 */
	@ScopeMap(Scope.REQUEST)
	public Map<String, Object> requestMap() {
		System.out.println("Injetou Dependencia de alura-lib - Scope.REQUEST");
		return context.getExternalContext().getRequestMap();
	}
	
	@Produces
	// Importamos a anotacao abaixo do qualificador que criamos em
	// br.com.alura.alura_lib.jsf.annotation.ScopeMap
	@ScopeMap(Scope.SESSION)
	public Map<String, Object> sessionMap() {
		System.out.println("Injetou Dependencia de alura-lib - Scope.SESSION");
		return context.getExternalContext().getSessionMap();
	}
	
	@Produces
	// Importamos a anotacao abaixo do qualificador que criamos em
	// br.com.alura.alura_lib.jsf.annotation.ScopeMap
	@ScopeMap(Scope.APPLICATION)
	public Map<String, Object> applicationMap() {
		System.out.println("Injetou Dependencia de alura-lib - Scope.APPLICATION");
		return context.getExternalContext().getApplicationMap();
	}
	
	@Produces
	@RequestScoped
	public NavigationHandler getNavigationHandler() {
		System.out.println("Injetou Dependencia de alura-lib - NavigationHandler");
		return context.getApplication().getNavigationHandler();
	}
}
