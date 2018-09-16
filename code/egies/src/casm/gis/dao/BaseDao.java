package casm.gis.dao;

import java.util.List;

import casm.gis.retrieve.api.FullTextService;

/*
 * interface
 * 2017-05-14 23:15:21
 */
public interface BaseDao {
	
	public void addObject(Object object);
	
	public void updateObject(Object object);
	
	public void deleteObject(Object object);
	
	public Object getObjectById(Class clazz,int id);
	
	public List<Object> getAllObject(Class clazz);
	
	public List<Object> getAllObject(Class clazz,String id,boolean isAsc);
	
	public List<Object> getAllObject(String className,String id,boolean isAsc);
	
	public List<Object> pagination(int pageNow,int pageSize,String queryString);
	
	public List<Object> listPagination(int pageNow,int pageSize,List<Object> list);
	
	public FullTextService getSearchService(String type,String serverName,String flag,String className,String indexPath,String url);
}
