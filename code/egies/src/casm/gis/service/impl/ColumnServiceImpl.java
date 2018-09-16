package casm.gis.service.impl;

import java.util.ArrayList;
import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.dao.impl.BaseDaoImpl;
import casm.gis.domain.Column;
import casm.gis.service.ColumnService;

/*
 * Create a ColumnService interface implementation class
 * 2017-05-28 20:47:57
 */
public class ColumnServiceImpl extends BaseDaoImpl implements ColumnService {

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	@Override
	public List<Column> columnPagination(int pageSize, int pageNow) {
		List<Column> result = new ArrayList<Column>();
		try {
			String queryString = "from Column order by columnId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int getRow() {
		String queryString = "select count(*) from Column";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}
}
