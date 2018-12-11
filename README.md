# **AROM** (**A**lignments **R**euse for **O**ntology **M**erging)
A novel approach for refactoring and merging multiple ontologies at a time using alignments


# Example


## Input Alignments

These are three equivalence correspondences from the "Large Biomedical Ontologies" OAEI reference alignments.

#### FMA-NCI Alignment

The first correspondence matches the __Skin_of_head__ class (in FMA) to the __Head_Skin__ class (in NCI):

![FMA-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-NCI.png)

#### SNOMED-NCI Alignment

The second correspondence matches the __Skin_structure_of_head__ class (in SNOMED) to the __Head_Skin__ class (in NCI):

![SNOMED-NCI alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)

#### FMA-SNOMED Alignment

The third correspondence matches the __Skin_of_head__ class (in FMA) to the __Skin_structure_of_head__ class (in SNOMED):

![FMA-SNOMED alignment](https://github.com/inesosman/AROM/blob/master/Figures/FMA-SNOMED.png)



## Input Ontologies


#### FMA

Here is the definition/description of __Skin_of_head__ class in its original ontology (from FMA (Ont1)) :

![Skin_of_head](https://github.com/inesosman/AROM/blob/master/Figures/FMA_Class.png)

#### NCI

Here is the definition/description of __Head_Skin__ class in its original ontology (from NCI (Ont2)) :

![Head_Skin](https://github.com/inesosman/AROM/blob/master/Figures/NCI_Class.png)

#### SNOMED

Here is the definition/description of __Skin_structure_of_head__ class in its original ontology (from SNOMED (Ont3)) :

![Skin_structure_of_head](https://github.com/inesosman/AROM/blob/master/Figures/SNOMED_Class.png)


==> These correspondences will lead to the merging of the three matched classes: "Skin_of_head" (from FMA (Ont1)), "Head_Skin" (from NCI (Ont2)), and "Skin_structure_of_head" (from SNOMED (Ont3)).
