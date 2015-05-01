package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

//�ͻ��˵�д����
public class ClientWriter {

	private Socket socket;
	private String userName;
	private String friendName;

	private PrintWriter writer;

	public ClientWriter(Socket socket, String userName, String friendName) {
		this.socket = socket;
		this.userName = userName;
		this.friendName = friendName;

		// ָ����
		Director director = new Director(new PrintWriterBuilder(this.socket));
		// ʹ��ָ��������һ��writer
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) {
		try {
			MsgTrans msgTrans = new MsgTrans();
			msgTrans.setPublisher(userName);
			msgTrans.setReceiver(friendName);
			msgTrans.setMsgNum("2");

			msgTrans.setWords(msg);
			String sendMsg = msgTrans.getResult();
			String a = JsonTrans.buildJson("msg", sendMsg);

			writer.println(a);
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
