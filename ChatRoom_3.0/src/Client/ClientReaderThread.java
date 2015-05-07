package Client;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import Tools.Bulider.BufferedReaderBuilder;
import Tools.Bulider.Director;
import Client.ResCollection.ResponseCollectionManager;

//�ͻ��˵Ķ�ȡ�߳�
public class ClientReaderThread extends Thread {

	private Socket socket;
	private ResponseCollectionManager resCollection;
	
	public ClientReaderThread(Socket socket) {
		this.socket = socket;
		this.resCollection = null;
	}
	
	public void setResponseCollectionManager(ResponseCollectionManager m){
		this.resCollection = m;
	}

	public void run() {
		// ָ����
		Director director = new Director(new BufferedReaderBuilder(socket));
		try {
			// ʹ��ָ��������reader
			BufferedReader reader = (BufferedReader) director.construct();

			String receiveMsg = "";
			while (true) {
				//�������Է���������Ϣ
				receiveMsg = reader.readLine();
				
				//������Ϣ����
				resCollection.addMsg(receiveMsg);				
				//System.out.println(receiveMsg);
			}
			// reader.close();// �ر����ᵼ��socket�Ĺرգ���
		} catch (SocketException e) {
			System.out.print("�ͻ���socket�ѹرգ�");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.print("�������׳��쳣��");
			e.printStackTrace();
		} finally {
			System.out.println("������ִ����ϣ�");
		}
	}

}
