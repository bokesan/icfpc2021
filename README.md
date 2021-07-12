# ICFP Contest 2021 "Rotten Lambdas" Entry

We wrote two interactive tools to manipulate figures.
These allow basic manipulation of figures, like moving
the whole figure or selected vertices.

We also wrote some variations of automated solvers. Some of these
worked well on smaller problems, but we did not manage to come up
with a way to solve the "ball of yarn" problems like 105 und 106.
The basic approach is randomly assigning vertices while keeping
the length constraints satisfied. 

We also combined automatic and manual solving by hand-optimizing
some of the solutions of out autoamtic solvers. We did not keep
track of details, but we think aboutn half of our solutions where
generated by solver and the other half manually.

Languages used were Java for the solvers and one interactive tool,
and C# for the second interactive tool.

## Building and running

Change into the `java` directory and use `./gradlew build` to
build the solution. To run the interactive tool, use
`./run <problem-file>`. To run the automatic solvers,
use `./run -solve files...`.
