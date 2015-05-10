package Client;

import java.net.*;
import java.util.HashMap;
import java.io.*;

import Client.ResCollection.ResponseCollectionManager;
import Client.ResponseHandlers.ResDisplayFriendsHandler;
import Client.ResponseHandlers.ResGroupChatHandler;
import Client.ResponseHandlers.ResLoginHandler;
import Client.ResponseHandlers.ResPrivateChatHandler;
import Client.ResponseHandlers.ResReceiveFileHandler;
import Client.ResponseHandlers.ResRegistHandler;
import Client.ResponseHandlers.ResSendFileHandler;
import Client.ResponseHandlers.ResSoundChatHandler;
import Client.ResponseHandlers.ResStopSoundChatHandler;
import Client.ResponseHandlers.ResponseHandler;
import Client.ResponseHandlers.SetNameHandler;

public class Client {

	public Client() {
	}

	// ��Ҫ���������̵߳�����ע��
	public void runClient() {
		try {
			Socket socket = new Socket("localhost", 8888);
			System.out.println("���ӵ�ͨ�ŷ�����������ͨ�š�����");
			Socket fileSocket = new Socket("localhost", 9999);
			System.out.println("���ӵ��ļ������������׼�������ļ�������");
			Socket soundSocket = new Socket("localhost", 9090);
			System.out.println("���ӵ����������������׼������ͨ��������");

			// �ظ�����Ϣ����
			ResponseCollectionManager resCollec = new ResponseCollectionManager();

			// ����map
			HashMap<String, ResponseHandler> responseMap = new HashMap<String, ResponseHandler>();

			ResponseHandler resRegistHandler = new ResRegistHandler(socket);
			ResponseHandler resLoginHandler = new ResLoginHandler(socket);
			ResponseHandler resDisplayFriendsHandler = new ResDisplayFriendsHandler(
					socket);
			ResponseHandler resPrivateChatHandler = new ResPrivateChatHandler(
					socket);
			ResponseHandler resGroupChatHandler = new ResGroupChatHandler(
					socket);
			ResponseHandler setNameHandler = new SetNameHandler(socket);
			ResponseHandler resSendFileHandler = new ResSendFileHandler(socket,
					fileSocket);
			ResponseHandler resReceiveFileHandler = new ResReceiveFileHandler(
					socket, fileSocket);
			ResponseHandler resSoundChatHandler = new ResSoundChatHandler(
					socket, soundSocket);
			ResponseHandler resStopSoundChatHandler = new ResStopSoundChatHandler(
					socket);
			// ע������
			((ResStopSoundChatHandler) resStopSoundChatHandler)
					.setResSoundChatHandler((ResSoundChatHandler) resSoundChatHandler);

			responseMap.put("5", resRegistHandler);
			responseMap.put("6", resLoginHandler);
			responseMap.put("1", resDisplayFriendsHandler);
			responseMap.put("2", resPrivateChatHandler);
			responseMap.put("3", resGroupChatHandler);
			responseMap.put("0", setNameHandler);
			responseMap.put("8", resSendFileHandler);
			responseMap.put("9", resReceiveFileHandler);
			responseMap.put("10", resSoundChatHandler);
			responseMap.put("11", resStopSoundChatHandler);

			// ��ȡ����server��Ϣ���߳�
			ClientReaderThread readerThread = new ClientReaderThread(socket);
			readerThread.setResponseCollectionManager(resCollec);
			readerThread.setDaemon(true); // ���̸߳�Ϊ�ػ�����
			readerThread.start();

			// ������Ϣ
			ResponseManager manager = new ResponseManager(socket);
			manager.setResponseCollectionManager(resCollec);
			manager.setResponseMap(responseMap); // ע�����map
			manager.runManager();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		Client client = new Client();
		client.runClient();

	}
}
