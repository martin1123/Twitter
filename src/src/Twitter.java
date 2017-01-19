package src;

import java.util.Vector;

import daos.Audience_DAO_Impl;
import dtos.AudDtoCol_ID_DName;
import dtos.Audience_DTO;
import factory.Ufactory;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitterCred.TwitterCred;

public class Twitter {

	public static void main(String args[]) throws InterruptedException {
		Audience_DAO_Impl audTable = null;
		Vector<Audience_DTO> audCollect;
		Runnable proceso;

		// Conexion a base de datos
		audTable = connectDB(audTable);

		if (audTable == null)
			return;

		// Conexion a la api de twitter
		twitter4j.Twitter twitter = connectTwitter();

		// Buscar audiencia en base de datos
		audCollect = searchAudience(audTable);

		if (audCollect == null)
			return;

		AudDtoCol_ID_DName audDtoCol = new AudDtoCol_ID_DName();
		
		for (int i = 0; i < audCollect.size(); i++) {
			if (i != 0 && (i % 100 == 0)) {
				proceso = new Updater(audDtoCol, twitter);
				new Thread(proceso).run();
				audDtoCol = new AudDtoCol_ID_DName();
			} else {
				audDtoCol.addAudDto(audCollect.get(i));
			}

		}

		if (audDtoCol.size() > 0) {
			proceso = new Updater(audDtoCol, twitter);
			Thread taux = new Thread(proceso);
			taux.run();
		}
	}

	/**
	 * @return
	 */
	private static twitter4j.Twitter connectTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(TwitterCred.getTwitterConsumerKey())
				.setOAuthConsumerSecret(TwitterCred.getTwitterSecretKey())
				.setOAuthAccessToken(TwitterCred.getTwitterAccessToken())
				.setOAuthAccessTokenSecret(TwitterCred.getTwitterAccessTokenSecret());

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter twitter = tf.getInstance();
		return twitter;
	}

	/**
	 * @param audTable
	 * @return
	 */
	private static Audience_DAO_Impl connectDB(Audience_DAO_Impl audTable) {
		try {
			audTable = (Audience_DAO_Impl) Ufactory.getInstance("AUCIENCE_IDS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return audTable;
	}

	/**
	 * @param audTable
	 */
	private static Vector<Audience_DTO> searchAudience(Audience_DAO_Impl audTable) {
		Vector<Audience_DTO> aud = null;
		try {
			aud = (Vector<Audience_DTO>) audTable.searchAudience();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aud;
	}

}