package casm.gis.service.impl;

import java.util.ArrayList;
import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.dao.impl.BaseDaoImpl;
import casm.gis.domain.Index;
import casm.gis.service.IndexService;

/*
 * Implementation class of the IndexService interface
 * 2017-05-28 20:43:35
 */
public class IndexServiceImpl extends BaseDaoImpl implements IndexService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<Index> indexPagination(int pageSize, int pageNow) {
		List<Index> result = new ArrayList<Index>();
		try {
			String queryString = "from Index order by indexId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int getRow() {
		String queryString = "select count(*) from Index";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}
}
