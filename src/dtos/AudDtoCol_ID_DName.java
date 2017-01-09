package dtos;

public class AudDtoCol_ID_DName extends AudDtoCollect{

	public long [] getIds() {
		
		long []lista = new long[100];
		int i = 0;
		for(Audience_DTO a :ad ){
			lista[i] = Long.valueOf(a.getUserId());
			i++;
		}
		
		return lista;
	}

}
