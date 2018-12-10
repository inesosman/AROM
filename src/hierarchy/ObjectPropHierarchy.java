package hierarchy;

import java.io.File;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class ObjectPropHierarchy {
	static int k = 0;

	public static void printHierarchy(Set<String> list, OWLOntology ont, OWLReasoner reasoner, OWLObjectProperty objProp, int level,
			Set<OWLObjectProperty> visited) throws OWLException {

		if (!visited.contains(objProp)) {
			//visited.add(objProp);

			for (int i = 0; i < level * 4; i++) {
				System.out.print(" ");
			}

			System.out.println(level + ".  " + objProp.getIRI().getFragment() + "  (" + k + ")");
			k++;

			list.add(String.valueOf(level));

			/* Find the children and recurse */
			NodeSet<OWLObjectPropertyExpression> subObjProp = reasoner.getSubObjectProperties(objProp, true);

			for (OWLObjectPropertyExpression child : subObjProp.getFlattened()) {
				if (!child.isAnonymous() && !child.isOWLBottomObjectProperty()) {
					OWLObjectProperty son = child.asOWLObjectProperty();
					printHierarchy(list, ont, reasoner, son, level + 1, visited);
				}
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

		System.out.println("\nThe ontology's data properties hierarchy :\n");

		// Get TopObjectProperty
		OWLObjectProperty topObjProp = datafactory.getOWLTopObjectProperty();

		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // with the default reasoner
		// OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // with the HermiT reasoner
        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);
		
		
		//OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
		reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);

		Set<String> levelsList = new HashSet<String>();
		// Print the hierarchy below TopObjectProperty
		printHierarchy(levelsList, ont, reasoner, topObjProp, 0, new HashSet<OWLObjectProperty>());

		System.out.println("\n --> Levels Number : "+ ((levelsList.size())-1));
		reasoner.dispose();	

		System.clearProperty("jdk.xml.entityExpansionLimit");
	}
}
