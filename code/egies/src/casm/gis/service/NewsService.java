package casm.gis.service;

import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.domain.News;

/*
 * Create information interface content
 * 2017-06-01 01:22:09
 */
public interface NewsService extends BaseDao{
	
	public List<News> newsPagination(int pageSize,int pageNow);
	
	public int getRow();
}
