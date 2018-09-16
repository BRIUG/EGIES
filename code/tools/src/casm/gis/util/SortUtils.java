package casm.gis.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SortUtils {
	
	/*
	 * Sort the statistics word frequency
	 * true:Descending
	 * false:Ascending
	 * 2017年4月9日12:01:12
	 */
	public static Map sortByValues(Map map,final boolean isDec){
		Map result = new LinkedHashMap();
		List list = new LinkedList(map.entrySet());
		Collections.sort(list,new Comparator(){

			@Override
			public int compare(Object o1, Object o2) {
				if(isDec){
					return -((Comparable)((Map.Entry)o1).getValue()).compareTo(((Map.Entry)o2).getValue());
				}else{
					return ((Comparable)((Map.Entry)o1).getValue()).compareTo(((Map.Entry)o2).getValue());
				}
			}
			
		});
		
		for(Iterator iter = list.iterator();iter.hasNext();){
			Map.Entry entry = (Map.Entry) iter.next();
			result.put(entry.getKey(), entry.getValue());
//			System.out.println(entry.getKey()+":"+entry.getValue());
		}
		return result;
	}
	
	public static Map sortByValues(Map map){
		return sortByValues(map, true);
	}
}
