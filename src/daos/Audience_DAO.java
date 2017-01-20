package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import conexionDB.ConnectionPool;
import dtos.Audience_DTO;

/**
*
* Clase abstracta hecha con el fin ralizar querys de consulta y updates a la tabla AUDIENCE de Bluemix.
* Dependiendo de que datos se quieran recuperar o que datos
* 
* Nota: Las clases que hereden de esta clase, deberan implementar la query de busqueda, filtros de busqueda, 
* el update a realizar sobre la tabla, y los parametros del update.
*
* @author: Martin Maccio
*
* @version: 1.0.0
*
*/
public abstract class Audience_DAO {

	/**
	 * Metodo abstracto que retorna un String que representa la query a realizar sobre la tabla
	 * AUDIENCE.
	 * @return query
	 */
	protected abstract String query(); 

	/**
	 * Metodo abstracto que retorna un String que representa el update a realizar sobre la tabla
	 * AUDIENCE.
	 * @return queryUpd
	 */
	protected abstract String queryUpd();

	/**
	 * Metodo abstracto que setea los valores del update a realizar sobre la tabla AUDIENCE
	 * @param audDto
	 * @param pstm
	 */
	protected abstract void setValuesUpd(Audience_DTO audDto, PreparedStatement pstm) throws SQLException;
	
	/**
	 * Metodo abstracto que setea los filtros de una query. En caso de que la query
	 * no tenga ninguna clase de filtro(ningun where), este metodo se deberá implementar
	 * vacio.
	 * @param ps
	 */
	protected abstract void prepareFilters(PreparedStatement ps);

	/**
	 * Metodo abstracto que retorna un DTO que representa una fila de la tabla AUDIENCE.
	 * @param rs
	 * @return audDTO
	 */
	protected abstract Audience_DTO setUserAud(ResultSet rs) throws SQLException;


	/**
	 * Metodo encargado de la conexion a la base de datos de bluemix y de realizar
	 * la consulta a la tabla AUDIENCE. Retorna una coleccion de Audience_DTO, que representarian
	 * las filas retornadas por la query.
	 * @return ret
	 */
	public Collection<Audience_DTO> searchAudience() throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			/*Se obtiene la conexion desde un pool de conexiones*/
			con = ConnectionPool.getPool().getConnection();
			/*Se obtiene query*/
			String sql = query();
			pstm = con.prepareStatement(sql);
			/*En caso de haber algun where se preparan se setean los valores a filtrar*/
			prepareFilters(pstm);
			rs = pstm.executeQuery();
			Vector<Audience_DTO> ret = new Vector<Audience_DTO>();
			/*Se mappean cada registro resultante en la tabla Audience_DTO*/
			while (rs.next()) {
				ret.add(setUserAud(rs));
			}
			return ret;
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			/*Se liberan los recursos de conexion*/
			releaseResources(con, pstm, rs, true);
		}
	}

	/**
	 * Metodo que actualiza la base de datos segun el DTO que se reciba de la tabla de AUDIENCE.
	 * El parametro releaseConAtFinish sirve para indicar si se realiza o no un commit al finalizar
	 * el update.
	 * 
	 * @param audDto
	 * @param con
	 * @param releaseConAtFinish
	 * @return con
	 */
	public Connection UpdateAudience(Audience_DTO audDto, Connection con, boolean releaseConAtFinish) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			if(con == null)
				con = ConnectionPool.getPool().getConnection();
			String sql = queryUpd();
			pstm = con.prepareStatement(sql);
			setValuesUpd(audDto, pstm);
			pstm.executeUpdate();
			if(releaseConAtFinish){
				con.commit();
				return null;
			}else{
				return con;
			}
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			releaseResources(con, pstm, rs, releaseConAtFinish);
		}

	}
	
	/**
	 * 
	 * Se liberan los recursos de la conexion. El parametro releaseConAtFinish indica si ya se puede
	 * dejar de utilizar la conexion.
	 * @param con
	 * @param pstm
	 * @param rs
	 * @param closeConAtFinish 
	 * @throws SQLException
	 */
	private void releaseResources(Connection con, PreparedStatement pstm, ResultSet rs, boolean releaseConAtFinish) throws SQLException {
		if (rs != null)
			rs.close();
		if (pstm != null)
			pstm.close();

		if (con != null && releaseConAtFinish) {
			ConnectionPool.getPool().releaseConnection(con);
		}
	}
}
