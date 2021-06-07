package br.com.alura.alura_lib.tx;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

@Typed(Transacionado.class)
public class TransacionadoPadrao implements Transacionado {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	protected EntityManager em;
	
	
	public Object executaComTransacao(InvocationContext context) {
		
		// Interceptador que faz alguma coisa entre o "begin()" e o "commit()" abaixo
		
		em.getTransaction().begin();
		
		try {
			// A declaracao "context.proceed()" executa o metodo que esta' sento interceptado e retorna um object
			// E' o metodo proceed() abaixo que lança a excecao que esta' sendo tratada
			Object resultado = context.proceed();
			
			em.getTransaction().commit();
			
			return resultado;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new RuntimeException();
		}
	}
}
