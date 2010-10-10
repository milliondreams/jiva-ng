package net.kogics.jiva.population;


public interface JPopulation<A> extends Iterable<JChromosome<A>> {
	public JChromosome<A> fittestChromosome();
}
