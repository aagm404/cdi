package br.com.alura.alura_lib.jsf.phaselistener.annotation;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.PhaseId;

/*
 * No momento em que criamos este projeto, o Eclipse ficava dando mensagem de "warning"
 * ao implementar a anotacao Phase como feito abaixo.
 * 
 * Mas deixamos assim mesmo, porque funciona
 */
public class PhaseLiteral extends AnnotationLiteral<Phase> implements Phase {

	private static final long serialVersionUID = 1L;
	
	private Phases phases;
	
	public PhaseLiteral(PhaseId phaseId) {
		this.phases = Phase.Phases.valueOf(phaseId.getName());
		
	}
	
	@Override
	public Phases value() {
		return phases;
	}

}
