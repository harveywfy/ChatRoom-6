package Server.UserCollection;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

//�û��б�ĳ����࣬������������ݿ��ʾ��Ҳ�������ڴ�������ʾ
public abstract class UserList implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserList() {
	}

	// ��ȡ����
	public abstract Socket getSocket(String name);

	// ����һ���û�
	public abstract void loginUser(String name, Socket socket);

	// ����һ���û�
	public abstract void logoutUser(String name);

	// ������ʽ�û�
	public abstract void loginFormalUser(String tempName, String name);

	// ������ʽ�û�
	public abstract void logoutFormalUser(String name);

	// ע���û�
	public abstract void registUser(String name);

	// ע���û�
	public abstract void deleteUser(String name);

	// �Ƿ�ע��
	public abstract boolean isRegister(String name);

	// ��ѯ��������
	public abstract HashMap<String, Socket> getSocketsMap();

	// ��ѯ���������û�
	public abstract HashMap<String, String> getOnlineUsers();

}
