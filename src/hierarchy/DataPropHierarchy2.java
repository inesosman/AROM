package hierarchy;

import java.io.File;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class DataPropHierarchy2 {

	static int j = 0;

	private static void print(Set<String> list, Node<OWLDataProperty> parent, OWLReasoner reasoner, int depth, OWLOntology ont) {
		// We don't want to print out the bottom node (containing owl:Nothing
		// and unsatisfiable classes) because this would appear as a leaf node everywhere
		if (parent.isBottomNode()) {
			return;
		}

		// Now print the node (containing the child classes)

		for (Iterator<OWLDataProperty> iterator = parent.getEntities().iterator(); iterator.hasNext(); j++) {
			OWLDataProperty prop = iterator.next();
			// DefaultPrefixManager prefixManager = new DefaultPrefixManager("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
			// User a prefix manager to provide a slightly nicer shorter name
			// String shortForm = prefixManager.getShortForm(cls);
			// System.out.println(shortForm);

			for (int i = 0; i < depth * 4; i++) {
				System.out.print(" ");
			}

			System.out.println("Level N° " + depth + "  :  " + prop.getIRI().getFragment() + " (" + j + ")");
			list.add(String.valueOf(depth));

		}

		for (Node<OWLDataProperty> child : reasoner.getSubDataProperties(parent.getRepresentativeElement(),
				true)) {
			assert child != null;

			print(list, child, reasoner, depth + 1, ont);
		}

	}



	public static void main(String[] args) throws OWLOntologyCreationException {

		System.setProperty("jdk.xml.entityExpansionLimit", "0");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("Results/MergedOntology_WithRefact.owl");
		OWLOntology ont = manager.loadOntologyFromOntologyDocument(file);


		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // with the default reasoner
		//OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // with HermiT reasoner
        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);
		
		
		OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		//OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
		reasoner.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);

		// reasoner.precomputeInferences();

		System.out.println("\nThe ontology's data properties hierarchy :\n");

		// let's print out the class hierarchy.
		Node<OWLDataProperty> topPropNode = reasoner.getTopDataPropertyNode();
		Set<String> levelsList = new HashSet<String>();		
		print(levelsList, topPropNode, reasoner, 0, ont);
		System.out.println("\n --> Number of levels : "+ ((levelsList.size())-1));	

		reasoner.dispose();
		
		System.clearProperty("jdk.xml.entityExpansionLimit");

	}

}
