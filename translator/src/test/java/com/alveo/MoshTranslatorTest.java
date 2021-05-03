package com.alveo;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoshTranslatorTest {
	MoshTranslator m;
	String outputPath = "src/main/resources/translated.tsv";
	String inputPath = "src/main/resources/input.tsv";
	String rowPath = "src/main/resources/rowmap.tsv";
	String columnPath = "src/main/resources/colmap.tsv";

	
	
	@BeforeEach
	void init() throws Exception {
		m = new MoshTranslator(rowPath, 
							   columnPath, 
							   inputPath, outputPath);
	}

	@Test
	void rowMap_Contains_Values() throws Exception {
		m.readMapFile(rowPath);
		Assertions.assertTrue(m.getRowMap().size() > 0);
	}

	@Test
	void column_Map_Contains_Values() throws Exception {
		m.readMapFile(columnPath);
		Assertions.assertTrue(m.getColumnMap().size() > 0);
	}

	@Test
	void column_Map_Is_Populated_Correctly() throws Exception {
		m.readMapFile(columnPath);
		HashMap<String, String> map = m.getColumnMap();
		Assertions.assertEquals(map.get("COL0"), "OURID");
		Assertions.assertEquals(map.get("COL1"), "OURCOL1");
		Assertions.assertEquals(map.get("COL3"), "OURCOL3");

	}

	@Test
	void rowMap_Is_Populated_Correctly() throws IOException {
		m.readMapFile(rowPath);
		HashMap<String, String> map = m.getRowMap();
		Assertions.assertEquals(map.get("ID2"), "OURID2");
		Assertions.assertEquals(map.get("ID3"), "OURID3");

	}

	@Test
	void translation_is_taking_place() throws IOException {

		File tempFile = new File(outputPath);
		tempFile.delete();

		Assertions.assertTrue(!tempFile.exists());

		m.filterTsv();

		Assertions.assertTrue(tempFile.exists());

	}


}
