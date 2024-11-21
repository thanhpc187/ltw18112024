package vn.iotstar.configs;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectSQL {
	private final String serverName = "THANHPC\\MSSQLSERVER01";

	private final String dbName = "laptrinhwebchieuthu2";

	private final String portNumber = "1433";

	private final String userID = "laptrinhwebchieuthu2";

	private final String password = "thanh0974100417";

	public Connection getConnection() throws Exception {

		String url = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return DriverManager.getConnection(url, userID, password);

	}
}