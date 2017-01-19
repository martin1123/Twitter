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
	
	public String toString(){
		String ret = new String();
		for(Audience_DTO a :ad ){
			ret += a.getUserId() + ", ";
		}
		return ret;
	}

}
