package Client;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

//�ͻ��˵Ķ�ȡ�߳�
public class ClientReaderThread extends Thread {

	private Socket socket;

	// private InputStream input;

	public ClientReaderThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		// ָ����
		Director director = new Director(new BufferedReaderBuilder(socket));
		try {
			// ʹ��ָ��������reader
			BufferedReader reader = (BufferedReader) director.construct();

			String receiveMsg = "";
			while (!receiveMsg.equalsIgnoreCase("bye")) {
				receiveMsg = reader.readLine();
				System.out.println(receiveMsg);
			}
			//reader.close();// �ر����ᵼ��socket�Ĺرգ���
		} catch (SocketException e) {
			System.out.print("�ͻ���socket�ѹرգ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
