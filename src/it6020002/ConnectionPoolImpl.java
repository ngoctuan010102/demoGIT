package it6020002;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import com.mysql.cj.jdbc.Driver;

public class ConnectionPoolImpl implements ConnectionPool {
	
	//Trình điều khiển làm việc với MýQL
	private String driver;
	
	//Đường dẫn thực thi của MySQL
	private String url;
	
	//Tài khoản làm việc
	private String username;
	private String userpass;
	
	//Đối tượng lưu trữ kết nối
	
	private Stack<Connection> pool;
	public ConnectionPoolImpl() {
		
		//Xác định trình điều khiển
		this.driver = "com.mysql.cj.jdbc.Driver";
		
		//Nạp trình điều khiển
		try {
			Class.forName(this.driver);
		}catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//xác định đường dẫn thực thi
		this.url = "jdbc:mysql://localhost:3306/20231it6020002";
		
		//Xác định tài khaonr làm việc
		this.username = "root";
		this.userpass = "123456789";
		
		// khởi tạo đối tượng lưu trữ
		this.pool = new Stack<>();
	}
	
	@Override
	public Connection getConnection(String objectName) throws SQLException {
		// TODO Auto-generated method stub
		
		if(this.pool.isEmpty()) {
			System.out.print(objectName + "have created a new Connection!\n" );
			return DriverManager.getConnection(this.url, this.username, this.userpass);
		}else {
			System.out.println(objectName + "have popped the Connection!");
			return this.pool.pop();
		}
	}

	@Override
	public void releaseConnection(Connection con, String objectName) throws SQLException {
		// TODO Auto-generated method stub
		
		System.out.println(objectName + "have pushed the Connection!");
		this.pool.push(con);
	}
	protected void finalize() throws Throwable {
		//loại bỏ các kết nối trong pool
		
		this.pool.clear();
		this.pool = null;
				
		System.out.println("ConnectionPool is closed");
	}

}
