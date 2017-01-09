package factory;

import java.util.ResourceBundle;

public class Ufactory {
	private static Object obj = null;
	
	public static Object getInstance(String objName) throws Exception{
		if(obj==null){
			ResourceBundle rb = ResourceBundle.getBundle("factory.factory");
			String sClassName = rb.getString(objName);
			obj = Class.forName(sClassName).newInstance();
		}
		return obj;
	}
}
