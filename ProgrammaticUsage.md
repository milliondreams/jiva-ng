To use Jiva programmatically, you need to use the Problem Builder: `net.xofar.jiva.gaprob.ProbBuilder`. The Builder can be used in two modes:

  * Preferred method for use from Scala code:
```
val problem = ProbBuilder.buildPermutedProb {buildr =>
    buildr name "N Queens"
    buildr popSize 400
    buildr chrSize numq 
    buildr numEvolutions 200
    buildr fitnessFunction new YourFitnessFunction
    buildr defaultEvolver (0.5, 0.9)
}
problem.run() // returns the population of the last generation
```

  * Method for use from Java code (currently available on svn trunk only):
```
IntegerProbBuilder buildr = JProbBuilder.integerProbBuilder;
buildr.name("N Queens");
buildr.popSize(400);
buildr.chrSize(numq);
buildr.numEvolutions(200);
buildr.fitnessFunction(new YourFitnessFunction());
buildr.defaultEvolver(0.5, 0.9);
GaProblem problem = buildr.build();
problem.run(); // returns the population of the last generation
```