package merging;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;


public class OntologyConsistencyAndCoherence {

	public static void verify(OWLOntology ont) throws OWLOntologyCreationException{

		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // with the default reasoner
		//OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // with HermiT reasoner
        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);
		
		
		OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		
		
		boolean consistent = reasoner.isConsistent();
		System.out.println("\n\nOntology consistency : " + consistent);


		/** Now print out any unsatisfiable classes*/
		//System.out.println("\nUnsatisfiable classes : ");
		int i=0;
		for (OWLClass cls : ont.getClassesInSignature()) {
			if (!reasoner.isSatisfiable(cls)) {
				i++;
				//System.out.println(i+"/. "+ cls.getIRI().getFragment());
			}
		}


		// Another method for extracting unsatisfiable classes
		/**
		System.out.println("\nUnsatisfiable classes : ");
		int j=0;
		for(OWLClass cls : reasoner.getUnsatisfiableClasses()){
			if(!insatis.isOWLNothing()){
				j++;
			System.out.println(j+ "/. "+ cls.getIRI().getFragment());
			}
		}
		 */


		// Another method for extracting unsatisfiable classes
		/**
		System.out.println("\nUnsatisfiable classes : ");
		Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
		// This node contains owl:Nothing and all the classes that are
		// equivalent to owl:Nothing - i.e. the unsatisfiable classes. We just
		// want to print out the unsatisfiable classes excluding owl:Nothing,
		// and we can used a convenience method on the node to get these
        int k=0;
		for (OWLClass cls : bottomNode.getEntitiesMinusBottom()) {
			k++;
			System.out.println(k+"/. "+ cls.getIRI().getFragment());
		}
		 */


		//System.out.println("\nOntology consistency : " + consistent);
		System.out.println("Number of unsatisfiable classes : " + i);		
		
		reasoner.dispose();
	}


	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException{


		System.setProperty("jdk.xml.entityExpansionLimit", "0");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("Results/MergedOntology_WithRefact.owl");
		OWLOntology ont = manager.loadOntologyFromOntologyDocument(file);

		verify(ont);

		System.clearProperty("jdk.xml.entityExpansionLimit");	

	}

}
