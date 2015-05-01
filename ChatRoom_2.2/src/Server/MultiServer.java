package Server;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiServer {
	private ServerSocket server;
	private UserDB users;

	public MultiServer() throws IOException {
		users = UserDB.getInstance();
		server = new ServerSocket(8888);
	}

	public MultiServer(int port) throws IOException {
		users = UserDB.getInstance();
		server = new ServerSocket(port);
	}

	public void runServer() throws IOException {
		// �����̳߳أ���������ӵ�runnable�У�Ȼ���ڴ����̺߳��Զ�������Щ����
		ExecutorService exec = Executors.newCachedThreadPool();

		System.out.println("��������������ʼ����������");
		while (true) {
			Socket socket = server.accept();
			System.out.println("���ܿͻ���" + socket.getInetAddress()
					+ "���������󣬿�ʼͨ�š�����");

			// �����Ӻ��socket�½��߳�
			exec.execute(new SingleServer(users, socket));

			//server.close();
		}
	}

	public static void main(String[] args) throws IOException {

		MultiServer server = new MultiServer(8888);
		server.runServer();

	}
}
