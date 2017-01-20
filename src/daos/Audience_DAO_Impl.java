package daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dtos.Audience_DTO;

/**
*
* Clase que implementa los metodos abstractos de la clase Audience_DAO.
* En esat clase se definen las querys y sus filtros, como asi tambien los updates
* con los valores a actualizar.
*
* @author: Martin Maccio
*
* @version: 1.0.0
*
*/
public class Audience_DAO_Impl extends Audience_DAO {

	@Override
	protected String query() {
//		return "SELECT USER_ID FROM AUDIENCE WHERE DISPLAY_NAME = '' ORDER BY USER_ID ASC";
		return "SELECT USER_ID FROM AUDIENCE";
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
		String sql = "UPDATE AUDIENCE SET DISPLAY_NAME = ?," + 
					 "PREFERRED_USERNAME = ?," + 
					 "STATUSES_COUNT = ?," + 
					 "FRIENDS_COUNT = ?," + 
					 "FOLLOWERS_COUNT = ?," + 
					 "IS_VERIFIED = ? ," + 
					 "USER_LOCATION = ? ," + 
					 "UPDATE_TIME = ? " + 
					 "WHERE USER_ID = ?";
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
		pstm.setString(2, audDto.getPreferredUserName());
		pstm.setString(3, String.valueOf(audDto.getStatusesCount()));
		pstm.setString(4, String.valueOf(audDto.getFriendsCount()));
		pstm.setString(5, String.valueOf(audDto.getFollowersCount()));
		pstm.setString(6, String.valueOf(audDto.getIsVerified()));
		pstm.setString(7, audDto.getLocation());
		pstm.setTimestamp(8, audDto.getTimeStamp());
		pstm.setString(9, audDto.getUserId());
	}

}
