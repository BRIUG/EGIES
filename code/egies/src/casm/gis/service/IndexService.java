package casm.gis.service;

import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.domain.Index;

/*
 * Create an index interface
 * 2017-05-28 19:05:43
 */
public interface IndexService extends BaseDao{

	public List<Index> indexPagination(int pageSize,int pageNow);
	
	public int getRow();
}
