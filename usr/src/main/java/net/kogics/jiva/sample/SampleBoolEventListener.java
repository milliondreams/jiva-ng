package net.kogics.jiva.sample;

import net.kogics.jiva.evolution.JGaEvent;
import net.kogics.jiva.population.JChromosome;
import net.kogics.jiva.population.JGene;
import net.kogics.jiva.population.JPopulation;
import net.kogics.util.listener.EventListener;

public class SampleBoolEventListener implements EventListener<JGaEvent<Boolean>>{ 

	public void eventFired(JGaEvent<Boolean> event) {
		System.out.println("Event Fired: " + event.etype());
		JPopulation<Boolean> pop = event.population();
		System.out.print("Population(");
		for (JChromosome<Boolean> chromosome : pop) {
			System.out.print("Chromosome(");
			for (JGene<Boolean> gene : chromosome) {
				System.out.print(gene.allele().toString());
				System.out.print(",");
			}
			System.out.print(")");
			System.out.print(", ");
		}
		System.out.println(")");
		System.out.println("Fittest Chromosome in Population: " + pop.fittestChromosome());
		System.out.println("Best fitness: " + pop.fittestChromosome().fitnessVal());
	}
}
