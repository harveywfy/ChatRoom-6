package Server.UserCollection;
import java.net.Socket;

//�û��б�ĳ����࣬������������ݿ��ʾ��Ҳ�������ڴ�������ʾ
public abstract class UserList {

	public UserList() {

	}

	public abstract Socket getSocket(String name);

	// ���һ����Ա
	public abstract void addUser(String name, Socket socket);

	// ɾ��һ����Ա
	public abstract void deleteUser(String name);

	// �������е��û�
	public abstract String getUsersList(String name);
}
