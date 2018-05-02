# percolator-peptide-list-dumper
This program reads the XML output of a Percolator analysis (Percolator run
with the -X option), and filters the list of peptides by the user-
supplied q-value and PSMs by the user-supplied q-value and generates
a tab-delimited text file containing unmodified peptide sequences and
associated spectral counts.

# How to run:
Ensure Java is installed on your system: https://java.com/en/download/

Download the latest release: https://github.com/metagomics/percolator-peptide-list-dumper/releases

Run with: ``java -jar peptideDumper.jar -p <peptide q-value cutoff> -s <psm q-value cutoff> -f <percolator output XML file> >peptide_psm_count_list.txt``

To create a file called ``peptide_psm_count_list.txt`` containing the data.
