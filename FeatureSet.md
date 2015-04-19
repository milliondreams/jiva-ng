Jiva includes commonly used variants of Genetic Algorithms like:
  * APGA (Adaptive Population GA)
  * Simple GA
  * Elitist GA

These algorithm implementations can be used out-of-the-box to solve real problems.

Jiva also provides an extensible framework for the development and investigation of new variants of Genetic Algorithms. A Genetic algorithms is made up of a number of steps: parent selection, mutation, crossover, evaluation, and survivor selection. Jiva allows customization/redefinition of any of these steps in a plug-and-play manner for the definition of custom versions of genetic algorithms.

The following is a catalog of currently available components:

#### Selection ####
  * Tournament Selection
  * Roulette Wheel Selection

#### Mutation ####
  * Random Reset Mutation
  * Inversion Mutation (for permutation based representations)

#### Crossover ####
  * Single Point Crossover
  * Chunked Crossover
  * Partially Mapped Crossover (for permutation based representations)

#### Population Evaluation ####
  * Simple GA Evaluation
  * Adaptive Population GA Evaluation

#### Replacement ####
  * Generational Replacement
  * Adaptive Population GA Replacement
  * Elitist Replacement (_coming soon_)