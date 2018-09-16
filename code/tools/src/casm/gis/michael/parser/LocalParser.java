package casm.gis.michael.parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import casm.gis.config.ConstantParams;
import casm.gis.util.FullToponymyUtils;
import casm.gis.util.LngAndLatUtils;
import casm.gis.util.StringUtils;

public class LocalParser {

	/*
	 * This method is used to parse and extract local web content: set for news of China National Emergency Broadcasting website.
	 * Write all web pages to a local folder
	 * 2017-04-03 12:54:28
	 */
	public void parserCNEBNews(String inputPath,String outputPath){
		StringUtils su = new StringUtils(inputPath);
		List<String> allPath = su.allPathResult;
		for (String path : allPath) {
			String fileName = StringUtils.getFileNameFromPath(path);
			//Set the output type and name of the file
//			singleParserCNEBNews(htmlContent, outputPath+"/"+fileName+".txt");
			parserCNEBs(path, outputPath+"/"+fileName+".txt");
		}
	}
	
	/*
	 * Batch method for extracting titles
	 * 2017-04-04 10:45:24
	 */
	public void parserCNEBNewsTitle(String inputPath,String outputPath){
		StringUtils su = new StringUtils(inputPath);
		List<String> allPath = su.allPathResult;
		for (String path : allPath) {
			String htmlContent = StringUtils.getContent(path);
			String fileName = StringUtils.getFileNameFromPath(path);
			//Set the output type and name of the file
			singleParserCNEBNewsTitle(htmlContent, outputPath+"/"+fileName+".txt");
		}
	}
	
	/*
	 * Used to parse the content of a single web page: title, time, source, body, and write the content of the obtained web page to the local disk
	 * htmlContent:Web page
	 * outputPath:Local address
	 * 2017-04-03 13:17:16
	 */
	public void parserCNEBs(String inputPath,String outputPath){
		String result = "";
		try {
			String content = StringUtils.getContent(inputPath);
			//title
			Parser parserTitle1 = Parser.createParser(content, "UTF-8");
			Parser parserTitle2 = Parser.createParser(content, "UTF-8");
			
			// Get the title content of the web page: p tag name, class attribute name, title attribute value
			NodeFilter titleFilter1 = new AndFilter(new TagNameFilter("p"),
					new HasAttributeFilter("class", "title"));
			
			NodeFilter titleFilter2 = new AndFilter(new TagNameFilter("h3"),
					new HasAttributeFilter("id", "t_title_201309"));
			
			NodeList titleList1 = parserTitle1.parse(titleFilter1);
			NodeList titleList2 = parserTitle2.parse(titleFilter2);
			
			NodeList titleList = null;
			if(titleList1.size() != 0){
				titleList = titleList1;
			}else if(titleList2.size() != 0){
				titleList = titleList2;
			}
			
			String title = "";
			if(titleList != null){
				Node titleNode = titleList.elementAt(0);
				if(titleNode != null){
					//Get the title
					title = StringUtils.replaceBlank(titleNode.toPlainTextString());
				}
			}
			
			//Geographic location
//			String[] places = FullToponymyUtils.getFullTopo(title);
			String[] places = FullToponymyUtils.getFullTopoSuper(title);
			String place = "";
			Double lng = 0D;
			Double lat = 0D;
			//Place name latitude and longitude
			String pll = "";
			if(places.length <2){
				for(int i=0;i< places.length;i++){
					if(places[i] != null){
						place = places[i];
					}
				}
					//Latitude and longitude
					String ll = LngAndLatUtils.getLngAndLatAMap(place);
			        if(ll !=null){
				       	 String[] location = ll.split(",");
				       	 lng = Double.valueOf(location[0]).doubleValue();
				       	 lat = Double.valueOf(location[1]).doubleValue();
					}else{
						lng = 0D;
						lat = 0D;
					}
			   
					pll = "地理位置："+place+ConstantParams.CHANGE_LINE+"经纬度:"+lng+ConstantParams.TAB+lat+ConstantParams.CHANGE_LINE;
			}else{
				for(int i=0;i< places.length;i++){
					if(places[i] != null){
						place = places[i];
					}
					
					//Latitude and longitude
					String ll = LngAndLatUtils.getLngAndLatAMap(place);
					if(ll !=null){
						String[] location = ll.split(",");
						lng = Double.valueOf(location[0]);
						lat = Double.valueOf(location[1]);
					}
					pll += "地理位置"+(i+1)+"："+place+ConstantParams.CHANGE_LINE+"经纬度"+(i+1)+"："+lng+ConstantParams.TAB+lat+ConstantParams.CHANGE_LINE;
				}
			}
			parserTitle1.reset();
			parserTitle2.reset();
			
			//Time date, source source
			Parser parserDS1 = Parser.createParser(content, "UTF-8");
			Parser parserDS2 = Parser.createParser(content, "UTF-8");
			NodeFilter dsFilter1 = new AndFilter(new TagNameFilter("p"),
					new HasAttributeFilter("class", "subhead"));
			
			NodeFilter dsFilter2 = new AndFilter(new TagNameFilter("p"),
					new HasAttributeFilter("class", "info"));
			
			NodeList dsList1 = parserDS1.parse(dsFilter1);
			NodeList dsList2 = parserDS2.parse(dsFilter2);
			
			NodeList dsList = null;
			String date = "";
			String source = "";
			if(dsList1.size() != 0){
				dsList = dsList1;
				if(dsList != null){
					Node dsNode = dsList.elementAt(0);
					if(dsNode != null){
						//time
						date = dsNode.getFirstChild().toPlainTextString();
						//source
						source = dsNode.getLastChild().toPlainTextString();
					}
				}
			}else if(dsList2.size() != 0){
				dsList = dsList2;
				String dateSource = "";
				if(dsList != null){
					Node dsNode = dsList.elementAt(0);
					if(dsNode != null){
						dateSource = dsNode.toPlainTextString();
						String regDate = "[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}";
						String regSource = "来源[\\pP‘’“”][\u4E00-\u9FA5]+";
						Pattern patternDate = Pattern.compile(regDate);
						Pattern patternSource = Pattern.compile(regSource);
						Matcher matcherDate = patternDate.matcher(dateSource);
						Matcher matcherSource = patternSource.matcher(dateSource);
						// Finds if there is a character/string matching the regular expression in the string
						if(matcherDate.find()){
							date = matcherDate.group();
						}
						
						if(matcherSource.find()){
							source = matcherSource.group();
							source = source.replace("来源：", "");
						}
					}
				}
			}
			
			parserDS1.reset();
			parserDS2.reset();
			
			//details
			Parser parserDetail1 = Parser.createParser(content, "UTF-8");
			Parser parserDetail2 = Parser.createParser(content, "UTF-8");
			NodeFilter detailFilter1 = new AndFilter(new TagNameFilter("div"),
					new HasAttributeFilter("id", "content"));
			
			NodeFilter detailFilter2 = new AndFilter(new TagNameFilter("div"),
					new HasAttributeFilter("class", "con_cont"));
			
			NodeList detailList1 = parserDetail1.parse(detailFilter1);
			NodeList detailList2 = parserDetail2.parse(detailFilter2);
			
			NodeList detailList = null;
			if(detailList1.size() != 0){
				detailList = detailList1;
			}else if(detailList2.size() != 0){
				detailList = detailList2;
			}
			
			String detail = "";
			if(detailList != null){
				Node detailNode = detailList.elementAt(0);
				if(detailNode != null){
					//detail
					detail = StringUtils.replaceBlank(detailNode.toPlainTextString());
					detail = detail.replaceAll("([a-zA-Z])", "").replaceAll("([\\&|;])", "");
				}
			}
			
			parserDetail1.reset();
			parserDetail2.reset();
			//Integrate the title, time, source, and body parts into one result
			result = "标题："+title + ConstantParams.CHANGE_LINE  +pll+ ConstantParams.CHANGE_LINE + "时间："+date + ConstantParams.CHANGE_LINE+ 
					"来源："+source + ConstantParams.CHANGE_LINE + "详细信息："+detail;
			
			//Write all parsed content locally
			StringUtils.string2File(result, outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method is used to save the title content of the web page extraction.
	 * 2017-04-04 10:34:00
	 */
	public void singleParserCNEBNewsTitle(String htmlContent,String outputPath){
		try {
			Parser parser1 = Parser.createParser(htmlContent, "UTF-8");
			Parser parser2 = Parser.createParser(htmlContent, "UTF-8");
			
			// Get the title content of the web page: p tag name, class attribute name, title attribute value
			NodeFilter titleFilter1 = new AndFilter(new TagNameFilter("p"),
					new HasAttributeFilter("class", "title"));
			
			NodeFilter titleFilter2 = new AndFilter(new TagNameFilter("h3"),
					new HasAttributeFilter("id", "t_title_201309"));
			
			NodeList titleList1 = parser1.parse(titleFilter1);
			NodeList titleList2 = parser2.parse(titleFilter2);
			
			NodeList titleList = null;
			if(titleList1.size() != 0){
				titleList = titleList1;
			}else if(titleList2.size() != 0){
				titleList = titleList2;
			}
			
			String title = "";
			if(titleList != null){
				Node titleNode = titleList.elementAt(0);
				if(titleNode != null){
					//title
					title = StringUtils.replaceBlank(titleNode.toPlainTextString());
				}
			}
//			System.out.println(title);
			parser1.reset();
			parser2.reset();
			//Write all parsed content locally
			StringUtils.string2File(title, outputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
