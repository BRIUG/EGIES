package casm.gis.service.impl;

import casm.gis.dao.BaseDao;
import casm.gis.dao.impl.BaseDaoImpl;
import casm.gis.service.UserService;

/*
 * Create an interface implementation
 * 2017-05-14 23:27:25
 */
public class UserServiceImpl extends BaseDaoImpl implements UserService {
	
	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
}
