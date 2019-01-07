package merging;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import fr.inrialpes.exmo.align.parser.AlignmentParser;
import uk.ac.manchester.cs.owl.owlapi.OWLSubPropertyChainAxiomImpl;

public class MergingWithoutRefactoring {

	public static void main(String[] args) throws OWLException, AlignmentException, IOException {


		System.setProperty("jdk.xml.entityExpansionLimit", "0");

		ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
		long chrono1 = bean.getCurrentThreadCpuTime();


		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory datafactory = manager.getOWLDataFactory();

		/**********************************************************************************************************************/

		ArrayList<String> ontologiesFiles = new ArrayList<String>();
		ArrayList<OWLOntology> ontologiesSet = new ArrayList<OWLOntology>();

		/** selecting and entering input ontologies to be merged */

		/** Conference base */
//		ontologiesFiles.add("Data/cmt.owl");
//		ontologiesFiles.add("Data/Conference.owl");
//		ontologiesFiles.add("Data/confOf.owl");
//		ontologiesFiles.add("Data/edas.owl");
//		ontologiesFiles.add("Data/ekaw.owl");
//		ontologiesFiles.add("Data/iasted.owl");
//		ontologiesFiles.add("Data/sigkdd.owl");

		/** Anatomy base */
		//ontologiesFiles.add("Data/human.owl");
		//ontologiesFiles.add("Data/mouse.owl");

		/** LargeBio base */
		ontologiesFiles.add("Data/FMA3.owl");   /** whole FMA */
		ontologiesFiles.add("Data/NCI3.owl");   /** whole NCI */
		ontologiesFiles.add("Data/SNOMED3.owl");   /** extended SNOMED */


		for(int g=0; g<ontologiesFiles.size(); g++){
			ontologiesSet.add(manager.loadOntologyFromOntologyDocument(new File(ontologiesFiles.get(g))));
		}

		/******************************************************************************************************************************************************************/

		/** selecting and entering input reference alignments to be used in the merge process */

		double threshold = 0.0;
		ArrayList<String> alignmentsFiles = new ArrayList<String>();

		/** Conference base */
//		alignmentsFiles.add("Data/cmt-conference.rdf");
//		alignmentsFiles.add("Data/cmt-confOf.rdf");
//		alignmentsFiles.add("Data/cmt-edas.rdf");
//		alignmentsFiles.add("Data/cmt-ekaw.rdf");
//		alignmentsFiles.add("Data/cmt-iasted.rdf");
//		alignmentsFiles.add("Data/cmt-sigkdd.rdf");
//		alignmentsFiles.add("Data/conference-confOf.rdf");
//		alignmentsFiles.add("Data/conference-edas.rdf");
//		alignmentsFiles.add("Data/conference-ekaw.rdf");
//		alignmentsFiles.add("Data/conference-iasted.rdf");
//		alignmentsFiles.add("Data/conference-sigkdd.rdf");
//		alignmentsFiles.add("Data/confOf-edas.rdf");
//		alignmentsFiles.add("Data/confOf-ekaw.rdf");
//		alignmentsFiles.add("Data/confOf-iasted.rdf");
//		alignmentsFiles.add("Data/confOf-sigkdd.rdf");
//		alignmentsFiles.add("Data/edas-ekaw.rdf");
//		alignmentsFiles.add("Data/edas-iasted.rdf");
//		alignmentsFiles.add("Data/edas-sigkdd.rdf");
//		alignmentsFiles.add("Data/ekaw-iasted.rdf");
//		alignmentsFiles.add("Data/ekaw-sigkdd.rdf");
//		alignmentsFiles.add("Data/iasted-sigkdd.rdf");

		/** Anatomy base */
		//alignmentsFiles.add("Data/human-mouse.rdf");

		/** Largebio base */
		alignmentsFiles.add("Data/FMA2NCI.rdf");
		alignmentsFiles.add("Data/FMA2SNOMED.rdf");
		alignmentsFiles.add("Data/SNOMED2NCI.rdf");

		System.out.println("\n==> Step 1 is done\n");

		/**********************************************************************************************************************************************/		

		/** choose one of the four following methods (if you uncomment one, comment the three others, and vice versa) */

		HashMap<String, String> hash_entitiesMergedNames = new HashMap<String, String>();
		HashMap<String, HashSet<String>> hash_mergedNamesEntities = new HashMap<String, HashSet<String>>();

		HashSet<String> redundantEntities = new HashSet<String>();
		int codeCounter = 0;
		for (String alignment : alignmentsFiles) {
			//codeCounter = getMergedNamesForEquivalentEntitiesUsingOriginalAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter);
			//codeCounter = getMergedNamesForEquivalentEntitiesUsingRepairedAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter, redundantEntities);
			//codeCounter = getMergedNamesForEquivalentEntitiesUsingFilteredAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter, redundantEntities);
			codeCounter = getMergedNamesForEquivalentEntitiesUsingRepairedAndFilteredAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter, redundantEntities);
		}

		System.out.println("\n==> Step 2 and 3 are done");

		/***********************************************************************************************************************************************/

		/* Ontology entities can be classes/concepts, object properties, data type properties, individuals/instances,
		 * annotation properties, data types, and anonymous individuals.
		 */
		/** While axioms creation, each (to be merged) entity would have its unique code number as merged name */

		String mergedOntologyIRI = "http://merging";
		OWLOntology mergedOntology = manager.createOntology(IRI.create(mergedOntologyIRI));
		Set<OWLAxiom> mergedOntologyAxioms = new HashSet<OWLAxiom>();

		HashSet<String> classes = new HashSet<String>();
		HashSet<String> objectProps = new HashSet<String>();
		HashSet<String> dataProps = new HashSet<String>();
		HashSet<String> instances = new HashSet<String>();

		int ontologyCounter = 0;
		for(OWLOntology ontology_n : ontologiesSet){
			ontologyCounter++;
			String ontoCounter = String.valueOf(ontologyCounter);

			createAxiomsOfParsedClasses(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, classes, ontoCounter);
			createAxiomsOfParsedObjectProperties(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, objectProps, ontoCounter);
			createAxiomsOfParsedDataProperties(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, dataProps, ontoCounter);
			createAxiomsOfParsedIndividuals(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, instances, ontoCounter);

			createAxiomsOfParsedSubPropertyChainOfAxioms(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI);
			//createAxiomsOfParsedHasKeyAxioms(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI);
			
			/** these three following methods can be commented, because alignments don't map annotation Properties,
			 * Data Types, or anonymous individuals. We just put them to preserve all ontologies knowledge (axioms).
			 */

			createAxiomsOfParsedNonBuiltInAnnotationProperties(mergedOntologyAxioms, ontology_n, datafactory);
			//createAxiomsOfParsedDataTypes(mergedOntologyAxioms, ontology_n, datafactory);
			createAxiomsOfParsedAnonymousIndividuals(mergedOntologyAxioms, datafactory, ontology_n);
		}

		System.out.println("\n==> Step 4 is done");

		/***********************************************************************************************************************************************/

		/** choose either "createBridgingAxiomsUsingOriginalAlignments" method, or
		 * "createBridgingAxiomsUsingFilteredAlignments" method. (if you uncomment one, comment the other, and vice versa).
		 */

		for (String alignment : alignmentsFiles) {
			//createSubsumptionAndDisjointedBridgingAxiomsUsingOriginalAlignments(alignment, threshold, mergedOntologyIRI, hash_entitiesMergedNames, datafactory, mergedOntologyAxioms, classes, objectProps, dataProps, instances);
			createSubsumptionAndDisjointedBridgingAxiomsUsingFilteredAlignments(alignment, threshold, mergedOntologyIRI, hash_entitiesMergedNames, datafactory, mergedOntologyAxioms, classes, objectProps, dataProps, instances);
		}

		System.out.println("\n==> Step 5 is done");

		/***********************************************************************************************************************************************/

		manager.addAxioms(mergedOntology, mergedOntologyAxioms);

		manager.saveOntology(mergedOntology, new RDFXMLOntologyFormat(), IRI.create(new File("Results/MergedOntology_WithoutRefact.owl")));
		//manager.saveOntology(mergedOntology, new OWLXMLOntologyFormat(), IRI.create(new File("Results/MergedOntology_WithoutRefact.owl")));
		//manager.saveOntology(mergedOntology, new OWLFunctionalSyntaxOntologyFormat(), IRI.create(new File("Results/MergedOntology_WithoutRefact.owl")));	
		//manager.addOntologyStorer(new DLSyntaxOntologyStorer());
		//manager.saveOntology(mergedOntology, new DLSyntaxOntologyFormat(), IRI.create(new File("Results/MergedOntology_WithoutRefact.owl")));
		//manager.addOntologyStorer(new OWLTutorialSyntaxOntologyStorer());
		//manager.saveOntology(mergedOntology, new OWLTutorialSyntaxOntologyFormat(), IRI.create(new File("Results/MergedOntology_WithoutRefact.owl")));
		//manager.saveOntology(mergedOntology, new TurtleOntologyFormat(), IRI.create(new File("Results/MergedOntology_WithoutRefact.owl")));

		System.out.println("\n==> Step 6 is done : The merged ontology is created\n");


		long chrono2 = bean.getCurrentThreadCpuTime();
		long runtime = chrono2-chrono1;

		System.out.println("\n ****** Running the program took : "+runtime/1E6+" ms"); // CPU runtime in milliseconds
		System.out.println(" ****** Running the program took : "+runtime/1E9+" s\n"); // CPU runtime in seconds		

		/***********************************************************************************************************************************************/

		checkNumberOfEntitiesOfMergedOntology(mergedOntology, ontologiesSet, redundantEntities);
		OntologyConsistencyAndCoherence.verify(mergedOntology);


		System.clearProperty("jdk.xml.entityExpansionLimit");
	}

	

	public static int getMergedNamesForEquivalentEntitiesUsingOriginalAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment alignment = parser.parse(new File(alignmentFile).toURI());
		System.out.println("- The alignment " + alignmentFile + " has originally : " + alignment.nbCells() + " cells");
		alignment.cut(threshold);  //al.cut( "hard", threshold );

		int eq = 0, eq1 = 0, eq2 = 0;

		Enumeration<Cell> enumeration = alignment.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String entity1 = cell.getObject1AsURI().toString();
			String entity2 = cell.getObject2AsURI().toString();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=") || relation.equals("?")){
				eq++;
				if(relation.equals("=")){
					eq1++;	
				}else{
					eq2++;
				}
				k++;
				String name = "Code_"+String.valueOf(k);
				getEntitiesMergedNames(entity1, entity2, name, hash1, hash2);
			}
		}
		
		System.out.println("  --> There are " + eq + " equivalences that we will use :");
		System.out.println("     --> " + eq1 + " equivalences (=) ");
		System.out.println("     --> " + eq2 + " unknown equivalences (?)");
		
		return k;
	}


	public static void getEntitiesMergedNames(String entity1, String entity2, String name, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2){

		if(!(hash1.containsKey(entity1)) && !(hash1.containsKey(entity2))){
			HashSet<String> set = new HashSet<String>();
			set.add(entity1);
			set.add(entity2);
			hash2.put(name, set);
			hash1.put(entity1, name);
			hash1.put(entity2, name);
		}
		else{
			if(hash1.containsKey(entity1) && !(hash1.containsKey(entity2))){
				String code = hash1.get(entity1);
				hash1.put(entity2, code);
				HashSet<String> set = hash2.get(code);
				set.add(entity2);
				hash2.put(code, set);
			}
			else{
				if(!(hash1.containsKey(entity1)) && hash1.containsKey(entity2)){
					String code = hash1.get(entity2);
					hash1.put(entity1, code);
					HashSet<String> set = hash2.get(code);
					set.add(entity1);
					hash2.put(code, set);
				}
				else{
					if((hash1.containsKey(entity1)) && hash1.containsKey(entity2)){
						String code1 = hash1.get(entity1);
						String code2 = hash1.get(entity2);
						HashSet<String> set = hash2.get(code1);
						HashSet<String> finalSet = hash2.get(code2);
						set.addAll(finalSet);
						hash2.put(name, set);
						hash2.remove(code1);
						hash2.remove(code2);
						for(String entity : set){
							hash1.put(entity, name);
						}
					}
				}
			}
		}
	}


	public static int getMergedNamesForEquivalentEntitiesUsingRepairedAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k, HashSet<String> redundantEntities) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment alignment = parser.parse(new File(alignmentFile).toURI());
		System.out.println("- The alignment " + alignmentFile + " has originally : " + alignment.nbCells() + " cells");
		alignment.cut(threshold);  //al.cut( "hard", threshold );

		int eq = 0, eq1 = 0, eq2 = 0;
		
		Enumeration<Cell> enumeration = alignment.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String entity1 = cell.getObject1AsURI().toString();
			String entity2 = cell.getObject2AsURI().toString();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=")){
				k++;
				eq++;
				eq1++;
				String name = "Code_"+String.valueOf(k);
				getEntitiesMergedNames(entity1, entity2, name, hash1, hash2);
			}else {
				if(relation.equals("?")){
					eq++;
					eq2++;
					redundantEntities.add(entity1);
					redundantEntities.add(entity2);
				}
			}
		}
		
		System.out.println("  --> There are " + eq + " equivalences :");
		System.out.println("     --> " + eq1 + " equivalences (=) that will use");
		System.out.println("     --> " + eq2 + " unknown equivalences (?) that we won't use");
		
		return k;
	}


	public static int getMergedNamesForEquivalentEntitiesUsingFilteredAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k, HashSet<String> redundantEntities) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment alignment = parser.parse(new File(alignmentFile).toURI());
		System.out.println("- The alignment " + alignmentFile + " has originally : " + alignment.nbCells() + " cells");
		alignment.cut(threshold); //al.cut( "hard", threshold );

		HashMap<String, HashSet<String>> sourcesFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> sourceFilteringHashConf = new HashMap<String, String>();
		HashMap<String, HashSet<String>> targetFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> targetFilteringHashConf = new HashMap<String, String>();

		int eq = 0, eq1 = 0, eq2 = 0, newEq = 0;
		
		Enumeration<Cell> enumeration = alignment.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=") || relation.equals("?")){
				String entity1 = cell.getObject1AsURI().toString();
				String entity2 = cell.getObject2AsURI().toString();

				redundantEntities.add(entity1);
				redundantEntities.add(entity2);
				
				eq++;
				if(relation.equals("=")){
					eq1++;
				}else{
					eq2++;
				}
				
				filterEquivalentCellsHavingSameSources(sourcesFilteringHash, sourceFilteringHashConf, cell, entity1, entity2);
			}
		}

		filterEquivalentCellsHavingSameTargets(sourcesFilteringHash, sourceFilteringHashConf, targetFilteringHash, targetFilteringHashConf);
		ArrayList<Integer> list = mergeEquivalentNames(hash1, hash2, k, targetFilteringHash, newEq);

		getRedundantEntities(redundantEntities, targetFilteringHash);
		
		System.out.println("  --> There are " + eq + " equivalences");
		System.out.println("       * " + eq1 + " equivalences (=)");
		System.out.println("       * " + eq2 + " equivalences (?)");
		System.out.println(" ==> After filtering, we will only use : " + list.get(1) + " equivalences (= and ?) cells");

		return list.get(0);
	}


	public static void filterEquivalentCellsHavingSameSources(HashMap<String, HashSet<String>> sourcesFilteringHash,
			HashMap<String, String> sourceFilteringHashConf, Cell cell, String entity1, String entity2) {

		if ( (!sourcesFilteringHash.containsKey(entity1)) || (cell.getStrength() > Double.valueOf(sourceFilteringHashConf.get(entity1))) ) {
			HashSet<String> set = new HashSet<String>();
			set.add(entity2);
			sourcesFilteringHash.put(entity1, set);
			sourceFilteringHashConf.put(entity1, String.valueOf(cell.getStrength()));

		} else{
			if(cell.getStrength() == Double.valueOf(sourceFilteringHashConf.get(entity1))){
				HashSet<String> set = sourcesFilteringHash.get(entity1);
				set.add(entity2);
				sourcesFilteringHash.put(entity1, set);
			}
		}
	}


	public static void filterEquivalentCellsHavingSameTargets(HashMap<String, HashSet<String>> sourcesFilteringHash,
			HashMap<String, String> sourceFilteringHashConf, HashMap<String, HashSet<String>> targetFilteringHash,
			HashMap<String, String> targetFilteringHashConf) {

		for (Entry<String, HashSet<String>> entry : sourcesFilteringHash.entrySet()) {
			String b = entry.getKey();

			for(String a : entry.getValue()){

				if ( (!targetFilteringHash.containsKey(a)) || (Double.valueOf(sourceFilteringHashConf.get(b)) > Double.valueOf(targetFilteringHashConf.get(a))) ) {
					HashSet<String> set = new HashSet<String>();
					set.add(b);
					targetFilteringHash.put(a, set);
					targetFilteringHashConf.put(a, sourceFilteringHashConf.get(b));

				}else{
					if (Double.valueOf(sourceFilteringHashConf.get(b)).equals(Double.valueOf(targetFilteringHashConf.get(a)))) {
						HashSet<String> set = targetFilteringHash.get(a);
						set.add(b);
						targetFilteringHash.put(a, set);
					}
				}
			}
		}
	}


	public static ArrayList<Integer> mergeEquivalentNames(HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int uniqueNumber,
			HashMap<String, HashSet<String>> targetFilteringHash, int newEq) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Entry<String, HashSet<String>> entry : targetFilteringHash.entrySet()) {
			String entity2 = entry.getKey();

			for(String entity1 : entry.getValue()){
				newEq++;
				uniqueNumber++;
				String name = "Code_"+String.valueOf(uniqueNumber);

				getEntitiesMergedNames(entity1, entity2, name, hash1, hash2);
			}
		}
		list.add(uniqueNumber);
		list.add(newEq);
		return list;
	}

	private static void getRedundantEntities(HashSet<String> redundantEntities, HashMap<String, HashSet<String>> equivHash2) {

		for (Entry<String, HashSet<String>> entry : equivHash2.entrySet()) {
			if(redundantEntities.contains(entry.getKey())) {
				redundantEntities.remove(entry.getKey());
			}
			for(String st : entry.getValue()) {
				if(redundantEntities.contains(st)) {
					redundantEntities.remove(st);
				}
			}
		}
	}
	
	public static int getMergedNamesForEquivalentEntitiesUsingRepairedAndFilteredAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k, HashSet<String> redundantEntities) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment alignment = parser.parse(new File(alignmentFile).toURI());
		System.out.println("- The alignment " + alignmentFile + " has originally : " + alignment.nbCells() + " cells");
		alignment.cut(threshold); //al.cut( "hard", threshold );

		HashMap<String, HashSet<String>> sourcesFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> sourceFilteringHashConf = new HashMap<String, String>();
		HashMap<String, HashSet<String>> targetFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> targetFilteringHashConf = new HashMap<String, String>();

		int eq = 0, eq1 = 0, eq2 = 0, newEq = 0;
		
		Enumeration<Cell> enumeration = alignment.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String relation = cell.getRelation().getRelation();

			String entity1 = cell.getObject1AsURI().toString();
			String entity2 = cell.getObject2AsURI().toString();

			redundantEntities.add(entity1);
			redundantEntities.add(entity2);
			
			if(relation.equals("=")){
				eq++;
				eq1++;
				filterEquivalentCellsHavingSameSources(sourcesFilteringHash, sourceFilteringHashConf, cell, entity1, entity2);
			}else {
				if(relation.equals("?")) {
					eq++;
					eq2++;
				}
			}
		}

		filterEquivalentCellsHavingSameTargets(sourcesFilteringHash, sourceFilteringHashConf, targetFilteringHash, targetFilteringHashConf);
		ArrayList<Integer> list = mergeEquivalentNames(hash1, hash2, k, targetFilteringHash, newEq);

		System.out.println("  --> There are " + eq + " equivalences");
		System.out.println("       * " + eq1 + " equivalences (=)");
		System.out.println("       * " + eq2 + " equivalences (?)");
		System.out.println(" ==> After filtering, we will only use : " + list.get(1) + " equivalences (=) cells");
		
		getRedundantEntities(redundantEntities, targetFilteringHash);

		return list.get(0);
	}


	public static void createAxiomsOfParsedClasses(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, HashSet<String> hclasses, String ontNum) throws OWLException{

		for (OWLClass cls : ont.getClassesInSignature()) {

			if (!cls.isOWLThing()) {
				String ch = cls.getIRI().toString();
				hclasses.add(ch);
				OWLClass clsA;

				if(mergedEntitiesNewNames.containsKey(ch)){
					String c = mergedEntitiesNewNames.get(ch);

					clsA = df.getOWLClass(IRI.create(iri + "#" + c));
					//clsA = df.getOWLClass(IRI.create(iri + "#", cls.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(clsA);
					axioms.add(ax);

					addLabelForMergedEntity(ch, df, clsA, axioms, ontNum);
				}else{

					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(cls);
					axioms.add(ax);
					clsA = cls;
				}

				extractAndCreateClassAnnotations(clsA, axioms, df, cls, ont); //labels, comments, and non built-in annotations
				extractAndCreateSuperClassesOfClass(clsA, mergedEntitiesNewNames, iri, axioms, df, ont, cls);
				extractAndCreateEquivalentClassesOfClass(clsA, mergedEntitiesNewNames, iri, df, axioms, cls, ont);
				/** if you don't want to include disjoint axioms between classes, comment the following instruction */
				extractAndCreateDisjointClassesOfClass(clsA, mergedEntitiesNewNames, axioms, iri,df, cls, ont);

				//} else {
				//OWLClass clsA = df.getOWLThing();
				//OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(clsA);
				//axioms.add(ax);
				//}
			}
		}
	}

	public static void addLabelForMergedEntity(String ch, OWLDataFactory df, OWLEntity entity, Set<OWLAxiom> axioms, String numID){

		int index = ch.indexOf("#", 0);
		String c = ch.substring((index + 1));
		OWLAnnotation label = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(numID + ". " + c));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), label);
		axioms.add(ax);
	}

	public static void extractAndCreateClassAnnotations(OWLClass cls, Set<OWLAxiom> axioms, OWLDataFactory datafact, OWLClass concept, OWLOntology ont) {

		for (OWLAnnotation annotation : concept.getAnnotations(ont)) {

			OWLAnnotationProperty prop = annotation.getProperty();
			OWLAnnotationValue value = annotation.getValue();

			OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(prop, cls.getIRI(), value);
			axioms.add(ax);
		}
	}
	
	public static void extractAndCreateSuperClassesOfClass(OWLClass clsA, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLClass concept) {

		for (OWLClassExpression classExpression : concept.getSuperClasses(ont)) {

			OWLClassExpression clsExpression = getClassExpressions(classExpression, df, mergedEntitiesNewNames, iri);

			OWLSubClassOfAxiom axiom = df.getOWLSubClassOfAxiom(clsA, clsExpression);
			axioms.add(axiom);
		}
	}

	public static OWLClassExpression getClassExpressions(OWLClassExpression classExpr, OWLDataFactory df, HashMap<String, String> mergedEntitiesNewNames, String iri) {

		OWLClassExpression owlClassRestriction = null;

		//if(!classExpr.isAnonymous()){
		//else if(classExpr.equals(ClassExpressionType.OWL_CLASS)){
		if (classExpr instanceof OWLClass) {
			String expression = classExpr.asOWLClass().getIRI().toString();
			int index = expression.indexOf("#", 0);
			String c = expression.substring((index + 1));

			if (!c.equals("Thing")) {
				if(mergedEntitiesNewNames.containsKey(expression)){
					c = mergedEntitiesNewNames.get(expression);
					owlClassRestriction = df.getOWLClass(IRI.create(iri + "#" + c));
					//owlClassRestriction = df.getOWLClass(IRI.create(iri + "#" + classExpr.asOWLClass().getIRI().getFragment()));
				}else{
					owlClassRestriction = classExpr;
				}
			} else {
				owlClassRestriction = df.getOWLThing();
			}
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_SOME_VALUES_FROM)){
		//else if (classExpr instanceof OWLObjectSomeValuesFromImpl) {
		else if (classExpr instanceof OWLObjectSomeValuesFrom) {
			OWLObjectSomeValuesFrom expression = (OWLObjectSomeValuesFrom)classExpr;

			OWLObjectPropertyExpression objProp = expression.getProperty();
			OWLClassExpression exp = expression.getFiller();

			owlClassRestriction = df.getOWLObjectSomeValuesFrom(getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri), getClassExpressions(exp, df, mergedEntitiesNewNames, iri));			
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_ALL_VALUES_FROM)){
		//else if (classExpr instanceof OWLObjectAllValuesFromImpl) {
		else if (classExpr instanceof OWLObjectAllValuesFrom) {
			OWLObjectAllValuesFrom expression = (OWLObjectAllValuesFrom)classExpr;

			OWLObjectPropertyExpression objProp = expression.getProperty();
			OWLClassExpression exp = expression.getFiller();

			owlClassRestriction = df.getOWLObjectAllValuesFrom(getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri), getClassExpressions(exp, df, mergedEntitiesNewNames, iri));
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_MIN_CARDINALITY)){
		//else if (classExpr instanceof OWLObjectMinCardinalityImpl) {
		else if(classExpr instanceof OWLObjectMinCardinality){
			OWLObjectMinCardinality exp = (OWLObjectMinCardinality)classExpr;

			OWLObjectPropertyExpression objProp = exp.getProperty();
			OWLClassExpression range = exp.getFiller();
			int card = exp.getCardinality();

			owlClassRestriction = df.getOWLObjectMinCardinality(card, getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri), getClassExpressions(range, df, mergedEntitiesNewNames, iri));
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_MAX_CARDINALITY)){
		//else if (classExpr instanceof OWLObjectMaxCardinalityImpl) {
		else if(classExpr instanceof OWLObjectMaxCardinality){
			OWLObjectMaxCardinality exp = (OWLObjectMaxCardinality)classExpr;

			OWLObjectPropertyExpression objProp = exp.getProperty();
			OWLClassExpression range = exp.getFiller();
			int card = exp.getCardinality();

			owlClassRestriction = df.getOWLObjectMaxCardinality(card, getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri), getClassExpressions(range, df, mergedEntitiesNewNames, iri));
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_EXACT_CARDINALITY)){
		//else if (classExpr instanceof OWLObjectExactCardinalityImpl) {
		else if(classExpr instanceof OWLObjectExactCardinality){
			OWLObjectExactCardinality exp = (OWLObjectExactCardinality)classExpr;

			OWLObjectPropertyExpression objProp = exp.getProperty();
			OWLClassExpression range = exp.getFiller();
			int card = exp.getCardinality();

			owlClassRestriction = df.getOWLObjectExactCardinality(card, getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri), getClassExpressions(range, df, mergedEntitiesNewNames, iri));
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_HAS_VALUE)){
		//else if (classExpr instanceof OWLObjectHasValueImpl) {
		else if(classExpr instanceof OWLObjectHasValue){
			OWLObjectHasValue exp = (OWLObjectHasValue)classExpr;

			OWLObjectPropertyExpression objProp = exp.getProperty();
			OWLIndividual ind = exp.getValue();

			owlClassRestriction = df.getOWLObjectHasValue(getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri), getIndividuals(ind, df, mergedEntitiesNewNames, iri));
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_HAS_SELF)){
		//else if (classExpr instanceof OWLObjectHasSelfImpl) {
		else if(classExpr instanceof OWLObjectHasSelf){
			OWLObjectHasSelf exp = (OWLObjectHasSelf)classExpr;

			OWLObjectPropertyExpression objProp = exp.getProperty();

			owlClassRestriction = df.getOWLObjectHasSelf(getObjectPropertyExpressions(objProp, df, mergedEntitiesNewNames, iri));	
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_COMPLEMENT_OF)){
		//else if (classExpr instanceof OWLObjectComplementOfImpl) {
		else if(classExpr instanceof OWLObjectComplementOf){
			OWLObjectComplementOf exp = (OWLObjectComplementOf)classExpr;

			OWLClassExpression complement = exp.getOperand();

			owlClassRestriction = df.getOWLObjectComplementOf(getClassExpressions(complement, df, mergedEntitiesNewNames, iri));
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_UNION_OF)){
		//else if (classExpr instanceof OWLObjectUnionOfImpl) {
		else if (classExpr instanceof OWLObjectUnionOf) {
			OWLObjectUnionOf union = (OWLObjectUnionOf)classExpr;

			Set<OWLClassExpression> set = new HashSet<OWLClassExpression>();
			for(OWLClassExpression exp : union.getOperands()){
				set.add(getClassExpressions(exp, df, mergedEntitiesNewNames, iri));
			}
			owlClassRestriction = df.getOWLObjectUnionOf(set);
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_INTERSECTION_OF)){
		//else if (classExpr instanceof OWLObjectIntersectionOfImpl) {
		if (classExpr instanceof OWLObjectIntersectionOf) {
			OWLObjectIntersectionOf intersect = (OWLObjectIntersectionOf)classExpr;

			Set<OWLClassExpression> set = new HashSet<OWLClassExpression>();
			for(OWLClassExpression exp : intersect.getOperands()){
				set.add(getClassExpressions(exp, df, mergedEntitiesNewNames, iri));
			}
			owlClassRestriction = df.getOWLObjectIntersectionOf(set);
		}


		//else if(classExpr.equals(ClassExpressionType.OBJECT_ONE_OF)){
		//else if (classExpr instanceof OWLObjectOneOfImpl) {
		else if (classExpr instanceof OWLObjectOneOf) {
			OWLObjectOneOf exp = (OWLObjectOneOf)classExpr;

			Set<OWLIndividual> set = new HashSet<OWLIndividual>();
			for(OWLIndividual ind : exp.getIndividuals()){
				set.add(getIndividuals(ind, df, mergedEntitiesNewNames, iri));
			}
			owlClassRestriction = df.getOWLObjectOneOf(set);
		}


		//else if(classExpr.equals(ClassExpressionType.DATA_MIN_CARDINALITY)){
		//else if (classExpr instanceof OWLDataMinCardinalityImpl) {
		else if(classExpr instanceof OWLDataMinCardinality){
			OWLDataMinCardinality exp = (OWLDataMinCardinality)classExpr;

			OWLDataPropertyExpression dataProp = exp.getProperty();
			OWLDataRange range = exp.getFiller();
			int card = exp.getCardinality();

			owlClassRestriction = df.getOWLDataMinCardinality(card, getDataPropertyExpressions(dataProp, df, mergedEntitiesNewNames, iri), getDataRanges(range, df));
		}


		//else if(classExpr.equals(ClassExpressionType.DATA_MAX_CARDINALITY)){
		//else if (classExpr instanceof OWLDataMaxCardinalityImpl) {
		else if(classExpr instanceof OWLDataMaxCardinality){
			OWLDataMaxCardinality exp = (OWLDataMaxCardinality)classExpr;

			OWLDataPropertyExpression dataProp = exp.getProperty();
			OWLDataRange range = exp.getFiller();
			int card = exp.getCardinality();

			owlClassRestriction = df.getOWLDataMaxCardinality(card, getDataPropertyExpressions(dataProp, df, mergedEntitiesNewNames, iri), getDataRanges(range, df));
		}


		//else if(classExpr.equals(ClassExpressionType.DATA_EXACT_CARDINALITY)){
		//else if (classExpr instanceof OWLDataExactCardinalityImpl) {
		else if(classExpr instanceof OWLDataExactCardinality){
			OWLDataExactCardinality exp = (OWLDataExactCardinality)classExpr;

			OWLDataPropertyExpression dataProp = exp.getProperty();
			OWLDataRange range = exp.getFiller();
			int card = exp.getCardinality();

			owlClassRestriction = df.getOWLDataExactCardinality(card, getDataPropertyExpressions(dataProp, df, mergedEntitiesNewNames, iri), getDataRanges(range, df));
		}


		//else if(classExpr.equals(ClassExpressionType.DATA_HAS_VALUE)){
		//else if (classExpr instanceof OWLDataHasValueImpl) {
		else if(classExpr instanceof OWLDataHasValue){
			OWLDataHasValue exp = (OWLDataHasValue)classExpr;

			OWLDataPropertyExpression dataProp = exp.getProperty();
			OWLLiteral literal = exp.getValue();

			owlClassRestriction = df.getOWLDataHasValue(getDataPropertyExpressions(dataProp, df, mergedEntitiesNewNames, iri), literal);
		}


		//else if(classExpr.equals(ClassExpressionType.DATA_ALL_VALUES_FROM)){
		//else if (classExpr instanceof OWLDataAllValuesFromImpl) {
		else if(classExpr instanceof OWLDataAllValuesFrom){
			OWLDataAllValuesFrom exp = (OWLDataAllValuesFrom)classExpr;

			OWLDataPropertyExpression dataProp = exp.getProperty();
			OWLDataRange range = exp.getFiller();

			owlClassRestriction = df.getOWLDataAllValuesFrom(getDataPropertyExpressions(dataProp, df, mergedEntitiesNewNames, iri), getDataRanges(range, df));
		}


		//else if(classExpr.equals(ClassExpressionType.DATA_SOME_VALUES_FROM)){
		//else if (classExpr instanceof OWLDataSomeValuesFromImpl) {
		else if(classExpr instanceof OWLDataSomeValuesFrom){
			OWLDataSomeValuesFrom exp = (OWLDataSomeValuesFrom)classExpr;

			OWLDataPropertyExpression dataProp = exp.getProperty();
			OWLDataRange range = exp.getFiller();

			owlClassRestriction = df.getOWLDataSomeValuesFrom(getDataPropertyExpressions(dataProp, df, mergedEntitiesNewNames, iri), getDataRanges(range, df));
		}

		return owlClassRestriction;
	}

	public static OWLObjectPropertyExpression getObjectPropertyExpressions(OWLObjectPropertyExpression propExpr, OWLDataFactory df, HashMap<String, String> mergedEntitiesNewNames, String iri) {

		OWLObjectPropertyExpression owlObjectPropertyRestriction = null;

		//if(!propExpr.isAnonymous()){
		if (propExpr instanceof OWLObjectProperty) {

			String expression = propExpr.asOWLObjectProperty().getIRI().toString();
			int index = expression.indexOf("#", 0);
			String c = expression.substring((index + 1));

			if (!c.equals("topObjectProperty")) {
				if(mergedEntitiesNewNames.containsKey(expression)){
					c = mergedEntitiesNewNames.get(expression);
					owlObjectPropertyRestriction = df.getOWLObjectProperty(IRI.create(iri + "#" + c));
					//owlObjectPropertyRestriction = df.getOWLObjectProperty(IRI.create(iri + "#" + propExpr.asOWLObjectProperty().getIRI().getFragment()));
				}else{
					owlObjectPropertyRestriction = propExpr;
				}
			} else {
				owlObjectPropertyRestriction = df.getOWLTopObjectProperty();
			}	
		}

		//else if (propExpr instanceof OWLObjectInverseOfImpl) {
		else if(propExpr instanceof OWLObjectInverseOf){
			OWLObjectInverseOf exp = (OWLObjectInverseOf)propExpr;

			OWLObjectPropertyExpression inverseProp = exp.getInverse();

			owlObjectPropertyRestriction = df.getOWLObjectInverseOf(getObjectPropertyExpressions(inverseProp, df, mergedEntitiesNewNames, iri));		
		}
		
		return owlObjectPropertyRestriction;
	}


	public static OWLDataPropertyExpression getDataPropertyExpressions(OWLDataPropertyExpression dataExpr, OWLDataFactory df, HashMap<String, String> mergedEntitiesNewNames, String iri) {

		OWLDataPropertyExpression owlDataPropertyRestriction = null;

		//if(!dataExpr.isAnonymous()){
		if (dataExpr instanceof OWLDataProperty) {

			String expression = dataExpr.asOWLDataProperty().getIRI().toString();
			int index = expression.indexOf("#", 0);
			String c = expression.substring((index + 1));

			if (!c.equals("topDataProperty")) {
				if(mergedEntitiesNewNames.containsKey(expression)){
					c = mergedEntitiesNewNames.get(expression);
					owlDataPropertyRestriction = df.getOWLDataProperty(IRI.create(iri + "#" + c));
					//owlDataPropertyRestriction = df.getOWLDataProperty(IRI.create(iri + "#" + dataExpr.asOWLDataProperty().getIRI().getFragment()));
				}else{
					owlDataPropertyRestriction = dataExpr;
				}
			} else {
				owlDataPropertyRestriction = df.getOWLTopDataProperty();
			}		
		}

		return owlDataPropertyRestriction;
	}

	public static OWLIndividual getIndividuals(OWLIndividual individual, OWLDataFactory df, HashMap<String, String> mergedEntitiesNewNames, String iri) {

		OWLIndividual owlIndividual = individual;

		//if(individual.isNamed()){
		if (individual instanceof OWLNamedIndividual) {

			String expression = individual.asOWLNamedIndividual().getIRI().toString();
			int index = expression.indexOf("#", 0);
			String c = expression.substring((index + 1));

			if(mergedEntitiesNewNames.containsKey(expression)){
				c = mergedEntitiesNewNames.get(expression);
				owlIndividual = df.getOWLNamedIndividual(IRI.create(iri + "#" + c));
				//owlIndividual = df.getOWLNamedIndividual(IRI.create(iri + "#" + individual.asOWLNamedIndividual().getIRI().getFragment()));
			}
		}

		return owlIndividual;
	}

	public static OWLDataRange getDataRanges(OWLDataRange dataRange, OWLDataFactory df) {

		OWLDataRange owlDataRangeRestriction = null;

		//if(dataRange.isDatatype()) {
		if (dataRange instanceof OWLDatatype) {
			owlDataRangeRestriction = dataRange;
		}


		//else if (dataRange instanceof OWLDataOneOfImpl) {
		else if(dataRange instanceof OWLDataOneOf){
			OWLDataOneOf exp = (OWLDataOneOf)dataRange;

			Set<OWLLiteral> set = new HashSet<OWLLiteral>();
			for(OWLLiteral literal : exp.getValues()){
				set.add(literal);
			}
			owlDataRangeRestriction = df.getOWLDataOneOf(set);
		}


		//else if (dataRange instanceof OWLDataIntersectionOfImpl) {
		else if(dataRange instanceof OWLDataIntersectionOf){
			OWLDataIntersectionOf exp = (OWLDataIntersectionOf)dataRange;

			Set<OWLDataRange> set = new HashSet<OWLDataRange>();
			for(OWLDataRange range : exp.getOperands()){
				set.add(getDataRanges(range, df));
			}
			owlDataRangeRestriction = df.getOWLDataIntersectionOf(set);
		}


		//else if (dataRange instanceof OWLDataUnionOfImpl) {
		else if(dataRange instanceof OWLDataUnionOf){
			OWLDataUnionOf exp = (OWLDataUnionOf)dataRange;

			Set<OWLDataRange> set = new HashSet<OWLDataRange>();
			for(OWLDataRange range : exp.getOperands()){
				set.add(getDataRanges(range, df));
			}
			owlDataRangeRestriction = df.getOWLDataUnionOf(set);
		}


		//else if (dataRange instanceof OWLDataComplementOfImpl) {
		else if(dataRange instanceof OWLDataComplementOf){
			OWLDataComplementOf exp = (OWLDataComplementOf)dataRange;

			OWLDataRange complementRange = exp.getDataRange();
			owlDataRangeRestriction = df.getOWLDataComplementOf(getDataRanges(complementRange, df));
		}


		else if (dataRange instanceof OWLDatatypeRestriction) {
			OWLDatatypeRestriction exp = (OWLDatatypeRestriction)dataRange;

			OWLDatatype dataType = exp.getDatatype();

			Set<OWLFacetRestriction> set = new HashSet<OWLFacetRestriction>();
			for(OWLFacetRestriction facetRestriction : exp.getFacetRestrictions()){
				set.add(facetRestriction);
				//OWLFacet facet = facetRestriction.getFacet();
				//OWLLiteral literal = facetRestriction.getFacetValue();
			}
			owlDataRangeRestriction = df.getOWLDatatypeRestriction(dataType, set);
		}


		return owlDataRangeRestriction;
	}

	public static void extractAndCreateEquivalentClassesOfClass(OWLClass clsA, HashMap<String, String> mergedEntitiesNewNames, String iri, OWLDataFactory df, Set<OWLAxiom> axioms, OWLClass concept, OWLOntology ont) {

		for (OWLClassExpression equiv : concept.getEquivalentClasses(ont)) {

			OWLClassExpression restriction = getClassExpressions(equiv, df, mergedEntitiesNewNames, iri);

			OWLEquivalentClassesAxiom axiom = df.getOWLEquivalentClassesAxiom(clsA, restriction);
			axioms.add(axiom);
		}
	}

	public static void extractAndCreateDisjointClassesOfClass(OWLClass clsA, HashMap<String, String> mergedEntitiesNewNames, Set<OWLAxiom> axioms, String iri, OWLDataFactory df, OWLClass concept, OWLOntology ont) {

		for (OWLClassExpression disj : concept.getDisjointClasses(ont)) {

			OWLClassExpression restriction = getClassExpressions(disj, df, mergedEntitiesNewNames, iri);

			OWLDisjointClassesAxiom axiom = df.getOWLDisjointClassesAxiom(clsA, restriction);
			axioms.add(axiom);
		}
	}

	public static void createAxiomsOfParsedObjectProperties(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, HashSet<String> hashObjProp, String ontNum) throws OWLException{

		for (OWLObjectProperty objProperty : ont.getObjectPropertiesInSignature()) {

			if (!objProperty.isOWLTopObjectProperty()) {
				String ch = objProperty.getIRI().toString();
				hashObjProp.add(ch);
				OWLObjectProperty objectProp;

				if(mergedEntitiesNewNames.containsKey(ch)){
					String c = mergedEntitiesNewNames.get(ch);

					objectProp = df.getOWLObjectProperty(IRI.create(iri + "#" + c));
					//objectProp = df.getOWLObjectProperty(IRI.create(iri + "#", objProperty.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(objectProp);
					axioms.add(ax);

					addLabelForMergedEntity(ch, df, objectProp, axioms, ontNum);
				}else{

					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(objProperty);
					axioms.add(ax);
					objectProp = objProperty;
				}

				extractAndCreateObjectPropertyAnnotations(axioms, objectProp, ont, df, objProperty);  //labels, comments, and non built-in annotations
				extractAndCreateObjectPropertyDomains(objectProp, mergedEntitiesNewNames, iri, axioms, df, ont, objProperty);
				extractAndCreateObjectPropertyRanges(objectProp, mergedEntitiesNewNames, iri, axioms, df, ont, objProperty);
				extractAndCreateObjectPropertyTypes(objectProp, axioms, df, ont, objProperty);
				extractAndCreateSuperPropertiesOfObjectProperty(objectProp, mergedEntitiesNewNames, iri, axioms, df, ont, objProperty);
				extractAndCreateInversePropertiesOfObjectProperty(objectProp, mergedEntitiesNewNames, iri, axioms, df, ont, objProperty);
				extractAndCreateDisjointPropertiesOfObjectProperty(objectProp, mergedEntitiesNewNames, iri, axioms, df, ont, objProperty);
				extractAndCreateEquivalentPropertiesOfObjectProperty(objectProp, mergedEntitiesNewNames, iri, axioms, df, ont, objProperty);
				
				//} else {
				//OWLObjectProperty objectProp = df.getOWLTopObjectProperty();
				//OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(objectProp);
				//axioms.add(ax);
				//}
			}
		}
	}
	
	public static void extractAndCreateObjectPropertyAnnotations(Set<OWLAxiom> axioms, OWLObjectProperty objProp, OWLOntology ont,
			OWLDataFactory datafact, OWLObjectProperty objectProp) {

		for (OWLAnnotation annotation : objectProp.getAnnotations(ont)) {

			OWLAnnotationProperty prop = annotation.getProperty();
			OWLAnnotationValue value = annotation.getValue();

			OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(prop, objProp.getIRI(), value);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateObjectPropertyDomains(OWLObjectProperty objectProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLClassExpression clas : obj.getDomains(ont)) {

			OWLClassExpression restriction = getClassExpressions(clas, df, mergedEntitiesNewNames, iri);

			OWLObjectPropertyDomainAxiom ax = df.getOWLObjectPropertyDomainAxiom(objectProp, restriction);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateObjectPropertyRanges(OWLObjectProperty objProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLClassExpression classExp : obj.getRanges(ont)) {

			OWLClassExpression restriction = getClassExpressions(classExp, df, mergedEntitiesNewNames, iri);

			OWLObjectPropertyRangeAxiom ax = df.getOWLObjectPropertyRangeAxiom(objProp, restriction);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateObjectPropertyTypes(OWLObjectProperty objectProperty, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty prop) {

		if (prop.isSymmetric(ont)) {
			OWLSymmetricObjectPropertyAxiom typeAx = df
					.getOWLSymmetricObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);		}

		if (prop.isAsymmetric(ont)) {
			OWLAsymmetricObjectPropertyAxiom typeAx = df
					.getOWLAsymmetricObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);		}

		if (prop.isReflexive(ont)) {
			OWLReflexiveObjectPropertyAxiom typeAx = df
					.getOWLReflexiveObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);		}

		if (prop.isTransitive(ont)) {
			OWLTransitiveObjectPropertyAxiom typeAx = df
					.getOWLTransitiveObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);		}

		if (prop.isIrreflexive(ont)) {
			OWLIrreflexiveObjectPropertyAxiom typeAx = df
					.getOWLIrreflexiveObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);		}

		if (prop.isFunctional(ont)) {
			OWLFunctionalObjectPropertyAxiom typeAx = df
					.getOWLFunctionalObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);		}

		if (prop.isInverseFunctional(ont)) {
			OWLInverseFunctionalObjectPropertyAxiom typeAx = df
					.getOWLInverseFunctionalObjectPropertyAxiom(objectProperty);
			axioms.add(typeAx);
		}	
	}

	public static void extractAndCreateSuperPropertiesOfObjectProperty(OWLObjectProperty objProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLObjectPropertyExpression superProp : obj.getSuperProperties(ont)) {

			OWLObjectPropertyExpression expression = getObjectPropertyExpressions(superProp, df, mergedEntitiesNewNames,iri);

			OWLSubObjectPropertyOfAxiom ax = df.getOWLSubObjectPropertyOfAxiom(objProp, expression);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateInversePropertiesOfObjectProperty(OWLObjectProperty objectProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLObjectPropertyExpression invProp : obj.getInverses(ont)) {

			OWLObjectPropertyExpression expression = getObjectPropertyExpressions(invProp, df, mergedEntitiesNewNames,iri);

			OWLInverseObjectPropertiesAxiom invAx = df.getOWLInverseObjectPropertiesAxiom(objectProp, expression);
			axioms.add(invAx);
		}
	}

	public static void extractAndCreateDisjointPropertiesOfObjectProperty(OWLObjectProperty objProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty prop) {

		for (OWLObjectPropertyExpression propExpression : prop.getDisjointProperties(ont)) {

			OWLObjectPropertyExpression expression = getObjectPropertyExpressions(propExpression, df, mergedEntitiesNewNames,iri);

			OWLDisjointObjectPropertiesAxiom disjAx = df.getOWLDisjointObjectPropertiesAxiom(objProp, expression);
			axioms.add(disjAx);
		}
	}

	public static void extractAndCreateEquivalentPropertiesOfObjectProperty(OWLObjectProperty oProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty prop) {

		for (OWLObjectPropertyExpression equivProp : prop.getEquivalentProperties(ont)) {

			OWLObjectPropertyExpression expression = getObjectPropertyExpressions(equivProp, df, mergedEntitiesNewNames,iri);

			OWLEquivalentObjectPropertiesAxiom invAx = df.getOWLEquivalentObjectPropertiesAxiom(oProp, expression);
			axioms.add(invAx);
		}
	}

	
	public static void createAxiomsOfParsedDataProperties(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, HashSet<String> hdataprop, String ontNum) throws OWLException{

		for (OWLDataProperty dataProperty : ont.getDataPropertiesInSignature()) {
			if (!dataProperty.isOWLTopDataProperty()) {
				String ch = dataProperty.getIRI().toString();
				hdataprop.add(ch);
				OWLDataProperty dProp;

				if(mergedEntitiesNewNames.containsKey(ch)){
					String c = mergedEntitiesNewNames.get(ch);

					dProp = df.getOWLDataProperty(IRI.create(iri + "#" + c));
					//dProp = df.getOWLDataProperty(IRI.create(iri + "#", dataProperty.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(dProp);
					axioms.add(ax);

					addLabelForMergedEntity(ch, df, dProp, axioms, ontNum);
				}else{

					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(dataProperty);
					axioms.add(ax);
					dProp = dataProperty;
				}

				extractAndCreateDataPropertyAnnotations(dProp, axioms, dataProperty, ont, df);  //labels, comments, and non built-in annotations
				extractAndCreateDataPropertyDomains(dProp, mergedEntitiesNewNames, iri, axioms, df, dataProperty, ont);
				extractAndCreateDataPropertyRanges(dProp, iri, axioms, df, dataProperty, ont);
				extractAndCreateDataPropertyTypes(dProp, axioms, df, dataProperty, ont);
				extractAndCreateSuperPropertiesOfDataProperty(dProp, mergedEntitiesNewNames, iri, axioms, df, dataProperty, ont);
				extractAndCreateEquivalentPropertiesOfDataProperty(dProp, mergedEntitiesNewNames, iri, axioms, df, dataProperty, ont);
				extractAndCreateDisjointPropertiesOfDataProperty(dProp, mergedEntitiesNewNames, iri, axioms, df, dataProperty, ont);

				//} else {
				//OWLDataProperty dProp = df.getOWLTopDataProperty();
				//OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(dProp);
				//axioms.add(ax);
				//}
			}
		}
	}

	public static void extractAndCreateDataPropertyAnnotations(OWLDataProperty dataProp, Set<OWLAxiom> axioms, OWLDataProperty dataProperty,
			OWLOntology ont, OWLDataFactory df) {

		for (OWLAnnotation annotation : dataProperty.getAnnotations(ont)) {

			OWLAnnotationProperty prop = annotation.getProperty();
			OWLAnnotationValue value = annotation.getValue();

			OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(prop, dataProp.getIRI(), value);
			axioms.add(ax);
		}
	}
	
	public static void extractAndCreateDataPropertyDomains(OWLDataProperty dataProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dataProperty, OWLOntology ont) {

		for (OWLClassExpression classExp : dataProperty.getDomains(ont)) {

			OWLClassExpression restriction = getClassExpressions(classExp, df, mergedEntitiesNewNames, iri);

			OWLDataPropertyDomainAxiom ax = df.getOWLDataPropertyDomainAxiom(dataProp, restriction);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateDataPropertyRanges(OWLDataProperty dataProp, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dataProperty, OWLOntology ont) {

		for (OWLDataRange range : dataProperty.getRanges(ont)) {

			OWLDataRange dataRange = getDataRanges(range, df);

			OWLDataPropertyRangeAxiom ax = df.getOWLDataPropertyRangeAxiom(dataProp, dataRange);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateDataPropertyTypes(OWLDataProperty dataProp, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dataProperty, OWLOntology ont) {

		if (dataProperty.isFunctional(ont)) {
			OWLFunctionalDataPropertyAxiom typeAx = df.getOWLFunctionalDataPropertyAxiom(dataProp);
			axioms.add(typeAx);	
		}
	}

	public static void extractAndCreateSuperPropertiesOfDataProperty(OWLDataProperty dataProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dProp, OWLOntology ont) {

		for (OWLDataPropertyExpression superProp : dProp.getSuperProperties(ont)) {

			OWLDataPropertyExpression dataExpr = getDataPropertyExpressions(superProp, df, mergedEntitiesNewNames, iri);

			OWLSubDataPropertyOfAxiom ax = df.getOWLSubDataPropertyOfAxiom(dataProp, dataExpr);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateEquivalentPropertiesOfDataProperty(OWLDataProperty dataProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dProp, OWLOntology ont) {

		for (OWLDataPropertyExpression equiv : dProp.getEquivalentProperties(ont)) {

			OWLDataPropertyExpression dataExpr = getDataPropertyExpressions(equiv, df, mergedEntitiesNewNames, iri);

			OWLEquivalentDataPropertiesAxiom eqAxiom = df.getOWLEquivalentDataPropertiesAxiom(dataProp, dataExpr);
			axioms.add(eqAxiom);
		}
	}

	public static void extractAndCreateDisjointPropertiesOfDataProperty(OWLDataProperty dataProp, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dProp, OWLOntology ont) {

		for (OWLDataPropertyExpression disjProp : dProp.getDisjointProperties(ont)) {

			OWLDataPropertyExpression dataExpr = getDataPropertyExpressions(disjProp, df, mergedEntitiesNewNames, iri);

			OWLDisjointDataPropertiesAxiom disjAx = df.getOWLDisjointDataPropertiesAxiom(dataProp, dataExpr);
			axioms.add(disjAx);
		}
	}

	public static void createAxiomsOfParsedIndividuals(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, HashSet<String> hInstances, String ontNum) throws OWLException{

		for (OWLNamedIndividual individual : ont.getIndividualsInSignature()) {
			String ch = individual.getIRI().toString();
			hInstances.add(ch);
			OWLNamedIndividual instance;

			if(mergedEntitiesNewNames.containsKey(ch)){
				String c = mergedEntitiesNewNames.get(ch);

				instance = df.getOWLNamedIndividual(IRI.create(iri + "#" + c));
				//instance = df.getOWLNamedIndividual(IRI.create(iri + "#" + individual.getIRI().getFragment()));
				OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(instance);
				axioms.add(ax);

				addLabelForMergedEntity(ch, df, instance, axioms, ontNum);
			}else{

				OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(individual);
				axioms.add(ax);
				instance = individual;
			}

			extractAndCreateIndividualAnnotations(instance, axioms, individual, ont, df);   //labels, comments, and non built-in annotations
			extractAndCreateSameAsIndividualsOfIndividual(instance, mergedEntitiesNewNames, iri, axioms, df, individual, ont);
			extractAndCreateDifferentIndividualsOfIndividual(instance, mergedEntitiesNewNames, axioms, iri, df, individual, ont); 
			extractAndCreateClassAssertions(instance, mergedEntitiesNewNames, iri, axioms, df, individual, ont);
			extractAndCreateObjectPropertyAssertionsOfIndividual(instance, mergedEntitiesNewNames, iri, axioms, df, individual, ont); 
			extractAndCreateNegativeObjectPropertyAssertionsOfIndividual(instance, mergedEntitiesNewNames, iri, axioms, df, individual, ont); 
			extractAndCreateDataPropertyAssertionsOfIndividual(instance, mergedEntitiesNewNames, iri, axioms, df, individual, ont);
			extractAndCreateNegativeDataPropertyAssertionsOfIndividual(instance, mergedEntitiesNewNames, iri, axioms, df, individual, ont);
		}
	}

	public static void extractAndCreateIndividualAnnotations(OWLNamedIndividual instance, Set<OWLAxiom> axioms, OWLNamedIndividual individual, OWLOntology ont,
			OWLDataFactory df) {

		for (OWLAnnotation annotation : individual.getAnnotations(ont)) {

			OWLAnnotationProperty prop = annotation.getProperty();
			OWLAnnotationValue value = annotation.getValue();

			OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(prop, instance.getIRI(), value);
			axioms.add(ax);
		}
	}
	
	public static void extractAndCreateClassAssertions(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLNamedIndividual individual, OWLOntology ont) {

		for (OWLClassAssertionAxiom assertion : ont.getClassAssertionAxioms(individual)) {

			for (OWLClass concept : assertion.getClassesInSignature()) {

				OWLClassExpression clsExpression = getClassExpressions(concept, df, mergedEntitiesNewNames, iri);

				OWLClassAssertionAxiom classAssertionAx = df.getOWLClassAssertionAxiom(clsExpression, instance);
				axioms.add(classAssertionAx);
			}
		}
	}

	public static void extractAndCreateSameAsIndividualsOfIndividual(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLNamedIndividual individual, OWLOntology ont) {

		for (OWLIndividual sameIndiv : individual.getSameIndividuals(ont)) {

			OWLIndividual indiv = getIndividuals(sameIndiv, df, mergedEntitiesNewNames, iri);

			OWLSameIndividualAxiom sameIndivAx = df.getOWLSameIndividualAxiom(instance, indiv);
			axioms.add(sameIndivAx);
		}
	}

	public static void extractAndCreateDifferentIndividualsOfIndividual(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, Set<OWLAxiom> axioms, String iri, OWLDataFactory datafactory, OWLNamedIndividual indiv, OWLOntology ont) {

		Collection<OWLIndividual> differentIndiv = indiv.getDifferentIndividuals(ont);
		if(!differentIndiv.isEmpty()){
			Set<OWLIndividual> set = new HashSet<OWLIndividual>();

			for (OWLIndividual diffind : differentIndiv) {
				set.add(getIndividuals(diffind, datafactory, mergedEntitiesNewNames, iri));
			}
			set.add(instance);

			OWLDifferentIndividualsAxiom diffIndAx = datafactory.getOWLDifferentIndividualsAxiom(set);
			axioms.add(diffIndAx);
		}
	}

	public static void extractAndCreateObjectPropertyAssertionsOfIndividual(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individ, OWLOntology ont) {

		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> map = individ.getObjectPropertyValues(ont);
		for (Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty()) {

				OWLObjectPropertyExpression objectProp = getObjectPropertyExpressions(entry.getKey(), df, mergedEntitiesNewNames, iri);

				for (OWLIndividual indiv : entry.getValue()) {
					OWLIndividual individual = getIndividuals(indiv, df, mergedEntitiesNewNames, iri);

					OWLObjectPropertyAssertionAxiom propertyAssertion = df
							.getOWLObjectPropertyAssertionAxiom(objectProp, instance, individual);
					axioms.add(propertyAssertion);	
				}
			}
		}
	}

	public static void extractAndCreateNegativeObjectPropertyAssertionsOfIndividual(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individ, OWLOntology ont) {

		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> map = individ.getNegativeObjectPropertyValues(ont);
		for (Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty()) {

				OWLObjectPropertyExpression objectProp = getObjectPropertyExpressions(entry.getKey(), df, mergedEntitiesNewNames, iri);

				for (OWLIndividual indiv : entry.getValue()) {
					OWLIndividual individual = getIndividuals(indiv, df, mergedEntitiesNewNames, iri);

					OWLNegativeObjectPropertyAssertionAxiom propertyAssertion = df
							.getOWLNegativeObjectPropertyAssertionAxiom(objectProp, instance, individual);
					axioms.add(propertyAssertion);	
				}
			}
		}
	}

	public static void extractAndCreateDataPropertyAssertionsOfIndividual(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individual, OWLOntology ont) {

		Map<OWLDataPropertyExpression, Set<OWLLiteral>> map = individual.getDataPropertyValues(ont);
		for (Entry<OWLDataPropertyExpression, Set<OWLLiteral>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty()) {

				OWLDataPropertyExpression dataProp = getDataPropertyExpressions(entry.getKey(), df, mergedEntitiesNewNames, iri);

				for (OWLLiteral literal : entry.getValue()) {
					OWLDataPropertyAssertionAxiom dataPropertyAssertion = df
							.getOWLDataPropertyAssertionAxiom(dataProp, instance, literal);
					axioms.add(dataPropertyAssertion);
				}
			}
		}
	}

	public static void extractAndCreateNegativeDataPropertyAssertionsOfIndividual(OWLNamedIndividual instance, HashMap<String, String> mergedEntitiesNewNames, String iri, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individual, OWLOntology ont) {

		Map<OWLDataPropertyExpression, Set<OWLLiteral>> map = individual.getNegativeDataPropertyValues(ont);
		for (Entry<OWLDataPropertyExpression, Set<OWLLiteral>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty()) {

				OWLDataPropertyExpression dataProp = getDataPropertyExpressions(entry.getKey(), df, mergedEntitiesNewNames, iri);

				for (OWLLiteral literal : entry.getValue()) {
					OWLNegativeDataPropertyAssertionAxiom dataPropertyAssertion = df
							.getOWLNegativeDataPropertyAssertionAxiom(dataProp, instance, literal);
					axioms.add(dataPropertyAssertion);
				}
			}
		}
	}

	public static void createAxiomsOfParsedSubPropertyChainOfAxioms(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory datafactory, OWLOntology ontology_n, Set<OWLAxiom> axioms, String iri){
		
		Set<OWLSubPropertyChainOfAxiom> set = ontology_n.getAxioms(AxiomType.SUB_PROPERTY_CHAIN_OF);
		for(OWLSubPropertyChainOfAxiom chain : set){

			List<OWLObjectPropertyExpression> list = chain.getPropertyChain();
			OWLObjectPropertyExpression supObjProp = chain.getSuperProperty();

			List<OWLObjectPropertyExpression> modifiedList = new ArrayList<OWLObjectPropertyExpression>();
			for(OWLObjectPropertyExpression obExp : list){
				modifiedList.add(getObjectPropertyExpressions(obExp, datafactory, mergedEntitiesNewNames, iri));
			}

			if(chain.isAnnotated()){
				Set<OWLAnnotation> annotation = chain.getAnnotations();
				OWLSubPropertyChainOfAxiom ax = datafactory.getOWLSubPropertyChainOfAxiom(modifiedList, getObjectPropertyExpressions(supObjProp, datafactory, mergedEntitiesNewNames, iri), annotation);
				axioms.add(ax);
			}else{
				OWLSubPropertyChainOfAxiom ax = datafactory.getOWLSubPropertyChainOfAxiom(modifiedList, getObjectPropertyExpressions(supObjProp, datafactory, mergedEntitiesNewNames, iri));
				axioms.add(ax);
			}			
		}	
	}
	
	public static void createAxiomsOfParsedHasKeyAxioms(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory datafactory, OWLOntology ontology_n, Set<OWLAxiom> axioms, String iri){
		
		Set<OWLHasKeyAxiom> setKey = ontology_n.getAxioms(AxiomType.HAS_KEY);
		for(OWLHasKeyAxiom key : setKey){

			OWLClassExpression clsExp = key.getClassExpression();
			OWLClassExpression ModifiedClsExp = getClassExpressions(clsExp, datafactory, mergedEntitiesNewNames, iri);
			Set<OWLObjectPropertyExpression> objExpSet = key.getObjectPropertyExpressions();
			Set<OWLDataPropertyExpression> dataExpSet = key.getDataPropertyExpressions();

			
			Set<OWLObjectPropertyExpression> modifiedObjExpSet = new HashSet<OWLObjectPropertyExpression>();
			for(OWLObjectPropertyExpression objExp : objExpSet){
				modifiedObjExpSet.add(getObjectPropertyExpressions(objExp, datafactory, mergedEntitiesNewNames, iri));
			}
			Set<OWLDataPropertyExpression> modifiedDataExpSet = new HashSet<OWLDataPropertyExpression>();
			for(OWLDataPropertyExpression dataExp : dataExpSet){
				modifiedDataExpSet.add(getDataPropertyExpressions(dataExp, datafactory, mergedEntitiesNewNames, iri));
			}
			
			if(!key.isAnnotated()){
				OWLHasKeyAxiom haskey = datafactory.getOWLHasKeyAxiom(ModifiedClsExp, modifiedObjExpSet);
				OWLHasKeyAxiom haskeyy = datafactory.getOWLHasKeyAxiom(ModifiedClsExp, modifiedDataExpSet);
				axioms.add(haskey);
				axioms.add(haskeyy);
			}else{
				Set<OWLAnnotation> annotations = key.getAnnotations();
				OWLHasKeyAxiom haskey = datafactory.getOWLHasKeyAxiom(ModifiedClsExp, modifiedObjExpSet, annotations);
				OWLHasKeyAxiom haskeyy = datafactory.getOWLHasKeyAxiom(ModifiedClsExp, modifiedDataExpSet, annotations);
				axioms.add(haskey);
				axioms.add(haskeyy);
			}
		}
	}

	public static void createAxiomsOfParsedNonBuiltInAnnotationProperties(Set<OWLAxiom>axioms, OWLOntology ont, OWLDataFactory df){

		for (OWLAnnotationProperty annoProp : ont.getAnnotationPropertiesInSignature()){

			if(!annoProp.isBuiltIn()){
				OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(annoProp);
				axioms.add(ax);

				extractAndCreateAnnotationPropertyAnnotations(annoProp, axioms, ont, df);
				extractAndCreateSuperPropertiesOfAnnotationProperty(annoProp, axioms, df, ont);
				extractAndCreateAnnotationPropertyDomains(annoProp, axioms, df, ont);
				extractAndCreateAnnotationPropertyRanges(annoProp, axioms, df, ont);
			}
		}
	}

	public static void extractAndCreateAnnotationPropertyAnnotations(OWLAnnotationProperty prop, Set<OWLAxiom> axioms, OWLOntology ont,
			OWLDataFactory df) {

		for (OWLAnnotation annotation : prop.getAnnotations(ont)) {

			OWLAnnotationProperty AnnoProp = annotation.getProperty();
			OWLAnnotationValue value = annotation.getValue();
			
			OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(AnnoProp, prop.getIRI(), value);
			axioms.add(ax);
		}
	}
	
	public static void extractAndCreateSuperPropertiesOfAnnotationProperty(OWLAnnotationProperty annotationProperty, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont) {

		for (OWLAnnotationProperty superProp : annotationProperty.getSuperProperties(ont)) {

			OWLSubAnnotationPropertyOfAxiom ax = df.getOWLSubAnnotationPropertyOfAxiom(annotationProperty, superProp);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateAnnotationPropertyDomains(OWLAnnotationProperty prop, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLOntology ont) {

		for (OWLAnnotationPropertyDomainAxiom domain : ont.getAnnotationPropertyDomainAxioms(prop)) {

			OWLAnnotationPropertyDomainAxiom axiom = df.getOWLAnnotationPropertyDomainAxiom(prop, domain.getDomain());
			axioms.add(axiom);
		}
	}

	public static void extractAndCreateAnnotationPropertyRanges(OWLAnnotationProperty annotationProp, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLOntology ont) {

		for (OWLAnnotationPropertyRangeAxiom range : ont.getAnnotationPropertyRangeAxioms(annotationProp)) {

			OWLAnnotationPropertyDomainAxiom ax = df.getOWLAnnotationPropertyDomainAxiom(annotationProp, range.getRange());
			axioms.add(ax);
		}
	}

	public static void createAxiomsOfParsedDataTypes(Set<OWLAxiom>axioms, OWLOntology ont, OWLDataFactory df){

		for(OWLDatatype type : ont.getDatatypesInSignature()){

			OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(type);
			axioms.add(ax);
		}
	}

	public static void createAxiomsOfParsedAnonymousIndividuals(Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont) {

		for (OWLAnonymousIndividual individual : ont.getAnonymousIndividuals()) {
			for (OWLAnnotationAssertionAxiom assertion : ont.getAnnotationAssertionAxioms(individual)) {

				//OWLAnnotation anno = assertion.getAnnotation();
				OWLAnnotationProperty prop = assertion.getProperty();
				OWLAnnotationSubject sub = assertion.getSubject();
				OWLAnnotationValue value = assertion.getValue();
				Set<OWLAnnotation> annotations = assertion.getAnnotations();

				if(!assertion.isAnnotated()){
					OWLAnnotationAssertionAxiom x = df.getOWLAnnotationAssertionAxiom(prop, sub, value);
					//OWLAnnotationAssertionAxiom x = df.getOWLAnnotationAssertionAxiom(sub, anno);
					axioms.add(x);
				}else{
					OWLAnnotationAssertionAxiom x = df.getOWLAnnotationAssertionAxiom(prop, sub, value, annotations);
					//OWLAnnotationAssertionAxiom x = df.getOWLAnnotationAssertionAxiom(sub, anno, annotations);
					axioms.add(x);
				}
			}
		}
	}
	
	public static void createSubsumptionAndDisjointedBridgingAxiomsUsingOriginalAlignments(String alignmentFile, double threshold, String iri, HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, Set<OWLAxiom> axioms, HashSet<String> hClasses, HashSet<String> hObjProps, HashSet<String> hDataProps, HashSet<String> hInstances) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment al = parser.parse(new File(alignmentFile).toURI());
		al.cut(threshold);  //al.cut( "hard", threshold );

		Enumeration<Cell> enumeration = al.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();

			String entity1 = cell.getObject1AsURI().toString();
			String entity2 = cell.getObject2AsURI().toString();

			String relation = cell.getRelation().getRelation();

			if(relation.equals("<")){
				addSubsumptionBridgeAxioms(entity1, entity2, iri, mergedEntitiesNewNames, hClasses, hObjProps, hDataProps, hInstances, df, axioms);
			}else{
				if(relation.equals(">")){
					addSubsumptionBridgeAxioms(entity2, entity1, iri, mergedEntitiesNewNames, hClasses, hObjProps, hDataProps, hInstances, df, axioms);
				}else{
					if(relation.equals("%")){
						addDisjointedBridgeAxioms(entity1, entity2, iri, mergedEntitiesNewNames, hClasses, hObjProps, hDataProps, hInstances, df, axioms);
					}
				}
			}
		}
	}

	public static void addSubsumptionBridgeAxioms(String entity1, String entity2, String iri, HashMap<String, String> mergedEntitiesNewNames, HashSet<String> classes, HashSet<String> objProps, HashSet<String> dataProps, HashSet<String> instances, OWLDataFactory dfact, Set<OWLAxiom> axioms){

		if (classes.contains(entity1) && classes.contains(entity2)) {
			mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

			OWLClass clsA = dfact.getOWLClass(IRI.create(entity1));
			OWLClass clsB = dfact.getOWLClass(IRI.create(entity2));

			OWLSubClassOfAxiom axiom = dfact.getOWLSubClassOfAxiom(clsA, clsB);
			axioms.add(axiom);
		} else {

			if (objProps.contains(entity1) && objProps.contains(entity2)) {

				mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

				OWLObjectProperty prop1 = dfact.getOWLObjectProperty(IRI.create(entity1));
				OWLObjectProperty prop2 = dfact.getOWLObjectProperty(IRI.create(entity2));

				OWLSubObjectPropertyOfAxiom ax = dfact.getOWLSubObjectPropertyOfAxiom(prop1, prop2);
				axioms.add(ax);
			} else {

				if (dataProps.contains(entity1) && dataProps.contains(entity2)) {

					mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

					OWLDataProperty prop1 = dfact.getOWLDataProperty(IRI.create(entity1));
					OWLDataProperty prop2 = dfact.getOWLDataProperty(IRI.create(entity2));

					OWLSubDataPropertyOfAxiom ax = dfact.getOWLSubDataPropertyOfAxiom(prop1, prop2);
					axioms.add(ax);
				}
			}
		}
	}

	public static void mergedOrNot(String entity1, String entity2, HashMap<String, String> mergedEntitiesNewNames, String iri){

		if(mergedEntitiesNewNames.containsKey(entity1)){
			String c = mergedEntitiesNewNames.get(entity1);
			entity1 = iri + "#" + c;
		}
		if(mergedEntitiesNewNames.containsKey(entity2)){
			String c = mergedEntitiesNewNames.get(entity2);
			entity2 = iri + "#" + c;
		}	
	}

	public static void addDisjointedBridgeAxioms(String entity1, String entity2, String iri, HashMap<String, String> mergedEntitiesNewNames, HashSet<String> classes, HashSet<String> objProps, HashSet<String> dataProps, HashSet<String> instances, OWLDataFactory dfact, Set<OWLAxiom> axioms){

		if (classes.contains(entity1) && classes.contains(entity2)) {

			mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

			OWLClass clsA = dfact.getOWLClass(IRI.create(entity1));
			OWLClass clsB = dfact.getOWLClass(IRI.create(entity2));

			OWLDisjointClassesAxiom axiom = dfact.getOWLDisjointClassesAxiom(clsA, clsB);
			axioms.add(axiom);
		} else {

			if (objProps.contains(entity1) && objProps.contains(entity2)) {

				mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

				OWLObjectProperty prop1 = dfact.getOWLObjectProperty(IRI.create(entity1));
				OWLObjectProperty prop2 = dfact.getOWLObjectProperty(IRI.create(entity2));

				OWLDisjointObjectPropertiesAxiom ax = dfact.getOWLDisjointObjectPropertiesAxiom(prop1, prop2);
				axioms.add(ax);
			} else {

				if (dataProps.contains(entity1) && dataProps.contains(entity2)) {

					mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

					OWLDataProperty prop1 = dfact.getOWLDataProperty(IRI.create(entity1));
					OWLDataProperty prop2 = dfact.getOWLDataProperty(IRI.create(entity2));

					OWLDisjointDataPropertiesAxiom ax = dfact.getOWLDisjointDataPropertiesAxiom(prop1, prop2);
					axioms.add(ax);
				} else {

					if (instances.contains(entity1) && instances.contains(entity2)) {

						mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri);

						OWLNamedIndividual indiv1 = dfact.getOWLNamedIndividual(IRI.create(entity1));
						OWLNamedIndividual indiv2 = dfact.getOWLNamedIndividual(IRI.create(entity2));

						OWLDifferentIndividualsAxiom ax = dfact.getOWLDifferentIndividualsAxiom(indiv1, indiv2);
						axioms.add(ax);
					}
				}
			}
		}
	}

	public static void createSubsumptionAndDisjointedBridgingAxiomsUsingFilteredAlignments(String alignmentFile, double threshold, String iri, HashMap<String, String> bridge_entities_code, OWLDataFactory df, Set<OWLAxiom> axioms, HashSet<String> hClasses, HashSet<String> hObjProps, HashSet<String> hDataProps, HashSet<String> hInstances) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment al = parser.parse(new File(alignmentFile).toURI());
		al.cut(threshold); //al.cut( "hard", threshold );

		HashMap<String, String> is_a_Hash = new HashMap<String, String>();
		HashMap<String, String> is_a_HashConf = new HashMap<String, String>();
		HashMap<String, String> reverse_is_a_Hash = new HashMap<String, String>();
		HashMap<String, String> reverse_is_HashConf = new HashMap<String, String>();
		HashMap<String, String> disjHash = new HashMap<String, String>();
		HashMap<String, String> disjHashConf = new HashMap<String, String>();

		HashMap<String, String> is_a_Hash2 = new HashMap<String, String>();
		HashMap<String, String> is_a_HashConf2 = new HashMap<String, String>();
		HashMap<String, String> reverse_is_a_Hash2 = new HashMap<String, String>();
		HashMap<String, String> reverse_is_a_HashConf2 = new HashMap<String, String>();
		HashMap<String, String> disjHash2 = new HashMap<String, String>();
		HashMap<String, String> disjHashConf2 = new HashMap<String, String>();

		Enumeration<Cell> enumeration = al.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String entity1 = cell.getObject1AsURI().toString();
			String entity2 = cell.getObject2AsURI().toString();
			String relation = cell.getRelation().getRelation();

			filterIsACellsHavingSameSources(is_a_Hash, is_a_HashConf, cell, entity1, entity2, relation);
			filterReverseIsACellsHavingSameSources(reverse_is_a_Hash, reverse_is_HashConf, cell, entity1, entity2, relation);
			filterDisjunctionCellsHavingSameSources(disjHash, disjHashConf, cell, entity1, entity2, relation);
		}

		filterSubsumptionOrDisjunctionCellsHavingSameTargets(is_a_Hash, is_a_HashConf, is_a_Hash2, is_a_HashConf2);
		filterSubsumptionOrDisjunctionCellsHavingSameTargets(reverse_is_a_Hash, reverse_is_HashConf, reverse_is_a_Hash2, reverse_is_a_HashConf2);
		filterSubsumptionOrDisjunctionCellsHavingSameTargets(disjHash, disjHashConf, disjHash2, disjHashConf2);

		createSubsumptionOrDisjointedBridgingAxioms(iri, bridge_entities_code, df, axioms, hClasses, hObjProps, hDataProps, hInstances, is_a_Hash2, reverse_is_a_Hash2, disjHash2);
	}

	public static void filterIsACellsHavingSameSources(HashMap<String, String> is_a_Hash,
			HashMap<String, String> is_a_HashConf, Cell cell, String entity1, String entity2, String relation) {

		if(relation.equals("<")){
			filterCellsHavingSameSources(is_a_Hash, is_a_HashConf, cell, entity1, entity2);
		}
	}

	public static void filterCellsHavingSameSources(HashMap<String, String> is_a_Hash, HashMap<String, String> is_a_HashConf, Cell cell,
			String entity1, String entity2) {

		if ((!is_a_Hash.containsKey(entity1)) || (cell.getStrength() > Double.valueOf(is_a_HashConf.get(entity1)))) {
			is_a_Hash.put(entity1, entity2);
			is_a_HashConf.put(entity1, String.valueOf(cell.getStrength()));
		}
	}

	public static void filterReverseIsACellsHavingSameSources(HashMap<String, String> reverse_is_a_Hash,
			HashMap<String, String> reverse_is_HashConf, Cell cell, String entity1, String entity2, String relation) {

		if(relation.equals(">")){
			filterCellsHavingSameSources(reverse_is_a_Hash, reverse_is_HashConf, cell, entity1, entity2);
		}
	}

	public static void filterDisjunctionCellsHavingSameSources(HashMap<String, String> disjHash,
			HashMap<String, String> disjHashConf, Cell cell, String entity1, String entity2, String relation) {

		if(relation.equals("%")){
			filterCellsHavingSameSources(disjHash, disjHashConf, cell, entity1, entity2);
		}
	}

	public static void filterSubsumptionOrDisjunctionCellsHavingSameTargets(HashMap<String, String> is_a_Hash,
			HashMap<String, String> is_a_HashConf, HashMap<String, String> is_a_Hash2,
			HashMap<String, String> is_a_HashConf2) {

		for (Entry<String, String> entry : is_a_Hash.entrySet()) {
			String a = entry.getValue();
			String b = entry.getKey();

			filterCellsHavingSameTargets(is_a_HashConf, is_a_Hash2, is_a_HashConf2, a, b);	
		}
	}

	public static void filterCellsHavingSameTargets(HashMap<String, String> is_a_HashConf, HashMap<String, String> is_a_Hash2,
			HashMap<String, String> is_a_HashConf2, String a, String b) {

		if ((!is_a_Hash2.containsKey(a)) || (Double.valueOf(is_a_HashConf.get(b)) > Double.valueOf(is_a_HashConf2.get(a)))){
			is_a_Hash2.put(a, b);
			is_a_HashConf2.put(a, is_a_HashConf.get(b));
		}
	}

	public static void createSubsumptionOrDisjointedBridgingAxioms(String iri,
			HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, Set<OWLAxiom> axioms,
			HashSet<String> hClasses, HashSet<String> hObjProps, HashSet<String> hDataProps, HashSet<String> hInstances,
			HashMap<String, String> is_a_Hash2, HashMap<String, String> reverse_is_a_Hash2,
			HashMap<String, String> disjHash2) {

		for (Entry<String, String> entry : is_a_Hash2.entrySet()) {
			addSubsumptionBridgeAxioms(entry.getValue(), entry.getKey(), iri, mergedEntitiesNewNames, hClasses, hObjProps, hDataProps, hInstances, df, axioms);
		}

		for (Entry<String, String> entry : reverse_is_a_Hash2.entrySet()) {
			addSubsumptionBridgeAxioms(entry.getKey(), entry.getValue(), iri, mergedEntitiesNewNames, hClasses, hObjProps, hDataProps, hInstances, df, axioms);
		}

		for (Entry<String, String> entry : disjHash2.entrySet()) {
			addDisjointedBridgeAxioms(entry.getValue(), entry.getKey(), iri, mergedEntitiesNewNames, hClasses, hObjProps, hDataProps, hInstances, df, axioms);
		}
	}

	public static void checkNumberOfEntitiesOfMergedOntology(OWLOntology mergedOntology, ArrayList<OWLOntology> ontologiesSet, HashSet<String> redundantEntities) {
		System.out.println("");

		int nbClasses = getNumberOfClasses(mergedOntology);
		int classes = 0;
		for(OWLOntology ontology : ontologiesSet){
			classes = classes + getNumberOfClasses(ontology);
		}
		System.out.println("--> There are : " + nbClasses + " classes in the merged ontology");
		System.out.println("    ==> The number of classes in a bridge ontology would be "+ classes);


		int nbObjProps = getNumberOfObjectProperties(mergedOntology);
		int objectProperties = 0;
		for(OWLOntology ontology : ontologiesSet){
			objectProperties = objectProperties + getNumberOfObjectProperties(ontology);
		}
		System.out.println("--> There are : " + nbObjProps + " object properties in the merged ontology");
		System.out.println("    ==> The number of object properties in a bridge ontology would be "+ objectProperties);


		int nbDataProperties = getNumberOfDataProperties(mergedOntology);
		int dataProperties = 0;
		for(OWLOntology ontology : ontologiesSet){
			dataProperties = dataProperties + getNumberOfDataProperties(ontology);
		}
		System.out.println("--> There are : " + nbDataProperties + " data properties in the merged ontology");
		System.out.println("    ==> The number of data properties in a bridge ontology would be "+ dataProperties);


		int nbIndividuals = getNumberOfIndividuals(mergedOntology);
		int individuals = 0;
		for(OWLOntology ontology : ontologiesSet){
			individuals = individuals + getNumberOfIndividuals(ontology);
		}
		System.out.println("--> There are : " + nbIndividuals + " individuals in the merged ontology");
		System.out.println("    ==> The number of individuals in a bridge ontology would be "+ individuals);


		int nbAxioms = mergedOntology.getLogicalAxiomCount();
		int predictedNumberOfAxioms = 0;
		for(OWLOntology ontology : ontologiesSet){
			predictedNumberOfAxioms = predictedNumberOfAxioms + ontology.getLogicalAxiomCount();
		}
		System.out.println("--> There are : " + nbAxioms + " axioms in the merged ontology");
		System.out.println("    ==> The number of logical axioms in a bridge ontology would be "+ predictedNumberOfAxioms);
	
		System.out.println("--> There are " + redundantEntities.size() + " redundant classes (that are not merged)");
	}


	public static int getNumberOfClasses(OWLOntology mergedOntology){
		int nbClasses=0;
		for (OWLClass cl : mergedOntology.getClassesInSignature()){
			if(!cl.isOWLThing()){
				//System.out.println(nbClasses + "/. " + cl.getIRI().getFragment());
				nbClasses++;
			}
		}
		return nbClasses;
	}

	public static int getNumberOfObjectProperties(OWLOntology mergedOntology){
		int nbObjProps=0;
		for (OWLObjectProperty prop : mergedOntology.getObjectPropertiesInSignature()){
			//System.out.println(nbObjProps + "/. " + prop.getIRI().getFragment());
			nbObjProps++;
		}
		return nbObjProps;
	}

	public static int getNumberOfDataProperties(OWLOntology mergedOntology){
		int nbDataProperties = 0;
		for(OWLDataProperty prop : mergedOntology.getDataPropertiesInSignature()){
			//System.out.println(nbDataProperties + "/. " + prop);
			nbDataProperties++;
		}
		return nbDataProperties;
	}

	public static int getNumberOfIndividuals(OWLOntology mergedOntology){
		int nbIndividuals = 0;
		for (OWLNamedIndividual instance : mergedOntology.getIndividualsInSignature()){
			//System.out.println(nbIndividuals + "/. " + instance);
			nbIndividuals++;
		}
		return nbIndividuals;
	}



}
