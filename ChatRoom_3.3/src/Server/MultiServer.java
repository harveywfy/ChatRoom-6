package Server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.*;

import Server.Log.LogInvoHandler;
import Server.MsgCollection.MsgManager;
import Server.MsgCollection.MsgManagerInter;
import Server.RequestHandlers.DisplayFriendsHandler;
import Server.RequestHandlers.GroupChatHandler;
import Server.RequestHandlers.LoginHandler;
import Server.RequestHandlers.LogoutHandler;
import Server.RequestHandlers.PrivateChatHandler;
import Server.RequestHandlers.ReceiveFileHandler;
import Server.RequestHandlers.RegistHandler;
import Server.RequestHandlers.RequestHandler;
import Server.RequestHandlers.SendFileHandler;
import Server.RequestHandlers.SoundChatHandler;
import Server.RequestHandlers.StopSoundChatHandler;
//import Server.UserCollection.UserDB;
import Server.UserCollection.UserList;
import Server.UserCollection.UserPersistence;

public class MultiServer {
	private ServerSocket server; // ͨ����
	private ServerSocket fileServer; // �����ļ���
	private ServerSocket soundServer; // ����ͨ��

	private UserList users;

	public MultiServer() throws IOException {
		// users = UserDB.getInstance();
		users = new UserPersistence();

		server = new ServerSocket(8888);
		System.out.println("ͨ�ŷ�������������ʼ����������");

		fileServer = new ServerSocket(9999);
		System.out.println("�ļ������������������ʼ����������");

		soundServer = new ServerSocket(9090);
		System.out.println("���������������������ʼ����������");
	}

	public void runServer() throws IOException {

		// ʹ����־������¼MsgManager�����Ϊ ��������Spring AOP ��ʹ�ã�
		MsgManagerInter msgController = LogInvoHandler
				.getProxyInstance(MsgManager.class);
		// MsgManager msgController = new MsgManager();

		HashMap<String, RequestHandler> requestMap = new HashMap<String, RequestHandler>();

		RequestHandler listFriendsHandler = new DisplayFriendsHandler(users);
		RequestHandler privateChatHandler = new PrivateChatHandler(users);
		RequestHandler groupChatHandler = new GroupChatHandler(users);
		RequestHandler logoutHandler = new LogoutHandler(users);
		RequestHandler registHandler = new RegistHandler(users);
		RequestHandler loginHandler = new LoginHandler(users);
		RequestHandler sendFileHandler = new SendFileHandler(users);
		RequestHandler receiveFileHandler = new ReceiveFileHandler(users);
		RequestHandler soundChatHandler = new SoundChatHandler(users);
		RequestHandler stopSoundChatHandler = new StopSoundChatHandler(users);
		//ע��ֹͣ����Handler������
		((StopSoundChatHandler) stopSoundChatHandler)
				.setSoundChatHandler((SoundChatHandler) soundChatHandler);

		requestMap.put("1", listFriendsHandler);
		requestMap.put("2", privateChatHandler);
		requestMap.put("3", groupChatHandler);
		requestMap.put("4", logoutHandler);
		requestMap.put("5", registHandler);
		requestMap.put("6", loginHandler);
		requestMap.put("8", sendFileHandler);
		requestMap.put("9", receiveFileHandler);
		requestMap.put("10", soundChatHandler);
		requestMap.put("11", stopSoundChatHandler);

		// ��Ϣ�����߳�
		RequestsManager manager = new RequestsManager();
		manager.setMsgManager(msgController);
		manager.setRequestMap(requestMap);
		manager.start();

		// �����̳߳أ���������ӵ�runnable�У�Ȼ���ڴ����̺߳��Զ�������Щ����
		ExecutorService exec = Executors.newCachedThreadPool();

		while (true) {
			Socket socket = server.accept();
			System.out.println("��ͨ�ŷ��������ܿͻ���" + socket.getInetAddress()
					+ "���������󣬿�ʼͨ�š�������");

			Socket fileSocket = fileServer.accept();
			System.out.println("���ļ�������������ܿͻ���" + socket.getInetAddress()
					+ "���������󣬵ȴ��ļ����䡣������");

			Socket soundSocket = soundServer.accept();
			System.out.println("������������������ܿͻ���" + socket.getInetAddress()
					+ "���������󣬵ȴ�����ͨ����������");

			// �����Ӻ��socket�½��߳�
			SingleServer single = new SingleServer(socket, fileSocket,
					soundSocket, users);
			single.setMsgManager(msgController);
			exec.execute(single);

		}
	}

	public void closeServer() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		MultiServer server = new MultiServer();
		server.runServer();

	}
}
