The first thing you need to do to get Jiva to run your GA Problem is to:
  * Decide on a Representation for the Chromosomes that specify solutions to your problem. This involves choosing a Chromosome Type and a Chromosome Size
  * Determine a Mapping from Genotype (GA) to Phenotype (problem-domain) space, and then write a Fitness Function (in Scala or Java) that assigns fitness values to your Chromosomes based on this Mapping

Once your Fitness function is written, you are ready to:
  * Build your problem with Jiva's help
  * Get Jiva to run your problem

To build your problem, you need to provide Jiva at least the following information:
  * The _type_ of your problem (Boolean, Integer, Or Permuted)
  * Population Size
  * Chromosome Size
  * Max Chromosome value (for Integer problems)
  * Number of Evolutions/Generations
  * Mutation Probability
  * Crossover Probability
  * Fitness Function

Let us dig into this some more with the help of the [Knapsack sample problem](KnapsackProblem.md). Here's the GA definition for this problem:
```
class Knapsack extends jiva.ProbDefiner {
  def gaProblem = buildBooleanProb {buildr =>
    buildr name "Knapsack"
    buildr popSize 40
    buildr chrSize 13 
    buildr numEvolutions 50
    buildr fitnessFunction new KnapsackFf
    buildr defaultEvolver (0.1, 0.9)
  } 
}
```
Your problem definition class needs to extend ProbDefiner, and it needs to have a method called gaProblem.
Depending on the type of your problem (as determined by the structure of your Chromosome), you can call one of the following three builder methods in the definition of gaProblem:
```
buildIntegerProb
buildPermutedProb
buildBooleanProb
```
These builder methods supply you with a problem builder that you can use to incrementally define your GA problem.
The setting of the population size, chromosome size, Number of evolutions, and the fitness function - is pretty straightforward (as seen in the Knapsack sample).
The call to `defaultEvolver` needs a little bit of explanation:

Inside each GA problem is an Evolver that holds together the core moving parts of the GA algorithm that will be used to solve the problem. To dig deeper inside an Evolver, it is instructive to look at code that constructs an Evolver from scratch (this code is borrowed from the [SpearsScape sample](SpearsScapeProblem.md):
```
    val popDelta = Math.ceil(100 / 4.0).toInt
    val selector = new TournamentSelector[jbool](popDelta, buildr().rgen, 4)
    val replacer = new ApgaReplacer[jbool]
    val evaluator = new ApgaFitnessEvaluator[jbool](buildr().ff)
    val mutOp = new RandomResetMutationOp[jbool](0.01, buildr().rgen)
    val crossOp = new SinglePointCrossoverOp[jbool](0.9, buildr().rgen, new ShufflerImpl)
    buildr evolver (selector, replacer, evaluator, mutOp, crossOp)
```
So - an evolver is made up of a Selector, a Replacer, a Fitness evaluator, a Mutation operation, and a Crossover operation. You can pick and choose the precise type of each of these building blocks when you construct your Evolver, as seen in the code sample above. Or you can ask the builder to create a default Evolver for you, based on your problem type (as seen in the Knapsack sample).

Once your GA problem has been built, you can use Jive to run it either programatically or from the command line.

  * [Building and Running your GA problems from the Command Line](CommandLineUsage.md)
  * [Building and Running your GA problems Programmatically](ProgrammaticUsage.md)