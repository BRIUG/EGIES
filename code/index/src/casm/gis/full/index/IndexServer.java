package casm.gis.full.index;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import casm.gis.retrieve.api.FullTextIndexParams;
import casm.gis.retrieve.api.FullTextService;
import casm.gis.retrieve.api.ServerFactory;
import casm.gis.util.StringUtils;
import casm.gis.full.config.InitParams;
import casm.gis.full.util.DataBaseUtils;

/*
 * The last call class of the index
 * 2017-05-13 13:04:19
 */
public class IndexServer extends IndexCommonServer{
	
	
	public static FullTextService solrService = null;
	
	//Open a solr service
	public static FullTextService getService(){
		Map<String,String> params = new HashMap<String,String>();
		String type = StringUtils.getConfigParam(InitParams.SERVERTYPE, "", InitParams.SEARCH_PROPERTIES);
		params.put("type", type);
		String serverName = StringUtils.getConfigParam(InitParams.SERVERNAME, "", InitParams.SEARCH_PROPERTIES);
		params.put("serverName", serverName);
		String url = StringUtils.getConfigParam(InitParams.SOLR_URL, "", InitParams.SEARCH_PROPERTIES);
		params.put("url", url);
		params.put("className", IndexServer.class.getName());
		ServerFactory serverFactory = new ServerFactory();
		solrService = serverFactory.beginService(params);
		solrService.setServerName(serverName);
		
		return solrService;
	}

	/*
	 * Get the data content that needs to create an index
	 * Prepare data for index creation
	 * 2017-05-13 16:02:35
	 */
	public List<Map<String,Object>> prepareIndexData(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		
		String[] uniqueKeys = this.uniqueKey;
		if(uniqueKeys == null){
			return list;
		}
		
		//Obtain the business table id to be indexed from the index data table t_index
		List<Object> listIDs = getIndexIDs(InitParams.ADD_INDEX);
		if(listIDs == null || listIDs.size() < 1){
			return list;
		}
		StringBuilder ids = new StringBuilder();
		ids.append(" ( ");
		for (int i = 0; i < listIDs.size(); i++) {
			ids.append(listIDs.get(i));
			if(i < listIDs.size()-1){
				ids.append(",");
			}
		}
		ids.append(" ) ");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
//			String sql = "SELECT * FROM t_user WHERE id IN ";
			String sql = this.indexSql;
			System.out.println("sql:"+sql+ids.toString());
			pstmt = conn.prepareStatement(sql+ids.toString());
			rs = pstmt.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()){
				map = new HashMap<String,Object>();
				//Index unique key
				map.put(uniqueKeys[0], uniqueKeys[1]+"_"+rs.getObject(uniqueKeys[2]));
				
				for(int i=0;i<columnCount;i++){
					String columnLabel = metaData.getColumnLabel(i+1);
					System.out.println("columnLable:"+columnLabel);
					if("filePath".equals(columnLabel)){
						map.put("content", StringUtils.getContent(rs.getString(columnLabel)));
					}
					map.put(columnLabel, rs.getObject(columnLabel));
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
		return list;
	}
	
	
	@Override
	public void afterIndexMethod() {
		getIndexIDs("add");
		if(recIdList == null || recIdList.size() < 1){
			return ;
		}
		StringBuilder recID = new StringBuilder();
		recID.append(" ( ");
		for (int i = 0; i < recIdList.size(); i++) {
			recID.append(recIdList.get(i));
			if(i < recIdList.size() - 1){
				recID.append(",");
			}
		}
		recID.append(" ) ");
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "UPDATE "+indexTable+" SET flag=?,INDEXEDDATE=? WHERE recid IN ";
			pstmt = conn.prepareStatement(sql+recID.toString());
			pstmt.setInt(1, 1);
			Date d = new Date();
			pstmt.setObject(2, d);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}

	@Override
	public void preIndexMethod() {
		//Go to the index table to find the data id that needs to be modified.
		List[] updateResults = getIDs(indexType, InitParams.UPDATE_INDEX, 0);
		List<String> updateIDs = null;
		if(updateResults != null && updateResults.length == 2){
			List<Object> update = updateResults[1];
			if(update != null && update.size() > 0){
				updateIDs = new ArrayList<String>();
				for (int i = 0; i < update.size(); i++) {
					updateIDs.add(this.uniqueKey[1]+"_"+update.get(i));
				}
			}
		}
		
		//Go to the index table to find the data id that needs to be deleted.
		List[] delResults = getIDs(indexType, InitParams.DELETE_INDEX, 0);
//		List<Object> delete = getIndexIDs(InitParams.DELETE_INDEX);
		List<String> deleteIDs = null;
		if(delResults != null && delResults.length == 2){
			List<Object> delete = delResults[1];
			if(delete != null && delete.size() > 0){
				deleteIDs = new ArrayList<String>();
				for (int i = 0; i < delete.size(); i++) {
					deleteIDs.add(this.uniqueKey[1]+"_"+delete.get(i));
				}
			}
		}
		
		if(updateIDs == null && deleteIDs == null){
			return ;
		}
		
		if(updateIDs != null){
			FullTextIndexParams fullTextIndexParams = new FullTextIndexParams();
			fullTextIndexParams.setIds(updateIDs);
			solrService.updateIndex(fullTextIndexParams);
			
			List<Integer> recUpdateList = updateResults[0];
			StringBuilder updateRecIDs = null;
			if(recUpdateList != null && recUpdateList.size() > 0){
				updateRecIDs = new StringBuilder();
				updateRecIDs.append(" ( ");
				for (int i = 0; i < recUpdateList.size(); i++) {
					updateRecIDs.append(recUpdateList.get(i));
					if (i < recUpdateList.size() -1) {
						updateRecIDs.append(",");
					}
				}
				updateRecIDs.append(" ) ");
			}else{
				return;
			}
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = DataBaseUtils.getConnection();
				String sql = "UPDATE "+indexTable+" SET action = ?,INDEXEDDATE = ? WHERE recid IN ";
				pstmt = conn.prepareStatement(sql+updateRecIDs.toString());
				pstmt.setString(1, InitParams.ADD_INDEX);
				pstmt.setDate(2, null);
				pstmt.execute();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
		
		if(deleteIDs != null){
			FullTextIndexParams fullTextIndexParams = new FullTextIndexParams();
			fullTextIndexParams.setIds(deleteIDs);
			solrService.deleteIndex(fullTextIndexParams);
			
			List<Integer> recList = delResults[0];
			StringBuilder delRecIDs = null;
			if(recList != null && recList.size() > 0){
				delRecIDs = new StringBuilder();
				delRecIDs.append(" ( ");
				for (int i = 0; i < recList.size(); i++) {
					delRecIDs.append(recList.get(i));
					if (i < recList.size() -1) {
						delRecIDs.append(",");
					}
				}
				delRecIDs.append(" ) ");
			}else{
				return;
			}
			
			Connection conn = null;
			PreparedStatement pstmt = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = DataBaseUtils.getConnection();
				String sql = "DELETE FROM "+indexTable+" WHERE recid IN ";
				pstmt = conn.prepareStatement(sql+delRecIDs.toString());
				pstmt.execute();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
			}
		}
	}
	
	/*
	 * Create index
	 *2017-05-13 16:51:13 
	 */
	public void createIndex(){
		List<Map<String,Object>> indexData = prepareIndexData();
		FullTextIndexParams fullTextIndexParams = new FullTextIndexParams();
		fullTextIndexParams.setIndexData(indexData);
		solrService.doIndex(fullTextIndexParams);
	}
}
