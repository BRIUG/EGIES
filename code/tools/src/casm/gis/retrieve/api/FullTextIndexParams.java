package casm.gis.retrieve.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullTextIndexParams {

	//Data that needs to be indexed
	private List<Map<String,Object>> indexData = new ArrayList<Map<String,Object>>();
	
	//Lucene index path
	private String indexPath = "";
	
	//Incoming id when deleting an index
	private List<String> ids = new ArrayList<String>();
	private String id;
	
	

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Map<String, Object>> getIndexData() {
		return indexData;
	}

	public void setIndexData(List<Map<String, Object>> indexData) {
		this.indexData = indexData;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	
	
}
