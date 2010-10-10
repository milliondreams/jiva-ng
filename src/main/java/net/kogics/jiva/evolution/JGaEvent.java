package net.kogics.jiva.evolution;

import net.kogics.jiva.population.JPopulation;

public interface JGaEvent<A> {
	public EventType etype();
	public JPopulation<A> population();
}
