package Server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.*;

import Server.MsgCollection.MsgManager;
import Server.RequestHandlers.DisplayFriendsHandler;
import Server.RequestHandlers.GroupChatHandler;
import Server.RequestHandlers.LogoutHandler;
import Server.RequestHandlers.PrivateChatHandler;
import Server.RequestHandlers.RequestHandler;
import Server.UserCollection.UserDB;

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
		MsgManager msgController = new MsgManager();

		HashMap<String, RequestHandler> requestMap = new HashMap<String, RequestHandler>();

		RequestHandler listFriendsHandler = new DisplayFriendsHandler(users);
		RequestHandler privateChatHandler = new PrivateChatHandler(users);
		RequestHandler groupChatHandler = new GroupChatHandler(users);
		RequestHandler logoutHandler = new LogoutHandler(users);

		requestMap.put("1", listFriendsHandler);
		requestMap.put("2", privateChatHandler);
		requestMap.put("3", groupChatHandler);
		requestMap.put("4", logoutHandler);

		// ��Ϣ�����߳�
		RequestsManager manager = new RequestsManager();
		manager.setMsgManager(msgController);
		manager.setRequestMap(requestMap);
		
		manager.start();

		// �����̳߳أ���������ӵ�runnable�У�Ȼ���ڴ����̺߳��Զ�������Щ����
		ExecutorService exec = Executors.newCachedThreadPool();

		System.out.println("��������������ʼ����������");
		while (true) {
			Socket socket = server.accept();
			System.out.println("���ܿͻ���" + socket.getInetAddress()
					+ "���������󣬿�ʼͨ�š�����");

			// �����Ӻ��socket�½��߳�
			SingleServer single = new SingleServer(socket, users);
			single.setMsgManager(msgController);
			exec.execute(single);

			// server.close();
		}
	}

	public static void main(String[] args) throws IOException {

		MultiServer server = new MultiServer(8888);
		server.runServer();

	}
}
