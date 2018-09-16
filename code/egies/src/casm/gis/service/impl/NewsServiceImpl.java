package casm.gis.service.impl;

import java.util.ArrayList;
import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.dao.impl.BaseDaoImpl;
import casm.gis.domain.News;
import casm.gis.service.NewsService;

/*
 * Create an interface implementation class
 * 2017-06-01 01:23:53
 */
public class NewsServiceImpl extends BaseDaoImpl implements NewsService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<News> newsPagination(int pageSize, int pageNow) {
		List<News> result = new ArrayList<News>();
		try {
			String queryString = "from News order by newsId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int getRow() {
		String queryString = "select count(*) from News";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}
}