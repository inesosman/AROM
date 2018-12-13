# **AROM** (**A**lignments **R**euse for **O**ntology **M**erging)
AROM is a novel approach for merging multiple ontologies using alignments. It automatically customizes and merges multiple large ontologies at a time in very short times.

As an example, let's merge the three ontologies of the **Large Biomedical Ontologies** OAEI track. We will merge them using reference alignments between all possible ontology pairs. This will ensure a complete semantic interoperability between them. For this example, the IRI of our future merged ontology is "http://merging". All tests were performed with a confidence threshold equal to $0.0$, so we kept all correspondences/cells of the input alignments.


# Input Alignments

These are three equivalence correspondences from the "Large Biomedical Ontologies" OAEI reference alignments.

### FMA-NCI

The first correspondence (in [FMA-NCI](https://github.com/inesosman/AROM/blob/master/Data/FMA2NCI.rdf)) matches the __Skin_of_head__ class (from FMA) to the __Head_Skin__ class (from NCI):

![FMA-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-NCI.png)

### SNOMED-NCI

The second correspondence (in [SNOMED-NCI](https://github.com/inesosman/AROM/blob/master/Data/SNOMED2NCI.rdf)) matches the __Skin_structure_of_head__ class (from SNOMED) to the __Head_Skin__ class (from NCI):

![SNOMED-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED-NCI.png)

### FMA-SNOMED

The third correspondence (in [FMA-SNOMED](https://github.com/inesosman/AROM/blob/master/Data/FMA2SNOMED.rdf)) matches the __Skin_of_head__ class (from FMA) to the __Skin_structure_of_head__ class (rom SNOMED):

![FMA-SNOMED alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)



# Input Ontologies


### FMA

Here is the definition/description of __Skin_of_head__ class in its original ontology (FMA (Ont1)) :

![Skin_of_head](https://github.com/inesosman/AROM/blob/master/Figures/FMA_Class.png)

### NCI

Here is the definition/description of __Head_Skin__ class in its original ontology (NCI (Ont2)) :

![Head_Skin](https://github.com/inesosman/AROM/blob/master/Figures/NCI_Class.png)

### SNOMED

Here is the definition/description of __Skin_structure_of_head__ class in its original ontology (SNOMED (Ont3)) :

![Skin_structure_of_head](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED_Class.png)


# Output Merged Ontology
These correspondences will lead to the merging of the three matched classes: __Skin_of_head__ (from FMA (Ont1)), __Head_Skin__ (from NCI (Ont2)), and __Skin_structure_of_head__ (from SNOMED (Ont3)).


The following figures show the merged class in our output ontology that resulted from the merging of the three LargeBio ontologies. The merged class captures all knowledge (axioms and expressions) defining the three equivalent classes. It has a unique code (__Code\_1379__) as an abbreviated name, and all entities (existing in its description), that have been merged, also have their corresponding unique code as an abbreviated name. Besides, the merged class have three added labels (framed in red), which mention the original abbreviated names of the classes that have been merged. We attached each name to its ontology number (ID) to directly see from which ontology it originates.

You can view and download all our merged ontologies from the [Results folder.](https://github.com/inesosman/AROM/tree/master/Results)


### Non Refactored Version

![MergedClass](https://github.com/inesosman/AROM/blob/master/Figures/MergedClass.png)

For the non customized version, axioms are exactly like the original ones.
We could not upload this merged ontology because of its big size.

### Refactored Version

![RefactoredMergedClass](https://github.com/inesosman/AROM/blob/master/Figures/RefactoredMergedClass.png)

For the customized version, axioms are like the original ones, except that the IRIs of all mentioned entities are customized.
You can view and download this [merged ontology](https://github.com/inesosman/AROM/blob/master/Results/Merge_LargeBio_Refactored.owl) from the Results folder.
# Conclusion

* Our final ontology is complete, in the sense that it retains all entities, axioms and hierarchies from the input ontologies.
* Running AROM for merging the *Large Biomedical Ontologies* do not exceed **one minute**.

