package casm.gis.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import casm.gis.config.ConstantParams;

public class SimilaryUtils {
	
	/*
	 * Web page deduplication: minimum edit distance algorithm
	 * 2017-04-09 13:36:50
	 */
	public static int med(String target,String source){
		int result = 0;
		
		int n = target.length();
		int m = source.length();
		int[][] distance = new int[n+1][m+1];
		distance[0][0] = 0;
	
		for(int i=1;i<=n;i++){
			distance[i][0] = distance[i-0][0]+ins_cost(target.charAt(i-1));
		}
		for(int j=1;j<=m;j++){
			distance[0][j] = distance[0][j-1]+ins_cost(target.charAt(j-1));
		}
		
		for(int i=1;i<=n;i++){
			for(int j=1;j<=m;j++){
				int ins = distance[i-1][j]+ins_cost(target.charAt(i-1));
				int sub = distance[i-1][j-1] + subs_cost(target.charAt(i-1), source.charAt(j-1));
				int del = distance[i][j-1] + del_cost(source.charAt(j-1));
				distance[i][j] = min(ins,min(sub,del));
			}
		}
		result = distance[n][m];
		return result;
	}
	
	/*
	 * Web page deduplication: minimum edit distance algorithm, minimum step to count minimum edit distance
	 * 2017-04-09 13:43:38
	 */
	private static int min(int d1,int d2){
		return d1<d2?d1:d2;
	}
	private static int ins_cost(char c){
		return 1;
	}
	
	private static int del_cost(char c){
		return 1;
	}
	
	private static int subs_cost(char c1,char c2){
		return c1 != c2 ? 2:0;
	}
	
	public static void splitWords(String inputPath,String outputPath){
		String str = StringUtils.getContent(inputPath);
		String splitStr = SplitWordsUtils.ikSplit(str,ConstantParams.CHANGE_LINE);
		StringUtils.string2File(splitStr, outputPath);
	}
	
	public static List<String> getAllWords(String inputPath){
		List<String> list = new ArrayList<String>();
		try{
			File file = new File(inputPath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";
			while((temp=br.readLine()) != null){
				list.add(temp);
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
			return list;
	}
	
	public static List<String> getAllWords(String inputPath,int flag){
		List<String> list = new ArrayList<String>();
		try{
			File file = new File(inputPath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";
			while((temp=br.readLine()) != null){
				String[] temps = temp.split(ConstantParams.TAB);
				if(flag > temps.length-1){
					list.add(temps[temps.length-1]);
				}else{
					list.add(temps[flag]);
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static Double getWordNum(String word,List<String> list){
		Double result = 0.0;
		for(String w : list){
			if(w.equals(word)){
				result ++;
			}
		}
		return result;
	}
	
	public static void getTF(String inputPath,String outputPath){
		String result = "";
		List<String> list = getAllWords(inputPath);
		Double allNums = Double.valueOf(list.size());
		for(String w : list){
			Double num = getWordNum(w, list);
			Double TF = num/allNums;
			result += w + ConstantParams.TAB + TF + ConstantParams.CHANGE_LINE;
		}
		StringUtils.string2File(result, outputPath);
	}
	
	public static Map<String,Double> getMapFromFile(String inputPath){
		Map<String,Double> map = new HashMap<String,Double>();
		try{
			File file = new File(inputPath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";
			while((temp=br.readLine()) != null){
				String[] temps = temp.split(ConstantParams.TAB);
				if(temps.length > 1){
					map.put(temps[0], Double.valueOf(temps[1]));
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String,Double> getMapFromFile(String inputPath,int[] flag){
		Map<String,Double> map = new HashMap<String,Double>();
		try{
			File file = new File(inputPath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String temp = "";
			while((temp=br.readLine()) != null){
				String[] temps = temp.split(ConstantParams.TAB);
				if(temps.length > 1){
					map.put(temps[flag[0]], Double.valueOf(temps[flag[1]]));
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static void getDF(String inputPath,String inputPathAll,String outputPath){
		StringUtils su = new StringUtils(inputPathAll);
		List<String> filesName = su.allPathResult;
		String result = "";
		try {
			File file = new File(inputPath);
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String temp = "";

			while ((temp = br.readLine()) != null) {
				Double df = 0.0;
				String[] temps = temp.split(ConstantParams.TAB);
				
				for (String fileName : filesName) {
					String f = StringUtils.getFileNameFromPath(fileName);
					String content = StringUtils.getContent(fileName);
					if(content.contains(temps[0])){
						df ++;
					}
				}
				result += temp+ConstantParams.TAB + df + ConstantParams.CHANGE_LINE;
			}
			br.close();
			StringUtils.string2File(result, outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getTFIDF(String inputPath,String inputPathAll,String outputPath){
		String result = "";
		StringUtils su = new StringUtils(inputPathAll);
		List<String> filesName = su.allPathResult;
		Double nums = Double.valueOf(filesName.size());
//		System.out.println("nums:"+nums);
		try {
			File file = new File(inputPath);
			InputStream is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String temp = "";

			while ((temp = br.readLine()) != null) {
				Double tfidf = 0.0;
				String[] temps = temp.split(ConstantParams.TAB);
				Double idf = Math.log(nums/Double.valueOf(temps[2]));
//				System.out.println("temps[2]:"+temps[2]);
//				System.out.println("idf:"+idf);
				tfidf = Double.valueOf(temps[1])*idf;
				result += temp+ConstantParams.TAB + tfidf + ConstantParams.CHANGE_LINE;
			}
			br.close();
			StringUtils.string2File(result, outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getSimilarity(String inputPath1,String inputPath2,String outputPath){
		try {
			List<String> list1 = getAllWords(inputPath1, 1);
			List<String> list2 = getAllWords(inputPath2, 1);
			int len = 0;
			int len1 = list1.size();
			int len2 = list2.size();
			if(len1 < len2){
				len = len1;
			}else{
				len = len2;
			}
			
			Map<String,Double> map1 = getMapFromFile(inputPath1);
			Map<String,Double> map2 = getMapFromFile(inputPath2);
			Double fz = 0.0;
			Double fm = 0.0;
			Double fma = 0.0;
			Double fmb = 0.0;
			
			Set<String> set1 = map1.keySet();
			Iterator<String> iter1 = set1.iterator();
			while(iter1.hasNext()){
				String key = iter1.next();
				Double value = map1.get(key);
				fma += value*value;
			}
			
			Set<String> set2 = map2.keySet();
			Iterator<String> iter2 = set2.iterator();
			while(iter2.hasNext()){
				String key = iter2.next();
				Double value = map2.get(key);
				fmb += value*value;
			}
			
			fm = Math.sqrt(fma)*Math.sqrt(fmb);
			
			for(int i=0; i<len; i++){
				String key1 = list1.get(i);
				String key2 = list2.get(i);
				fz += Double.valueOf(key1)*Double.valueOf(key2);
			}
			
			String result = fz/fm+ConstantParams.CHANGE_LINE;
			System.out.println("余弦夹角(文本相识度）： "+result);
			StringUtils.string2File(result, outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
