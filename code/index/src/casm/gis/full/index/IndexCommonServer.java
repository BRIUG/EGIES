package casm.gis.full.index;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import casm.gis.retrieve.spi.SolrService;
import casm.gis.util.StringUtils;
import casm.gis.full.config.InitParams;
import casm.gis.full.util.DataBaseUtils;

/*
 * Create an indexing service class: generic features
 * 2017-05-13 13:02:19
 */
public class IndexCommonServer extends SolrService{

	//Get the number of entries each time you create an index
	private int indexCount = getIndexCount();
	//Get the type of index
	protected String indexType = getIndexType();
	//Get the unique key of the index
	protected String[] uniqueKey = getUniqueKey();
	//Get the configuration sql statement related to the business
	protected String indexSql = getIndexSql();
	//The name of the index table
	protected String indexTable = getIndexTable();
	
	
	protected List<Integer> recIdList = new ArrayList<Integer>();
	
	/*
	 * Count all the number of indexes in the t_index table
	 */
	public int getIndexCount(){
		String count = StringUtils.getConfigParam(InitParams.INDEX_COUNT, "200", InitParams.SEARCH_PROPERTIES);
		if(StringUtils.isNotEmpty(count)){
			return Integer.parseInt(count);
		}
		return 1;
	}
	
	/*
	 * Processing the t_user table
	 */
	public String getIndexType(){
		return StringUtils.getConfigParam(InitParams.INDEX_TYPE, "test", InitParams.SEARCH_PROPERTIES);
	}
	
	/*
	 * Get unique key
	 */
	public String[] getUniqueKey(){
		String uk = StringUtils.getConfigParam(InitParams.UNIQUEKEY, "", InitParams.SEARCH_PROPERTIES);
		if(uk != null && uk.contains(",")){
			String[] temp = uk.split(",");
			if(temp.length == 3){
				return temp;
			}
		}
		return null;
	}
	
	/*
	 * Get the name method of the index table
	 * 2017-05-13 21:00:53
	 */
	public String getIndexTable(){
		return StringUtils.getConfigParam(InitParams.INDEX_TABLE, "t_index", InitParams.SEARCH_PROPERTIES);
	}
	
	/*
	 * Query the business of the sql statement
	 */
	public String getIndexSql(){
		return StringUtils.getConfigParam(InitParams.INDEX_SQL, "", InitParams.SEARCH_PROPERTIES);
	}
	
	/*
	 * Get the information in the t_index table
	 * Get the id of the index record you need
	 */
	public List<Object> getIndexIDs(String type,String action,int flag){
		List<Object> list = new ArrayList<Object>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "SELECT RECID,businessId FROM "+indexTable+" WHERE TYPE=? AND action=? AND flag=? LIMIT ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, type);
			pstmt.setString(2, action);
			pstmt.setInt(3, flag);
			pstmt.setInt(4, indexCount);
			rs = pstmt.executeQuery();
			while(rs.next()){
				int bID  = rs.getInt("businessId");
				int recID = rs.getInt("RECID");
				list.add(bID);
				recIdList.add(recID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
		return list;
	}
	
	/*
	 * Method overload
	 * Get the businessId, recId that requires an index record.
	 * 2017-05-13 22:53:04
	 */
	public List[] getIDs(String type,String action,int flag){
		List<Object> bnList = new ArrayList<Object>();
		List<Object> recList = new ArrayList<Object>();
		List[] results = new List[2];
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "SELECT RECID,businessId FROM "+indexTable+" WHERE TYPE=? AND action=? AND flag=? LIMIT ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, type);
			pstmt.setString(2, action);
			pstmt.setInt(3, flag);
			pstmt.setInt(4, indexCount);
			rs = pstmt.executeQuery();
			while(rs.next()){
				int bID  = rs.getInt("businessId");
				int recID = rs.getInt("RECID");
				bnList.add(bID);
				recList.add(recID);
			}
			results[0] = recList;
			results[1] = bnList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
		return results;
	}
	
	/*
	 * Method overload
	 */
	public List<Object> getIndexIDs(String type,String action){
		return getIndexIDs(type,action,0);
	}
	
	public List<Object> getIndexIDs(String action){
		return getIndexIDs(indexType,action,0);
	}
	
	/*
	 * Add index table
	 * Synchronize with the business, add index table data
	 * 2017-05-13 20:53:43
	 */
	public void addIndexTable(int businessId,String type,String action){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "INSERT INTO "+indexTable+" (businessId,TYPE,action) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, businessId);
			pstmt.setString(2, type);
			pstmt.setString(3, action);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}
	
	/*
	 * Modify index table
	 * Synchronize with the business, modify and delete the index table data
	 * 2017-05-13 21:10:50
	 */
	public void updateIndexTable(int businessId,String type,String action){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "UPDATE "+indexTable+" SET action = ?,flag = ? WHERE businessId = ? AND type = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, action);
			pstmt.setInt(2, 0);
			pstmt.setInt(3, businessId);
			pstmt.setString(4, type);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}
}
