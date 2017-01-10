package src;

import java.sql.Connection;

import daos.Audience_DAO_Impl;
import dtos.AudDtoCol_ID_DName;
import dtos.Audience_DTO;
import factory.Ufactory;
import twitter4j.ResponseList;
import twitter4j.User;
import twitter4j.api.UsersResources;

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
		Audience_DAO_Impl ai = null;

		try {
			ai = (Audience_DAO_Impl) Ufactory.getInstance("AUCIENCE_IDS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}	

		try {
			ResponseList<User> users = ((UsersResources) twitter).lookupUsers(ad.getIds());
			int i;
			Audience_DTO aDTO;

			for (User user : users) {
				aDTO = ad.getAudDtoPerId(String.valueOf(user.getId()));
				if(aDTO == null){
					System.out.println("No se encontro usuario");
					break;
				}
				aDTO.setDisplayName(user.getScreenName());
			}
			
			Connection con = null;
			for (i = 0; i < ad.size(); i++) {
				aDTO = ad.getAudDto(i);
				if(i < ad.size()-1)
					con = ai.UpdateAudience(aDTO, con,false);
				else
					con = ai.UpdateAudience(aDTO, con,true);
			}

//			notifyAll();
//			Thread.currentThread().notify();
			System.exit(0);
		} catch (Exception te) {
			te.printStackTrace();
			System.out.println("Failed to lookup users: " + te.getMessage());
			System.exit(-1);
		}
	}
}
