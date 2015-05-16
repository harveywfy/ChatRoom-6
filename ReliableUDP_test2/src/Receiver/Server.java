package Receiver;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Server {

	private DatagramSocket dataSocket;
	private ReceiveWindow window;
	private int numScope;

	public Server(String host, int port, int numScope) {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		try {
			dataSocket = new DatagramSocket(socketAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.numScope = numScope;
		window = new ReceiveWindow((this.numScope + 1) / 2);// ���մ���
		System.out.println("���������������");
	}

	public void runServer() {
		// �����߳�
		ReceiveThread thread = new ReceiveThread(dataSocket, window);
		thread.start();

		// ��Ӧ�߳�
		ResponseThread thread2 = new ResponseThread(dataSocket, window);
		thread2.start();
	}

	public static void main(String[] args) {

		String serverHost = "127.0.0.1";
		int serverPort = 3344;
		int numScope = 8;// ��ſռ�0 - 7�����ڴ�СΪ��ſռ��һ��

		Server server = new Server(serverHost, serverPort, numScope);// ������
		server.runServer();
	}
}
