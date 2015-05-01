package Server;

import java.io.IOException;
//import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import net.sf.json.JSONObject;
import Client.Director;
import Client.PrintWriterBuilder;

public class DisplayFriendsHandler extends RequestHandler {

	public DisplayFriendsHandler(UserDB users, String name) {
		super(users, name);
	}

	@Override
	public void handleRequest() {
		//ָ����
		Director director = new Director(new PrintWriterBuilder(
				users.getSocket(name)));
		try {
			//ʹ��ָ��������writer
			PrintWriter writer = (PrintWriter) director.construct();
			
			//��hashMapת����string
			JSONObject json = JSONObject.fromObject(users.getSocketsMap());
			String jsonString = JsonTrans.buildJson("userMap", json);
			
			writer.println(jsonString.toString());
			
			System.out.println(name + "��������б�");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
