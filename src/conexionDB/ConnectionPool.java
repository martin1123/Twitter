package conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;

public class ConnectionPool {
	private Vector<Connection> free;
	private Vector<Connection> used;
	
	/*Connection data*/
	private String user;
	private String pass;
	private String host;
	private String port;
	private String sid;
	private String driver;
	private String url;
	
	/*Pool min size and max size*/
	private int minSize;
	private int maxSize;
	private int steep;
	
	//Only instance. Singleton Pattern
	private static ConnectionPool pool = null;
	
	/*Constructor*/
	private ConnectionPool(){
		try{
			ResourceBundle rb = ResourceBundle.getBundle("conexionDB.jdbc");
			user = rb.getString("user");
			pass = rb.getString("pass");
			host = rb.getString("host");
			port = rb.getString("port");
			sid = rb.getString("sid");
			driver = rb.getString("driver");
			url = rb.getString("url");
			Class.forName(driver);
			
			minSize = Integer.parseInt(rb.getString("minSize"));
			maxSize = Integer.parseInt(rb.getString("maxSize"));
			steep = Integer.parseInt(rb.getString("steep"));
			
			free = new Vector<Connection>();
			used = new Vector<Connection>();
			
			_instance(minSize);
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	/*Get Pool with singleton pattern*/
	public synchronized static ConnectionPool getPool(){
		if(pool == null){
			pool = new ConnectionPool();
		}
		return pool;
	}
	
	
	public synchronized Connection getConnection(){
		if(free.size() == 0){
			if(!_createMoreConnections()){
				throw new RuntimeException ("No more connections available");
			}
		}
		Connection con = free.remove(0);
		used.addElement(con);
		return con;
	}
	
	private boolean _createMoreConnections(){
		int current = free.size() + used.size();
		int n = Math.min(maxSize - current, steep);
		
		if(n > 0){
			_instance(n);
		}
		
		return n>0;
	}
	
	private void _instance(int n){
		try{
			Connection con;
			
			for(int i = 0; i < n; i++){
				con = DriverManager.getConnection(url+"://"+host+":"+port+"/"+sid+"?useUnicode=yes&characterEncoding=UTF-8", user, pass);
				con.setAutoCommit(false);
				free.add(con);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public synchronized void releaseConnection(Connection con){
		boolean ok = used.remove(con);
		if(ok){
//			free.add(con);
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException("Error close connection");
			}
		}else{
			throw new RuntimeException("Returns an exception that is not mine");
		}
	}
	
	public synchronized void close(){
		try{
			for(Connection c : free){
				c.close();
			}
			
			for(Connection c : used){
				c.close();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}
