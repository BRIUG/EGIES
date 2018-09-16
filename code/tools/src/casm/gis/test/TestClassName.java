package casm.gis.test;

import casm.gis.retrieve.spi.SolrService;
import junit.framework.TestCase;

public class TestClassName extends TestCase{

	/*
	 * Create a test method to test the content of the reflection
	 * 2017-04-30 16:29:08
	 */
	public void test1(){
		String className = SolrService.class.getName();
		System.out.println(className);
	}
}
