package daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dtos.Audience_DTO;

public class Audience_DAO_Impl extends Audience_DAO {

	@Override
	protected String query() {
		return "SELECT USER_ID FROM AUDIENCE WHERE DISPLAY_NAME = '' ORDER BY USER_ID ASC";
	}

	@Override
	protected void prepareFilters(PreparedStatement ps) {
		// TODO Auto-generated method stub
		// Para este caso el metodo lo implemento vacio
	}

	@Override
	protected Audience_DTO setUserAud(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return new Audience_DTO(rs.getString("USER_ID"));
	}
	
	@Override
	protected String queryUpd() {
		String sql = "UPDATE AUDIENCE SET DISPLAY_NAME = ? WHERE USER_ID = ?";
		return sql;
	}
	
	/**
	 * @param audDto
	 * @param pstm
	 * @throws SQLException
	 */
	@Override
	protected void setValuesUpd(Audience_DTO audDto, PreparedStatement pstm) throws SQLException {
		pstm.setString(1, audDto.getDisplayName());
		pstm.setString(2, audDto.getUserId());
	}

}
