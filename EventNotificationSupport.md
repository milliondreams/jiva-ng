Event Listeners receive JGaEvents:
```
package net.xofar.jiva.evolution;

import net.xofar.jiva.population.JPopulation;

public interface JGaEvent<A> {
	public EventType etype();
	public JPopulation<A> population();
}
```

So the first thing you need to do is to create an Event Listener for your type of JGaEvents.
> JGaEvents provide you access to the population at the start of every generation. A population is an `iterable` for JChromosomes, and JChromosomes are `iterables` for JGenes, so a population can be cracked open very easily as demonstrated in the sample listener below:
```
package net.xofar.jiva.sample;

import net.xofar.jiva.evolution.JGaEvent;
import net.xofar.jiva.population.JChromosome;
import net.xofar.jiva.population.JGene;
import net.xofar.jiva.population.JPopulation;
import net.xofar.util.listener.EventListener;

public class SampleBoolEventListener implements EventListener<JGaEvent<Boolean>>{ 

	public void eventFired(JGaEvent<Boolean> event) {
		System.out.println("Event Fired: " + event.etype() + "; Pop: ");
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
	}
}
```
You then need to register your Listener with a GA Evolver to start receiving JGaEvents
```
    // In Problem Definition file
    val gae = new GaEvolver[jbool](selector, mutOp, crossOp, evaluator, replacer)
    gae.addListener(new SampleBoolEventListener)
```