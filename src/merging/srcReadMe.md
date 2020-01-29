* You can expand the JVM space before running MergingWithRefactoring.java or MergingWithoutRefactoring.java by putting these JVM arguments :

  ** -Xms1024m -Xmx2048m

or if you need more (especially if your input ontologies are large) :

  ** -Xms2048m -Xmx4096m

* If you use HermiT reasoner in the OntologyConsistencyAndCoherence.java, you need to put the following JVM arguments before running that class, because HermiT needs more time to compute all the ontology inferences, unlike ELK reasoner :

  ** -Xms2048m -Xmx4096m -Xss208m
