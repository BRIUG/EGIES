package casm.gis.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseUtils {

	/*
	 * Get the connection content class
	 * 2017-04-30 09:20:12
	 */
	public static Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8"	;
			String user = "root";
			String password = "root";
			conn = DriverManager.getConnection(url,user,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
