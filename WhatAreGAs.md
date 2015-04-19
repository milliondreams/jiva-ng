#### Introduction ####
Genetic Algorithms belong to a class of problem-solving techniques called Evolutionary Algorithms. Evolutionary computation is a relatively new area of computer science that closely resembles trial and error based search. It is based on a simple idea: a given environment is filled with a population of individuals who strive for survival. The fitness of these individuals, as determined by the environment, relates to how well they succeed in achieving this goal.

In terms of trial and error based search, you have a set of candidate solutions, and their quality is used to determine how likely they are to contribute to future candidate solutions.

#### How Evolutionary Algorithms work ####
Given a function to be maximized, you randomly create a set of solutions. You then evaluate the quality of these solutions; some of the better ones are chosen to seed the next generation of solutions by applying recombination and mutation to them. These ‘offsprings’ compete with the initial solutions for a place in the next generation. This process is iterated till a ‘good enough’ solution is found or a given computational limit is reached.

Two fundamental types of forces are at work in evolutionary systems:
  * Exploratory forces: are brought into play by Variation operators, and create the population diversity required to search across the search space.
  * Exploitative forces: are brought into play by the selection of the fittest, and drive the search towards high quality ‘peaks’.

The basic structure of an evolutionary algorithm can be described with the following pseudo-code:
```
Init population with random candidate solutions
Evaluate each candidate
Repeat until (Termination Condition becomes true) do
  Select parents
  Mutate the resulting offspring
  Recombine pairs of parents
  Evaluate new candidates
  Select candidates for the next generation
od
```