package net.kogics.jiva.population;

public interface JChromosome<A> extends Iterable<JGene<A>> {
	public double fitnessVal();
	public int size();
}
