package casm.gis.util;

import casm.gis.util.CommonUtils;
import junit.framework.TestCase;

public class TestCommonUtils extends TestCase{

	public void test1() throws Exception{
		String inputPath = "O:/WorkSpace/JAVA/EGISTest/sampleTest/18.egisretrieve/1.sourcepath/heritrixtool/jobs/output/mirror/www.cneb.gov.cn/ARTI1379467985095419.shtml";
		String content = CommonUtils.getContent(inputPath);
		System.out.println(content);
	}
}
