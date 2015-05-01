package Server;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import Client.Director;
import Client.BufferedReaderBuilder;
import Client.PrintWriterBuilder;

/* �̳߳���ʵ����Runnable�ӿ� */
class SingleServer implements Runnable {

	private UserDB users;
	private Socket socket;

	public SingleServer(UserDB users, Socket socket) {
		this.users = users;
		this.socket = socket;
	}

	private RequestsManager manager;

	// ����ʵ��ʱ�Զ�����
	public void run() {
		try {	
			Director director = new Director(new PrintWriterBuilder(socket));
			PrintWriter writer = (PrintWriter) director.construct();

			director = new Director(new BufferedReaderBuilder(socket));
			BufferedReader reader = (BufferedReader) director.construct();

			// ��������Ƿ��ظ�
			while (true) {

				//writer.println("�������ǳƣ�");
				// ����һ������
				String name = reader.readLine();

				// ��Ӧһ�� �ɹ� ���� ʧ��
				if (users.getSocket(name) != null) {
					writer.println("ʧ��");
					continue;
				} else {
					// ���û��ǳƺ����׽�����ӵ�HashMap
					users.addUser(name, socket);
					writer.println("�ɹ�");

					System.out.println("��ע���û���" + name);

					HashMap<String, RequestHandler> requestMap = new HashMap<String, RequestHandler>();

					RequestHandler listFriendsHandler = new DisplayFriendsHandler(
							users, name);
					RequestHandler privateChatHandler = new PrivateChatHandler(
							users, name);
					RequestHandler groupChatHandler = new GroupChatHandler(
							users, name);
					RequestHandler logoutHandler = new LogoutHandler(users,
							name);

					requestMap.put("1", listFriendsHandler);
					requestMap.put("2", privateChatHandler);
					requestMap.put("3", groupChatHandler);
					requestMap.put("4", logoutHandler);

					manager = new RequestsManager(users, name, requestMap);
					if (!manager.runManager())
						break;
				}
			}// end of while
			reader.close();
			writer.close();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			System.out.println("��������ͻ�" + socket.getInetAddress() + "ͨ�Ž�����");
			try {
				socket.close();// ��ǰ�����ӵĿͻ���
			} catch (IOException e) {
				System.out.println("��������ͻ�" + socket.getInetAddress()
						+ "ͨ��δ��ȷ�رգ�");
			}
		}
	}

}
