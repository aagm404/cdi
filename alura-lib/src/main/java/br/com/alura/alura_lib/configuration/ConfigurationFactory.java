/**
 * A classe abaixo foi criada na ultima aula, como uma fabrica de configuracao para injecao de dependencia
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

package br.com.alura.alura_lib.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import br.com.alura.alura_lib.configuration.annotation.Configuration;

public class ConfigurationFactory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Produces
	@Configuration
	@ApplicationScoped
	public Properties getProperties() throws IOException {
		InputStream inputStream = ConfigurationFactory.class.getResourceAsStream("/alura-lib.properties");

		Properties properties = new Properties();
		properties.load(inputStream);

		return properties;
	}
}
