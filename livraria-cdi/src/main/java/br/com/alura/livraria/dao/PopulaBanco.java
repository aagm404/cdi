package br.com.alura.livraria.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import br.com.alura.livraria.modelo.Autor;
import br.com.alura.livraria.modelo.Livro;
import br.com.alura.livraria.modelo.Usuario;
import br.com.alura.livraria.modelo.Venda;

/*
 * Classe para popular dados iniciais na base de dados
 */
public class PopulaBanco {

	public static void main(String[] args) {
		
		EntityManager em = Persistence.createEntityManagerFactory("livraria-cdi").createEntityManager();
		
		em.getTransaction().begin();

		Autor assis = geraAutor("Machado de Assis", "machadinho@gmail.com");
		Autor amado = geraAutor("Jorge Amado", "jorge@amado.com");
		Autor coelho = geraAutor("Paulo Coelho", "paul@rabbit.com");
		Autor ziraldo = geraAutor("Ziraldo", "ziraldol@maluquinho.com");
		Autor poe = geraAutor("Edgar Allan Poe", "edgar@poe.com");
		Autor agathaC = geraAutor("Agatha Christie", "agathac@gmail.com");
		
		em.persist(assis);
		em.persist(amado);
		em.persist(coelho);
		em.persist(ziraldo);
		em.persist(poe);
		em.persist(agathaC);

		Livro casmurro = geraLivro("978-8-52-504464-8", "Dom Casmurro",
				"10/01/1899", 24.90, assis);
		Livro memorias = geraLivro("978-8-50-815415-9",
				"Memorias Postumas de Bras Cubas", "01/01/1881", 19.90, assis);
		Livro quincas = geraLivro("978-8-50-804084-1", "Quincas Borba",
				"01/01/1891", 16.90, assis);

		
		Livro alquimista = geraLivro("978-8-57-542758-3", "O Alquimista",
				"01/01/1988", 19.90, coelho);
		Livro brida = geraLivro("978-8-50-567258-7", "Brida", "01/01/1990",
				12.90, coelho);
		Livro valkirias = geraLivro("978-8-52-812458-8", "As Valkirias",
				"01/01/1992", 29.90, coelho);
		Livro mago = geraLivro("978-8-51-892238-9", "O Diario de um Mago",
				"01/01/1987", 9.90, coelho);
		
		Livro capitaes = geraLivro("978-8-50-831169-1", "Capitaes da Areia",
				"01/01/1937", 6.90, amado);
		Livro flor = geraLivro("978-8-53-592569-9",
				"Dona Flor e Seus Dois Maridos", "01/01/1966", 18.90, amado);
		
		
		Livro maluqinho = geraLivro("978-8-50-804864-1", "O Menino Maluquinho",
				"01/01/1980", 99.90, ziraldo);
		Livro corvo = geraLivro("978-8-50-056831-1", "O Corvo",
				"01/01/1845", 120.50, poe);
		Livro roger = geraLivro("978-8-50-056977-0", "O Assassinato de Roger Ackroyd",
				"01/01/1926", 115.30, agathaC);

		em.persist(casmurro);
		em.persist(memorias);
		em.persist(quincas);
		
		em.persist(alquimista);
		em.persist(brida);
		em.persist(valkirias);
		em.persist(mago);
		
		em.persist(capitaes);
		em.persist(flor);
		
		em.persist(maluqinho);
		em.persist(corvo);
		em.persist(roger);

		Venda venda1 = geraVenda(casmurro, 1000);
		Venda venda2 = geraVenda(memorias, 1250);
		Venda venda3 = geraVenda(quincas, 980);
		Venda venda4 = geraVenda(alquimista, 500);
		Venda venda5 = geraVenda(brida, 300);
		Venda venda6 = geraVenda(valkirias, 900);
		Venda venda7 = geraVenda(mago, 650);
		Venda venda8 = geraVenda(capitaes, 2500);
		Venda venda9 = geraVenda(flor, 1800);
		Venda venda10 = geraVenda(maluqinho, 750);
		Venda venda11 = geraVenda(corvo, 1500);
		Venda venda12 = geraVenda(roger, 1980);
		
		
		em.persist(venda1);
		em.persist(venda2);
		em.persist(venda3);
		em.persist(venda4);
		em.persist(venda5);
		em.persist(venda6);
		em.persist(venda7);
		em.persist(venda8);
		em.persist(venda9);
		em.persist(venda10);
		em.persist(venda11);
		em.persist(venda12);
		
		Usuario user1 = geraUsuario("user1@gmail.com", "123");
		em.persist(user1);
		
		Usuario user2 = geraUsuario("usuario@exemplo.com", "abc");
		em.persist(user2);

		em.getTransaction().commit();
		em.close();

	}

	private static Autor geraAutor(String nome, String email) {
		Autor autor = new Autor();
		autor.setNome(nome);
		autor.setEmail(email);
		return autor;
	}

	private static Livro geraLivro(String isbn, String titulo, String data,
			Double preco, Autor autor) {
		Livro livro = new Livro();
		livro.setIsbn(isbn);
		livro.setTitulo(titulo);
		livro.setDataLancamento(parseData(data));
		livro.setPreco(preco);
		livro.adicionaAutor(autor);
		return livro;
	}
	
	private static Usuario geraUsuario(String email, String senha) {
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(senha);
		return usuario;
	}
	
	private static Venda geraVenda(Livro livro, Integer quantidade) {
		return new Venda(livro, quantidade);
	}

	private static Calendar parseData(String data) {
		try {
			Date date = new SimpleDateFormat("dd/MM/yyyy").parse(data);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
