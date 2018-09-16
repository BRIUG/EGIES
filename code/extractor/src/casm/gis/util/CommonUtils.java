package casm.gis.util;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import casm.gis.util.StringUtils;

public class CommonUtils {  
  
		public static String getContent(String inputPath) throws Exception{
			
			String result = "";
			if(StringUtils.isEmpty(inputPath)){
				return result;
			}
	        String charset = "UTF-8";   
	         // Read bytes into characters
	        FileInputStream inputStream = null;
	        InputStreamReader reader = null;
	         try {   
	        	 inputStream = new FileInputStream(inputPath);   
	        	 reader = new InputStreamReader(inputStream, charset);   
	        	 StringBuffer buffer = new StringBuffer();   
	        	 char[] buf = new char[64];   
	        	 int count = 0;   
	            while ((count = reader.read(buf)) != -1) {   
	                buffer.append(buf, 0, count);   
	            }   
	            result = buffer.toString();
	         } finally {   
	            reader.close();   
	         }   
			return result;
		}
}  