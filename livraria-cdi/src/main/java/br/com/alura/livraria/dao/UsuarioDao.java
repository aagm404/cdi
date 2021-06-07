package br.com.alura.livraria.dao;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.alura.alura_lib.jpa.annotation.Query;
import br.com.alura.livraria.modelo.Usuario;

public class UsuarioDao {
	
	/*
	 * Conforme o instrutor do curso, a injecao de dependencia abaixo e' somente para exemplificar a 
	 * anotacao @Nonbinding de um qualificador. 
	 * 
	 * Mas na verdade, nao faz muito sentido injetar varios objetos diferente do tipo TypedQuery 
	 * toda vez que se quizer fazer uma query.
	 * 
	 * O correto seria injetar o EntityManager uma unica vez e, toda vez que se quisesse uma query diferente, 
	 * obtermos um objeto do tipo TypedQuery atraves da sentenca "em.createQuery()"
	 */
	@Inject
	@Query("select u from Usuario u where u.email = :pEmail and u.senha = :pSenha")
	private TypedQuery<Usuario> querySelectAll;
	
	public boolean existe(Usuario usuario) {
		querySelectAll.setParameter("pEmail", usuario.getEmail());
		querySelectAll.setParameter("pSenha", usuario.getSenha());
		try {
			querySelectAll.getSingleResult();
		} catch (NoResultException ex) {
			return false;
		}
		
		return true;
	}
}
