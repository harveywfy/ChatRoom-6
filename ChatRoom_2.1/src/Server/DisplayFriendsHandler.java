package Server;

import java.io.IOException;
import java.io.PrintWriter;

import Client.Director;
import Client.SocketWriterBuilder;

public class DisplayFriendsHandler extends RequestHandler {

	public DisplayFriendsHandler(UserDB users, String name) {
		super(users, name);
	}

	@Override
	public void handleRequest() {
		//ָ����
		Director director = new Director(new SocketWriterBuilder(
				users.getSocket(name)));
		try {
			//ʹ��ָ��������writer
			PrintWriter writer = (PrintWriter) director.construct();
			
			writer.println(users.getUsersList(name));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
