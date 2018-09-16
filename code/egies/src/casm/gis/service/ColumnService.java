package casm.gis.service;

import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.domain.Column;

/*
 * Create a table column service interface
 * 2017-05-28 19:07:32
 */
public interface ColumnService extends BaseDao{

	public List<Column> columnPagination(int pageSize,int pageNow);
	
	public int getRow();
}
