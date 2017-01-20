package conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;

/**
*
* Clase que se encarga de manejar el pool de conexiones a la base de datos de Bluemix.
* 
* Su principal motivo es para que se puedan compartir las conexiones entre los distintos
* threads de la aplicacion.
*
* @author: Martin Maccio
*
* @version: 1.0.0
*
*/
public class ConnectionPool {
	private Vector<Connection> free;
	private Vector<Connection> used;
	
	/*Datos de la conexion*/
	private String user;
	private String pass;
	private String host;
	private String port;
	private String sid;
	private String driver;
	private String url;
	
	/*Atributos del Pool*/
	private int minSize;
	private int maxSize;
	private int steep;
	
	/*Instancia del pool de conexiones. Se aplica patron singleton para manejar
	 * siempre el mismo pool de conexiones*/
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
	
	/*Metodo para obtener el pool de conexiones*/
	public synchronized static ConnectionPool getPool(){
		if(pool == null){
			pool = new ConnectionPool();
		}
		return pool;
	}
	
	/*Metodo para obtener una conexion del pool de conexiones*/
	public synchronized Connection getConnection(){
		//Si no hay ninguna conexion libre se crea una n conexiones segun lo indicado
		//en el atributo steep
		if(free.size() == 0){
			if(!_createMoreConnections()){
				throw new RuntimeException ("No more connections available");
			}
		}
		Connection con = free.remove(0);
		used.addElement(con);
		return con;
	}
	
	/*Metodo que se utiliza para crear nuevas conexiones*/
	private boolean _createMoreConnections(){
		int current = free.size() + used.size();
		int n = Math.min(maxSize - current, steep);
		
		if(n > 0){
			_instance(n);
		}
		
		return n>0;
	}
	
	/*Metodo para instanciar n conexiones a la base de datos*/
	private void _instance(int n){
		try{
			Connection con;
			
			for(int i = 0; i < n; i++){
				con = DriverManager.getConnection(url+"://"+host+":"+port+"/"+sid+"?useUnicode=yes", user, pass);
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
	
	/*Metodo para cerrar todas las conexiones a la base de datos*/
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
