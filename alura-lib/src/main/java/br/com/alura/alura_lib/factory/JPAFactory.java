package br.com.alura.alura_lib.factory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class JPAFactory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * A sentenca imediatamente abaixo existiu ate' a penultima aula.
	 * 
	 * Na ultima aula, o instrutor quis implementar a injecao de dependencia de um objeto do tipo "java.util.Properties"
	 * para que fosse possivel utilizar esta biblioteca (alura-lib) em varios projetos diferentes, mudando somente
	 * o valor do atributo "alura.lib.persistenceUnit" do arquivo "alura-lib.properties".
	 * 
	 * A ideia e' que toda aplicacao que utilize este projeto alura-lib como biblioteca, deve ter um arquivo chamado
	 * "alura-lib.porperties", com uma chave "alura.lib.persistenceUnit", cujo valor seja o nome da unidade
	 * de persistencia do projeto
	 * 
	 * As classes implementadas para a injecao de dependencia citada anteriormente foram 
	 * br.com.alura.alura_lib.configuration.ConfigurationFactory
	 * e
	 * br.com.alura.alura_lib.configuration.annotation.Configuration
	 * 
	 * Porem nao deu certo. Aparentemente e' algum problema com a versao do combo 
	 * JSF 2.3.9, CDI 2.0, Weld 3.1.6.Final, e Tomcat 9.0.41
	 * 
	 * Entao deixei minha implementacao particular (sem injecao de dependencia) da leitura de um arquivo para 
	 * se obter a unidade de persistencia de um projeto que use esta biblioteca.
	 * Esta implementacao encontra-se no @PostConstruct loadEMF() desta classe
	 * 
	 */
//	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("livraria-cdi");

	/**
	 * O codigo comentado imediatamente abaixo e' o que o instrutor implementou na ultima aula do curso
	 * Nao deu certo. Tomcat nao subia o CDI/Weld devido a um problema de um metodo "not proxyable" 
	 * da classe Properties. A saber, o metodo em questao e' o java.util.Hashtable.cloneHashtable()
	 */
//	private EntityManagerFactory emf;
//	
//	@Inject	
//	@Configuration
//	private static Properties properties;
	
	private EntityManagerFactory emf;
	
	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		System.out.println("Injetou Dependencia alura-lib - EntityManager");
		return emf.createEntityManager();
	}
	
	public void close(@Disposes EntityManager em) {
		if (em.isOpen()) {
			em.close();
		}
	}

	// Essa anotacao faz com que o CDI execute o metodo abaixo antes de destruir
	// este objeto em memoria
	@PreDestroy
	public void preDestroy() {
		if (emf.isOpen()) {
			emf.close();
		}
	}
	
	/**
	 * O codigo comentado imediatamente abaixo e' o que o instrutor implementou na ultima aula do curso
	 * Nao deu certo. Tomcat nao subia o CDI/Weld devido a um problema de um metodo "not proxyable" da 
	 * classe Properties. A saber, o metodo em questao e' o java.util.Hashtable.cloneHashtable()
	 */	
//	@PostConstruct
//	public void loadEMF(){
//		emf = Persistence.createEntityManagerFactory(properties.getProperty("alura.lib.persistenceUnit"));
//	}
	
	
	/**
	 * Metodo que le a unidade de persistencia de um arquivo chamado
	 * "alura-lib.properties", do valor da chave "alura.lib.persistenceUnit" 
	 * 
	 * Nao faz injecao de dependencia
	 * 
	 */
	@PostConstruct
	public void loadEMF() {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/alura-lib.properties")) {
			Properties properties = new Properties();
			
			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file 'alura-lib.properties' not found in the classpath");
			}
			
			emf = Persistence.createEntityManagerFactory(properties.getProperty("alura.lib.persistenceUnit"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
}
