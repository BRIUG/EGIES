package casm.gis.retrieve.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullTextSearchParams {

	//search keyword
	private String queryWord = "";
	
	//Specify the search domain, and the default relationship is OR
	private List<String> assignmentFields = new ArrayList<String>();
	/**
	 * Specify the relationship between the search domain and the search domain
	 * Map<String,String> The first String is a domain name, such as: name
	 *                    The second String is a relation, which is written as: or: OR and : AND
	 */
	private List<Map<String,String>> assignFields = new ArrayList<Map<String,String>>();
	
	//Display domain
	private String[] viewFields;
	
	//Whether it is highlighted
	private Boolean isHighlight = true;
	
	//Highlight field
	private String[] highlightFields;
	
	//Highlight prefix
	private String preHighlight = "<em class=\"highlight\">";
	
	//Highlight suffix
	private String postHighlight = "</em>";
	
	//Sort field String: the domain name to be sorted, Boolean: true ascending false descending
	private Map<String,Boolean> sortField = new HashMap<String,Boolean>();
	
	//Filter domain
	private Map<String,String> filterField = new HashMap<String,String>();
	
	//Start line
	private int startNums = 0;
	
	//How many lines are displayed on one page
	private int pageCount = 50;
	
	//Whether to count
	private Boolean isFacet = false;
	
	//Statistical domain
	private String[] facetFields;

	//Number of results returned
	private long numFound;
	
	//Number of words showing the result
	private int viewNums = 200;
	
	//Specify search weight
	private Map<String,Float> boost = new HashMap<String,Float>();
	
	//Specify the number of results returned in lucene
	private int returnNums = 10;
	
	public String getQueryWord() {
		return queryWord;
	}

	public void setQueryWord(String queryWord) {
		this.queryWord = queryWord;
	}

	public List<String> getAssignmentFields() {
		return assignmentFields;
	}

	public void setAssignmentFields(List<String> assignmentFields) {
		this.assignmentFields = assignmentFields;
	}

	public String[] getViewFields() {
		return viewFields;
	}

	public void setViewFields(String[] viewFields) {
		this.viewFields = viewFields;
	}

	public String[] getHighlightFields() {
		return highlightFields;
	}

	public void setHighlightFields(String[] highlightFields) {
		this.highlightFields = highlightFields;
	}

	public String getPreHighlight() {
		return preHighlight;
	}

	public void setPreHighlight(String preHighlight) {
		this.preHighlight = preHighlight;
	}

	public String getPostHighlight() {
		return postHighlight;
	}

	public void setPostHighlight(String postHighlight) {
		this.postHighlight = postHighlight;
	}

	public Map<String, Boolean> getSortField() {
		return sortField;
	}

	public void setSortField(Map<String, Boolean> sortField) {
		this.sortField = sortField;
	}

	public Map<String, String> getFilterField() {
		return filterField;
	}

	public void setFilterField(Map<String, String> filterField) {
		this.filterField = filterField;
	}

	public int getStartNums() {
		return startNums;
	}

	public void setStartNums(int startNums) {
		this.startNums = startNums;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String[] getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(String[] facetFields) {
		this.facetFields = facetFields;
	}

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	public int getViewNums() {
		return viewNums;
	}

	public void setViewNums(int viewNums) {
		this.viewNums = viewNums;
	}

	public List<Map<String, String>> getAssignFields() {
		return assignFields;
	}

	public void setAssignFields(List<Map<String, String>> assignFields) {
		this.assignFields = assignFields;
	}

	public Boolean getIsHighlight() {
		return isHighlight;
	}

	public void setIsHighlight(Boolean isHighlight) {
		this.isHighlight = isHighlight;
	}

	public Boolean getIsFacet() {
		return isFacet;
	}

	public void setIsFacet(Boolean isFacet) {
		this.isFacet = isFacet;
	}

	public Map<String, Float> getBoost() {
		return boost;
	}

	public void setBoost(Map<String, Float> boost) {
		this.boost = boost;
	}

	public int getReturnNums() {
		return returnNums;
	}

	public void setReturnNums(int returnNums) {
		this.returnNums = returnNums;
	}
}
