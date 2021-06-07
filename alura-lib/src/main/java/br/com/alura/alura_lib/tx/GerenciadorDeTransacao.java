package br.com.alura.alura_lib.tx;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import br.com.alura.alura_lib.tx.annotation.Transacional;

/* A anotacao @Priority abaixo serve para definir a prioridade de um interceptor e, tambem,
 * para habilitar o intercetor em uma aplicacao, sem termos que inserir qualquer codigo xml
 * no arquivo "beans.xml" da aplicacao
 * 
 * @Priority(Interceptor.Priority.APPLICATION)
*/
@Interceptor
@Transacional
public class GerenciadorDeTransacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Transacionado transacionado;
	
	@Inject
	public GerenciadorDeTransacao(Transacionado transacionado) {
		this.transacionado = transacionado;
	}

	@AroundInvoke
	public Object interceptar(InvocationContext context) {
		return transacionado.executaComTransacao(context);
	}
}
