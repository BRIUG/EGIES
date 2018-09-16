package casm.gis.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/*
	 * Get year information
	 * Created to test the search year of solr
	 * 2017-04-25 00:07:30
	 */
	public static Date getYear(String year){
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		try {
			date = sdf.parse(year);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/*
	 * Package time tool
	 * 2017-05-29 11:11:03
	 */
	public static Date getCurrentYMDHMS(){
		Date date = new Date();
		Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String currentDate = sdf.format(date);
			result = sdf.parse(currentDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
