package Tools.Bulider;

import java.io.IOException;

//ָ���ߣ�����ָ�����������builder�����
public class Director {

	private Builder builder;
	
	public Director(){
		builder = null;
	}
	
	public Director(Builder builder){
		this.builder = builder;
	}
	
	public Object construct() throws IOException{
		return builder.getResult();
	}
}
