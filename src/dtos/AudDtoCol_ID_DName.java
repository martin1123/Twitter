package dtos;

/**
 * Clase utilizada para acumular 100 UIDs de la tabla AUDIENCE con el fin de enviar en un solo request
 * los 100 uids al metodo /users/lookup.json de la api de twitter.
 * @author mmaccio
 *
 */
public class AudDtoCol_ID_DName extends AudDtoCollect{

	/**
	 * Metodo que retorna un array de uids para enviar a la api de Twitter
	 * TODO: Verificar que en el for no se acumulen mas de 100 uids.
	 * @return
	 */
	public long [] getIds() {
		
		long []lista = new long[100];
		int i = 0;
		for(Audience_DTO a :ad ){
			lista[i] = Long.valueOf(a.getUserId());
			i++;
		}
		
		return lista;
	}
	
	public String toString(){
		String ret = new String();
		for(Audience_DTO a :ad ){
			ret += a.getUserId() + ", ";
		}
		return ret;
	}

}
