package casm.gis.file;

import java.io.File;

import casm.gis.file.OPFile;
import junit.framework.TestCase;

public class TestOPFile extends TestCase {

	public void test1() {
		String inputPath = "O:/WorkSpace/JAVA/EGISTest/sampleTest/1.2017/01/01/ARTI1483268806908910.shtml";
		String outputPath = "Q:/one/testContent.txt";
		OPFile.parserCNEBs(inputPath, outputPath);
	}

	public void test2() {
//		String inputPath = "O:/WorkSpace/JAVA/EGIES/tools/egies/1.sourcepath/heritrixtool/jobs/output/mirror";
		String inputPath = "O:/WorkSpace/JAVA/EGIES/code/crawler/jobs/output/mirror";
		OPFile.saveDataSuper(inputPath);
	}

	public void test3() {
		String inputPath = "Q:/cneb/jobs/output/mirror/www.cneb.gov.cn";
		String outputPath = "Q:/cneb/1.title";
		File sourceFile = new File(inputPath);
		File targetFile = new File(outputPath);
		OPFile.copyFile(sourceFile, targetFile);
	}
}
