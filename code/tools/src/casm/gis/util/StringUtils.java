package casm.gis.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import casm.gis.config.ConstantParams;

public class StringUtils {

	public List<String> allPathResult = new ArrayList<String>();
	
	public StringUtils(String inputPath){
		getAllPath(inputPath);
	}
	
	public static boolean isEmpty(String str){
		
		boolean b = false;
		
		if (null == str || "".equals(str)) {
			b = true;
		}
		return b;
	}
	
	public static boolean isNotEmpty(String str){
		
		boolean b = false;
		
		if (null != str && !"".equals(str)) {
			b = true;
		}
		return b;
	}
	
	public static String getContentUseRegex(String regexString,String sourceString){
		String result = "";
		if(isEmpty(regexString) || isEmpty(sourceString)){
			return result;
		}
		try {
			Pattern pattern = Pattern.compile(regexString);
			Matcher matcher = pattern.matcher(sourceString);
			if(matcher.find()){
				result = matcher.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getContentUseRegex(String regexString,String sourceString,String splitMark){
		String result = "";
		if(isEmpty(regexString) || isEmpty(sourceString)){
			return result;
		}
		if(isEmpty(splitMark)){
			splitMark = ConstantParams.CHANGE_LINE;
		}
		try {
			Pattern pattern = Pattern.compile(regexString);
			Matcher matcher = pattern.matcher(sourceString);
			while(matcher.find()){
				result += matcher.group()+splitMark;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void getAllPath(String inputPath){
		
		try {
			File file = new File(inputPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if(f.isDirectory()){
					getAllPath(f.getAbsolutePath());
				}else{
//					System.out.println(f.getAbsolutePath());
					allPathResult.add(f.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getContent(String inputPath){
		String result = "";
		if(isEmpty(inputPath)){
			return result;
		}
		
		try {
			File file = new File(inputPath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";
			while((temp=br.readLine()) != null){
				result += (temp + ConstantParams.CHANGE_LINE);
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	private static String inputStreamToString(InputStream is, String encoding) throws IOException{  
        
        int count = is.available();
          
        byte[] b = new byte[count]; //
        int readCount = 0; // 已经成功读取的字节的个数  
        while (readCount < count) {  
          readCount += is.read(b, readCount, count - readCount);  
        }  
          
        return new String(b, 0, count, encoding);  
	}
	
	public static boolean string2File(String str,String outputPath){
		boolean b = false;
		if(isEmpty(outputPath)){
			return b;
		}
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			File file = new File(outputPath);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(str);
			b = true;
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		} finally{
			try {
				if(bw != null){
					bw.close();
				}
				
				if(fw != null){
					fw.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return b;
	}
	
	public static String getFileNameFromPath(String path){
		String result = "";
		if(isEmpty(path)){
			return result;
		}
		
		if(path.contains("/")){
			result = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."));
		}else if(path.contains("\\")){
			result = path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf("."));
		}else{
			result = path;
		}
		return result;
	}
	
	public static List<String> getContentFromPath(String inputPath){
		List<String> result = new ArrayList<String>();
		try {
			List<String> list = new ArrayList<String>();
			File file = new File(inputPath);
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			
			while((temp = br.readLine()) != null){
				result.add(temp);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getConfigParam(String params,String defaultValue,String fileName){
		String result = "";
		if(isEmpty(fileName) || isEmpty(params)){
			return result;
		}
		try {
			Properties properties = loadConfig(fileName);
			result = properties.getProperty(params);
			if(isEmpty(result)){
				result = defaultValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Properties loadConfig(String fileName){
		Properties properties = new Properties();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if(classLoader == null){
				classLoader = StringUtils.class.getClassLoader();
			}
			InputStream is = classLoader.getResourceAsStream(fileName);
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}
	
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
