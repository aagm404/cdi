/**
 * A classe abaixo foi criada na ultima aula, como um @Qualifier para injecao de dependencia
 * de um objeto do tipo "java.util.Properties" na classe "br.com.alura.alura_lib.factory.JPAFactory"
 * 
 * O intuito era fazer, por meio de injecao de dependencia, que esta aplicacao, quando utilizada como
 * biblioteca por outras aplicacoes, fizesse a leitura de um arquivo, chamado "alura-lib.properties" nos
 * projetos em que estivesse sendo utilizada, para obter o nome da unidade de persistencia em que a aplicacao
 * vai se conectar, através do valor da chave "alura.lib.persistenceUnit"
 * 
 * Porem, nao deu certo para mim esta abordagem de implementacao.Imagino que seja por conta das versoes das 
 * tecnologias utilizadas. O Tomcat nao subia o CDI/Weld devido a um problema de um metodo "not proxyable" 
 * da classe Properties. A saber, o metodo em questao e' o "java.util.Hashtable.cloneHashtable()"
 * 
 * Portanto, a classe abaixo nao esta sendo utilizada na aplicacao, mas foi mantida para caso algum dia eu saiba
 * como consertar o problema ocorrido
 */

package br.com.alura.alura_lib.configuration.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

}
