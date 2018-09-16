package casm.gis.full.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import casm.gis.util.StringUtils;
import casm.gis.full.config.InitParams;

/*
 * Database tool class
 * 2017-05-13 12:31:17
 */
public class DataBaseUtils {

	private static String URL;
	private static String USER;
	private static String PASSWORD;
	
	static{
		URL = StringUtils.getConfigParam(InitParams.DATABASE_URL, "", InitParams.SEARCH_PROPERTIES);
		USER = StringUtils.getConfigParam(InitParams.DATABASE_USER, "", InitParams.SEARCH_PROPERTIES);
		PASSWORD = StringUtils.getConfigParam(InitParams.DATABASE_PASSWORD, "", InitParams.SEARCH_PROPERTIES);
	}
	
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/*
	 * Write a tool class for data closure
	 * 2017-05-13 13:15:48
	 */
	public static void closeConnection(Connection conn,	PreparedStatement pstmt, Statement stmt, ResultSet rs){
		try {
			if(rs != null){
				rs.close();
			}
			
			if(stmt != null){
				stmt.close();
			}
			
			if(pstmt != null){
				pstmt.close();
			}
			
			if(conn != null){
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
