package casm.gis.service;

import java.util.List;

import casm.gis.dao.BaseDao;
import casm.gis.domain.FileDocument;

public interface FileDocumentService extends BaseDao{

	public List<FileDocument> fileDocumentPagination(int pageSize,int pageNow);
	
	public int getRow();
}
