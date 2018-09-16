package casm.gis.action;

import java.util.ArrayList;
import java.util.List;

import casm.gis.full.config.InitParams;
import casm.gis.full.index.IndexCommonServer;
import casm.gis.util.DateUtils;
import casm.gis.util.StringUtils;
import casm.gis.domain.Column;
import casm.gis.domain.News;
import casm.gis.service.ColumnService;
import casm.gis.service.NewsService;

/*
 * Create information operation
 * 2017-06-01 01:22:31
 */
public class NewsAction extends BaseAction{
	
	private static final long serialVersionUID = -7889025802793592218L;

	private NewsService newsService;
	private ColumnService columnService;
	
	private List<Column> columnList = new ArrayList<Column>();
	private List<News> newsAllListPa = new ArrayList<News>();
	
	private News news;
	private int pageNow = 1;
	private int pageCount;
	private String newsId;
	
	private Column column;

	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	public void setColumnService(ColumnService columnService) {
		this.columnService = columnService;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}
	
	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
	
	public List<News> getNewsAllListPa() {
		return newsAllListPa;
	}

	public void setNewsAllListPa(List<News> newsAllListPa) {
		this.newsAllListPa = newsAllListPa;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getAllColumn(){
		this.columnList = (List)this.columnService.getAllObject(Column.class, "columnId", false);
		return "getAllColumn";
	}
	
	public String addNews(){
		this.news.setCreateTime(DateUtils.getCurrentYMDHMS());
		this.news.setColumn(this.column);
		this.newsService.addObject(this.news);
		
		List<News> list = (List)this.newsService.getAllObject(News.class, "newsId", false);
		int businessId = 0;
		if(list != null && list.size() > 0){
			businessId = list.get(0).getNewsId();
			IndexCommonServer indexCommonServer = new IndexCommonServer();
			indexCommonServer.addIndexTable(businessId, "news", InitParams.ADD_INDEX);
		}
		
		return "addNews";
	}
	
	public String searchAllNewsPa(){
		int pageSize = 10;
		int pageRow = this.newsService.getRow();
		
		if(pageRow%pageSize == 0){
			this.pageCount = pageRow/pageSize;
		}else{
			this.pageCount = pageRow/pageSize + 1;
		}
		
		if(this.pageNow < 1){
			this.pageNow = 1;
		}
		
		if(this.pageNow > this.pageCount){
			this.pageNow = this.pageCount;
		}
		this.newsAllListPa = this.newsService.newsPagination(pageSize, this.pageNow);
		return "searchAllNewsPa";
	}
	
	public String delNews(){
		int id = 0;
		if(StringUtils.isNotEmpty(this.newsId)){
			id = Integer.parseInt(this.newsId);
		}
		if(id > 0){
			News news = (News)this.newsService.getObjectById(News.class, id);
			this.newsService.deleteObject(news);
			
			IndexCommonServer indexCommonServer = new IndexCommonServer();
			indexCommonServer.updateIndexTable(id, "news", InitParams.DELETE_INDEX);
		}
		
		return "delNews";
	}
	
}
