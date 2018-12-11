# **AROM** (**A**lignments **R**euse for **O**ntology **M**erging)
A novel approach for refactoring and merging multiple ontologies at a time using alignments.
Here is a merge example.


# Input Alignments

These are three equivalence correspondences from the "Large Biomedical Ontologies" OAEI reference alignments.

### FMA-NCI Alignment

The first correspondence matches the __Skin_of_head__ class (in FMA) to the __Head_Skin__ class (in NCI):

![FMA-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-NCI.png)

### SNOMED-NCI Alignment

The second correspondence matches the __Skin_structure_of_head__ class (in SNOMED) to the __Head_Skin__ class (in NCI):

![SNOMED-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)

### FMA-SNOMED Alignment

The third correspondence matches the __Skin_of_head__ class (in FMA) to the __Skin_structure_of_head__ class (in SNOMED):

![FMA-SNOMED alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)



# Input Ontologies


### FMA

Here is the definition/description of __Skin_of_head__ class in its original ontology (from FMA (Ont1)) :

![Skin_of_head](https://github.com/inesosman/AROM/blob/master/Figures/FMA_Class.png)

### NCI

Here is the definition/description of __Head_Skin__ class in its original ontology (from NCI (Ont2)) :

![Head_Skin](https://github.com/inesosman/AROM/blob/master/Figures/NCI_Class.png)

### SNOMED

Here is the definition/description of __Skin_structure_of_head__ class in its original ontology (from SNOMED (Ont3)) :

![Skin_structure_of_head](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED_Class.png)


# Output Merged Ontology
These correspondences will lead to the merging of the three matched classes: __Skin_of_head__ (from FMA (Ont1)), __Head_Skin__ (from NCI (Ont2)), and __Skin_structure_of_head__ (from SNOMED (Ont3)).


The following figures show the merged class (__Code_1379__) in our output ontology that resulted from the merging of the three LargeBio ontologies. It captures all knowledge (axioms and expressions) defining the three equivalent classes. It has a unique code (Code\_1379) as an abbreviated name, and all entities (existing in its description), that have been merged, also have their corresponding unique code as an abbreviated name.  Besides, it will have three added labels (framed in red), which mention the original abbreviated names of the classes that have been merged. We attached each name to its ontology number (ID) to directly see from which ontology it originates.


### Non Refactored Version

![MergedClass](https://github.com/inesosman/AROM/blob/master/Figures/MergedClass.png)

For the non customized version, axioms are exactly like the original ones.

### Refactored Version

![RefactoredMergedClass](https://github.com/inesosman/AROM/blob/master/Figures/RefactoredMergedClass.png)

For the customized version, axioms are like the original ones, except that the IRIs of all mentioned entities are customized.

