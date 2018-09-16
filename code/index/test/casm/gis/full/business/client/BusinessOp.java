package casm.gis.full.business.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import casm.gis.full.index.IndexCommonServer;
import casm.gis.full.util.DataBaseUtils;
import junit.framework.TestCase;

/*
 * Business related tests
 * 2017-05-14 13:51:25
 */
public class BusinessOp extends TestCase{

	/*
	 * Add content test for t_user table
	 */
	public void test1(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String bSql = "INSERT INTO t_user (username,age) VALUES (?,?)";
			pstmt = conn.prepareStatement(bSql);
			pstmt.setString(1, "Youg");
			pstmt.setInt(2, 27);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	
		IndexCommonServer ics = new IndexCommonServer();
		ics.addIndexTable(5, "user", "add");
		
/*		try {
			conn = DataBaseUtils.getConnection();
			String sql = "INSERT INTO t_index (businessId,TYPE,flag) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 5);
			pstmt.setString(2, "user");
			pstmt.setInt(3, 0);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}*/
	}
}
