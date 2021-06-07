package br.com.alura.livraria.bean;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.alura.alura_lib.helper.MessageHelper;
import br.com.alura.alura_lib.jsf.annotation.ScopeMap;
import br.com.alura.alura_lib.jsf.annotation.ScopeMap.Scope;
import br.com.alura.livraria.dao.UsuarioDao;
import br.com.alura.livraria.modelo.Usuario;

/*
 * @Model e' um Stereotype (esteriotipo) definido pelo CDI, para as anotacoes @Named e @RequestScoped.
 * Ou seja, usar estas duas ultimas anotacoes e' a mesma coisa que usar a anotacao @Model
 * 
 * @Model = @Named + @RequestScopeds
 * 
 */

// @Named
// @RequestScoped

@Model
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Usuario usuario = new Usuario();
	
	// @Inject --> Colocar a anotação @Inject em um construtor da classe e, no construtor, colocarmos um atriubuto,
	// e' o mesmo que colocar a anotação @Inject em cima deste atributo. Veja o construtor abaixo
	private UsuarioDao usuarioDao;

	// Classe auxiliar criada para insercao de mensagens personalizadas ao contexto JSF
	private MessageHelper messageHelper;

	private Map<String, Object> sessionMap;
	
	@Inject
	public LoginBean(UsuarioDao usuarioDao, MessageHelper messageHelper, @ScopeMap(Scope.SESSION) Map<String, Object> sessionMap) {
		this.usuarioDao = usuarioDao;
		this.messageHelper = messageHelper;
		this.sessionMap = sessionMap;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	
	public String efetuaLogin() {
		System.out.println("fazendo login do usuario " + this.usuario.getEmail());
		 
		boolean existe = this.usuarioDao.existe(this.usuario);
		if (existe) {
//			context.getExternalContext().getSessionMap().put("usuarioLogado", this.usuario);
			sessionMap.put("usuarioLogado", this.usuario);
			return "livro?faces-redirect=true";
		}
		
		messageHelper.onFlash().addMessage("Usuário não encontrado");
		
		return "login?faces-redirect=true";
	}
	
	public String deslogar() {
//		context.getExternalContext().getSessionMap().remove("usuarioLogado");
		sessionMap.remove("usuarioLogado");
		return "login?faces-redirect=true";
	}
}
