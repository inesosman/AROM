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
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class DataPropHierarchy {
	static int k = 0;

	public static void printHierarchy(Set<String> list, OWLOntology ont, OWLReasoner reasoner, OWLDataProperty dataProp, int level,
			Set<OWLDataProperty> visited) throws OWLException {

		if (!visited.contains(dataProp)) {
			//visited.add(dataProp);

			for (int i = 0; i < level * 4; i++) {
				System.out.print(" ");
			}

			System.out.println(level + ".  " + dataProp.getIRI().getFragment() + "  (" + k + ")");
			k++;

			list.add(String.valueOf(level));

			/* Find the children and recurse */
			NodeSet<OWLDataProperty> subDataProp = reasoner.getSubDataProperties(dataProp, true);

			for (OWLDataProperty child : subDataProp.getFlattened()) {
			// if(!child.isAnonymous() && !child.isOWLBottomDataProperty()){
				if (!child.isOWLBottomDataProperty()) {
					// OWLDataProperty child = child.asOWLDataProperty();
					printHierarchy(list, ont, reasoner, child, level + 1, visited);
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
		System.out.println("Document IRI : " + file);
		System.out.println("Ontology : " + ont.getOntologyID().getOntologyIRI());
		System.out.println("Format : " + manager.getOntologyFormat(ont));

		OWLDataFactory datafactory = manager.getOWLDataFactory();

		System.out.println("\nThe ontology's data properties hierarchy :\n");
		// Get TopDataProperty
		OWLDataProperty topDataProp = datafactory.getOWLTopDataProperty();

		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // with the default reasoner
		//OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // with HermiT reasoner
        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);
		
		
		//OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
		reasoner.precomputeInferences(InferenceType.DATA_PROPERTY_HIERARCHY);

		Set<String> levelsList = new HashSet<String>();
		// Print the hierarchy below TopDataProperty
		printHierarchy(levelsList, ont, reasoner, topDataProp, 0, new HashSet<OWLDataProperty>());

		System.out.println("\n --> Number of levels : "+ ((levelsList.size())-1));
		reasoner.dispose();

		System.clearProperty("jdk.xml.entityExpansionLimit");
	}
}
