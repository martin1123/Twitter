package conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class DBConnection {
	
	private static Connection conn = null;
	
	public static Connection getConnection(){
		try{
			//if(conn == null){
				Runtime.getRuntime().addShutdownHook(new MyShutDownHook());
				
				ResourceBundle rb = ResourceBundle.getBundle("conexionDB.jdbc");
				String user = rb.getString("user");
				String pass = rb.getString("pass");
				String host = rb.getString("host");
				String port = rb.getString("port");
				String sid = rb.getString("sid");
				String driver = rb.getString("driver");
				String url = rb.getString("url");
				
//				String url = rb.getString("url") + user + "/" + pass + "@" + host + ":" + port + ":" + sid;
				Class.forName(driver);
				conn = DriverManager.getConnection(url+"://"+host+":"+port+"/"+sid, user, pass);
				conn.setAutoCommit(false);
			//}

			return conn;
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Error al crear conexion",ex);
		}
		
	}
	
	static class MyShutDownHook extends Thread{
		public void run(){
			try{
				Connection con = DBConnection.getConnection();
				con.close();
			}catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}
}
