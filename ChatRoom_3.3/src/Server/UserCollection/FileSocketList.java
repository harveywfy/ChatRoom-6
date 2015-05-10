package Server.UserCollection;

import java.net.Socket;
import java.util.HashMap;

public class FileSocketList {
	// �ļ����ӱ�<��ʱ���֣�socket > �����������ļ�����һ���˿ڵ�socket
	private HashMap<String, Socket> fileSocketMap = new HashMap<String, Socket>();

	public void loginFileSocket(String linkName, Socket socket) {
		fileSocketMap.put(linkName, socket);
		System.out.println("���ļ�socket���ӣ�" + linkName);
	}

	public void logoutFileSocket(String linkName) {
		fileSocketMap.remove(linkName);
		System.out.println("�ļ�socket���ӣ�" + linkName + "�Ͽ�");
	}

	public HashMap<String, Socket> getFileSocketsMap() {
		return fileSocketMap;
	}
}
