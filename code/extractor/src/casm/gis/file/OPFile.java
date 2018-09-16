package casm.gis.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
import casm.gis.full.config.InitParams;
import casm.gis.util.CommonUtils;
import casm.gis.util.DataBaseUtils;
import casm.gis.util.FullToponymyUtils;
import casm.gis.util.LngAndLatUtils;
import casm.gis.util.SplitEmergencyTypeUtils;
import casm.gis.util.SplitWordsUtils;
import casm.gis.util.StringUtils;

/*
 * Operator File
 * 2017-06-03 22:27:16
 */
public class OPFile {

	/*
	 * Used to parse the content of a single web page: title, time, source, body, and write the content of the obtained web page to the local disk htmlContent: web page outputPath: local address
	 * 2017-04-03 13:17:16
	 */
	public static void parserCNEBs(String inputPath, String outputPath) {
		
		String result = "";
		try {
			String content = StringUtils.getContent(inputPath);
			//title
			Parser parserTitle1 = Parser.createParser(content, "UTF-8");
			Parser parserTitle2 = Parser.createParser(content, "UTF-8");
			
			// Get the title content of the web page: p tag name, class attribute name, title attribute value
			NodeFilter titleFilter1 = new AndFilter(new TagNameFilter("p"),
					new HasAttributeFilter("class", "title"));
			
			// Get the title content of the web page: h3 tag name, id attribute name, t_title_201309 attribute value
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
			   
					pll = "Geographic location："+place+ConstantParams.CHANGE_LINE+"Latitude and longitude:"+lng+ConstantParams.TAB+lat+ConstantParams.CHANGE_LINE;
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
					pll += "Geographic location"+(i+1)+"："+place+ConstantParams.CHANGE_LINE+"Latitude and longitude"+(i+1)+"："+lng+ConstantParams.TAB+lat+ConstantParams.CHANGE_LINE;
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
					//Get the title
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
	
	//Save data locally and in the database
	@SuppressWarnings("resource")
	public static void saveDataSuper(String inputPath){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		String result = "";
		String filePath = StringUtils.getConfigParam(InitParams.TXT_FILEPATH,"", InitParams.PROPERTIES_NAME);
		try {
			conn = DataBaseUtils.getConnection();
			String sqlString = "INSERT INTO t_news (newsTitle,type,level,location,longitude,latitude,newsTime,sourceNet,detail,sourceUrl,filePath,columnId) VALUES ";
			StringBuilder stringBuilder = null;
			File file = new File(inputPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					if (f.getAbsolutePath() != null) {
						saveDataSuper(f.getAbsolutePath());// Recursive
					}
				} else {

					String sourcePath = f.getPath();
					String content = CommonUtils.getContent(sourcePath);

					String fileName = StringUtils.getFileNameFromPath(sourcePath);
					String fileFullPath = filePath + fileName + ".txt";

					int position = sourcePath.indexOf("mirror") + 6;
					String sourceUrl = sourcePath.substring(position);
					sourceUrl = "http:/" + sourceUrl.replace("\\", "/");
					// title
					Parser parserTitle1 = Parser.createParser(content, "UTF-8");
					Parser parserTitle2 = Parser.createParser(content, "UTF-8");
					
					// Get the title content of the web page: p tag name, class attribute name, title attribute value
					NodeFilter titleFilter1 = new AndFilter(new TagNameFilter("p"),	new HasAttributeFilter("class", "title"));
					
					// Get the title content of the web page: h3 tag name, id attribute name, t_title_201309 attribute value
					NodeFilter titleFilter2 = new AndFilter(new TagNameFilter("h3"),new HasAttributeFilter("id", "t_title_201309"));
					
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
					
					parserTitle1.reset();
					parserTitle2.reset();
					
					//Get category content
					String type = "";
					String level = "无";
					String type_logs = "";
					String[] types = SplitEmergencyTypeUtils.getEmergencyType(title);
					if(types != null){
							for (int i = 0; i < types.length; i++) {
								if(types.length <= 1){
									if(types[i] != null){
										type =types[i];
									}
								}else{
									if(types[i] != null){
										type += types[i]+ConstantParams.TAB;
									}
								}
						}
					}else{
						 type_logs += "名称为："+title+ConstantParams.CHANGE_LINE;
						//Save the data that cannot be found to the local log is the log
						StringUtils.string2File(type_logs, InitParams.TYPE_LOG);
					}
					
					//Geographic location
					String[] places = FullToponymyUtils.getFullTopoSuper(title);
					
					String location = "";
					//Place name latitude and longitude
					String pll = "";
					Double longitude = 0D;
					Double latitude = 0D;
					String date = "";
					String source = "";
					String detail = "";
					if(places == null || (places != null && places.length == 0)){
						pll = "";
					}else if(places.length < 2){
						for(int i=0;i< places.length;i++){
							if(places[i] != null){
								location = places[i];
							}
						}
							//Latitude and longitude
							String ll = LngAndLatUtils.getLngAndLatAMap(location);
					        if(ll !=null){
						       	String[] LngLats = ll.split(",");
						    	if(LngLats == null || (LngLats != null && LngLats.length == 2)){
						    		longitude = Double.valueOf(("".equals(LngLats[0])?"0":LngLats[0])).doubleValue();
						    		latitude = Double.valueOf(("".equals(LngLats[1])?"0":LngLats[1])).doubleValue();
						    		
//						    		longitude = Double.valueOf(LngLats[0]).doubleValue();
//						    		latitude = Double.valueOf(LngLats[1]).doubleValue();
						    	}else{
						    		longitude = 0D;
						    		latitude = 0D;
						    	}
							}else{
								longitude = 0D;
								latitude = 0D;
							}
					   
							pll = "地理位置："+location+ConstantParams.CHANGE_LINE+"经纬度:"+longitude+ConstantParams.TAB+latitude+ConstantParams.CHANGE_LINE;
							
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
							
							if(detailList != null){
								Node detailNode = detailList.elementAt(0);
								if(detailNode != null){
									//Get details
									detail = StringUtils.replaceBlank(detailNode.toPlainTextString());
									detail = detail.replaceAll("([a-zA-Z])", "").replaceAll("([\\&|;])", "");
								}
							}
							
							parserDetail1.reset();
							parserDetail2.reset();
							
							stringBuilder = new StringBuilder();
							
							stringBuilder.append("(");
							stringBuilder.append("'" + title + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + type + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + level + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + location + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + longitude + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + latitude + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + date + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + source + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + detail + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + sourceUrl + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + fileFullPath + "'");
							stringBuilder.append(",");
							stringBuilder.append("1");
							stringBuilder.append(")");

//							System.out.println(sqlString + stringBuilder.toString());

							pstmt = conn.prepareStatement(sqlString	+ stringBuilder.toString());
							pstmt.execute();

							// Maintenance index table
							String sql = "SELECT LAST_INSERT_ID()";
							stmt = conn.createStatement();
							rs = stmt.executeQuery(sql);
							int id = 0;
							if (rs.next()) {
								id = rs.getInt(1);
							}
							String indexTable = StringUtils.getConfigParam(InitParams.INDEXTABLE, "",InitParams.PROPERTIES_NAME);
							String insertSql = "insert into " + indexTable+ " (businessId,TYPE,action) values (?,?,?)";
							pstmt = conn.prepareStatement(insertSql);
							pstmt.setInt(1, id);
							pstmt.setString(2, "news");
							pstmt.setString(3, "add");
							pstmt.execute();
							
					}else{
						for(int i=0;i< places.length;i++){
							if(places[i] != null){
								location = places[i];
							}
							
							//Latitude and longitude
							String ll = LngAndLatUtils.getLngAndLatAMap(location);
					        if(ll !=null){
						       	String[] LngLats = ll.split(",");
						    	if(LngLats == null || (LngLats != null && LngLats.length != 0)){
						    		longitude = Double.valueOf(("".equals(LngLats[0])?"0":LngLats[0])).doubleValue();
						    		latitude = Double.valueOf(("".equals(LngLats[1])?"0":LngLats[1])).doubleValue();
						    	}else{
						    		longitude = 0D;
						    		latitude = 0D;
						    	}
							}else{
								longitude = 0D;
								latitude = 0D;
							}
							
							pll += "地理位置"+(i+1)+"："+location+ConstantParams.CHANGE_LINE+"经纬度"+(i+1)+"："+longitude+ConstantParams.TAB+latitude+ConstantParams.CHANGE_LINE;
							
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
							
							if(detailList != null){
								Node detailNode = detailList.elementAt(0);
								if(detailNode != null){
									//Get details
									detail = StringUtils.replaceBlank(detailNode.toPlainTextString());
									detail = detail.replaceAll("([a-zA-Z])", "").replaceAll("([\\&|;])", "");
								}
							}
							
							parserDetail1.reset();
							parserDetail2.reset();
							
							stringBuilder = new StringBuilder();

							stringBuilder.append("(");
							stringBuilder.append("'" + title + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + type + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + level + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + location + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + longitude + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + latitude + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + date + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + source + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + detail + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + sourceUrl + "'");
							stringBuilder.append(",");
							stringBuilder.append("'" + fileFullPath + "'");
							stringBuilder.append(",");
							stringBuilder.append("1");
							stringBuilder.append(")");

//							System.out.println(sqlString + stringBuilder.toString());

							pstmt = conn.prepareStatement(sqlString	+ stringBuilder.toString());
							pstmt.execute();

							// Maintenance index table
							String sql = "SELECT LAST_INSERT_ID()";
							stmt = conn.createStatement();
							rs = stmt.executeQuery(sql);
							int id = 0;
							if (rs.next()) {
								id = rs.getInt(1);
							}
							String indexTable = StringUtils.getConfigParam(InitParams.INDEXTABLE, "",InitParams.PROPERTIES_NAME);
							String insertSql = "insert into " + indexTable+ " (businessId,TYPE,action) values (?,?,?)";
							pstmt = conn.prepareStatement(insertSql);
							pstmt.setInt(1, id);
							pstmt.setString(2, "news");
							pstmt.setString(3, "add");
							pstmt.execute();
						}
					}
					
					//Integrate the title, time, source, and body parts into one result
					result = "标题："+title + ConstantParams.CHANGE_LINE +"类型："+type+ConstantParams.CHANGE_LINE +"等级："+level+ConstantParams.CHANGE_LINE +pll+ ConstantParams.CHANGE_LINE + "时间："+date + ConstantParams.CHANGE_LINE+ 
							"来源："+source + ConstantParams.CHANGE_LINE + "详细信息："+detail;
					
					//Write all parsed content locally
					StringUtils.string2File(result, fileFullPath);
					
					// Processing files: (delete, copy)
					String copyPath = StringUtils.getConfigParam(InitParams.COPY_FILEPATH, "",InitParams.PROPERTIES_NAME);
					String fullName = getFileFullNameFromPath(sourcePath);
					File targetFile = new File(copyPath + fullName);
					copyFile(f, targetFile);
					f.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}

	// Save data locally and in the database
	@SuppressWarnings("resource")
	public static void saveData(String inputPath) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		String detail = "";
		String filePath = StringUtils.getConfigParam(InitParams.TXT_FILEPATH,
				"", InitParams.PROPERTIES_NAME);
		try {
			conn = DataBaseUtils.getConnection();
			String sqlString = "INSERT INTO s_news (newsTitle,location,longitude,latitude,newsTime,sourceNet,detail,sourceUrl,filePath,columnId) VALUES ";
			StringBuilder stringBuilder = null;
			File file = new File(inputPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					if (f.getAbsolutePath() != null) {
						saveData(f.getAbsolutePath());// Recursive
					}
				} else {

					String sourcePath = f.getPath();
					String content = CommonUtils.getContent(sourcePath);

					String fileName = StringUtils
							.getFileNameFromPath(sourcePath);
					String fileFullPath = filePath + fileName + ".txt";

					int position = sourcePath.indexOf("mirror") + 6;
					String sourceUrl = sourcePath.substring(position);
					sourceUrl = "http:/" + sourceUrl.replace("\\", "/");

					// title
					Parser parserTitle1 = Parser.createParser(content, "UTF-8");
					Parser parserTitle2 = Parser.createParser(content, "UTF-8");

					// Get the title content of the web page: p tag name, class attribute name, title attribute value
					NodeFilter titleFilter1 = new AndFilter(new TagNameFilter(
							"p"), new HasAttributeFilter("class", "title"));

					// Get the title content of the web page: h3 tag name, id attribute name, t_title_201309 attribute value
					NodeFilter titleFilter2 = new AndFilter(new TagNameFilter(
							"h3"), new HasAttributeFilter("id",
							"t_title_201309"));

					NodeList titleList1 = parserTitle1.parse(titleFilter1);
					NodeList titleList2 = parserTitle2.parse(titleFilter2);

					NodeList titleList = null;
					if (titleList1.size() != 0) {
						titleList = titleList1;
					} else if (titleList2.size() != 0) {
						titleList = titleList2;
					}

					String title = "";
					if (titleList != null) {
						Node titleNode = titleList.elementAt(0);
						if (titleNode != null) {
							// Get the title
							title = StringUtils.replaceBlank(titleNode
									.toPlainTextString());
						}
					}

					// Geographic location
					String location = SplitWordsUtils.hanLPSplit(title);

					// Latitude and longitude
					String ll = LngAndLatUtils.getLngAndLatAMap(location);
					Double longitude = null;
					Double latitude = null;
					if (ll != null) {
						String[] LngLat = ll.split(",");
						longitude = Double.valueOf(LngLat[0]);
						latitude = Double.valueOf(LngLat[1]);
					}
					
					parserTitle1.reset();
					parserTitle2.reset();

					// Time date, source source
					Parser parserDS1 = Parser.createParser(content, "UTF-8");
					Parser parserDS2 = Parser.createParser(content, "UTF-8");
					NodeFilter dsFilter1 = new AndFilter(
							new TagNameFilter("p"), new HasAttributeFilter(
									"class", "subhead"));

					NodeFilter dsFilter2 = new AndFilter(
							new TagNameFilter("p"), new HasAttributeFilter(
									"class", "info"));

					NodeList dsList1 = parserDS1.parse(dsFilter1);
					NodeList dsList2 = parserDS2.parse(dsFilter2);

					NodeList dsList = null;
					String date = "";
					String source = "";
					if (dsList1.size() != 0) {
						dsList = dsList1;
						if (dsList != null) {
							Node dsNode = dsList.elementAt(0);
							if (dsNode != null) {
								// time
								date = dsNode.getFirstChild()
										.toPlainTextString();
								// source
								source = dsNode.getLastChild()
										.toPlainTextString();
							}
						}
					} else if (dsList2.size() != 0) {
						dsList = dsList2;
						String dateSource = "";
						if (dsList != null) {
							Node dsNode = dsList.elementAt(0);
							if (dsNode != null) {
								dateSource = dsNode.toPlainTextString();
								String regDate = "[0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2}[ ][0-9]{1,2}[:][0-9]{1,2}";
								String regSource = "来源[\\pP‘’“”][\u4E00-\u9FA5]+";
								Pattern patternDate = Pattern.compile(regDate);
								Pattern patternSource = Pattern
										.compile(regSource);
								Matcher matcherDate = patternDate
										.matcher(dateSource);
								Matcher matcherSource = patternSource
										.matcher(dateSource);
								// Finds if there is a character/string matching the regular expression in the string
								if (matcherDate.find()) {
									date = matcherDate.group();
								}

								if (matcherSource.find()) {
									source = matcherSource.group();
									source = source.replace("来源：", "");
								}
							}
						}
					}

					parserDS1.reset();
					parserDS2.reset();

					// details
					Parser parserDetail1 = Parser
							.createParser(content, "UTF-8");
					Parser parserDetail2 = Parser
							.createParser(content, "UTF-8");
					NodeFilter detailFilter1 = new AndFilter(new TagNameFilter(
							"div"), new HasAttributeFilter("id", "content"));

					NodeFilter detailFilter2 = new AndFilter(new TagNameFilter(
							"div"), new HasAttributeFilter("class", "con_cont"));

					NodeList detailList1 = parserDetail1.parse(detailFilter1);
					NodeList detailList2 = parserDetail2.parse(detailFilter2);

					NodeList detailList = null;
					if (detailList1.size() != 0) {
						detailList = detailList1;
					} else if (detailList2.size() != 0) {
						detailList = detailList2;
					}

					if (detailList != null) {
						Node detailNode = detailList.elementAt(0);
						if (detailNode != null) {
							// Get the title
							detail = StringUtils.replaceBlank(detailNode.toPlainTextString());
							detail = detail.replaceAll("([a-zA-Z])", "").replaceAll("([\\&|;])", "");
						}
					}

					parserDetail1.reset();
					parserDetail2.reset();

					// Integrate the title, time, source, and body parts into one result
					String result = "标题：" + title + ConstantParams.CHANGE_LINE
							+ "地理位置：" + location + ConstantParams.CHANGE_LINE
							+ "经纬度：" + longitude + ":Lng" + ConstantParams.TAB
							+ latitude + ":Lat" + ConstantParams.CHANGE_LINE
							+ "时间：" + date + ConstantParams.CHANGE_LINE + "来源："
							+ source + ConstantParams.CHANGE_LINE + "详细信息："
							+ detail;

					// Write all parsed content locally
					StringUtils.string2File(result, fileFullPath);

					stringBuilder = new StringBuilder();

					stringBuilder.append("(");
					stringBuilder.append("'" + title + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + location + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + longitude + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + latitude + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + date + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + source + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + detail + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + sourceUrl + "'");
					stringBuilder.append(",");
					stringBuilder.append("'" + fileFullPath + "'");
					stringBuilder.append(",");
					stringBuilder.append("1");
					stringBuilder.append(")");

					System.out.println(sqlString + stringBuilder.toString());

					pstmt = conn.prepareStatement(sqlString
							+ stringBuilder.toString());
					pstmt.execute();

					// Maintenance index table
					String sql = "SELECT LAST_INSERT_ID()";
					stmt = conn.createStatement();
					rs = stmt.executeQuery(sql);
					int id = 0;
					if (rs.next()) {
						id = rs.getInt(1);
					}
					String indexTable = StringUtils.getConfigParam(
							InitParams.INDEXTABLE, "",
							InitParams.PROPERTIES_NAME);
					String insertSql = "insert into " + indexTable
							+ " (businessId,TYPE,action) values (?,?,?)";
					pstmt = conn.prepareStatement(insertSql);
					pstmt.setInt(1, id);
					pstmt.setString(2, "news");
					pstmt.setString(3, "add");
					pstmt.execute();

					// Processing files: (delete, copy)
					String copyPath = StringUtils.getConfigParam(
							InitParams.COPY_FILEPATH, "",
							InitParams.PROPERTIES_NAME);
					String fullName = getFileFullNameFromPath(sourcePath);
					File targetFile = new File(copyPath + fullName);
					copyFile(f, targetFile);
					f.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}

	}

	// File copy method
	public static void copyFile(File sourceFile, File targetFile) {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inBuff != null) {
					inBuff.close();
				}
				if (outBuff != null) {
					outBuff.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileFullNameFromPath(String path) {
		String result = "";
		if (StringUtils.isEmpty(path)) {
			return result;
		}
		if (path.contains("/")) {
			result = path.substring(path.lastIndexOf("/") + 1);
		} else if (path.contains("\\")) {
			result = path.substring(path.lastIndexOf("\\") + 1);
		} else {
			result = path;
		}
		return result;
	}
}
