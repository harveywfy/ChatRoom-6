package Server.UserCollection;

import java.net.Socket;
import java.util.HashMap;

public class SoundSocketList {
	// �������ӱ�<�������֣�socket > ��������������һ���˿ڵ�socket
	private HashMap<String, Socket> soundSocketMap = new HashMap<String, Socket>();
	
	public void loginSoundSocket(String linkName, Socket socket) {
		soundSocketMap.put(linkName, socket);
		System.out.println("������socket���ӣ�" + linkName);
	}

	public void logoutSoundSocket(String linkName) {
		soundSocketMap.remove(linkName);
		System.out.println("����socket���ӣ�" + linkName + "�Ͽ�");
	}

	public HashMap<String, Socket> getSoundSocketsMap() {
		return soundSocketMap;
	}
}
