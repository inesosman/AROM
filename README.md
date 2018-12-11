# AROM
A novel approach for refactoring and merging multiple ontologies at a time using alignments


# Example

## Correspondences / Cells

These are three equivalence correspondences from the "Large Biomedical Ontologies" OAEI reference alignments.

### Correspondences 1

* The first correspondence matches the "Skin_of_head" class (in FMA) to the "Head_Skin" class (in NCI):

![FMA-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-NCI.png)

### Correspondences 2

* The second correspondence matches the "Skin_structure_of_head" class (in SNOMED) to the "Head_Skin" class (in NCI):

![SNOMED-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)

### Correspondences 3

* The third correspondence matches the "Skin_of_head" class (in FMA) to the "Skin_structure_of_head" class (in SNOMED):

![FMA-SNOMED alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)


## Classes to be merged

==> These correspondences will lead to the merging of the three matched classes: "Skin_of_head" (from FMA (Ont1)), "Head_Skin" (from NCI (Ont2)), and "Skin_structure_of_head" (from SNOMED (Ont3)).

### Class 1

* Here is the definition/description of "Skin_of_head" class in its original ontology (from FMA (Ont1)) :

![Skin_of_head](https://github.com/inesosman/AROM/blob/master/Figures/FMA_Class.png)

### Class 2

* Here is the definition/description of "Head_Skin" class in its original ontology (from NCI (Ont2)) :

![Head_Skin](https://github.com/inesosman/AROM/blob/master/Figures/NCI_Class.png)

### Class 3

* Here is the definition/description of "Skin_structure_of_head" class in its original ontology (from SNOMED (Ont3)) :

![Skin_structure_of_head](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED_Class.png)
