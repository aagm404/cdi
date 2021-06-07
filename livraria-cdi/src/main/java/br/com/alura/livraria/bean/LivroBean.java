package br.com.alura.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import br.com.alura.alura_lib.dao.DAO;
import br.com.alura.alura_lib.jsf.annotation.ViewModel;
import br.com.alura.alura_lib.tx.annotation.Transacional;
import br.com.alura.livraria.modelo.Autor;
import br.com.alura.livraria.modelo.Livro;

/*
 * @ViewModel e' um Stereotype (esteriotipo) definido por mim, para as anotacoes @Named e @ViewScoped.
 * Ou seja, usar estas duas ultimas anotacoes e' a mesma coisa que usar a anotacao @ViewModel
 * 
 * @ViewModel = @Named + @ViewScoped (javax.faces.view.ViewScoped)
 * 
 */

// @Named
// @ViewScoped //javax.faces.view.ViewScoped

@ViewModel
public class LivroBean implements Serializable {

	private static final long serialVersionUID = 1L;


	private Livro livro = new Livro();

	private Integer autorId;

	private List<Livro> livros;

	private DAO<Livro, Integer> livroDao;
	private DAO<Autor, Integer> autorDao;
	
	
	@Inject
	public LivroBean(DAO<Livro, Integer> livroDao, DAO<Autor, Integer> autorDao) {
		this.livroDao = livroDao;
		this.autorDao = autorDao;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public List<Livro> getLivros() {
		if(this.livros == null) {
			this.livros = this.livroDao.listaTodos();
		}
		return livros;
	}

	public List<Autor> getAutores() {
		return this.autorDao.listaTodos();
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public void carregarLivroPelaId() {
		this.livro = this.livroDao.buscaPorId(this.livro.getId()); 
	}
	
	public void gravarAutor() {
		Autor autor = this.autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Escrito por: " + autor.getNome());
	}

	@Transacional
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));
			return;
		}

		if(this.livro.getId() == null) {
			this.livroDao.adiciona(this.livro);
		} else {
			this.livroDao.atualiza(this.livro);
		}

		// O trecho abaixo e' para atualizar a lista de livros exibida.
		// Este trecho entra no if e no else acima. Logo, podemos coloca-lo para fora
		this.livros = this.livroDao.listaTodos();
		
		// O trecho abaixo e' para limpar os campos dos formularios da pagina
		// "livro.xhtml"
		// depois de clicar no botao de gravar
		this.livro = new Livro();
	}

	@Transacional
	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		this.livroDao.remove(livro);
		this.livros = this.livroDao.listaTodos();
	}
	
	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}
	
	public void carregar(Livro livro) {
		System.out.println("Carregando livro");
		this.livro = this.livroDao.buscaPorId(livro.getId());
	}
	
	public String formAutor() {
		System.out.println("Chamanda do formulário do Autor.");
		return "autor?faces-redirect=true";
	}

	public void comecaComDigitoNove(FacesContext fc, UIComponent component, Object value) throws ValidatorException {

		String valor = value.toString();
		if (!valor.startsWith("9")) {
			throw new ValidatorException(new FacesMessage("ISBN deveria começar com 9"));
		}

	}
}
