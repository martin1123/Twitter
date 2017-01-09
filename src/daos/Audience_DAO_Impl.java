package daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dtos.Audience_DTO;

public class Audience_DAO_Impl extends Audience_DAO{

	public Audience_DAO_Impl() throws SQLException{
		super();
		connectDB();
	}
	
	@Override
	protected String query() {
		return "SELECT USER_ID FROM AUDIENCE WHERE DISPLAY_NAME = ''";
	}

	@Override
	protected void prepareFilters(PreparedStatement ps) {
		// TODO Auto-generated method stub	
		//Para este caso el metodo lo implemento vacio
	}

	@Override
	protected Audience_DTO setUserAud(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return new Audience_DTO(rs.getString("USER_ID"));
	}

	@Override
	public void UpdateAudience(Audience_DTO audDto) throws SQLException {
		PreparedStatement pstm;
		String sql = "UPDATE AUDIENCE SET DISPLAY_NAME = ? WHERE USER_ID = ?";
		pstm = conn.prepareStatement(sql);
		pstm.setString(1, audDto.getDisplayName());
		pstm.setString(2, audDto.getUserId());
		pstm.executeUpdate();
	}
	

}
