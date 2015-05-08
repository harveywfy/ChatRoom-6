package Server.UserCollection;

import java.net.Socket;
import java.util.HashMap;

public class ConnectUserList {
	// �洢����������ڽ������ӵ��û�<��ʱ���֣�socket> ������ͨ�ŵ�
	private HashMap<String, Socket> socketsMap = new HashMap<String, Socket>();

	// ������ʱ�û�
	public Socket getSocket(String name) {
		return (Socket) socketsMap.get(name);
	}

	// ������ʱ�û�
	public void loginUser(String name, Socket socket) {
		socketsMap.put(name, socket);
		System.out.println("�½������ӣ�" + name);
	}

	// ������ʱ�û�
	public void logoutUser(String name) {
		socketsMap.remove(name);
		System.out.println("���ӣ�" + name + "�Ͽ�");
	}
	
	public HashMap<String, Socket> getSocketsMap() {
		return socketsMap; // Ⱥ��ʱ��
	}
}
