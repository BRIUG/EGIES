package casm.gis.test;

import java.io.File;

import casm.gis.domain.Index;
import junit.framework.TestCase;

/*
 * Test class
 * 2017-05-29 14:13:45
 */
public class Test extends TestCase{

	public void test1(){
		String className = Index.class.getSimpleName();
		System.out.println(className);
	}
	
	public void test2(){
		String path = "O:\\WorkSpace\\JAVA\\EGISTest\\sampleTest\\13.egissearch\\3.news\\1.txt";
		File file = new File(path);
		file.delete();
	}
}
