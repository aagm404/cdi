# Curso CDI
  
## Informações do curso  
  
**Curso:** CDI 1.2: Use uma das principais especificações do JavaEE  
**Plataforma:** Alura  
**Instrutor:** Fernando Furtado  
  
## Projetos  
  
**Projeto Principal:** livraria-cdi  
**Projeto utilizado como dependência:** alura-lib  
  
## Ambiente de desenvolvimento  
  
**JDK:** Oracle jdk 11.0.7  
**Servlet Container:** Apache Tomcat 9.0.41  
**Banco de Dados:** MySQL 8.0.24  
**Primefaces:** 8.0  
  
## Informações relevantes  
  
**01 -** O projeto principal do curso é o `livraria-cdi`. O projeto auxiliar, `alura-lib`, foi desenvolvido dentro do projeto principal, porém foi separado para fins didáticos. Assim, toda aplicação que queira utilizar um contexto de injeção de depedências já implementado, pode adicionar o projeto `alura-lib` às suas dependências, e criar um arquivo de configuração, chamado `alura-lib.properties`, dentro do diretório `src/main/resources`. Dentro deste arquivo, setar uma unidade de persistência como valor da chave `alura.lib.persistenceUnit`  
  
**02 -** Para executar a aplicação principal, basta criar um database de nome `alura_lib`, subir o Tomcat, e executar, como uma aplicação Java, a classe `br.com.alura.livraria.dao.PopulaBanco`. Dentro desta classe, inserimos, de forma hardcoded, dois usuários para login na aplicação. Uilize qualquer um dois dois usuários abaixo para efetuar o login:  
```txt  
email: user1@gmail.com
senha: 123

ou
email: usuario@exemplo.com
senha: abc
```  
    
**03 -** Da forma como foi modelada a base de dados, o botão `x [remover]` da aplicação não funciona para o livros e autores que forma inseridos de forma hardcoded, pela classe `br.com.alura.livraria.dao.PopulaBanco`, por conta de restrição de chave estrangeira nas associações entre as tabelas de autor, livro e venda  
    
**04 -** O contexto raiz (context-root) da aplicação é `livraria-cdi`, e os recursos disponíveis são `login.xhtml`, `autor.xhtml`, `livro.xhtml`,  e `vendas.xhtml`. Para acessar os recusos, é necessário estar logado na aplicação  
  
## Configurando o WELD no Tomcat  
  
**CDI 2.0 e WELD 3.1.6.Final, JSF 2.3.9, Servlet 4.0 e Apache Tomcat 9.0.41**  
  
A implementação do CDI utilizada neste projeto necessita de uma configuração adicional para rodar em Servlet Containers. No caso, utilizamos o Apache Tomcat 9.0.41 como Servlet Container. Abaixo segue o passo-a-passo para configurar corretamente o WELD  
  
01) Criar um arquivo com o nome de `context.xml` dentro do diretório `src/main/webapp/META-INF/`, com o seguinte conteúdo:  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context>
  <Resource name="BeanManager" auth="Container"
    type="javax.enterprise.inject.spi.BeanManager"
    factory="org.jboss.weld.resources.ManagerObjectFactory" />
</Context>
```  
  
02) Criar um arquivo com o nome de `MANIFEST.MF` dentro do diretório `src/main/webapp/META-INF/`, com o seguinte conteúdo:  
```txt
Manifest-Version: 1.0
Class-Path:
```  
  
03) Criar um arquivo com o nome de `beans.xml` dentro do diretório `src/main/webapp/WEB-INF/`, com o seguinte conteúdo:  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
       xmlns="http://xmlns.jcp.org/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                           http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
       version="2.0"
       bean-discovery-mode="all">

</beans>
```  
Veja a propriedade `bean-discovery-mode="all"`. Ela indica que todos os beans serão gerenciados pelo CDI. Além de "all", temos mais duas opções. São elas: "annotated" e "none". Para o CDI 2.0 rodar com o servlet 4.0, a opção "none" não convém. Se quisermos utilizar a opção "annotated", teremos que colocar a anotação `@FacesConfig` descrita no item 5) abaixo em TODOS os beans que quisermos que o CDI gerencie. Quando não definimos esta propriedade, o valor padrão é "annotated"  
  
04) Colocar o trecho de código abaixo ao final do arquivo `src/main/webapp/WEB-INF/web.xml` e dentro da tab `<web-app>`:  
```xml
<listener>
		<listener-class>org.jboss.weld.environment.servlet.Listener</listener-class>
</listener>

<resource-env-ref>
	  <resource-env-ref-name>BeanManager</resource-env-ref-name>
	  <resource-env-ref-type>
	    javax.enterprise.inject.spi.BeanManager
	  </resource-env-ref-type>
</resource-env-ref>
```  
05) Para ativar o CDI, criar uma classe vazia Java arbitrária, em qualquer lugar dentro de `src/main/java`, anotada com as anotações `@FacesConfig` e `ApplicationScoped` [Veja as referências 27, 28, 29, 30, 31 e 32 abaixo]:  
```java
@FacesConfig
@ApplicationScoped
public class YourApplicationConfig {
    // ...
}
```  
No caso desta aplicação, criamos uma classe chamada `Config`, no pacote `br.com.alura.config e colocamos estas anotações. Abaixo segue o código desta classe:  
```java
package br.com.alura.livraria.config;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;

@FacesConfig
@ApplicationScoped
public class Config {

}
```  

06) Quando trocamos as anotações `@ManagedBean` pela a anotação `@Named`, foi necessário definir um escopo para o CDI, para que a aplicação continuasse funcionando. Por padrão, um bean com a anotação `@Named` tem um escopo chamado `@Dependent`. É por isso que se trocarmos apenas `@ManagedBean` por `@Named` não conseguimos, por exemplo, logar na aplicação. A aplicação sobe normalmente, mas ao inserirmos o usuário, a aplicação não consegue buscar no banco de dados este usuário. Para resolver este problema, devemos utilizar, junto com a anotação `@Named`, uma anotação de escopo, como, por exemplo, `@RequestScoped`, `@ViewScoped`, `@ApplicationScoped`, `@SessionScoped` ou `@ConversationScoped`. Todos estes escopos mencionados são pré-definidos para o CDI, com exceção do escopo `@ViewScoped` que é definido pelo o JSF  
  
07) Quando utilizamos o CDI 2.0 com JSF 2.3, devemos nos atentar aos imports, pois algumas anotações mantiveram o mesmo nome, porém os imports da mesma mudaram. Abaixo temos algumas anotações importantes para este projeto. Para uma lista completa, consulte a referência 26 no parágrafo abaixo  

| Anotação | Import Antigo| Import Novo | Tecnologia |
| :----: | :----: | :----: | :----: |
| `@RequestScoped` | javax.faces.bean.RequestScoped | javax.enterprise.context.RequestScoped | CDI |
| `@SessionScoped` | javax.faces.bean.SessionScoped | javax.enterprise.context.SessionScoped | CDI |
| `@ViewScoped` | javax.faces.bean.ViewScoped | javax.faces.view.ViewScoped | JSF |  
  
## Bugs encontrados durante o desenvolvimento do projeto  
  
**01 -** O MOJARRA 2.3.9 (junto com o CDI 2.0) apresenta um problema para validar tipos primitivos (int, double, etc.). Ao validar um atributo de tipo primitivo de uma classe, a aplicação lança `NullPointerException` em `CdiUtils.createConverter`  
```txt
Caused by: java.lang.NullPointerException
	at com.sun.faces.cdi.CdiUtils.createConverter(CdiUtils.java:125) ~[javax.faces-2.3.4.jar:2.3.4]
```  
O segredo é utilizar as Wrapper Classes para este tipo de conteúdo  
Aparentemente, o bug é percebido à partir do MOJARRA 2.3.4 e implementou-se uma correção para o MOJARRA 2.3.10  
*Verifique as [referências](#referências) [44], [45], [46] e [47]*  
  
**02 -** O Primefaces 8.0 apresenta um problema em um atributo do componente `inputNumber`. Por padrão, todo campo implementado com este componente pode ter o seu valor alterado pelo usuário, com a roda do mouse  
O componente `inputNumber` tem um atributo chamado `modifyValueOnWheel` e seu valor default é `true`  
O bug encontrado é que, na versão 8.0 do Primefaces, não adianta setar este atributo como `false` (veja o exemplo abaixo):  
```xhtml  
<p:inputNumber id="foo" value="#{bean.method}" modifyValueOnWheel="false" />
```  
Mesmo com a declaração exemplificada acima, o usuário ainda podrá aumentar ou diminuir o valor deste `inputNumber` com a roda do mouse  
Este é um bug reportado pelo próprio Primefaces e que teve sua correção somente na versão 8.0.3 (versão paga)  
*Verifique a [referência](#referências) [48]*  
  
## Referências  
  
Links úteis para complementar o desenvolvimento da aplicação:  
  
01) [JavaEE 8 Specification APIs](https://javaee.github.io/javaee-spec/javadocs/)  
02) [JavaServer Faces Spec Page](https://javaee.github.io/javaserverfaces-spec/)  
03) [JavaServer Faces 2.3 Specification File - JSF 2.3 Specification (JSR 372)](https://download.oracle.com/otn-pub/jcp/jsf-2_3-final-eval-spec/JSF_2.3.pdf?AuthParam=1611452757_293bc2afcdcbb27110111036d45045ae)  
04) [JSF 2.3 API documentation](https://javaserverfaces.github.io/docs/2.3/javadocs/index.html)  
05) [JSF 2.3 VDL documentation](https://javaserverfaces.github.io/docs/2.3/vdldoc/index.html)  
06) [JSF 2.3 JS documentation](https://javaserverfaces.github.io/docs/2.3/jsdocs/index.html)  
07) [Getting Started with Web Applications-JavaEE8 Tutorial](https://javaee.github.io/tutorial/webapp.html#BNADR)  
08) [JavaServer Faces Technology-JavaEE7 Tutorial](https://docs.oracle.com/javaee/7/tutorial/jsf-intro.htm)  
09) [JSF Facelets Tag Library](https://javaee.github.io/glassfish/doc/5.0/vdldoc/)  
10) [What's new in JSF 2.3](https://javaserverfaces.github.io/whats-new-in-jsf23.html)  
11) [What's new in JSF 2.3? - ARJAN TIJMS Weblog](https://arjan-tijms.omnifaces.org/p/jsf-23.html)  
12) [Java EE Kickoff Application](https://github.com/javaeekickoff/java-ee-kickoff-app)  
13) [JCP: JSR372 - JavaServer Faces 2.3 Specification](https://jcp.org/aboutJava/communityprocess/final/jsr372/index.html)  
14) [JCP: JSR365 - Contexts and Dependency Injection for Java 2.0 Specification](https://jcp.org/aboutJava/communityprocess/final/jsr365/index.html)  
15) [JSF 2.2 View Declaration Language: Facelets Variant](https://docs.oracle.com/javaee/7/javaserver-faces-2-2/vdldocs-facelets/index.html)  
16) [JavaEE7 Tutorial-JSF 2.2 Configure](https://docs.oracle.com/javaee/7/tutorial/jsf-configure002.htm)  
17) [JSF 2.3 Schema](https://javaserverfaces.github.io/docs/2.3/javadocs/web-facesconfig.html)  
18) [Primefaces](https://www.primefaces.org/)  
19) [Baeldung JavaEE CDI](https://www.baeldung.com/java-ee-cdi)  
20) [cdi-spec.org](http://cdi-spec.org/)  
21) [Jboss CDI 2.0 specification](https://docs.jboss.org/cdi/spec/2.0/cdi-spec.html)  
22) [Weld CDI](http://weld.cdi-spec.org/)  
23) [Weld-Version / CDI-Version](http://weld.cdi-spec.org/documentation/#9)  
24) [Documentation Weld 3.1.6.Final - CDI Reference Implementation](https://docs.jboss.org/weld/reference/latest-3.1/en-US/html_single/)  
25) [Configuração em Servlet Containers (como Tomcat ou Jetty)](https://docs.jboss.org/weld/reference/latest-3.1/en-US/html_single/#weld-servlet)  
26) [Deprecated API](https://javaserverfaces.github.io/docs/2.3/managed-bean-javadocs/deprecated-list.html)  
27) [Configurando um projeto MAVEN JSF 2.3, CDI 2.0 e Serlvet 4.0 no Eclipse](http://fritzthecat-blog.blogspot.com/2019/08/jsf-23-maven-project-in-eclipse.html?m=1)  
28) [JSF 2.3 Mojarra - github](https://github.com/javaserverfaces/mojarra)  
29) [Activating CDI in JSF 2.3](http://hantsy.blogspot.com/2017/11/activating-cdi-in-jsf-23.html?m=1)  
30) [How to install CDI in Tomcat?](https://balusc.omnifaces.org/2013/10/how-to-install-cdi-in-tomcat.html?m=1)  
31) [Deploying a JSF 2.3 Application on Tomcat 9](http://alibassam.com/deploying-jsf-2-3-application-tomcat-9/)  
32) [CDI + JSF no Java EE7](https://emmanuelneri.com.br/2015/08/23/cdi-jsf-no-java-ee7/)  
33) [Github issue forum bug inf mojarra: JSF Facelet can not recognize CDI beans](https://github.com/javaserverfaces/mojarra/issues/4264)  
34) [Stackoverflow: The project does not work with servlet 4.0 and jsf 2.3](https://stackoverflow.com/questions/51468241/the-project-does-not-work-with-servlet-4-0-and-jsf-2-3)  
35) [Página no github com exemplos de JSF 2.3](https://github.com/AnghelLeonard/JSF-2.3)  
36) [Archetype para JSF 2.3](https://rieckpil.de/howto-bootstrap-a-jsf-2-3-maven-project-in-seconds/)  
37) [Dependências de versões diferentes do JSF ](https://javaserverfaces.github.io/download.html)  
38) [Tutorial para montar um projeto JSF 2.3](https://balusc.omnifaces.org/2020/04/jsf-23-tutorial-with-eclipse-maven.html?m=1)  
39) [JSF 2.3 released!](https://arjan-tijms.omnifaces.org/2017/03/jsf-23-released.html?m=1)  
40) [Oracle's implementation of the JSF 2.3 specification API](https://frontbackend.com/maven/artifact/javax.faces/javax.faces-api/2.3)  
41) [Configuração JSF 2.3 no Websphere - Contém uma informações importantes sobre o JSF 2.3 que não estão relacionadas ao WebSphere](https://www.ibm.com/support/knowledgecenter/pt-br/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_config_jsf23.html)  
42) [Problemas com JSF, Weld CDI e TomCat [RESOLVIDO]](https://www.guj.com.br/t/problemas-com-jsf-weld-cdi-e-tomcat-resolvido/201136/18)  
43) [Migrando para o JSF 2.3, Problemas com MyFaces Inicialização](https://pt.coredump.biz/questions/59383725/migrating-to-jsf-23-problems-with-myfaces-initialization)  
44) [Primitive types cause a NullPointerException in CdiUtils.createConverter](https://github.com/javaserverfaces/mojarra/issues/4367)  
45) [NPE when determining converter for primitive values](https://github.com/eclipse-ee4j/mojarra/issues/4500)  
46) [NPE on enabled primefaces.CLIENT_SIDE_VALIDATION](https://github.com/eclipse-ee4j/mojarra/issues/4634)  
47) [Multiple Autocomplete with a value of a list of strings can't generate Converter JSF?](https://stackoverflow.com/questions/57947555/multiple-autocomplete-with-a-value-of-a-list-of-strings-cant-generate-converter)  
48) [PrimeFaces 7.0.15 And 8.0.3 Released --> InputNumber: option to disable modifyValueOnWheel (8.0.3)](https://www.primefaces.org/primefaces-7-0-15-and-8-0-3-released/)  