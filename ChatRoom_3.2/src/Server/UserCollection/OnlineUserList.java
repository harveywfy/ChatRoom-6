package Server.UserCollection;

import java.util.HashMap;

public class OnlineUserList {

	// ���ߺ���<���֣� ��ʱ����> �������ظ�Client�����б��
	private HashMap<String, String> usersMap = new HashMap<String, String>();

	// ������ʽ�û�
	public void loginFormalUser(String name, String linkName) {
		usersMap.put(name, linkName);
		System.out.println("�������û���" + name);
	}

	// ������ʽ�û�
	public void logoutFormalUser(String name) {
		usersMap.remove(name);
		System.out.println("�û���" + name + "����");
	}
	
	public HashMap<String, String> getOnlineUsersMap() {
		// �û�ע��ɹ��󣬾ͽ�������Ϣ�������map
		return usersMap;
	}
}
