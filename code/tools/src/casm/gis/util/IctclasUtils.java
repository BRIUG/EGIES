package casm.gis.util;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class IctclasUtils {

	/*
	 * This method is used for Chinese word segmentation of ictclas
	 * 2017-04-04 16:00:27
	 */
	public static String ictclasSplit(String str){
		String result = "";
		try{
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			String argu = ".";
			if (testICTCLAS50.ICTCLAS_Init(argu.getBytes("GB2312")) == false){
				System.out.println("Init Fail!");
				return "";
			}

			testICTCLAS50.ICTCLAS_SetPOSmap(2);

			byte nativeBytes[] = testICTCLAS50.ICTCLAS_ParagraphProcess(str.getBytes("GB2312"), 0, 1);//分词处理
			result = new String(nativeBytes, 0, nativeBytes.length, "GB2312");

			testICTCLAS50.ICTCLAS_Exit();
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
}
