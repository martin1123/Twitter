package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import conexionDB.ConnectionPool;
import dtos.Audience_DTO;

public abstract class Audience_DAO {

	protected abstract String query();

	protected abstract String queryUpd();

	protected abstract void setValuesUpd(Audience_DTO audDto, PreparedStatement pstm) throws SQLException;
	
	protected abstract void prepareFilters(PreparedStatement ps);

	protected abstract Audience_DTO setUserAud(ResultSet rs) throws SQLException;

	public Collection<Audience_DTO> searchAudience() throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = ConnectionPool.getPool().getConnection();
			String sql = query();
			pstm = con.prepareStatement(sql);
			prepareFilters(pstm);
			rs = pstm.executeQuery();
			Vector<Audience_DTO> ret = new Vector<Audience_DTO>();
			while (rs.next()) {
				ret.add(setUserAud(rs));
			}
			return ret;
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			releaseResources(con, pstm, rs, true);
		}
	}

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
