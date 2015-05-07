package Server.UserCollection;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

//�û��б�ĳ����࣬������������ݿ��ʾ��Ҳ�������ڴ�������ʾ
public abstract class UserList implements Serializable{

	private static final long serialVersionUID = 1L;

	public UserList() {

	}

	public abstract Socket getSocket(String name);

	// ���һ����Ա
	public abstract void addUser(String name, Socket socket);

	// ɾ��һ����Ա
	public abstract void deleteUser(String name);

	// �������е��û� 
	public abstract String getUsersList(String name); // �Ѿ������ķ���������

	// ���������Ϊ���ڲ��ı�����ʵ�ֵ�����£���ȫ������ת�ķ�������ʵ��һ��ʼû�ж�����ʵ�ֺã�
	public abstract HashMap<String, Socket> getSocketsMap();
}
