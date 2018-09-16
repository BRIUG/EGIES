package casm.gis.service.impl;

import java.util.ArrayList;
import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.dao.impl.BaseDaoImpl;
import casm.gis.domain.FileDocument;
import casm.gis.service.FileDocumentService;

public class FileDocumentServiceImpl extends BaseDaoImpl implements FileDocumentService{

	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	@Override
	public List<FileDocument> fileDocumentPagination(int pageSize, int pageNow) {
		List<FileDocument> result = new ArrayList<FileDocument>();
		try {
			String queryString = "from FileDocument order by fdId desc";
			result = (List)pagination(pageNow, pageSize, queryString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int getRow() {
		String queryString = "select count(*) from FileDocument";
		List list = this.getHibernateTemplate().find(queryString);
		long temp = (Long)list.get(0);
		int result = Integer.parseInt(temp+"");
		return result;
	}

}
