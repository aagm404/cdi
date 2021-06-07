package br.com.alura.alura_lib.jsf.phaselistener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class PhaseListenerGenerico implements PhaseListener {

	private static final long serialVersionUID = 1L;

	// O objeto abaixo nao pode ser injetado pelo CDI,
	// pois e' um objeto do JSF
	private PhaseListenerObserver observer = new PhaseListenerObserver();
	
	@Override
	public void afterPhase(PhaseEvent event) {
		observer
		.after()
		.fire(event);
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		observer
		.before()
		.fire(event);
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
