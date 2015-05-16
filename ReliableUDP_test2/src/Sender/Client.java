package Sender;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Client {

	private DatagramSocket dataSocket;
	private SendWindow window;
	private int numScope;

	public Client(int numScope) {
		try {
			dataSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.numScope = numScope;
		window = new SendWindow((this.numScope + 1) / 2);
		System.out.println("�ͻ�������������");
	}

	public void runClient(String host, int port) {
		// �������ݰ�
		SendThread thread = new SendThread(dataSocket, window);
		thread.setNumScope(numScope);
		thread.setServerHost(host);
		thread.setServerPort(port);
		thread.start();

		// ����ȷ�ϰ�
		ACKReceiverThread ackReceiver = new ACKReceiverThread(dataSocket,
				window);
		ackReceiver.start();

		// ���ݰ��ط��߳�
		ReSendThread thread2 = new ReSendThread(dataSocket, window);
		thread2.setServerHost(host);
		thread2.setServerPort(port);
		thread2.start();
	}

	public static void main(String[] args) {
		int numScope = 8;// ��ſռ�0 - 7 , ���ڴ�СΪ��ſռ��һ��

		String serverHost = "127.0.0.1"; // ָ�����ն˵�ַ
		int serverPort = 3344;

		Client client = new Client(numScope);
		client.runClient(serverHost, serverPort);
	}
}
