# **AROM** (**A**lignments **R**euse for **O**ntology **M**erging)
AROM is an approach for creating a new ontology resulting from a full merge of multiple ontologies using pairwise alignments between them. It automatically customizes and merges multiple ontologies in a holistic manner and in very short times.



## Installation

Download AROM and open it in your IDE, then manually add all the jars of the [lib](https://github.com/inesosman/AROM/tree/master/lib) folder to the classpath. The [lib](https://github.com/inesosman/AROM/tree/master/lib) folder contains OWL API jars and their dependencies, Alignment API jars, HermiT jars, and ELK jars. Then, run one of the following classes of the [src/merging](https://github.com/inesosman/AROM/tree/master/src/merging) folder (depending on your choice) :

* [MergingWithoutRefactoring](https://github.com/inesosman/AROM/blob/master/src/merging/MergingWithoutRefactoring.java) : It parses all the input ontologies' axioms, and creates an exact copy of them. Then, it merges equivent entities as prescribed in the input alignment(s), and adds bridging axioms (other than equivalence relations) to link different entities.

* [MergingWithRefactoring](https://github.com/inesosman/AROM/blob/master/src/merging/MergingWithRefactoring.java) : It parses all the input ontologies' axioms, and creates a refactored copy of them. A full IRI of an entity (which can be a class, a property, or an individual) is composed of a prefix, followed by a local name (*i.e.*, a suffix or an abbriviated name). Refactoring consists in replacing the prefix IRI of each entity (*i.e.*, the part before the #) by the IRI of the furture merged ontology. Then, it merges equivent refactored entities as prescribed in the input alignment(s), and adds bridging axioms (other than equivalence relations) to link different refactored entities.

### Input

* Two or more OWL ontologies to be merged (owl files in the [Data](https://github.com/inesosman/AROM/tree/master/Data) folder) (the path of the files should be correct !)
* One or more ontology alignments (in the Alignment API format) (rdf files in the [Data](https://github.com/inesosman/AROM/tree/master/Data) folder) (the path of the files should be correct !)
* A new IRI for the output merged ontology
* A threshold real value between [0,1] to filter the alignment correspondences by their confidence measure.

### Output

* A new merged ontology (an owl file in the [Results](https://github.com/inesosman/AROM/tree/master/Results) folder).


# Example

Let's merge the three ontologies of the **Large Biomedical Ontologies** OAEI track. We will merge them using reference alignments between all possible ontology pairs. This will ensure a complete semantic interoperability between them. All tests were performed with a confidence threshold equal to 0.0, so we kept all correspondences / cells of the input alignments.


## Input Alignments (FMA-NCI + SNOMED-NCI + FMA-SNOMED)

These are three equivalence correspondences from the "Large Biomedical Ontologies" OAEI reference alignments :

### FMA-NCI

The first correspondence (in [FMA-NCI](https://github.com/inesosman/AROM/blob/master/Data/FMA2NCI.rdf)) matches the __Skin_of_head__ class (from FMA) to the __Head_Skin__ class (from NCI):

![FMA-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-NCI.png)

### SNOMED-NCI

The second correspondence (in [SNOMED-NCI](https://github.com/inesosman/AROM/blob/master/Data/SNOMED2NCI.rdf)) matches the __Skin_structure_of_head__ class (from SNOMED) to the __Head_Skin__ class (from NCI):

![SNOMED-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED-NCI.png)

### FMA-SNOMED

The third correspondence (in [FMA-SNOMED](https://github.com/inesosman/AROM/blob/master/Data/FMA2SNOMED.rdf)) matches the __Skin_of_head__ class (from FMA) to the __Skin_structure_of_head__ class (from SNOMED):

![FMA-SNOMED alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)



## Input Ontologies (FMA + NCI + SNOMED)

### FMA

Here is the definition/description of the __Skin_of_head__ class in its original ontology ([FMA](https://github.com/inesosman/AROM/blob/master/Data/FMA.owl) (Ont1)) :

![Skin_of_head](https://github.com/inesosman/AROM/blob/master/Figures/FMA_Class.png)

### NCI

Here is the definition/description of the __Head_Skin__ class in its original ontology ([NCI](https://github.com/inesosman/AROM/blob/master/Data/NCI.owl) (Ont2)) :

![Head_Skin](https://github.com/inesosman/AROM/blob/master/Figures/NCI_Class.png)

### SNOMED
Here is the definition/description of the __Skin_structure_of_head__ class in its original ontology ([SNOMED](https://github.com/inesosman/AROM/blob/master/Data/SNOMED3.owl) (Ont3)) :

![Skin_structure_of_head](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED_Class.png)


## Output Merged Ontology

The above-mentioned correspondences will lead to the merging of the three matched classes: __Skin_of_head__ (from FMA (Ont1)), __Head_Skin__ (from NCI (Ont2)), and __Skin_structure_of_head__ (from SNOMED (Ont3)). For this example, we chose to give our future merged ontology the following IRI : "http://merging". 


The following figures show the merged class in our output ontology that resulted from the merging of the three _LargeBio_ ontologies. The merged class captures all knowledge (axioms and expressions) defining the three equivalent classes. It has a unique code (__Code\_1379__) as a (local) name, and all entities (existing in its description), that have been merged, also have their corresponding unique code as a (local) name. Besides, the merged class have three added labels (framed in red), which mention the original (local) names of the classes that have been merged. We attached each name to its ontology number (ID) to directly see from which ontology it originates.

You can view and download all our merged ontologies from the [Results folder](https://github.com/inesosman/AROM/tree/master/Results).


### Non-Refactored Version

![MergedClass](https://github.com/inesosman/AROM/blob/master/Figures/MergedClass.png)

For the non-customized version, axioms are exactly like the original ones.
You can download and view this [non-refactored merged ontology](https://github.com/inesosman/AROM/blob/master/Results/Merge_LargeBio.owl) from the Results folder.

### Refactored Version

![RefactoredMergedClass](https://github.com/inesosman/AROM/blob/master/Figures/RefactoredMergedClass.png)

For the customized version, axioms are like the original ones, except that the IRIs of all mentioned entities are customized.
You can download and view this [refactored merged ontology](https://github.com/inesosman/AROM/blob/master/Results/Merge_LargeBio_Refactored.owl) from the Results folder.

## Conclusion

* Our final ontology is complete, in the sense that it retains all entities, axioms and hierarchies from the input ontologies, and correspondences from the input alignments.
* Running AROM for merging the *Large Biomedical Ontologies* does not exceed **one minute**.

