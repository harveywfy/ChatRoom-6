package Server;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import Client.Director;
import Client.BufferedReaderBuilder;
import Client.PrintWriterBuilder;

public class RequestsManager {

	private UserDB users;
	private String userName;
	private Map<?, ?> requestMap;

	public void setRequestMap(Map<?, ?> map) {
		requestMap = map;
	}

	public RequestsManager(UserDB user, String userName, Map<?, ?> requestMap) {
		this.users = user;
		this.userName = userName;
		this.requestMap = requestMap;
	}

	public boolean runManager() {
		Socket socket = users.getSocket(userName);

		try {
			Director director = new Director(new PrintWriterBuilder(socket));
			PrintWriter writer = (PrintWriter) director.construct();

			director = new Director(new BufferedReaderBuilder(socket));
			BufferedReader reader = (BufferedReader) director.construct();

			RequestHandler handler;

			while (true) {
//				writer.println("���룺\n" + "1�������б�\n" + "2��˽��\n" + "3��Ⱥ��\n"
//						+ "4���˳�\n");
				
				//���������Ҫ�½��̣߳�����
				
				String str = reader.readLine();
				System.out.println(userName + " ѡ��" + str);

				try{		
					handler = (RequestHandler) requestMap.get(str);
					handler.handleRequest();
					
					if (str.equals("4")) {
						break;
					}
				}catch(NullPointerException e){
					writer.println("����������޷�������");
				}
				
			}//end of while
			writer.close();
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		return false;
	}
}
