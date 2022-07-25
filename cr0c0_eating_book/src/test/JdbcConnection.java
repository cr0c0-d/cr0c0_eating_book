package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcConnection {
	public Connection conn;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	
	public Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@cr0c0db_medium?TNS_ADMIN=C:/Users/최지예/Downloads/Wallet_cr0c0db";
			String user = "cr0c0_eating_book";
			String password = "Cr0c0eatingbook()";
			conn = DriverManager.getConnection(url, user, password);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}

	
	
}