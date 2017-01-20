package src;

import java.sql.Connection;
import java.sql.Timestamp;

import daos.Audience_DAO;
import dtos.AudDtoCol_ID_DName;
import dtos.Audience_DTO;
import factory.Ufactory;
import twitter4j.ResponseList;
import twitter4j.User;
import twitter4j.api.UsersResources;


/**
*
* Clase que se encarga de realizar la parte correspondiente a la busqueda de informacion de 100 usuarios
* y actualizar sus datos en la tabla AUDIENCE de bluemix. 
*
* @author: Martin Maccio
*
* @version: 1.0.0
*
*/
public class Updater implements Runnable {

	AudDtoCol_ID_DName ad;
	twitter4j.Twitter twitter = null;

	public Updater(AudDtoCol_ID_DName ad, twitter4j.Twitter twitter) {
		this.ad = ad;
		this.twitter = twitter;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Audience_DAO ai = null;
		Timestamp timestamp = null;

		try {
			ai = (Audience_DAO) Ufactory.getInstance("AUCIENCE_IDS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}	

		try {
			//Se obtiene la informacion de los uids
			ResponseList<User> users = ((UsersResources) twitter).lookupUsers(ad.getIds());
			int i;
			Audience_DTO aDTO;

			//Por cada usuario que se haya encontrado resultados se setean los valores
			//recuperados.
			//En caso de no encontrarse algun valor para el usuario, no se seteara ningun valor.
			for (User user : users) {
				aDTO = ad.getAudDtoPerId(String.valueOf(user.getId()));
				if(aDTO == null){
					System.out.println("No se encontro usuario");
					break;
				}
				timestamp = new Timestamp(System.currentTimeMillis());
				aDTO.setTimeStamp(timestamp);
				aDTO.setDisplayName(user.getName());
				aDTO.setPreferredUserName(user.getScreenName());
				aDTO.setFriendsCount(user.getFriendsCount());
				aDTO.setFollowersCount(user.getFollowersCount());
				aDTO.setStatusesCount(user.getStatusesCount());
				aDTO.setIsVerified((byte)((user.isVerified())?1:0));
				aDTO.setLocation(user.getLocation());
			}
			
			Connection con = null;
			for (i = 0; i < ad.size(); i++) {
				aDTO = ad.getAudDto(i);
				//Cuando se llegue al ultimo eleento se actualiza el ultimo registro
				//Y se realiza un commit.
				if(i < ad.size()-1)
					//Se actualiza tabla sin commitear
					con = ai.UpdateAudience(aDTO, con,false);
				else
					//Se actualiza y se commitea
					con = ai.UpdateAudience(aDTO, con,true);
			}

//			notifyAll();
//			Thread.currentThread().notify();
		} catch (Exception te) {
			te.printStackTrace();
			System.out.println("Failed to lookup users: " + te.getMessage());
			System.out.println(ad.toString());
//			System.exit(-1);
		}
	}
}
