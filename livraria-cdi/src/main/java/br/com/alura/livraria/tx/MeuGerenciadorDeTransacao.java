package br.com.alura.livraria.tx;

import javax.enterprise.inject.Specializes;
import javax.interceptor.InvocationContext;

import br.com.alura.alura_lib.tx.TransacionadoPadrao;

/*
 * A anotacao abaixo resolve o problema de ambiguidade de injecao de dependencias de objetos do tipo Transacionado
 * 
 * A classe br.com.alura.alura_lib.tx.GerenciadorDeTransacao e' um interceptador que recebe, atraves de injecao de
 * dependencia pelo CDI, um objeto do tipo "Transacionado"
 * 
 * Transacionado e' uma interface --> br.com.alura.alura_lib.tx.Transacionado
 * 
 * A classe br.com.alura.alura_lib.tx.TransacionadoPadrao e' uma implementacao explicita da interface Transacionado,
 * que determina o comportamento do metodo do interceptor GerenciadorDeTransacao
 * 
 * Porem, criamos uma classe neste projeto que extente a classe TransacionadoPadrao. Logo, esta classe tambem e' uma
 * implementacao da classe Transacionado. Podemos fazer uma implementacao particular do metodo "executaComTransacao"
 * 
 * O problema e' que geramos uma ambiguidade para injecao de dependencia do objeto Transacionado, no interceptor
 * GerenciadorDeTransacao. Temos duas implementacoes e o CDI nao sabe qual utilizar. O Tomcat nem sobe e acusa
 * problema de ambiguidade de injecao de dependencia.
 * 
 * Para resolver este problema, utilizamos a anotacao @Specializaes abaixo
 * 
 * Ela define que eu quero que esta classe seja a implementacao de Transacionado que o CDI deve injetar no interceptor
 * 
 * Esta anotacao e' util quando temos heranca envolvida. Quando estamos herdando de outra classe
 * 
 * Optamos por herdar a classe TransacionadoPadrao (ao inves de implementar a interface Tansacionad), pois herdamos junto
 * o EntityManager que tem modificador de acesso "protected" na classe TransacionadoPadrao.
 * 
 * Poderiamos, sem problema algum, ao inves de extender a classe TransacionadoPadrao, implementar diretamente a interface
 * Transacionado. Precisariamos injetar o EntityManager e implementar o metodo "executaComTransacao" como quisessemos
 * Neste caso, ao inves da anotacao @Specializes, teriamos que utilizar as anotacoes 
 * @Alternative, @Priority(Interceptor.Priority.APPLICATION) e @Typed(Transacionado.class)
 */

@Specializes
public class MeuGerenciadorDeTransacao extends TransacionadoPadrao {

	private static final long serialVersionUID = 1L;

	@Override
	public Object executaComTransacao(InvocationContext context) {
		System.out.println("Abrindo uma transacao");

		em.getTransaction().begin();

		try {
			System.out.println("Antes de executar o metodo interceptado");
			Object resultado = context.proceed();

			System.out.println("Antes de commitar a transacao");
			em.getTransaction().commit();

			return resultado;
		} catch (Exception e) {
			System.out.println("Antes de efeturar o rollback da transacao");
			em.getTransaction().rollback();
			e.printStackTrace();

			throw new RuntimeException(e);
		}

	}
}
