package src;

import java.util.ResourceBundle;
import java.util.Vector;

import daos.Audience_DAO;
import dtos.AudDtoCol_ID_DName;
import dtos.Audience_DTO;
import factory.Ufactory;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
*
* Programa que recupera los UIDs de usuarios de la tabla AUDIENCE de bluemix, y con dichos UIDs
* se recupera desde la API de Twitter la información de cada usuario asociado.
* Finalmente con la información recuperada de Twitter se actualiza la tabla AUDIENCE.
*
* Archivos de configuracion:
* 
* /src/connectionTwitterApi/twitterConn.properties: Archivo de credenciales para conectar a la APi de Twitter.
* 
* /src/conexionDB/jdbc.properties: Archivo para la configuracion de la base de datos.
* 
* /src/factory/factory.properties: Archivo que indica la clase a utilizar para mappear la tabla AUDIENCE 
*                                  de la base de datos.
* 
* 
* Nota: El programa fue originalmente pensado para soportar Multithreading, pero como la base de datos
* no soportaba multiples updates, se tuvo que llamar al metodo run de la clase Updater para correr un solo thread
* para realizar la menor cantidad de modificaciones posibles. 
* Sin embargo, con minimos cambios que a continuacion se especifican, puede hacerse un programa
* que soporte multithreading para que se actualicen varios registros en paralelo y asi reducir
* consirablemente la duracion del programa, siempre y cuando la base de datos lo permita.
* 
* Consideraciones para multithreading:
* 
* 1) En el main, en vez de invocar al metodo run de la clase Thread, se debe invocar al metodo start
*    para que comience un thread paralelo de ejecucion. 
*    
* 2) Tener en cuenta de limitar la cantidad de Threads que se ejecuten en paralelo.
*    Asi como esta implementado el programa ahora, ejecutaría una cantidad indefinida de threads,
*    lo cual provocaría que la maquina se quede sin recursos y colapse el sistema.
*    
* 3) Modificar el archivo jdbc.properties en el parquete conexionDB y configurar los parametros para
*    el pool de conexiones. (Tener en cuenta máxima cantidad de conexiones permitida por la Base de datos).
*    
* 4) En el metodo releaseConnection de la clase ConnectionPool hay que descomentar
*    la linea en la que aparece "free.add(con);" y comentar el bloque try/catch que aparece debajo de esa linea.
*    Con esto, al terminar de usarse una conexion, pasara a una cola de conexiones disponibles, que podrán ser
*    reutilizables por otros threads.
*   
* 5) Una vez finalizado el programa, se debe invocar al metodo close() de la clase ConnectionPool para cerrar
*    todas las conexiones abiertas en el pool de conexiones.
* 
* @author: Martin Maccio
*
* @version: 1.0.0
*
*/
public class Twitter {

	public static void main(String args[]) throws InterruptedException {
		Audience_DAO audTable = null;
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
	 * Metodo estatico que se conecta a la API de Twitter y devuelve una instancia 
	 * para poder trabajar con los metodos de la API.
	 * Las credenciales para la conexion a la API de Twitter se encuentran en el
	 * archivo twitterConn.properties del paquete connectionTwitterApi
	 * @return
	 */
	private static twitter4j.Twitter connectTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		ResourceBundle rb = ResourceBundle.getBundle("connectionTwitterApi.twitterConn");
		cb.setDebugEnabled(true).setOAuthConsumerKey(rb.getString("TWITTER_CONSUMER_KEY"))
		.setOAuthConsumerSecret(rb.getString("TWITTER_SECRET_KEY"))
		.setOAuthAccessToken(rb.getString("TWITTER_ACCESS_TOKEN"))
		.setOAuthAccessTokenSecret(rb.getString("TWITTER_ACCESS_TOKEN_SECRET"));

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter twitter = tf.getInstance();
		return twitter;
	}

	/**
	 * Metodo estatico que retorna una instancia que representa a la tabla de Audiencia.
	 * @param audTable
	 * @return audTable
	 */
	private static Audience_DAO connectDB(Audience_DAO audTable) {
		try {
			/*Se utiliza un factory method para el desacoplamiento de las clases que hereden de
			 * la clase Audience_DAO. La clase con la que se desee trabajar la tabla de AUDIENCE
			 * esta definida en el archivo factory.properties en el paquete factory*/
			audTable = (Audience_DAO) Ufactory.getInstance("AUCIENCE_IDS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return audTable;
	}

	/**
	 * Metodo estático que retorna una coleccion de DTOS, que representan las filas
	 * resultantes de la query de busqueda en la tabla de AUDIENCE. 
	 * @param audTable
	 */
	private static Vector<Audience_DTO> searchAudience(Audience_DAO audTable) {
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