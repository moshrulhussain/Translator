package com.alveo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author moshrul
 *
 *	<p>MoshTranslator will read a tab separated file, with column headers and row headers.
 *  <p>It will then filter the file based on the specified rows and columns in 2 separate mapping files.
 *  <p>Subsequently, it will also convert the row headers and column headers to custom values based on the mapping file
 *  and output the result into a new file.
 *
 *  <p>Usage is as follows:<p> <i>MoshTranslator [rowMappingFilename] [columnMappingFilename] [input filename] [output filename]</i>
 *
 *	<p>Input file and output files are tab-separated files.
 *
 */
public class MoshTranslator {

	public String inputFile;
	public String outputFile;
	
	private BufferedReader buf;
	
	/**
	 * Contains the mapping and filters for columns
	 */
	private HashMap<String,String> columnMap = new HashMap<String, String>();
	
	/**
	 * Contains the mapping and filters for rows
	 */
	private HashMap<String,String> rowMap = new HashMap<String, String>();
	
	/**
	 * Holds the index of each column that we want to keep based on column filter.
	 */
	private List<Integer> colIndex = new ArrayList<Integer>();
	
	public MoshTranslator() {
		
	}
	
	public MoshTranslator(String rowFile,String colFile,String input, String output) throws Exception {
		columnMap = readMapFile(colFile);
		rowMap = readMapFile(rowFile);
		inputFile=input;
		outputFile=output;
	}
	
	
	
	/**
	 * 
	 * <p>Reads any mapping file, be it column or row and creates a HashMap
	 * 
	 * <p>Each map file is a tab separated file in the following format:
	 * 
	 * <p>input1	output1
	 * <p>input2	output2
	 * <p>..
	 * <p>inputn	outputn
	 * 
	 * @param filename
	 * @return HashMap of mappings
	 * @throws IOException in case of any problems while reading file
	 * 

	 */
	public HashMap<String,String> readMapFile(String filename) throws IOException {
		HashMap<String,String> valueMap = new HashMap<String,String>();		
        List<String> cols = new ArrayList<String>();        
		
        try{
    	    buf = new BufferedReader(new FileReader(filename));     
	        String strRead;   
	        while ((strRead=buf.readLine())!=null) {
	        	cols = Arrays.asList(strRead.split("\t"));       	
	        	if(cols.size() != 2)
	        		continue;     	
	        	valueMap.put(cols.get(0),cols.get(1));    	
	       	 }
	    } catch(IOException e){
	        e.printStackTrace();
	    } finally {
	    	buf.close();  
	    }
			
		return valueMap;
	}
	
	
	public HashMap<String,String> getRowMap() {
		return rowMap;
	}
	
	
	public HashMap<String,String> getColumnMap() {
		return columnMap;
	}
	
	
	/**
	 * 
	 * <p>Reads in the input file, filters which rows and columns to display based on
	 * what is specified in the row and column mapping files.
	 * <p>Finally, also maps the filtered values to custom values as per the mapping file before writing out to a file.
	 * 
	 * @throws IOException in case of any problems while reading or writing to a file
	 */
	public void filterTsv() throws IOException {
		List<List<String>> rows = new ArrayList<>();
		List<String> cols = new ArrayList<String>();
		List<String> head = new ArrayList<String>();

		try {
			buf = new BufferedReader(new FileReader(inputFile));  
			
			// determine which column to filter based on columnMap
			String strRead = buf.readLine();
			String[] myline = strRead.split("\t");

			for (int i = 0; i < myline.length; i++) {
				String mappedValue = columnMap.get(myline[i]);
				if (mappedValue != null) {
					colIndex.add(i);
					head.add(mappedValue);
				}
			}
			rows.add(head);

			while ((strRead = buf.readLine()) != null) {
				cols = new ArrayList<String>();
				myline = strRead.split("\t");
				
				//skip row if not in rowMap
				if (rowMap.get(myline[0]) == null)
					continue;
				
				for (Integer index : colIndex) {
					if (index < myline.length) {
						if (index == 0)
							cols.add(rowMap.get(myline[0]));
						else
							cols.add(myline[index]);
					}
				}
				rows.add(cols);
			}

			writeTsv(rows, outputFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			buf.close();
		}
	}
	
	
	/**
	 * 
	 * Takes a 2-dimensional List of Strings and writes to a file
	 * as a tab-separated file.
	 * 
	 * @param rows List of Lists of Strings
	 * @param filename name of output file.
	 * @throws IOException in case of any problems when writing to the file.
	 */
	public static void writeTsv(List<List<String>> rows, String filename) throws IOException 
	{  
	    FileWriter fileWriter = new FileWriter(filename);
	    PrintWriter printWriter = new PrintWriter(fileWriter);
	    
		// print row and columns
		for (List<String> r : rows) {
			for (String s : r) {
				printWriter.print(s + "\t");
			}
			printWriter.print("\n");
		}
	    printWriter.close();
	}

	/**
	 * 
	 * Prints the contents of the row and column Maps
	 * 
	 */
	public void printMappings() {	
		System.out.println("Printing rowsMap. ");
		for (Map.Entry<String, String> entry : rowMap.entrySet()) {
		    System.out.println(entry);
		}
		
		System.out.println("Printing columnMap. ");
		for (Map.Entry<String, String> entry : columnMap.entrySet()) {
		    System.out.println(entry);
		}
	}
	
		
	public static void main(String[] args) throws Exception {
		
		if(args.length != 4) { 
			System.out.println("Usage: MoshTranslator [row filename] [col filename] [input filename] [output filename]");
			return;
		}
		
		MoshTranslator m = new MoshTranslator(args[0],args[1],args[2],args[3]);
		m.filterTsv();
		
	}
	
}
