package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import conexionDB.DBConnection;
import dtos.Audience_DTO;

public abstract class Audience_DAO {
	protected Connection conn = null;
	protected abstract String query();
	protected abstract void prepareFilters(PreparedStatement ps);
	protected abstract Audience_DTO setUserAud(ResultSet rs) throws SQLException;
	public abstract void UpdateAudience(Audience_DTO audDto) throws SQLException;
	
	public Collection<Audience_DTO> searchAudience() throws Exception{
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try{
			con = DBConnection.getConnection();
			String sql = query();
			pstm = con.prepareStatement(sql);
			prepareFilters(pstm);
			rs = pstm.executeQuery();
			Vector<Audience_DTO> ret = new Vector<Audience_DTO>();
			while(rs.next()){
				ret.add(setUserAud(rs));
			}
			return ret;
		}catch(Exception ex){
			throw new Exception(ex);
		}
	}
	
	public void commit(){
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 * @throws SQLException
	 */
	protected void connectDB() throws SQLException {
		conn = DBConnection.getConnection();
	}
}
