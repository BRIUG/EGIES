package casm.gis.full.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import casm.gis.full.util.DataBaseUtils;
import junit.framework.TestCase;

/*
 * Create a database test tool class
 * 2017-05-13 13:06:29
 */
public class TestDataBaseUtils extends TestCase{

	//add
	public void test1(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "INSERT INTO t_index (businessId,TYPE,flag) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setString(2, "user");
			pstmt.setInt(3, 0);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}
	
	//update
	public void test2(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "UPDATE t_index SET flag=? WHERE recid = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}
	
	//find
	public void test3(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "SELECT businessId FROM t_index WHERE TYPE=? AND flag=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "user");
			pstmt.setInt(2, 1);
			rs = pstmt.executeQuery();
			while(rs.next()){
				int bID  = rs.getInt("businessId");
				System.out.println("businessId:"+bID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}
	
	//delete
	public void test4(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtils.getConnection();
			String sql = "DELETE FROM t_index WHERE recid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DataBaseUtils.closeConnection(conn, pstmt, stmt, rs);
		}
	}
	
}
