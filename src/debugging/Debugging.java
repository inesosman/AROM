package debugging;

import java.io.File;

import java.util.Set;
//import java.util.logging.LogManager;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.debugging.BlackBoxOWLDebugger;
import org.semanticweb.owlapi.debugging.OWLDebugger;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;


public class Debugging {

	public static void main(String[] args) throws OWLException{

		//System.setProperty("jdk.xml.entityExpansionLimit", "0");


		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("Results/MergedOntology_WithRefact.owl");
		OWLOntology ont = manager.loadOntologyFromOntologyDocument(file);
	
		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); // for the default reasoner
		//OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); // for HermiT reasoner
        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory(); // with ELK reasoner
		
		LogManager.getLogger("org.semanticweb.elk").setLevel(Level.OFF); // Level.ERROR
		//LogManager.getLogger("org.semanticweb.elk.reasoner.indexing").setLevel(Level.ERROR);
		//LogManager.getLogManager().reset();

		
		OWLReasoner reasoner = reasonerFactory.createReasoner(ont);

		boolean consistent = reasoner.isConsistent();
		System.out.println("\nOntology Consistency : " + consistent);


		OWLDebugger debugger = new BlackBoxOWLDebugger(manager, ont, reasonerFactory);

		//Now print out any unsatisfiable classes*/
		System.out.println("\nUnsatisfiable classes : ");
		int i=0;
		for (OWLClass cls : ont.getClassesInSignature()) {
			if (!reasoner.isSatisfiable(cls)) {
				i++;

				/*
				 * Find the set of support for the ontology incoherence. This will
				 * return us a collection of axioms causing the unsatisfiability of classes
				 */

				Set<OWLAxiom> sos = debugger.getSOSForIncosistentClass(cls);
				/* Print the axioms. */

				System.out.println("\n"+i+"/. "+ cls.getIRI() +": \n");

				System.out.println(" --> These are the axioms that made this class unsatisfiable :\n");
				for (OWLAxiom axiom : sos) {
					System.out.println("  " + axiom);
				}

				System.out.println("");
			}	
		}


		System.out.println("\nOntology consistency : " + consistent);
		reasoner.dispose();

		//System.clearProperty("jdk.xml.entityExpansionLimit");
	}

}
