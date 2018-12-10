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
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
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
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import fr.inrialpes.exmo.align.parser.AlignmentParser;


public class MissingMergingWithRefactoring {


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
		ontologiesFiles.add("Data/cmt.owl");
		ontologiesFiles.add("Data/Conference.owl");
		ontologiesFiles.add("Data/confOf.owl");
		ontologiesFiles.add("Data/edas.owl");
		ontologiesFiles.add("Data/ekaw.owl");
		ontologiesFiles.add("Data/iasted.owl");
		ontologiesFiles.add("Data/sigkdd.owl");

		/** Anatomy base */
		//ontologiesFiles.add("Data/human.owl");
		//ontologiesFiles.add("Data/mouse.owl");

		/** LargeBio base */
//		ontologiesFiles.add("Data/FMA3.owl");   /** whole FMA */
//		ontologiesFiles.add("Data/NCI3.owl");   /** whole NCI */
//		ontologiesFiles.add("Data/SNOMED3.owl");   /** extended SNOMED */


		for(int g=0; g<ontologiesFiles.size(); g++){
			ontologiesSet.add(manager.loadOntologyFromOntologyDocument(new File(ontologiesFiles.get(g))));
		}

		/******************************************************************************************************************************************************************/

		/** selecting and entering input reference alignments to be used in the merge process */

		double threshold = 0.0;
		ArrayList<String> alignmentsFiles = new ArrayList<String>();

		/** Conference base */
		alignmentsFiles.add("Data/cmt-conference.rdf");
		alignmentsFiles.add("Data/cmt-confOf.rdf");
		alignmentsFiles.add("Data/cmt-edas.rdf");
		alignmentsFiles.add("Data/cmt-ekaw.rdf");
		alignmentsFiles.add("Data/cmt-iasted.rdf");
		alignmentsFiles.add("Data/cmt-sigkdd.rdf");
		alignmentsFiles.add("Data/conference-confOf.rdf");
		alignmentsFiles.add("Data/conference-edas.rdf");
		alignmentsFiles.add("Data/conference-ekaw.rdf");
		alignmentsFiles.add("Data/conference-iasted.rdf");
		alignmentsFiles.add("Data/conference-sigkdd.rdf");
		alignmentsFiles.add("Data/confOf-edas.rdf");
		alignmentsFiles.add("Data/confOf-ekaw.rdf");
		alignmentsFiles.add("Data/confOf-iasted.rdf");
		alignmentsFiles.add("Data/confOf-sigkdd.rdf");
		alignmentsFiles.add("Data/edas-ekaw.rdf");
		alignmentsFiles.add("Data/edas-iasted.rdf");
		alignmentsFiles.add("Data/edas-sigkdd.rdf");
		alignmentsFiles.add("Data/ekaw-iasted.rdf");
		alignmentsFiles.add("Data/ekaw-sigkdd.rdf");
		alignmentsFiles.add("Data/iasted-sigkdd.rdf");

		/** Anatomy base */
		//alignmentsFiles.add("Data/human-mouse.rdf");

		/** Largebio base */
//		alignmentsFiles.add("Data/FMA2NCI.rdf");
//		alignmentsFiles.add("Data/FMA2SNOMED.rdf");
//		alignmentsFiles.add("Data/SNOMED2NCI.rdf");

		System.out.println("==> Step 1 is done");

		/**********************************************************************************************************************************************/		

		/** choose one of the four following methods (if you uncomment one, comment the three others, and vice versa) */

		HashMap<String, String> hash_entitiesMergedNames = new HashMap<String, String>();
		HashMap<String, HashSet<String>> hash_mergedNamesEntities = new HashMap<String, HashSet<String>>();

		int codeCounter = 0;
		for (String alignment : alignmentsFiles) {
			//codeCounter = getMergedNamesForEquivalentEntitiesUsingOriginalAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter);
			//codeCounter = getMergedNamesForEquivalentEntitiesUsingRepairedAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter);
			//codeCounter = getMergedNamesForEquivalentEntitiesUsingFilteredAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter);
			codeCounter = getMergedNamesForEquivalentEntitiesUsingRepairedAndFilteredAlignments(alignment, threshold, hash_entitiesMergedNames, hash_mergedNamesEntities, codeCounter);
		}

		System.out.println("==> Step 2 and 3 are done");

		/***********************************************************************************************************************************************/

        /* Ontology entities can be classes/concepts, object properties, data type properties, individuals/instances,
         * annotation properties, data types, and anonymous individuals.
         */
		/** While axioms creation, each (to be merged) entity would have its unique code number as merged name */
	
		String mergedOntologyIRI = "http://merging";
		OWLOntology mergedOntology = manager.createOntology(IRI.create(mergedOntologyIRI));
		Set<OWLAxiom> mergedOntologyAxioms = new HashSet<OWLAxiom>();

		HashMap<String, String> classes = new HashMap<String, String>();
		HashMap<String, String> objectProps = new HashMap<String, String>();
		HashMap<String, String> dataProps = new HashMap<String, String>();
		HashMap<String, String> instances = new HashMap<String, String>();

		int ontologyCounter = 0;
		
		for(OWLOntology ontology_n : ontologiesSet){
			ontologyCounter++;
			String ontoCounter = String.valueOf(ontologyCounter);

			createAxiomsOfParsedClasses(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, ontoCounter, classes);
			createAxiomsOfParsedObjectProperties(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, ontoCounter, objectProps);
			createAxiomsOfParsedDataProperties(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, ontoCounter, dataProps);
			createAxiomsOfParsedIndividuals(hash_entitiesMergedNames, datafactory, ontology_n, mergedOntologyAxioms, mergedOntologyIRI, ontoCounter, instances);
		
			/** these three following methods can be commented, because alignments don't map annotation Properties,
			 * Data Types, or anonymous individuals. We just put them to preserve all ontologies knowledge (axioms).
			 */
			
			createAxiomsOfParsedNonBuiltInAnnotationProperties(mergedOntologyAxioms, ontology_n, datafactory);
			//createAxiomsOfParsedDataTypes(mergedOntologyAxioms, ontology_n, datafactory);
			createAxiomsOfParsedAnonymousIndividuals(mergedOntologyAxioms, datafactory, ontology_n);
		}

		System.out.println("==> Step 4 is done");

		/***********************************************************************************************************************************************/

		/** choose either "createBridgingAxiomsUsingOriginalAlignments" method, or
		 * "createBridgingAxiomsUsingFilteredAlignments" method. (if you uncomment one, comment the other, and vice versa).
		 */

		for (String alignment : alignmentsFiles) {
			//createSubsumptionAndDisjointedBridgingAxiomsUsingOriginalAlignments(alignment, threshold, mergedOntologyIRI, hash_entitiesMergedNames, datafactory, mergedOntologyAxioms, classes, objectProps, dataProps, instances);
			createSubsumptionAndDisjointedBridgingAxiomsUsingFilteredAlignments(alignment, threshold, mergedOntologyIRI, hash_entitiesMergedNames, datafactory, mergedOntologyAxioms, classes, objectProps, dataProps, instances);
		}

		System.out.println("==> Step 5 is done");

		/***********************************************************************************************************************************************/

		manager.addAxioms(mergedOntology, mergedOntologyAxioms);
		
		manager.saveOntology(mergedOntology, new RDFXMLOntologyFormat(), IRI.create(new File("Results/MissingMergedOntology_WithRefact.owl")));
//		manager.saveOntology(mergedOntology, new OWLXMLOntologyFormat(), IRI.create(new File("Results/MissingMergedOntology_WithRefact.owl")));
//      manager.saveOntology(mergedOntology, new OWLFunctionalSyntaxOntologyFormat(), IRI.create(new File("Results/MissingMergedOntology_WithRefact.owl")));	
//		manager.addOntologyStorer(new DLSyntaxOntologyStorer());
//		manager.saveOntology(mergedOntology, new DLSyntaxOntologyFormat(), IRI.create(new File("Results/MissingMergedOntology_WithRefact.owl")));
//		manager.addOntologyStorer(new OWLTutorialSyntaxOntologyStorer());
//		manager.saveOntology(mergedOntology, new OWLTutorialSyntaxOntologyFormat(), IRI.create(new File("Results/MissingMergedOntology_WithRefact.owl")));
//		manager.saveOntology(mergedOntology, new TurtleOntologyFormat(), IRI.create(new File("Results/MissingMergedOntology_WithRefact.owl")));
		
		System.out.println("==> Step 6 is done : The merged ontology is created\n");


		long chrono2 = bean.getCurrentThreadCpuTime();
		long runtime = chrono2-chrono1;

		System.out.println("\n ****** Running the program took : "+runtime/1E6+" ms"); // CPU runtime in milliseconds
		System.out.println(" ****** Running the program took : "+runtime/1E9+" s\n"); // CPU runtime in seconds		

		/***********************************************************************************************************************************************/

		checkNumberOfEntitiesOfMergedOntology(mergedOntology, ontologiesSet);
		OntologyConsistencyAndCoherence.verify(mergedOntology);


		System.clearProperty("jdk.xml.entityExpansionLimit");

	}


	
	public static int getMergedNamesForEquivalentEntitiesUsingOriginalAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment al = parser.parse(new File(alignmentFile).toURI());
		al.cut(threshold);  //al.cut( "hard", threshold );

		Enumeration<Cell> enumeration = al.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=") || relation.equals("?")){

				String entity1 = cell.getObject1AsURI().toString();
				String entity2 = cell.getObject2AsURI().toString();

				k++;
				String name = "Code_"+String.valueOf(k);

				getEntitiesMergedNames(entity1, entity2, name, hash1, hash2);
			}
		}
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
				}else{
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

	
	public static int getMergedNamesForEquivalentEntitiesUsingRepairedAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment al = parser.parse(new File(alignmentFile).toURI());
		al.cut(threshold); //al.cut( "hard", threshold );

		Enumeration<Cell> enumeration = al.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=")){

				String entity1 = cell.getObject1AsURI().toString();
				String entity2 = cell.getObject2AsURI().toString();

				k++;
				String name = "Code_"+String.valueOf(k);

				getEntitiesMergedNames(entity1, entity2, name, hash1, hash2);
			}
		}
		return k;
	}
	
	
	public static int getMergedNamesForEquivalentEntitiesUsingFilteredAlignments(String alignmentFile, double threshold, HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int k) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment al = parser.parse(new File(alignmentFile).toURI());
		al.cut(threshold);  //al.cut( "hard", threshold );

		HashMap<String, HashSet<String>> sourcesFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> sourceFilteringHashConf = new HashMap<String, String>();
		HashMap<String, HashSet<String>> targetFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> targetFilteringHashConf = new HashMap<String, String>();

		Enumeration<Cell> enumeration = al.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=") || relation.equals("?")){

				String entity1 = cell.getObject1AsURI().toString();
				String entity2 = cell.getObject2AsURI().toString();

				filterEquivalentCellsHavingSameSources(sourcesFilteringHash, sourceFilteringHashConf, cell, entity1, entity2);
			}
		}

		filterEquivalentCellsHavingSameTargets(sourcesFilteringHash, sourceFilteringHashConf, targetFilteringHash, targetFilteringHashConf);
		k = mergeEquivalentNames(hash1, hash2, k, targetFilteringHash);

		return k;
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
	
	
	public static int mergeEquivalentNames(HashMap<String, String> hash1, HashMap<String, HashSet<String>> hash2, int uniqueNumber,
			HashMap<String, HashSet<String>> targetFilteringHash) {
		
		for (Entry<String, HashSet<String>> entry : targetFilteringHash.entrySet()) {
			String entity2 = entry.getKey();

			for(String entity1 : entry.getValue()){
				uniqueNumber++;
				String name = "Code_"+String.valueOf(uniqueNumber);

				getEntitiesMergedNames(entity1, entity2, name, hash1, hash2);
			}
		}
		return uniqueNumber;
	}
	
	
	public static int getMergedNamesForEquivalentEntitiesUsingRepairedAndFilteredAlignments(String alignmentFile, double threshold, HashMap<String, String> hash_un, HashMap<String, HashSet<String>> hash_deux, int k) throws AlignmentException{

		AlignmentParser parser = new AlignmentParser(0);
		Alignment al = parser.parse(new File(alignmentFile).toURI());
		al.cut(threshold);  //al.cut( "hard", threshold );

		HashMap<String, HashSet<String>> sourcesFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> sourceFilteringHashConf = new HashMap<String, String>();
		HashMap<String, HashSet<String>> targetFilteringHash = new HashMap<String, HashSet<String>>();
		HashMap<String, String> targetFilteringHashConf = new HashMap<String, String>();

		Enumeration<Cell> enumeration = al.getElements();
		while (enumeration.hasMoreElements()) {
			Cell cell = enumeration.nextElement();
			String relation = cell.getRelation().getRelation();

			if(relation.equals("=")){

				String entity1 = cell.getObject1AsURI().toString();
				String entity2 = cell.getObject2AsURI().toString();

				filterEquivalentCellsHavingSameSources(sourcesFilteringHash, sourceFilteringHashConf, cell, entity1, entity2);
			}
		}

		filterEquivalentCellsHavingSameTargets(sourcesFilteringHash, sourceFilteringHashConf, targetFilteringHash, targetFilteringHashConf);
		k = mergeEquivalentNames(hash_un, hash_deux, k, targetFilteringHash);

		return k;
	}
	

	public static void createAxiomsOfParsedClasses(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, String ontologyNumber, HashMap<String, String> H_Classes) throws OWLException{

		for (OWLClass cls : ont.getClassesInSignature()) {
			if (!cls.isOWLThing()) {
				String ch = cls.getIRI().toString();
				H_Classes.put(ch, ontologyNumber);
				String number;
				OWLClass clsA;

				if(mergedEntitiesNewNames.containsKey(ch)){
					number = "/000";
					String c = mergedEntitiesNewNames.get(ch);

					clsA = df.getOWLClass(IRI.create(iri + number + "#" + c));
					//clsA = df.getOWLClass(IRI.create(iri + number + "#", cls.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(clsA);
					axioms.add(ax);

					addLabelForMergedEntity(ch, df, clsA, axioms, ontologyNumber);

				}else{
					number = entityID(ontologyNumber);

					int index = ch.indexOf("#", 0);
					String c = ch.substring((index + 1));

					clsA = df.getOWLClass(IRI.create(iri + number + "#" + c));
					//clsA = df.getOWLClass(IRI.create(iri + number + "#", cls.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(clsA);
					axioms.add(ax);
				}

				number = entityID(ontologyNumber);

				extractAndCreateClassLabels(axioms, clsA, cls, ont, df);
				extractAndCreateClassComments(axioms, clsA, cls, ont, df);
				extractAndCreateSuperClassesOfClass(mergedEntitiesNewNames, iri, number, axioms, clsA, df, ont, cls);
				extractAndCreateEquivalentClassesOfClass(mergedEntitiesNewNames, iri, number, df, clsA, axioms, cls, ont);
				extractAndCreateDisjointClassesOfClass(mergedEntitiesNewNames, clsA, axioms, iri, number, df, cls, ont);
				extractAndCreateNonBuiltInClassAnnotations(clsA, axioms, df, cls, ont);

				//} else {
				//OWLClass clsA = df.getOWLThing();
				//OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(clsA);
				//axioms.add(ax);
				//}
			}
		}
	}

	
	public static void addLabelForMergedEntity(String ch, OWLDataFactory df, OWLEntity entity, Set<OWLAxiom> axioms, String num){

		int index = ch.indexOf("#", 0);
		String c = ch.substring((index + 1));
		OWLAnnotation label = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(num + ". " + c));
		OWLAxiom ax = df.getOWLAnnotationAssertionAxiom(entity.getIRI(), label);
		axioms.add(ax);
	}
	
	
	public static String entityID(String ontologyNumber) {
		if(ontologyNumber.length()==1){
			ontologyNumber = "/00" + ontologyNumber;
		}
		else{
			if(ontologyNumber.length()==2){
				ontologyNumber = "/0" + ontologyNumber;
			}else{
				if(ontologyNumber.length() == 3){
					ontologyNumber = "/" + ontologyNumber;
				}
			}
		}
		return ontologyNumber;
	}
	
	
	public static void extractAndCreateClassLabels(Set<OWLAxiom> axioms, OWLClass cls, OWLClass concept, OWLOntology ont,
			OWLDataFactory datafact) {

		for (OWLAnnotation annotation : concept.getAnnotations(ont, datafact.getRDFSLabel())) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = datafact.getOWLAnnotation(datafact.getRDFSLabel(),
							datafact.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = datafact.getOWLAnnotation(datafact.getRDFSLabel(),
							datafact.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(cls.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = datafact.getOWLAnnotation(datafact.getRDFSLabel(), val);

					OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(cls.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateClassComments(Set<OWLAxiom> axioms, OWLClass cls, OWLClass concept, OWLOntology ont,
			OWLDataFactory datafact) {

		for (OWLAnnotation annot : concept.getAnnotations(ont, datafact.getRDFSComment())) {
			if (annot.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annot.getValue();
				OWLAnnotation annotation;

				if (val.hasLang()) {
					annotation = datafact.getOWLAnnotation(datafact.getRDFSComment(),
							datafact.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					annotation = datafact.getOWLAnnotation(datafact.getRDFSComment(),
							datafact.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}
				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(cls.getIRI(), annotation);
				axioms.add(ax);
			}
			else{
				if (annot.getValue() instanceof IRI) {
					IRI val = (IRI) annot.getValue();
					OWLAnnotation annotation = datafact.getOWLAnnotation(datafact.getRDFSComment(), val);

					OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(cls.getIRI(), annotation);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateSuperClassesOfClass(HashMap<String, String> mergedEntitiesNewNames, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLClass cls, OWLDataFactory df, OWLOntology ont,
			OWLClass concept) {

		for (OWLClassExpression classExpression : concept.getSuperClasses(ont)) {
			String number = ontologyNumber;

			if (!classExpression.isAnonymous()) {
				String sup = classExpression.asOWLClass().getIRI().toString();
				int index = sup.indexOf("#", 0);
				String c = sup.substring((index + 1));
				OWLClass clsB;

				if(mergedEntitiesNewNames.containsKey(sup)){
					number = "/000";
					c = mergedEntitiesNewNames.get(sup);
				}

				if (!c.equals("Thing")) {
					clsB = df.getOWLClass(IRI.create(iri + number + "#" + c));
					//clsB = df.getOWLClass(IRI.create(iri + number + "#" + classExpression.asOWLClass().getIRI().getFragment()));
				} else {
					clsB = df.getOWLThing();
				}

				OWLSubClassOfAxiom axiom = df.getOWLSubClassOfAxiom(cls, clsB);
				axioms.add(axiom);
			}
		}
	}

	
	public static void extractAndCreateEquivalentClassesOfClass(HashMap<String, String> mergedEntitiesNewNames, String iri, String ontologyNumber, OWLDataFactory df, OWLClass clsA, Set<OWLAxiom> axioms, OWLClass concept, OWLOntology ont) {

		for (OWLClassExpression equiv : concept.getEquivalentClasses(ont)) {
			String number = ontologyNumber;

			if (!equiv.isAnonymous()) {
				String eq = equiv.asOWLClass().getIRI().toString();
				int index = eq.indexOf("#", 0);
				String c = eq.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(eq)){
					number = "/000";
					c = mergedEntitiesNewNames.get(eq);
				}

				OWLClass clsB = df.getOWLClass(IRI.create(iri + number + "#" + c));
				//OWLClass clsB = df.getOWLClass(IRI.create(iri + number + "#" + equiv.asOWLClass().getIRI().getFragment()));

				OWLEquivalentClassesAxiom axiom = df.getOWLEquivalentClassesAxiom(clsA, clsB);
				axioms.add(axiom);
			}
		}
	}
	
	
	public static void extractAndCreateDisjointClassesOfClass(HashMap<String, String> mergedEntitiesNewNames, OWLClass cls, Set<OWLAxiom> axioms, String iri, String ontologyNumber, OWLDataFactory df, OWLClass concept, OWLOntology ont) {

		for (OWLClassExpression disj : concept.getDisjointClasses(ont)) {
			String number = ontologyNumber;

			if (!disj.isAnonymous()) {
				String dis = disj.asOWLClass().getIRI().toString();
				int index = dis.indexOf("#", 0);
				String c = dis.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(dis)){
					number = "/000";
					c = mergedEntitiesNewNames.get(dis);
				}

				OWLClass clsB = df.getOWLClass(IRI.create(iri + number + "#" + c));
				//OWLClass clsB = df.getOWLClass(IRI.create(iri + number + "#" + disj.asOWLClass().getIRI().getFragment()));

				OWLDisjointClassesAxiom axiom = df.getOWLDisjointClassesAxiom(cls, clsB);
				axioms.add(axiom);		
			}
		}
	}
	
	
	public static void extractAndCreateNonBuiltInClassAnnotations(OWLClass clsA, Set<OWLAxiom> axioms, OWLDataFactory datafact, OWLClass concept,
			OWLOntology ont) {

		for (OWLAnnotation anno : concept.getAnnotations(ont)) {

			if (!anno.getProperty().isBuiltIn()) {
				OWLAnnotationProperty annotationProp = datafact.getOWLAnnotationProperty(anno.getProperty().getIRI());
				OWLAnnotation annotation = datafact.getOWLAnnotation(annotationProp, anno.getValue());

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(clsA.getIRI(), annotation);
				axioms.add(ax);
			}
		}
	}
	
	
	public static void createAxiomsOfParsedObjectProperties(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, String ontologyNumber, HashMap<String, String> hashObjProp) throws OWLException{

		for (OWLObjectProperty objProperty : ont.getObjectPropertiesInSignature()) {

			if (!objProperty.isOWLTopObjectProperty()) {
				String ch = objProperty.getIRI().toString();
				hashObjProp.put(ch, ontologyNumber);
				String number;
				OWLObjectProperty objectProp;

				if(mergedEntitiesNewNames.containsKey(ch)){
					number = "/000";
					String c = mergedEntitiesNewNames.get(ch);

					objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
					//objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#", objProperty.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(objectProp);
					axioms.add(ax);

					addLabelForMergedEntity(ch, df, objectProp, axioms, ontologyNumber);

				}else{
					number = entityID(ontologyNumber);
					int index = ch.indexOf("#", 0);
					String c = ch.substring((index + 1));

					objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
					//objectProp = df.getOWLObjectProperty(IRI.create(iri + numéro + "#", objProperty.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(objectProp);
					axioms.add(ax);
				}

				number = entityID(ontologyNumber);

				extractAndCreateObjectPropertyLabels(axioms, objectProp, ont, df, objProperty);
				extractAndCreateObjectPropertyComments(axioms, objectProp, ont, df, objProperty);
				extractAndCreateObjectPropertyDomains(mergedEntitiesNewNames, objectProp, iri, number, axioms, df, ont, objProperty);
				extractAndCreateObjectPropertyRanges(mergedEntitiesNewNames, objectProp, iri, number, axioms, df, ont, objProperty);
				extractAndCreateObjectPropertyTypes(objectProp, axioms, df, ont, objProperty);
				extractAndCreateSuperPropertiesOfObjectProperty(mergedEntitiesNewNames, iri, number, axioms, objectProp, df, ont, objProperty);
				extractAndCreateInversePropertiesOfObjectProperty(mergedEntitiesNewNames, iri, number, axioms, objectProp, df, ont, objProperty);
				extractAndCreateDisjointPropertiesOfObjectProperty(mergedEntitiesNewNames, iri, number, axioms, objectProp, df, ont, objProperty);
				extractAndCreateEquivalentPropertiesOfObjectProperty(mergedEntitiesNewNames, iri, number, axioms, objectProp, df, ont, objProperty);
				extractAndCreateNonBuiltInObjectPropertyAnnotations(objectProp, axioms, df, objProperty, ont);

				//} else {
				//OWLObjectProperty objectProp = df.getOWLTopObjectProperty();
				//OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(objectProp);
				//axioms.add(ax);
				//}
			}
		}
	}
	
	
	public static void extractAndCreateObjectPropertyLabels(Set<OWLAxiom> axioms, OWLObjectProperty objProp, OWLOntology ont,
			OWLDataFactory datafact, OWLObjectProperty objectProp) {

		for (OWLAnnotation annotation : objectProp.getAnnotations(ont, datafact.getRDFSLabel())) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = datafact.getOWLAnnotation(datafact.getRDFSLabel(),
							datafact.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = datafact.getOWLAnnotation(datafact.getRDFSLabel(),
							datafact.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(objProp.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = datafact.getOWLAnnotation(datafact.getRDFSLabel(), val);

					OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(objProp.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateObjectPropertyComments(Set<OWLAxiom> axioms, OWLObjectProperty objProp, OWLOntology ont, OWLDataFactory datafact, OWLObjectProperty obj) {

		for (OWLAnnotation annot : obj.getAnnotations(ont, datafact.getRDFSComment())) {
			if (annot.getValue() instanceof OWLLiteral) {

				OWLLiteral val = (OWLLiteral) annot.getValue();
				OWLAnnotation annotation;

				if (val.hasLang()) {
					annotation = datafact.getOWLAnnotation(datafact.getRDFSComment(),
							datafact.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					annotation = datafact.getOWLAnnotation(datafact.getRDFSComment(),
							datafact.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(objProp.getIRI(), annotation);
				axioms.add(ax);
			}
			else{
				if (annot.getValue() instanceof IRI) {

					IRI val = (IRI) annot.getValue();
					OWLAnnotation annotation = datafact.getOWLAnnotation(datafact.getRDFSComment(), val);

					OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(objProp.getIRI(), annotation);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateObjectPropertyDomains(HashMap<String, String> mergedEntitiesNewNames, OWLObjectProperty prop_o, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLClassExpression clas : obj.getDomains(ont)) {
			String number = ontologyNumber;

			if (!clas.isAnonymous()) {
				String dom = clas.asOWLClass().getIRI().toString();
				int index = dom.indexOf("#", 0);
				String cc = dom.substring((index + 1));

				OWLClass cls;

				if(mergedEntitiesNewNames.containsKey(dom)){
					number = "/000";
					cc = mergedEntitiesNewNames.get(dom);
				}

				if (!cc.equals("Thing")) {
					cls = df.getOWLClass(IRI.create(iri + number + "#" + cc));
					//cls = df.getOWLClass(IRI.create(iri + number + "#" + clas.asOWLClass().getIRI().getFragment()));
				} else {
					cls = df.getOWLThing();
				}

				OWLObjectPropertyDomainAxiom ax = df.getOWLObjectPropertyDomainAxiom(prop_o, cls);
				axioms.add(ax);				
			}
		}
	}

	
	public static void extractAndCreateObjectPropertyRanges(HashMap<String, String> mergedEntitiesNewNames, OWLObjectProperty objProp, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLClassExpression classExp : obj.getRanges(ont)) {
			String number = ontologyNumber;

			if (!classExp.isAnonymous()) {

				String img = classExp.asOWLClass().getIRI().toString();
				int index = img.indexOf("#", 0);
				String c = img.substring((index + 1));

				OWLClass cls2;

				if(mergedEntitiesNewNames.containsKey(img)){
					number = "/000";
					c = mergedEntitiesNewNames.get(img);
				}

				if (!c.equals("Thing")) {
					cls2 = df.getOWLClass(IRI.create(iri+ number + "#" + c));
					//cls2 = df.getOWLClass(IRI.create(iri+ number + "#" + classExp.asOWLClass().getIRI().getFragment()));
				} else {
					cls2 = df.getOWLThing();
				}

				OWLObjectPropertyRangeAxiom ax = df.getOWLObjectPropertyRangeAxiom(objProp, cls2);
				axioms.add(ax);
			}
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

	
	public static void extractAndCreateSuperPropertiesOfObjectProperty(HashMap<String, String> mergedEntitiesNewNames, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLObjectProperty objProp, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLObjectPropertyExpression superProp : obj.getSuperProperties(ont)) {
			String number = ontologyNumber;

			if(!superProp.isAnonymous())	{			
				String sp = superProp.getNamedProperty().getIRI().toString();
				int index = sp.indexOf("#", 0);
				String c = sp.substring((index + 1));

				OWLObjectProperty prop;

				if(mergedEntitiesNewNames.containsKey(sp)){
					number = "/000";
					c = mergedEntitiesNewNames.get(sp);
				}

				if (!c.equals("topObjectProperty")) {
					prop = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
					//prop = df.getOWLObjectProperty(IRI.create(iri + number + "#" + superProp.getNamedProperty().getIRI().getFragment()));
				} else {
					prop = df.getOWLTopObjectProperty();
				}

				OWLSubObjectPropertyOfAxiom ax = df.getOWLSubObjectPropertyOfAxiom(objProp, prop);
				axioms.add(ax);
			}
		}
	}

	
	public static void extractAndCreateInversePropertiesOfObjectProperty(HashMap<String, String> mergedEntitiesNewNames, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLObjectProperty objProp, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty obj) {

		for (OWLObjectPropertyExpression invProp : obj.getInverses(ont)) {
			String number = ontologyNumber;

			if(!invProp.isAnonymous()){
				String inv = invProp.getNamedProperty().getIRI().toString();
				int index = inv.indexOf("#", 0);
				String c = inv.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(inv)){
					number = "/000";
					c = mergedEntitiesNewNames.get(inv);
				}

				OWLObjectProperty prop = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
				//OWLObjectProperty prop = df.getOWLObjectProperty(IRI.create(iri + number + "#" + invProp.getNamedProperty().getIRI().getFragment()));

				OWLInverseObjectPropertiesAxiom ax = df.getOWLInverseObjectPropertiesAxiom(objProp, prop);
				axioms.add(ax);			
			}
		}
	}
	
	
	public static void extractAndCreateDisjointPropertiesOfObjectProperty(HashMap<String, String> mergedEntitiesNewNames, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLObjectProperty objProp, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty prop) {

		for (OWLObjectPropertyExpression propExpression : prop.getDisjointProperties(ont)) {
			String number = ontologyNumber;

			if(!propExpression.isAnonymous()){
				String disj = propExpression.getNamedProperty().getIRI().toString();
				int index = disj.indexOf("#", 0);
				String c = disj.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(disj)){
					number = "/000";
					c = mergedEntitiesNewNames.get(disj);
				}

				OWLObjectProperty oProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
				//OWLObjectProperty oProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + propExpression.getNamedProperty().getIRI().getFragment()));

				OWLDisjointObjectPropertiesAxiom disjax = df.getOWLDisjointObjectPropertiesAxiom(objProp, oProp);
				axioms.add(disjax);	
			}
		}
	}
	
	
	public static void extractAndCreateEquivalentPropertiesOfObjectProperty(HashMap<String, String> mergedEntitiesNewNames, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLObjectProperty oProp, OWLDataFactory df, OWLOntology ont,
			OWLObjectProperty prop) {

		for (OWLObjectPropertyExpression equivProp : prop.getEquivalentProperties(ont)) {
			String number = ontologyNumber;

			if(!equivProp.isAnonymous()){
				String equiv = equivProp.getNamedProperty().getIRI().toString();
				int index = equiv.indexOf("#", 0);
				String c = equiv.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(equiv)){
					number = "/000";
					c = mergedEntitiesNewNames.get(equiv);
				}

				OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
				//OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + equivProp.getNamedProperty().getIRI().getFragment()));

				OWLEquivalentObjectPropertiesAxiom invAx = df.getOWLEquivalentObjectPropertiesAxiom(oProp, objProp);
				axioms.add(invAx);
			}
		}
	}
	
	
	public static void extractAndCreateNonBuiltInObjectPropertyAnnotations(OWLObjectProperty objProp, Set<OWLAxiom> axioms, OWLDataFactory datafact,
			OWLObjectProperty prop, OWLOntology ont) {

		for (OWLAnnotation annotation : prop.getAnnotations(ont)) {

			if (!annotation.getProperty().isBuiltIn()) {
				OWLAnnotationProperty annotationProp = datafact.getOWLAnnotationProperty(annotation.getProperty().getIRI());
				OWLAnnotation anno = datafact.getOWLAnnotation(annotationProp, annotation.getValue());

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(objProp.getIRI(), anno);
				axioms.add(ax);
			}
		}
	}
	
	
	public static void createAxiomsOfParsedDataProperties(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, String ontologyNumber, HashMap<String, String> hdataprop) throws OWLException{

		for (OWLDataProperty dataProperty : ont.getDataPropertiesInSignature()) {
			if (!dataProperty.isOWLTopDataProperty()) {
				String ch = dataProperty.getIRI().toString();
				hdataprop.put(ch, ontologyNumber);

				String number;
				OWLDataProperty dProp;

				if(mergedEntitiesNewNames.containsKey(ch)){
					number = "/000";
					String c = mergedEntitiesNewNames.get(ch);

					dProp = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
					//dProp = df.getOWLDataProperty(IRI.create(iri + number + "#", dataProperty.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(dProp);
					axioms.add(ax);

					addLabelForMergedEntity(ch, df, dProp, axioms, ontologyNumber);

				}else{
					number = entityID(ontologyNumber);
					int index = ch.indexOf("#", 0);
					String c = ch.substring((index + 1));

					dProp = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
					//dProp = df.getOWLDataProperty(IRI.create(iri + number + "#", dataProperty.getIRI().getFragment()));
					OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(dProp);
					axioms.add(ax);
				}

				number = entityID(ontologyNumber);

				extractAndCreateDataPropertyLabels(dProp, axioms, dataProperty, ont, df);
				extractAndCreateDataPropertyComments(dProp, axioms, dataProperty, ont, df);
				extractAndCreateDataPropertyDomains(mergedEntitiesNewNames, dProp, iri, number, axioms, df, dataProperty, ont);
				extractAndCreateDataPropertyRanges(dProp, iri, axioms, df, dataProperty, ont);
				extractAndCreateDataPropertyTypes(dProp, axioms, df, dataProperty, ont);
				extractAndCreateSuperPropertiesOfDataProperty(mergedEntitiesNewNames, dProp, iri, number, axioms, df, dataProperty, ont);
				extractAndCreateEquivalentPropertiesOfDataProperty(mergedEntitiesNewNames, dProp, iri, number, axioms, df, dataProperty, ont);
				extractAndCreateDisjointPropertiesOfDataProperty(mergedEntitiesNewNames, dProp, iri, number, axioms, df, dataProperty, ont);
				extractAndCreateNonBuiltInDataPropertyAnnotations(dProp, axioms, df, dataProperty, ont);

				//} else {
				//OWLDataProperty dProp = df.getOWLTopDataProperty();
				//OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(dProp);
				//axioms.add(ax);
				//}
			}
		}
	}
	
	
	public static void extractAndCreateDataPropertyLabels(OWLDataProperty dataProp, Set<OWLAxiom> axioms, OWLDataProperty dataProperty,
			OWLOntology ont, OWLDataFactory df) {

		for (OWLAnnotation annotation : dataProperty.getAnnotations(ont, df.getRDFSLabel())) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = df.getOWLAnnotation(df.getRDFSLabel(),
							df.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = df.getOWLAnnotation(df.getRDFSLabel(),
							df.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(dataProp.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = df.getOWLAnnotation(df.getRDFSLabel(), val);

					OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(dataProp.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateDataPropertyComments(OWLDataProperty dataProp, Set<OWLAxiom> axioms, OWLDataProperty dataProperty, OWLOntology ont, OWLDataFactory df) {

		for (OWLAnnotation annotation : dataProperty.getAnnotations(ont, df.getRDFSComment())) {

			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = df.getOWLAnnotation(df.getRDFSComment(),
							df.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = df.getOWLAnnotation(df.getRDFSComment(),
							df.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(dataProp.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = df.getOWLAnnotation(df.getRDFSComment(), val);

					OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(dataProp.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateDataPropertyDomains(HashMap<String, String> mergedEntitiesNewNames, OWLDataProperty dataProp, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dataProperty, OWLOntology ont) {

		for (OWLClassExpression classExp : dataProperty.getDomains(ont)) {
			String number = ontologyNumber;

			if(!classExp.isAnonymous()){
				String dom = classExp.asOWLClass().getIRI().toString();
				int index = dom.indexOf("#", 0);
				String c = dom.substring((index + 1));
				OWLClass cls;

				if(mergedEntitiesNewNames.containsKey(dom)){
					number = "/000";
					c = mergedEntitiesNewNames.get(dom);
				}

				if (!c.equals("Thing")) {
					cls = df.getOWLClass(IRI.create(iri + number + "#" + c));
					//cls = df.getOWLClass(IRI.create(iri + number + "#" + classExp.asOWLClass().getIRI().getFragment()));
				} else {
					cls = df.getOWLThing();
				}

				OWLDataPropertyDomainAxiom ax = df.getOWLDataPropertyDomainAxiom(dataProp, cls);
				axioms.add(ax);
			}
		}
	}

	
	public static void extractAndCreateDataPropertyRanges(OWLDataProperty dataProp, String iri, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dataProperty, OWLOntology ont) {

		for (OWLDataRange range : dataProperty.getRanges(ont)) {
			if (range.isDatatype()) {

				//String img = cc.toString().substring(4);
				//PrefixManager pm = new DefaultPrefixManager("http://www.w3.org/2001/XMLSchema#");
				//OWLDatatype Datatype = df.getOWLDatatype(img, pm);
				OWLDataPropertyRangeAxiom ax = df.getOWLDataPropertyRangeAxiom(dataProp, range);
				axioms.add(ax);
			}
		}
	}
	
	
	public static void extractAndCreateDataPropertyTypes(OWLDataProperty dataProp, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dataProperty, OWLOntology ont) {

		if (dataProperty.isFunctional(ont)) {
			OWLFunctionalDataPropertyAxiom typeAx = df.getOWLFunctionalDataPropertyAxiom(dataProp);
			axioms.add(typeAx);		}
	}
	
	
	public static void extractAndCreateSuperPropertiesOfDataProperty(HashMap<String, String> mergedEntitiesNewNames, OWLDataProperty dataProp, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dProp, OWLOntology ont) {

		for (OWLDataPropertyExpression superProp : dProp.getSuperProperties(ont)) {
			String number = ontologyNumber;

			if(!superProp.isAnonymous()){

				String sp = superProp.asOWLDataProperty().getIRI().toString();
				int index = sp.indexOf("#", 0);
				String c = sp.substring((index + 1));
				OWLDataProperty prop;

				if(mergedEntitiesNewNames.containsKey(sp)){
					number = "/000";
					c = mergedEntitiesNewNames.get(sp);
				}

				if (!c.equals("topDataProperty")) {
					prop = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
					//prop = df.getOWLDataProperty(IRI.create(iri + number + "#" + superProp.asOWLDataProperty().getIRI().getFragment()));
				} else {
					prop = df.getOWLTopDataProperty();
				}

				OWLSubDataPropertyOfAxiom ax = df.getOWLSubDataPropertyOfAxiom(dataProp, prop);
				axioms.add(ax);
			}
		}
	}

	
	public static void extractAndCreateEquivalentPropertiesOfDataProperty(HashMap<String, String> mergedEntitiesNewNames, OWLDataProperty dataProp, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dProp, OWLOntology ont) {

		for (OWLDataPropertyExpression equiv : dProp.getEquivalentProperties(ont)) {
			String number = ontologyNumber;

			if(!equiv.isAnonymous()){
				String eq = equiv.asOWLDataProperty().getIRI().toString();
				int index = eq.indexOf("#", 0);
				String c = eq.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(eq)){
					number = "/000";
					c = mergedEntitiesNewNames.get(eq);
				}

				OWLDataProperty prop = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
				//OWLDataProperty prop = df.getOWLDataProperty(IRI.create(iri + number + "#" + equiv.asOWLDataProperty().getIRI().getFragment()));

				OWLEquivalentDataPropertiesAxiom eqAxiom = df.getOWLEquivalentDataPropertiesAxiom(dataProp, prop);
				axioms.add(eqAxiom);
			}
		}
	}
	
	
	public static void extractAndCreateDisjointPropertiesOfDataProperty(HashMap<String, String> mergedEntitiesNewNames, OWLDataProperty dataProp, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLDataProperty dProp, OWLOntology ont) {

		for (OWLDataPropertyExpression disjProp : dProp.getDisjointProperties(ont)) {
			String number = ontologyNumber;

			if(!disjProp.isAnonymous()){
				String disj = disjProp.asOWLDataProperty().getIRI().toString();
				int index = disj.indexOf("#", 0);
				String c = disj.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(disj)){
					number = "/000";
					c = mergedEntitiesNewNames.get(disj);
				}

				OWLDataProperty prop = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
				//OWLDataProperty prop = df.getOWLDataProperty(IRI.create(iri + number + "#" + disjProp.asOWLDataProperty().getIRI().getFragment()));

				OWLDisjointDataPropertiesAxiom disjAx = df.getOWLDisjointDataPropertiesAxiom(dataProp, prop);
				axioms.add(disjAx);
			}
		}
	}
	
	
	public static void extractAndCreateNonBuiltInDataPropertyAnnotations(OWLDataProperty prop_d, Set<OWLAxiom> axioms, OWLDataFactory datafact,
			OWLDataProperty prop, OWLOntology ont) {

		for (OWLAnnotation annotation : prop.getAnnotations(ont)) {
			if (!annotation.getProperty().isBuiltIn()) {
				OWLAnnotationProperty annotationProp = datafact.getOWLAnnotationProperty(annotation.getProperty().getIRI());
				OWLAnnotation anno = datafact.getOWLAnnotation(annotationProp, annotation.getValue());

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(prop_d.getIRI(), anno);
				axioms.add(ax);
			}
		}
	}
	
	
	public static void createAxiomsOfParsedIndividuals(HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, OWLOntology ont, Set<OWLAxiom> axioms, String iri, String ontologyNumber, HashMap<String, String> hInstances) throws OWLException{

		for (OWLNamedIndividual individual : ont.getIndividualsInSignature()) {
			String ch = individual.getIRI().toString();
			hInstances.put(ch, ontologyNumber);

			String number;
			OWLNamedIndividual instance;

			if(mergedEntitiesNewNames.containsKey(ch)){
				number = "/000";
				String c = mergedEntitiesNewNames.get(ch);

				instance = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + c));
				//instance = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + individual.getIRI().getFragment()));
				OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(instance);
				axioms.add(ax);

				addLabelForMergedEntity(ch, df, instance, axioms, ontologyNumber);

			}else{
				number = entityID(ontologyNumber);
				int index = ch.indexOf("#", 0);
				String c = ch.substring((index + 1));

				instance = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + c));
				//instance = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + individual.getIRI().getFragment()));
				OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(instance);
				axioms.add(ax);
			}

			number = entityID(ontologyNumber);

			extractAndCreateIndividualLabels(instance, axioms, individual, ont, df);
			extractAndCreateIndividualComments(instance, axioms, individual, ont, df);
			extractAndCreateSameAsIndividualsOfIndividual(mergedEntitiesNewNames, instance, iri, number, axioms, df, individual, ont);
			extractAndCreateDifferentIndividualsOfIndividual(mergedEntitiesNewNames, axioms, instance, iri, number, df, individual, ont); 
			extractAndCreateClassAssertions(mergedEntitiesNewNames, instance, iri, number, axioms, df, individual, ont);
			extractAndCreateObjectPropertyAssertionsOfIndividual(mergedEntitiesNewNames, instance, iri, number, axioms, df, individual, ont); 
			extractAndCreateNegativeObjectPropertyAssertionsOfIndividual(mergedEntitiesNewNames, instance, iri, number, axioms, df, individual, ont); 
			extractAndCreateDataPropertyAssertionsOfIndividual(mergedEntitiesNewNames, instance, iri, number, axioms, df, individual, ont);
			extractAndCreateNegativeDataPropertyAssertionsOfIndividual(mergedEntitiesNewNames, instance, iri, number, axioms, df, individual, ont);
			extractAndCreateNonBuiltInIndividualAnnotations(instance, axioms, df, individual, ont);
		}
	}

	
	public static void extractAndCreateClassAssertions(HashMap<String, String> mergedEntitiesNewNames, OWLNamedIndividual instance, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLNamedIndividual individual, OWLOntology ont) {

		for (OWLClassAssertionAxiom assertion : ont.getClassAssertionAxioms(individual)) {
			String number = ontologyNumber;

			for (OWLClass concept : assertion.getClassesInSignature()) {
				String clas = concept.getIRI().toString();
				int index = clas.indexOf("#", 0);
				String c = clas.substring((index + 1));
				OWLClass cls;

				if(mergedEntitiesNewNames.containsKey(clas)){
					number = "/000";
					c = mergedEntitiesNewNames.get(clas);
				}

				if (!c.equals("Thing")) {
					cls = df.getOWLClass(IRI.create(iri + number + "#" + c));
					//cls = df.getOWLClass(IRI.create(iri + number + "#" + concept.getIRI().getFragment()));
				} else {
					cls = df.getOWLThing();
				}

				OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(cls, instance);
				axioms.add(classAssertion);
			}
		}
	}
	
	public static void extractAndCreateIndividualLabels(OWLNamedIndividual instance, Set<OWLAxiom> axioms, OWLNamedIndividual individual, OWLOntology ont,
			OWLDataFactory df) {

		for (OWLAnnotation annotation : individual.getAnnotations(ont, df.getRDFSLabel())) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = df.getOWLAnnotation(df.getRDFSLabel(),
							df.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = df.getOWLAnnotation(df.getRDFSLabel(),
							df.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(instance.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = df.getOWLAnnotation(df.getRDFSLabel(), val);

					OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(instance.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateIndividualComments(OWLNamedIndividual instance, Set<OWLAxiom> axioms, OWLNamedIndividual individual, OWLOntology ont,
			OWLDataFactory df) {

		for (OWLAnnotation annotation : individual.getAnnotations(ont, df.getRDFSComment())) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = df.getOWLAnnotation(df.getRDFSComment(),
							df.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = df.getOWLAnnotation(df.getRDFSComment(),
							df.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(instance.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = df.getOWLAnnotation(df.getRDFSComment(), val);

					OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(instance.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	
	public static void extractAndCreateSameAsIndividualsOfIndividual(HashMap<String, String> mergedEntitiesNewNames, OWLNamedIndividual instance, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df, OWLNamedIndividual individual, OWLOntology ont) {

		for (OWLIndividual sameIndiv : individual.getSameIndividuals(ont)) {
			String number = ontologyNumber;

			if(sameIndiv.isNamed()){
				String same = sameIndiv.asOWLNamedIndividual().getIRI().toString();
				int index = same.indexOf("#", 0);
				String c = same.substring((index + 1));

				if(mergedEntitiesNewNames.containsKey(same)){
					number = "/000";
					c = mergedEntitiesNewNames.get(same);
				}

				OWLNamedIndividual inst = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + c));
				//OWLNamedIndividual inst = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + sameIndiv.asOWLNamedIndividual().getIRI().getFragment()));

				OWLSameIndividualAxiom sameInds = df.getOWLSameIndividualAxiom(instance, inst);
				axioms.add(sameInds);
			}
		}
	}
	
	
	public static void extractAndCreateDifferentIndividualsOfIndividual(HashMap<String, String> mergedEntitiesNewNames, Set<OWLAxiom> axioms, OWLNamedIndividual instance, String iri, String ontologyNumber, OWLDataFactory datafactory, OWLNamedIndividual ind, OWLOntology ont) {

		Collection<OWLIndividual> differentIndiv = ind.getDifferentIndividuals(ont);
		if(!differentIndiv.isEmpty()){
			Set<OWLNamedIndividual> set = new HashSet<OWLNamedIndividual>();

			for (OWLIndividual diffind : differentIndiv) {
				if(diffind.isNamed()){
					String diff = diffind.asOWLNamedIndividual().getIRI().toString();
					int index = diff.indexOf("#", 0);
					String c = diff.substring((index + 1));
					String number = ontologyNumber;

					if(mergedEntitiesNewNames.containsKey(diff)){
						number = "/000";
						c = mergedEntitiesNewNames.get(diff);
					}

					OWLNamedIndividual inst = datafactory.getOWLNamedIndividual(IRI.create(iri + number + "#" + c));
					//OWLNamedIndividual inst = datafactory.getOWLNamedIndividual(IRI.create(iri + number + "#" + diffind.asOWLNamedIndividual().getIRI().getFragment()));
					set.add(inst);
					set.add(instance);
				}
			}

			OWLDifferentIndividualsAxiom diffIndAx = datafactory.getOWLDifferentIndividualsAxiom(set);
			axioms.add(diffIndAx);
		}
	}

	
	public static void extractAndCreateObjectPropertyAssertionsOfIndividual(HashMap<String, String> mergedEntitiesNewNames, OWLNamedIndividual instance, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individ, OWLOntology ont) {

		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> map = individ.getObjectPropertyValues(ont);
		for (Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty() &&  !entry.getKey().isAnonymous()) {

				for (OWLIndividual indiv : entry.getValue()) {
					if(indiv.isNamed()){
						String ch = entry.getKey().asOWLObjectProperty().getIRI().toString();
						int index = ch.indexOf("#", 0);
						String c = ch.substring((index + 1));
						String number = ontologyNumber;

						if(mergedEntitiesNewNames.containsKey(ch)){
							number = "/000";
							c = mergedEntitiesNewNames.get(ch);
						}

						OWLObjectProperty objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
						//OWLObjectProperty objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + entry.getKey().asOWLObjectProperty().getIRI().getFragment()));


						String ch1 = indiv.asOWLNamedIndividual().getIRI().toString();
						int indexx = ch1.indexOf("#", 0);
						String cc = ch1.substring((indexx + 1));

						if(mergedEntitiesNewNames.containsKey(ch1)){
							number = "/000";
							cc = mergedEntitiesNewNames.get(ch1);
						}

						OWLNamedIndividual individual = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + cc));
						//OWLNamedIndividual individual = fac.getOWLNamedIndividual(IRI.create(iri + number + "#" + indiv.asOWLNamedIndividual().getIRI().getFragment()));

						OWLObjectPropertyAssertionAxiom propertyAssertion = df
								.getOWLObjectPropertyAssertionAxiom(objectProp, instance, individual);
						axioms.add(propertyAssertion);
					}
				}
			}
		}
	}

	public static void extractAndCreateNegativeObjectPropertyAssertionsOfIndividual(HashMap<String, String> mergedEntitiesNewNames, OWLNamedIndividual instance, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individ, OWLOntology ont) {

		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> map = individ.getNegativeObjectPropertyValues(ont);
		for (Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty() &&  !entry.getKey().isAnonymous()) {

				for (OWLIndividual indiv : entry.getValue()) {
					if(indiv.isNamed()){
						String ch = entry.getKey().asOWLObjectProperty().getIRI().toString();
						int index = ch.indexOf("#", 0);
						String c = ch.substring((index + 1));
						String number = ontologyNumber;

						if(mergedEntitiesNewNames.containsKey(ch)){
							number = "/000";
							c = mergedEntitiesNewNames.get(ch);
						}

						OWLObjectProperty objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + c));
						//OWLObjectProperty objectProp = df.getOWLObjectProperty(IRI.create(iri + number + "#" + entry.getKey().asOWLObjectProperty().getIRI().getFragment()));


						String ch1 = indiv.asOWLNamedIndividual().getIRI().toString();
						int indexx = ch1.indexOf("#", 0);
						String cc = ch1.substring((indexx + 1));

						if(mergedEntitiesNewNames.containsKey(ch1)){
							number = "/000";
							cc = mergedEntitiesNewNames.get(ch1);
						}

						OWLNamedIndividual individual = df.getOWLNamedIndividual(IRI.create(iri + number + "#" + cc));
						//OWLNamedIndividual individual = fac.getOWLNamedIndividual(IRI.create(iri + number + "#" + indiv.asOWLNamedIndividual().getIRI().getFragment()));

						OWLNegativeObjectPropertyAssertionAxiom propertyAssertion = df
								.getOWLNegativeObjectPropertyAssertionAxiom(objectProp, instance, individual);
						axioms.add(propertyAssertion);
					}
				}
			}
		}
	}

	
	public static void extractAndCreateDataPropertyAssertionsOfIndividual(HashMap<String, String> mergedEntitiesNewNames, OWLNamedIndividual instance, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individual, OWLOntology ont) {

		Map<OWLDataPropertyExpression, Set<OWLLiteral>> map = individual.getDataPropertyValues(ont);
		for (Entry<OWLDataPropertyExpression, Set<OWLLiteral>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty() && !entry.getKey().isAnonymous()) {

				String prop = entry.getKey().asOWLDataProperty().getIRI().toString();
				int index = prop.indexOf("#", 0);
				String c = prop.substring((index + 1));
				String number = ontologyNumber;

				if(mergedEntitiesNewNames.containsKey(prop)){
					number = "/000";
					c = mergedEntitiesNewNames.get(prop);
				}

				OWLDataProperty dataProp = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
				//OWLDataProperty dataProp = df.getOWLDataProperty(IRI.create(iri + number + "#" + entry.getKey().asOWLDataProperty().getIRI().getFragment()));

				for (OWLLiteral literal : entry.getValue()) {
					OWLDataPropertyAssertionAxiom dataPropertyAssertion = df
							.getOWLDataPropertyAssertionAxiom(dataProp, instance, literal);
					axioms.add(dataPropertyAssertion);
				}
			}
		}
	}

	public static void extractAndCreateNegativeDataPropertyAssertionsOfIndividual(HashMap<String, String> mergedEntitiesNewNames, OWLNamedIndividual instance, String iri, String ontologyNumber, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLNamedIndividual individual, OWLOntology ont) {

		Map<OWLDataPropertyExpression, Set<OWLLiteral>> map = individual.getNegativeDataPropertyValues(ont);
		for (Entry<OWLDataPropertyExpression, Set<OWLLiteral>> entry : map.entrySet()) {
			if (!entry.getValue().isEmpty() && !entry.getKey().isAnonymous()) {

				String prop = entry.getKey().asOWLDataProperty().getIRI().toString();
				int index = prop.indexOf("#", 0);
				String c = prop.substring((index + 1));
				String number = ontologyNumber;

				if(mergedEntitiesNewNames.containsKey(prop)){
					number = "/000";
					c = mergedEntitiesNewNames.get(prop);
				}

				OWLDataProperty dataProp = df.getOWLDataProperty(IRI.create(iri + number + "#" + c));
				//OWLDataProperty dataProp = df.getOWLDataProperty(IRI.create(iri + number + "#" + entry.getKey().asOWLDataProperty().getIRI().getFragment()));

				for (OWLLiteral literal : entry.getValue()) {
					OWLNegativeDataPropertyAssertionAxiom dataPropertyAssertion = df
							.getOWLNegativeDataPropertyAssertionAxiom(dataProp, instance, literal);
					axioms.add(dataPropertyAssertion);
				}
			}
		}
	}

	
	public static void extractAndCreateNonBuiltInIndividualAnnotations(OWLNamedIndividual instance, Set<OWLAxiom> axioms, OWLDataFactory datafact,
			OWLNamedIndividual individual, OWLOntology ont) {

		for (OWLAnnotation annotation : individual.getAnnotations(ont)) {

			if (!annotation.getProperty().isBuiltIn()) {
				OWLAnnotationProperty annotationProp = datafact.getOWLAnnotationProperty(annotation.getProperty().getIRI());
				OWLAnnotation annoo = datafact.getOWLAnnotation(annotationProp, annotation.getValue());

				OWLAnnotationAssertionAxiom ax = datafact.getOWLAnnotationAssertionAxiom(instance.getIRI(), annoo);
				axioms.add(ax);
			}
		}
	}
	
	public static void createAxiomsOfParsedNonBuiltInAnnotationProperties(Set<OWLAxiom>axioms, OWLOntology ont, OWLDataFactory df){

		for (OWLAnnotationProperty annoProp : ont.getAnnotationPropertiesInSignature()){

			if(!annoProp.isBuiltIn()){
				OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(annoProp);
				axioms.add(ax);

				extractAndCreateAnnotationPropertyLabels(annoProp, axioms, ont, df);
				extractAndCreateAnnotationPropertyComments(annoProp, axioms, ont, df);
				extractAndCreateSuperPropertiesOfAnnotationProperty(annoProp, axioms, df, ont);
				extractAndCreateAnnotationPropertyDomains(annoProp, axioms, df, ont);
				extractAndCreateAnnotationPropertyRanges(annoProp, axioms, df, ont);
			}
		}
	}
	
	public static void extractAndCreateAnnotationPropertyLabels(OWLAnnotationProperty prop, Set<OWLAxiom> axioms, OWLOntology ont,
			OWLDataFactory df) {

		for (OWLAnnotation annotation : prop.getAnnotations(ont, df.getRDFSLabel())) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				OWLAnnotation anno;

				if (val.hasLang()) {
					anno = df.getOWLAnnotation(df.getRDFSLabel(),
							df.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					anno = df.getOWLAnnotation(df.getRDFSLabel(),
							df.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(prop.getIRI(), anno);
				axioms.add(ax);
			}
			else{
				if (annotation.getValue() instanceof IRI) {
					IRI val = (IRI) annotation.getValue();
					OWLAnnotation anno = df.getOWLAnnotation(df.getRDFSLabel(), val);

					OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(prop.getIRI(), anno);
					axioms.add(ax);
				}
			}
		}
	}

	public static void extractAndCreateAnnotationPropertyComments(OWLAnnotationProperty prop, Set<OWLAxiom> axioms, OWLOntology ont,
			OWLDataFactory df) {

		for (OWLAnnotation annot : prop.getAnnotations(ont, df.getRDFSComment())) {

			if (annot.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annot.getValue();
				OWLAnnotation annotation;

				if (val.hasLang()) {
					annotation = df.getOWLAnnotation(df.getRDFSComment(),
							df.getOWLLiteral(val.getLiteral(), val.getLang()));
				} else {
					annotation = df.getOWLAnnotation(df.getRDFSComment(),
							df.getOWLLiteral(val.getLiteral(), val.getDatatype()));
				}

				OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(prop.getIRI(), annotation);
				axioms.add(ax);
			}		
			else{
				if (annot.getValue() instanceof IRI) {
					IRI val = (IRI) annot.getValue();
					OWLAnnotation annotation = df.getOWLAnnotation(df.getRDFSComment(), val);

					OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(prop.getIRI(), annotation);
					axioms.add(ax);
				}
			}
		}
	}

	public static void extractAndCreateSuperPropertiesOfAnnotationProperty(OWLAnnotationProperty annotationProperty, Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont) {

		for (OWLAnnotationProperty superProp : annotationProperty.getSuperProperties(ont)) {

			OWLSubAnnotationPropertyOfAxiom ax = df.getOWLSubAnnotationPropertyOfAxiom(annotationProperty, superProp);
			axioms.add(ax);
		}
	}

	public static void extractAndCreateAnnotationPropertyRanges(OWLAnnotationProperty annotationProp, Set<OWLAxiom> axioms, OWLDataFactory df,
			OWLOntology ont) {

		for (OWLAnnotationPropertyRangeAxiom range : ont.getAnnotationPropertyRangeAxioms(annotationProp)) {

			OWLAnnotationPropertyDomainAxiom ax = df.getOWLAnnotationPropertyDomainAxiom(annotationProp, range.getRange());
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
	
	
	public static void createAxiomsOfParsedDataTypes(Set<OWLAxiom>axioms, OWLOntology ont, OWLDataFactory df){
		for(OWLDatatype type : ont.getDatatypesInSignature()){

			OWLDeclarationAxiom ax = df.getOWLDeclarationAxiom(type);
			axioms.add(ax);
		}
	}
	
	public static void createAxiomsOfParsedAnonymousIndividuals(Set<OWLAxiom> axioms, OWLDataFactory df, OWLOntology ont) {
		
		for (OWLAnonymousIndividual individual : ont.getAnonymousIndividuals()) {
			for (OWLAnnotationAssertionAxiom assertion : ont.getAnnotationAssertionAxioms(individual)) {

				OWLAnnotation annotation = null;

				if (assertion.getProperty().isLabel()) {
					// if (ann.getValue() instanceof OWLLiteral) {
					OWLLiteral val = (OWLLiteral) assertion.getValue();

					if (val.hasLang()) {
						annotation = df.getOWLAnnotation(df.getRDFSLabel(),
								df.getOWLLiteral(val.getLiteral(), val.getLang()));
					} else {
						annotation = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(val.getLiteral()));
					}
					// }
				} else {
					if (assertion.getProperty().isComment()) {
						// if (ann.getValue() instanceof OWLLiteral) {
						OWLLiteral val = (OWLLiteral) assertion.getValue();

						if (val.hasLang()) {
							annotation = df.getOWLAnnotation(df.getRDFSComment(),
									df.getOWLLiteral(val.getLiteral(), val.getLang()));
						} else {
							annotation = df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral(val.getLiteral()));
						}
						// }
					}
				}
				OWLAnnotationAssertionAxiom x = df.getOWLAnnotationAssertionAxiom(assertion.getSubject(), annotation);
				axioms.add(x);
			}
		}
	}

	
	public static void createSubsumptionAndDisjointedBridgingAxiomsUsingOriginalAlignments(String alignmentFile, double threshold, String iri, HashMap<String, String> mergedEntitiesNewNames, OWLDataFactory df, Set<OWLAxiom> axioms, HashMap<String, String> hClasses, HashMap<String, String> hObjProps, HashMap<String, String> hDataProps, HashMap<String, String> hInstances) throws AlignmentException{

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
	
	
	public static void addSubsumptionBridgeAxioms(String entity1, String entity2, String iri, HashMap<String, String> mergedEntitiesNewNames, HashMap<String, String> classes, HashMap<String, String> objProps, HashMap<String, String> dataProps, HashMap<String, String> instances, OWLDataFactory dfact, Set<OWLAxiom> axioms){

		if (classes.containsKey(entity1) && classes.containsKey(entity2)) {

			int index1 = entity1.indexOf("#", 0);
			String cls1 = entity1.substring((index1 + 1));
			int index2 = entity2.indexOf("#", 0);
			String cls2 = entity2.substring((index2 + 1));

			String number1 = entityID(classes.get(entity1));
			String number2 = entityID(classes.get(entity2));

			mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, cls1, cls2, number1, number2);

			OWLClass clsA = dfact.getOWLClass(IRI.create(iri + number1 + "#" + cls1));
			OWLClass clsB = dfact.getOWLClass(IRI.create(iri + number2 + "#" + cls2));

			OWLSubClassOfAxiom axiom = dfact.getOWLSubClassOfAxiom(clsA, clsB);
			axioms.add(axiom);
		} else {

			if (objProps.containsKey(entity1) && objProps.containsKey(entity2)) {

				int index1 = entity1.indexOf("#", 0);
				String objProp1 = entity1.substring((index1 + 1));
				int index2 = entity2.indexOf("#", 0);
				String objProp2 = entity2.substring((index2 + 1));

				String number1 = entityID(objProps.get(entity1));
				String number2 = entityID(objProps.get(entity2));

				mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, objProp1, objProp2, number1, number2);

				OWLObjectProperty prop1 = dfact
						.getOWLObjectProperty(IRI.create(iri + number1 + "#" + objProp1));
				OWLObjectProperty prop2 = dfact
						.getOWLObjectProperty(IRI.create(iri + number2 + "#" + objProp2));

				OWLSubObjectPropertyOfAxiom ax = dfact.getOWLSubObjectPropertyOfAxiom(prop1, prop2);
				axioms.add(ax);
			} else {

				if (dataProps.containsKey(entity1) && dataProps.containsKey(entity2)) {

					int index1 = entity1.indexOf("#", 0);
					String dataProp1 = entity1.substring((index1 + 1));
					int index2 = entity2.indexOf("#", 0);
					String dataProp2 = entity2.substring((index2 + 1));

					String number1 = entityID(dataProps.get(entity1));
					String number2 = entityID(dataProps.get(entity2));

					mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, dataProp1, dataProp2, number1, number2);

					OWLDataProperty prop1 = dfact
							.getOWLDataProperty(IRI.create(iri + number1 + "#" + dataProp1));
					OWLDataProperty prop2 = dfact
							.getOWLDataProperty(IRI.create(iri + number2 + "#" + dataProp2));

					OWLSubDataPropertyOfAxiom ax = dfact.getOWLSubDataPropertyOfAxiom(prop1, prop2);
					axioms.add(ax);
				}
			}
		}
	}

	public static void mergedOrNot(String entity1, String entity2, HashMap<String, String> mergedEntitiesNewNames, String iri, String ent1, String ent2, String number1, String number2){

		if(mergedEntitiesNewNames.containsKey(entity1)){
			ent1 = mergedEntitiesNewNames.get(entity1);
			number1="/000";
		}
		if(mergedEntitiesNewNames.containsKey(entity2)){
			ent2 = mergedEntitiesNewNames.get(entity2);
			number2="/000";
		}
	}
	
	public static void addDisjointedBridgeAxioms(String entity1, String entity2, String iri, HashMap<String, String> mergedEntitiesNewNames, HashMap<String, String> classes, HashMap<String, String> objProps, HashMap<String, String> dataProps, HashMap<String, String> instances, OWLDataFactory dfact, Set<OWLAxiom> axioms){

		if (classes.containsKey(entity1) && classes.containsKey(entity2)) {

			int index1 = entity1.indexOf("#", 0);
			String cls1 = entity1.substring((index1 + 1));
			int index2 = entity2.indexOf("#", 0);
			String cls2 = entity2.substring((index2 + 1));

			String number1 = entityID(classes.get(entity1));
			String number2 = entityID(classes.get(entity2));

			mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, cls1, cls2, number1, number2);

			OWLClass clsA = dfact.getOWLClass(IRI.create(iri + number1 + "#" + cls1));
			OWLClass clsB = dfact.getOWLClass(IRI.create(iri + number2 + "#" + cls2));

			OWLDisjointClassesAxiom axiom = dfact.getOWLDisjointClassesAxiom(clsA, clsB);
			axioms.add(axiom);
		} else {

			if (objProps.containsKey(entity1) && objProps.containsKey(entity2)) {

				int index1 = entity1.indexOf("#", 0);
				String objProp1 = entity1.substring((index1 + 1));
				int index2 = entity2.indexOf("#", 0);
				String objProp2 = entity2.substring((index2 + 1));

				String number1 = entityID(objProps.get(entity1));
				String number2 = entityID(objProps.get(entity2));

				mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, objProp1, objProp2, number1, number2);

				OWLObjectProperty prop1 = dfact
						.getOWLObjectProperty(IRI.create(iri + number1 + "#" + objProp1));
				OWLObjectProperty prop2 = dfact
						.getOWLObjectProperty(IRI.create(iri + number2 + "#" + objProp2));

				OWLDisjointObjectPropertiesAxiom ax = dfact.getOWLDisjointObjectPropertiesAxiom(prop1, prop2);
				axioms.add(ax);
			} else {

				if (dataProps.containsKey(entity1) && dataProps.containsKey(entity2)) {

					int index1 = entity1.indexOf("#", 0);
					String dataProp1 = entity1.substring((index1 + 1));
					int index2 = entity2.indexOf("#", 0);
					String dataProp2 = entity2.substring((index2 + 1));

					String number1 = entityID(dataProps.get(entity1));
					String number2 = entityID(dataProps.get(entity2));

					mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, dataProp1, dataProp2, number1, number2);

					OWLDataProperty prop1 = dfact
							.getOWLDataProperty(IRI.create(iri + number1 + "#" + dataProp1));
					OWLDataProperty prop2 = dfact
							.getOWLDataProperty(IRI.create(iri + number2 + "#" + dataProp2));

					OWLDisjointDataPropertiesAxiom ax = dfact.getOWLDisjointDataPropertiesAxiom(prop1, prop2);
					axioms.add(ax);
				} else {
					if (instances.containsKey(entity1) && instances.containsKey(entity2)) {

						int index1 = entity1.indexOf("#", 0);
						String instance1 = entity1.substring((index1 + 1));
						int index2 = entity2.indexOf("#", 0);
						String instance2 = entity2.substring((index2 + 1));

						String number1 = entityID(instances.get(entity1));
						String number2 = entityID(instances.get(entity2));

						mergedOrNot(entity1, entity2, mergedEntitiesNewNames, iri, instance1, instance2, number1, number2);

						OWLNamedIndividual individual1 = dfact
								.getOWLNamedIndividual(IRI.create(iri + number1 + "#" + instance1));
						OWLNamedIndividual individual2 = dfact
								.getOWLNamedIndividual(IRI.create(iri + number2 + "#" + instance2));

						OWLDifferentIndividualsAxiom ax = dfact.getOWLDifferentIndividualsAxiom(individual1, individual2);
						axioms.add(ax);
					}
				}
			}
		}
	}

	
	public static void createSubsumptionAndDisjointedBridgingAxiomsUsingFilteredAlignments(String alignmentFile, double threshold, String iri, HashMap<String, String> bridge_entities_code, OWLDataFactory df, Set<OWLAxiom> axioms, HashMap<String, String> hClasses, HashMap<String, String> hObjProps, HashMap<String, String> hDataProps, HashMap<String, String> hInstances) throws AlignmentException{

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
	
	public static void filterCellsHavingSameSources(HashMap<String, String> reverse_is_a_Hash, HashMap<String, String> reverse_is_HashConf,
			Cell cell, String entity1, String entity2) {
		
		if ((!reverse_is_a_Hash.containsKey(entity1)) || (cell.getStrength() > Double.valueOf(reverse_is_HashConf.get(entity1)))){
			reverse_is_a_Hash.put(entity1, entity2);
			reverse_is_HashConf.put(entity1, String.valueOf(cell.getStrength()));
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
			HashMap<String, String> hClasses, HashMap<String, String> hObjProps, HashMap<String, String> hDataProps,
			HashMap<String, String> hInstances, HashMap<String, String> is_a_Hash2,
			HashMap<String, String> reverse_is_a_Hash2, HashMap<String, String> disjHash2) {

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
	
	
	public static void checkNumberOfEntitiesOfMergedOntology(OWLOntology mergedOntology, ArrayList<OWLOntology> ontologiesSet) {
		System.out.println("");

		int nbClasses = getNumberOfClasses(mergedOntology);
		int classes = 0;
		for(OWLOntology ontology : ontologiesSet){
			classes = classes + getNumberOfClasses(ontology);
		}
		System.out.println("--> There are : "+ nbClasses +" classes in the merged ontology");
		System.out.println("    ==> The number of classes in a bridge ontology would be "+ classes);


		int nbObjProps = getNumberOfObjectProperties(mergedOntology);
		int objectProperties = 0;
		for(OWLOntology ontology : ontologiesSet){
			objectProperties = objectProperties + getNumberOfObjectProperties(ontology);
		}
		System.out.println("--> There are : "+ nbObjProps +" object properties in the merged ontology");
		System.out.println("    ==> The number of object properties in a bridge ontology would be "+ objectProperties);


		int nbDataProperties = getNumberOfDataProperties(mergedOntology);
		int dataProperties = 0;
		for(OWLOntology ontology : ontologiesSet){
			dataProperties = dataProperties + getNumberOfDataProperties(ontology);
		}
		System.out.println("--> There are : "+ nbDataProperties +" data properties in the merged ontology");
		System.out.println("    ==> The number of data properties in a bridge ontology would be "+ dataProperties);


		int nbIndividuals = getNumberOfIndividuals(mergedOntology);
		int individuals = 0;
		for(OWLOntology ontology : ontologiesSet){
			individuals = individuals + getNumberOfIndividuals(ontology);
		}
		System.out.println("--> There are : "+ nbIndividuals +" individuals in the merged ontology");
		System.out.println("    ==> The number of individuals in a bridge ontology would be "+ individuals);


		int nbAxioms = mergedOntology.getLogicalAxiomCount();
		int predictedNumberOfAxioms = 0;
		for(OWLOntology ontology : ontologiesSet){
			predictedNumberOfAxioms = predictedNumberOfAxioms + ontology.getLogicalAxiomCount();
		}
		System.out.println("--> There are : "+ nbAxioms +" axioms in the merged ontology");
		System.out.println("    ==> The number of logical axioms in a bridge ontology would be "+ predictedNumberOfAxioms);
	}

	public static int getNumberOfClasses(OWLOntology mergedOntology){
		int nbClasses = 0;
		for (OWLClass cl : mergedOntology.getClassesInSignature()){
			if(!cl.isOWLThing()){
				//System.out.println(nbClasses + "/. " + cl.getIRI().getFragment());
				nbClasses++;
			}
		}
		return nbClasses;
	}

	public static int getNumberOfObjectProperties(OWLOntology mergedOntology){
		int nbObjProps = 0;
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
