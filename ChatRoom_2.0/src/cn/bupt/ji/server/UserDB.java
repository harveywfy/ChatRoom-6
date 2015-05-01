package cn.bupt.ji.server;

import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
 
//��ӳ��ģ�����ݿ�
public class UserDB {
    private HashMap<String, Socket> sockets = new HashMap<String, Socket>();

    //���ص�ǰӳ�� 
    public HashMap<String, Socket> getSockets() {
        return sockets;
    }
    
    //���õ�ǰӳ��
    public void setSockets(HashMap<String, Socket> sockets) {
        this.sockets = sockets;
    }
 
    //����ӳ���key�ҵ���Ӧ��valve(socket)
    public Socket getSocket(String name){
        Socket socket = null;
        socket = sockets.get(name);//
        return socket;
    }
    
    //���һ����Ա
    public void addUser(String name,Socket socket){
        sockets.put(name, socket);
    }
    
    //ɾ��һ����Ա
    public void deleteUser(String name){
        sockets.remove(name);
    }
    
    //�������е��û� 
	public String GetAllUsers() {
		/**������һ��Ҫ�þֲ��ģ����������Ϊ��Ա��������ʼ����ʱ�����ǿյ�*/
		Iterator<String> iter = this.getSockets().keySet().iterator();
		String str = new String();// ��װ�࣬ʲôʱ���ã�����
		
		while (iter.hasNext()) {
			String s = (String) iter.next();// ���ص���������һ��keyֵ

			// ʹ���е��û���Ϣ���ӣ������巽ʽ���
			str = str + "�û���:" + s + "\n";
		}
		System.out.println(str);
		return str;
	}
}