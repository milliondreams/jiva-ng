package net.kogics.jiva.fitness;

import net.kogics.jiva.population.JChromosome;

public interface JFitnessFunction<A> {
	public double evaluate(JChromosome<A> chr);
}
