package casm.gis.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import casm.gis.full.config.InitParams;
import casm.gis.full.index.IndexServer;
import casm.gis.retrieve.api.FullTextIndexParams;
import casm.gis.retrieve.api.FullTextResult;
import casm.gis.retrieve.api.FullTextSearchParams;
import casm.gis.retrieve.api.FullTextService;
import casm.gis.retrieve.api.ServerFactory;
import casm.gis.retrieve.spi.LuceneService;
import casm.gis.util.DateUtils;
import casm.gis.util.StringUtils;
import casm.gis.domain.Column;
import casm.gis.domain.Index;
import casm.gis.service.ColumnService;
import casm.gis.service.IndexService;

/*
 * Create an index operation class
 * 2017-05-28 20:56:05
 */
public class IndexAction extends BaseAction{

	private static final long serialVersionUID = 5721487016043158475L;
	
	private Index index;
	private Column column;
	
	private List<Index> indexList = new ArrayList<Index>();
	
	private IndexService indexService;
	private ColumnService columnService;
	
	private LuceneService luceneService = null;
	private FullTextService searchService = null;
	
	private int pageCount;
	private int pageNow = 1;
	
	private String pageType;
	private String indexIdStr;
	
	private String queryString;
	
	private List searchList = new ArrayList();
	private List<Column> columnList = new ArrayList<Column>();
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}
	
	
	public List<Index> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<Index> indexList) {
		this.indexList = indexList;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getIndexIdStr() {
		return indexIdStr;
	}

	public void setIndexIdStr(String indexIdStr) {
		this.indexIdStr = indexIdStr;
	}
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public List getSearchList() {
		return searchList;
	}

	public void setSearchList(List searchList) {
		this.searchList = searchList;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	public void setColumnService(ColumnService columnService) {
		this.columnService = columnService;
	}

	public String addIndex(){
		this.index.setAction("add");
		this.index.setCreateTime(DateUtils.getCurrentYMDHMS());
		this.indexService.addObject(this.index);
		return "addIndex";
	}
	
	public String viewIndex(){
		int pageSize = 15;
		int pageRow = this.indexService.getRow();
		
		if(pageRow%pageSize == 0){
			this.pageCount = pageRow/pageSize;
		}else{
			this.pageCount = pageRow/pageSize + 1;
		}
		
		if("pre".equals(this.pageType)){
			-- this.pageNow;
			this.pageType = "";
		}
		
		if("next".equals(this.pageType)){
			++ this.pageNow;
			this.pageType = "";
		}
		
		if(this.pageNow < 1){
			this.pageNow = 1;
		}
		
		if(this.pageNow > this.pageCount){
			this.pageNow = this.pageCount;
		}
		
		this.indexList = this.indexService.indexPagination(pageSize, pageNow);
		return "viewIndex";
	}
	
	public void beginService(String flag,String indexPath){
		Map<String,String> params = new HashMap<String,String>();
		params.put("type", "lucene");
		params.put("serverName", "test");
		params.put("flag", flag);
		params.put("className", LuceneService.class.getName());
		params.put("indexPath", indexPath);
		ServerFactory serverFactory = new ServerFactory();
		luceneService = (LuceneService)serverFactory.beginService(params);
		luceneService.setServerName("test");
	}
	
	public void beginService(){
		Map<String,String> params = new HashMap<String,String>();
		String type = StringUtils.getConfigParam(InitParams.SERVERTYPE, "", InitParams.SEARCH_PROPERTIES);
		params.put("type", type);
		String serverName = StringUtils.getConfigParam(InitParams.SERVERNAME, "", InitParams.SEARCH_PROPERTIES);
		params.put("serverName", serverName);
		String url = StringUtils.getConfigParam(InitParams.SOLR_URL, "", InitParams.SEARCH_PROPERTIES);
		params.put("url", url);
		params.put("className", IndexServer.class.getName());
		ServerFactory serverFactory = new ServerFactory();
		searchService = serverFactory.beginService(params);
		searchService.setServerName(serverName);
	}
	
	public String createIndex(){
		int indexId = 0;
		if(StringUtils.isNotEmpty(this.indexIdStr)){
			indexId = Integer.parseInt(this.indexIdStr);
		}
		if(indexId != 0){
			Index index = (Index)this.indexService.getObjectById(Index.class, indexId);
			String indexPath = index.getIndexPath();
			String sourcePath = index.getSourcePath();
			
			//Start service
			beginService("writer",indexPath);
			
			FullTextIndexParams fullTextIndexParams = new FullTextIndexParams();
			List<Map<String,Object>> indexData = new ArrayList<Map<String,Object>>();
			
			StringUtils su = new StringUtils(sourcePath);
			List<String> pathList = su.allPathResult;
			Map<String,Object> map = null;
			for(String path : pathList){
				map = new HashMap<String,Object>();
				String fileName = StringUtils.getFileNameFromPath(path);
				String content = StringUtils.getContent(path);
				map.put("fileName", fileName);
				map.put("content", content);
				indexData.add(map);
			}
			fullTextIndexParams.setIndexData(indexData);
			luceneService.doIndex(fullTextIndexParams);
			
			//Modify table
			index.setStatus(1);
			index.setIndexTime(DateUtils.getCurrentYMDHMS());
			this.indexService.updateObject(index);
			
		}
		return "createIndex";
	}
	
	public String doQuery(){
		
		this.columnList = (List)this.columnService.getAllObject("Column", "columnId", false);
		
		int columnId = 0;
		if(this.column != null){
			columnId = this.column.getColumnId();
		}
		String indexPath = "";
		if(columnId > 0){
			Column c = (Column)this.columnService.getObjectById(Column.class, columnId);
			if(c != null){
				Set<Index> indexs = c.getIndexs();
				Iterator<Index> iter = indexs.iterator();
				while(iter.hasNext()){
					indexPath = iter.next().getIndexPath();
				}
			}
		}
		
		if(StringUtils.isNotEmpty(this.queryString)){
			if(StringUtils.isEmpty(indexPath)){
				beginService();
				FullTextSearchParams fullTextSearchParams = new FullTextSearchParams();
				fullTextSearchParams.setQueryWord(this.queryString);
				
				List<String> assignmentFields = new ArrayList<String>();
				assignmentFields.add("newsTitle");
				assignmentFields.add("content");
				fullTextSearchParams.setAssignmentFields(assignmentFields);
				
				String[] viewFields = new String[]{"docfullid","newsTitle","content","sourceUrl"};
				fullTextSearchParams.setViewFields(viewFields);
				
				fullTextSearchParams.setViewNums(150);
				fullTextSearchParams.setIsHighlight(true);
				String[] highlightFields = {"newsTitle","content"};
				fullTextSearchParams.setHighlightFields(highlightFields);
				fullTextSearchParams.setPreHighlight("<font color='red'>");
				fullTextSearchParams.setPostHighlight("</font>");
				
				Map<String,String> filterField = new HashMap<String,String>();
				filterField.put("columnId", columnId+"");
				fullTextSearchParams.setFilterField(filterField);
				
				FullTextResult result = searchService.doQuery(fullTextSearchParams);
				long numFound = result.getNumFound();
				List tempList = result.getResultList();
				
//				int pageRow = tempList.size();
				int pageRow = (int) numFound;
				int pageSize = 10;
				
				if(pageRow%pageSize == 0){
					this.pageCount = pageRow/pageSize;
				}else{
					this.pageCount = pageRow/pageSize+1;
				}
				
				if(this.pageNow < 1){
					this.pageNow = 1;
				}
				
				if(this.pageNow > this.pageCount){
					this.pageNow = this.pageCount;
				}
				if(tempList != null && tempList.size() > 0){
					this.searchList = this.indexService.listPagination(this.pageNow, pageSize, tempList);
				}
			}else{
				beginService("search", indexPath);
				FullTextSearchParams fullTextSearchParams = new FullTextSearchParams();
				fullTextSearchParams.setQueryWord(this.queryString);
				fullTextSearchParams.setReturnNums(1000);
				
				List<String> assignmentFields = new ArrayList<String>();
				assignmentFields.add("fileName");
				assignmentFields.add("content");
				fullTextSearchParams.setAssignmentFields(assignmentFields);
				
				String[] viewFields = new String[]{"fileName","content"};
				fullTextSearchParams.setViewFields(viewFields);
				
				fullTextSearchParams.setViewNums(150);
				fullTextSearchParams.setIsHighlight(true);
				fullTextSearchParams.setPreHighlight("<font color='red'>");
				fullTextSearchParams.setPostHighlight("</font>");
				
				FullTextResult result = luceneService.doQuery(fullTextSearchParams);
				long numFound = result.getNumFound();
				List tempList = result.getResultList();
				
				int pageRow = tempList.size();
				int pageSize = 10;
				
				if(pageRow%pageSize == 0){
					this.pageCount = pageRow/pageSize;
				}else{
					this.pageCount = pageRow/pageSize+1;
				}
				
				if(this.pageNow < 1){
					this.pageNow = 1;
				}
				
				if(this.pageNow > this.pageCount){
					this.pageNow = this.pageCount;
				}
				
				if(tempList != null && tempList.size() > 0){
					this.searchList = this.indexService.listPagination(this.pageNow, pageSize, tempList);
				}
			}
		}
		
		return "doQuery";
	}

}
