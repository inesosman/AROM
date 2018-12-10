package hierarchy;

import java.io.File;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class ClassHierarchy {
	static int k=0;

	public static void printHierarchy(Set<String> list, OWLOntology ont,OWLClass clas) throws OWLException {
		
		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // with the default reasoner
		//OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // with HermiT reasoner
		OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);

		OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
		//OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		
		printHierarchy(list, ont, reasoner, clas, 0, new HashSet<OWLClass>());

		reasoner.dispose();
	}


	public static void printHierarchy(Set<String> list, OWLOntology ont, OWLReasoner reasoner, OWLClass clas, int level, Set<OWLClass> visited)
			throws OWLException {
		//Only print satisfiable classes to skip Nothing
		/*
		 * Only print satisfiable classes -- otherwise we end up with bottom
		 * everywhere
		 */
		
		//if (!visited.contains(clas)) {
		if (!visited.contains(clas) && reasoner.isSatisfiable(clas)) {
			//visited.add(clas);

			for (int i = 0; i < level * 4; i++) {
				System.out.print(" ");
			}

			//System.out.println(level + "  " + labelFor(clas, reasoner.getRootOntology())+ "("+k+")");
			System.out.println(level + ".  "+ clas.getIRI() + "  ("+k+")");
			k++;

			list.add(String.valueOf(level));

			/* Find the children and recurse */
			NodeSet<OWLClass> subClasses = reasoner.getSubClasses(clas, true);
			for (OWLClass child : subClasses.getFlattened())
			{	//if(!child.isOWLNothing())
				printHierarchy(list, ont, reasoner, child, level + 1, visited);

			}
		}
	}


	public static void main(String[] args) throws OWLException {

		System.setProperty("jdk.xml.entityExpansionLimit", "0");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("Results/MergedOntology_WithRefact.owl");
		OWLOntology ont = manager.loadOntologyFromOntologyDocument(file);

		System.out.println("Ontology Loaded...");
		System.out.println("Document IRI: " + file);
		System.out.println("Ontology : " + ont.getOntologyID().getOntologyIRI());
		System.out.println("Format : " + manager.getOntologyFormat(ont));


		OWLDataFactory datafactory = manager.getOWLDataFactory();

		System.out.println("\nThe ontology's class hierarchy :\n");       
		// Get Thing class
		OWLClass thing = datafactory.getOWLThing();
		// Print the hierarchy below thing
		Set<String> levelsList = new HashSet<String>();
		printHierarchy(levelsList, ont, thing);	
		System.out.println("\n --> Number of levels : "+ ((levelsList.size())-1));

		System.clearProperty("jdk.xml.entityExpansionLimit");
	}
}

