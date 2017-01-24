package dtos;

import java.util.ArrayList;

public class AudDtoCollect {
	
	protected ArrayList<Audience_DTO> ad = new ArrayList<Audience_DTO>();
	
	public AudDtoCollect(){
		
	}
	
	public void addAudDto(Audience_DTO audDto){
		ad.add(audDto);
	}
	
	public int size(){
		return ad.size();
	}
	
	public Audience_DTO getAudDto(int index){
		return ad.get(index);
	}
	
	public Audience_DTO getAudDtoPerId(String id){
		Audience_DTO ret = null;
		for(Audience_DTO a : ad){
			if(a.getUserId().equals(id)){
				ret = a;
				break;
			}
		}
		return ret;
	}
	
}
