package Server.UserCollection;

import java.io.Serializable;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;

//��ӳ��ģ�����ݿ�
public class UserDB extends UserList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;

	private HashMap<String, Socket> socketsMap = new HashMap<String, Socket>();
	
	//����ģʽ���������ⲿ����new��������ʵ��
	private static UserDB s_userDB;
	
	private UserDB(){
		
	}
	
	public static UserDB getInstance(){
		if(s_userDB == null){
			s_userDB = new UserDB();
			System.out.println("�û����ݿⴴ���ɹ�");
		}
		else{
			System.out.println("�û����ݿ��Ѿ����ڣ������Ѵ��ڵ�ʵ��");
		}
		return s_userDB;
	}
	
	// get
	public HashMap<String, Socket> getSocketsMap() {
		return socketsMap; 
	}

	// set //û���ù�
	public void setSocketsMap(HashMap<String, Socket> socketsMap) {
		this.socketsMap = socketsMap;
	}

	// ����ӳ���key�ҵ���Ӧ��valve(socket)
	public Socket getSocket(String name) {
		Socket socket = null;
		socket = socketsMap.get(name);//
		return socket;
	}

	// ���һ����Ա
	public void addUser(String name, Socket socket) {
		socketsMap.put(name, socket);
	}

	// ɾ��һ����Ա
	public void deleteUser(String name) {
		socketsMap.remove(name);
	}

	// �������е��û�	���Ѿ������ķ�����������
	public String getUsersList(String name) {
		//������Ҫ����һ��û�е�ǰ�û����б�ģ�Ϊ�˷���Ͱ������û���������
		
		Iterator<String> iter = socketsMap.keySet().iterator();

		String str = new String();// ��װ��
		while (iter.hasNext()) {
			String s = (String) iter.next();// ���ص���������һ��keyֵ

			// ʹ���е��û���Ϣ���ӣ������巽ʽ���
			str = str + "�û���:" + s + "\n";
		}
		return str;
	}
}