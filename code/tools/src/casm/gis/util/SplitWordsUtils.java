package casm.gis.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import casm.gis.config.ConstantParams;

public class SplitWordsUtils {

	/*
	 * Use IKAnalyzer for word segmentation tools
	 * 2017-04-04 10:10:06
	 */
	public static String ikSplit(String str){
		
		return ikSplit(str, ConstantParams.BLANK);
	}
	
	public static String ikSplit(String str,String mark){
		String result = "";
		if(StringUtils.isEmpty(str)){
			return result;
		}
		try {
			byte[] bt = str.getBytes();
			InputStream ip = new ByteArrayInputStream(bt);
			Reader reader = new InputStreamReader(ip);
			IKSegmenter iks = new IKSegmenter(reader,true);
			Lexeme t;
			while((t=iks.next()) != null){
				result += t.getLexemeText()+mark;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String ikSplit(String str,String mark,boolean b){
		String result = "";
		if(StringUtils.isEmpty(str)){
			return result;
		}
		try {
			byte[] bt = str.getBytes();
			InputStream ip = new ByteArrayInputStream(bt);
			Reader reader = new InputStreamReader(ip);
			IKSegmenter iks = new IKSegmenter(reader,b);
			Lexeme t;
			while((t=iks.next()) != null){
				result += t.getLexemeText()+mark;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String ikSplit(String str,boolean b){
		return ikSplit(str,ConstantParams.BLANK,b);
	}
	
	public static String hanLPSplit(String str){
		String result = "";
		if(StringUtils.isEmpty(str)){
			return result;
		}
		try {
	        Segment segment = HanLP.newSegment().enablePlaceRecognize(true).enableCustomDictionary(true);
//					.enableOrganizationRecognize(true);
	            List<Term> termList = segment.seg(str);
	            StringBuilder places = new StringBuilder();
	            for (int i = 0; i < termList.size(); i++) {
	                String s = termList.get(i).toString();
	                if (Pattern.matches(".*/ns$", s)) {
	                    places.append(s.replace("/ns", ""));
	                }else if (Pattern.matches(".*/nsf$", s)) {
	                    places.append(s.replace("/nsf", ""));
	                }
	            }
	            result +=places;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
