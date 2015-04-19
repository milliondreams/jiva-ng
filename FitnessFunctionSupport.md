Your Java code needs to implement this interface:

```
package net.xofar.jiva.fitness;

import net.xofar.jiva.population.JChromosome;

public interface JFitnessFunction<A> {
	public double evaluate(JChromosome<A> chr);
}
```

A Chromosome is an `iterable` for JGenes, so it can be cracked open like this:

```
public double evaluate(JChromosome<Boolean> chr) {
	for (JGene<Boolean> gene : chr) {
                Boolean geneVal = gene.allele();
		// process gene allele
	}
}	
```