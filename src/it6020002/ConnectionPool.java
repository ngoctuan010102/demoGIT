package it6020002;
import java.sql.*;

public interface ConnectionPool {
	
	//phương thức xin kết nốt
	public Connection getConnection(String objectName) throws SQLException;
	
	//phương thức yêu cầu trả về kết nối
	public void releaseConnection(Connection con,String objectName )throws SQLException;
}
