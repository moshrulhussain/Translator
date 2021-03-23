# How to Run Translator

* To run, type in the following command:

`java -cp translator-0.0.1-SNAPSHOT.jar com.alveo.MoshTranslator` 

* This will give you the usage instructions:

`Usage: MoshTranslator [row filename] [col filename] [input filename] [output filename]`

* You will then need to supply the path of the files, which can be the filename if inside the current folder
or absolute path if elsewhere. For example: 

`java -cp translator-0.0.1-SNAPSHOT.jar com.alveo.MoshTranslator rowmap.tsv colmap.tsv input.tsv myout.tsv`

This wil take the input tsv file, and filter based on rowmap.tsv & colmap.tsv and then return the translated output file myout.tsv