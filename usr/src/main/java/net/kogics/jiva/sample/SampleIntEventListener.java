package net.kogics.jiva.sample;

import net.kogics.jiva.evolution.JGaEvent;
import net.kogics.jiva.population.JChromosome;
import net.kogics.jiva.population.JGene;
import net.kogics.jiva.population.JPopulation;
import net.kogics.util.listener.EventListener;

public class SampleIntEventListener implements EventListener<JGaEvent<Integer>> {

	public void eventFired(JGaEvent<Integer> event) {
		System.out.println("Event Fired. Pop: ");
		JPopulation<Integer> pop = event.population();
		System.out.print("Population(");
		for (JChromosome<Integer> chromosome : pop) {
			System.out.print("Chromosome(");
			for (JGene<Integer> gene : chromosome) {
				System.out.print(gene.allele().toString());
				System.out.print(",");
			}
			System.out.print(")");
			System.out.print(", ");
		}
		System.out.println(")");
	}
}
