You can expand the JVM space before running MergingWithRefactoring.java or MergingWithoutRefactoring.java by putting these JVM arguments : -Xms1024m -Xmx2048m
or -Xms2048m -Xmx4096m if you need more.

If you use HermiTreasoner in the OntologyConsistencyAndCoherence.java, you need to put the following JVM arguments :

-Xms2048m -Xmx4096m -Xss208m because HermiT needs more time to compute all the ontology inferences, unlike ELK reasoner.
