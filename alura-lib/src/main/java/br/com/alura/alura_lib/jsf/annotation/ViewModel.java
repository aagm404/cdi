package br.com.alura.alura_lib.jsf.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Stereotype;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/*
 * Anotacao criada para ser um Stereotype (esteriotipo) de @Named e @ViewScoped
 * 
 * @ViewModel = @Named + @ViewScoped
 * 
 */

@Stereotype
@Named
@ViewScoped
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
public @interface ViewModel {

}
