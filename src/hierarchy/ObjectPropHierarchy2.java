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
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class ObjectPropHierarchy2 {

	static int j = 0;

	private static void print(Set<String> list, Node<OWLObjectPropertyExpression> parent, OWLReasoner reasoner,
			int depth, HashSet<Node<OWLObjectPropertyExpression>> visited) {
		// We don't want to print out the bottom node (containing owl:Nothing
		// and unsatisfiable classes) because this would appear as a leaf node everywhere
		if (parent.isBottomNode()) {
			return;
		}

		if (!visited.contains(parent) ) {
			//visited.add(parent);
			
			// Now print the node (containing the child properties)
			for (Iterator<OWLObjectPropertyExpression> iterator = parent.getEntities().iterator(); iterator.hasNext(); j++) {
				OWLObjectPropertyExpression prop = iterator.next();
				// DefaultPrefixManager PrefixManager = new DefaultPrefixManager("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
				// User a prefix manager to provide a slightly nicer shorter name
				// String shortForm = PrefixManager.getShortForm(cls);
				// System.out.println(shortForm);

				for (int i = 0; i < depth * 4; i++) {
					System.out.print(" ");
				}

				System.out.println("N° " + depth + "  :  " + prop.getNamedProperty().getIRI().getFragment() + " (" + j + ")");

				list.add(String.valueOf(depth));

			}

			for (Node<OWLObjectPropertyExpression> child : reasoner
					.getSubObjectProperties(parent.getRepresentativeElement(), true)) {
				assert child != null;

				print(list, child, reasoner, depth + 1, visited);
			}
		}
	}




	public static void main(String[] args) throws OWLOntologyCreationException {

		System.setProperty("jdk.xml.entityExpansionLimit", "0");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("Results/MergedOntology_WithRefact.owl");
		OWLOntology ont = manager.loadOntologyFromOntologyDocument(file);

		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // with the default reasoner
		//OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // with the HermiT reasoner
        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);
		
		
		OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		//OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ont);
		reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);

		System.out.println("The ontology's data properties hierarchy :\n");

		// let's print out the class hierarchy.
		Node<OWLObjectPropertyExpression> topPropNode = reasoner.getTopObjectPropertyNode();

		Set<String> levelsList = new HashSet<String>();		
		print(levelsList, topPropNode, reasoner, 0, new HashSet<Node<OWLObjectPropertyExpression>>());
		System.out.println("\n --> Number Of Levels : "+ ((levelsList.size())-1));

		reasoner.dispose();

		System.clearProperty("jdk.xml.entityExpansionLimit");
	}


}
