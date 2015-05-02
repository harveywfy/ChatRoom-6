package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//import Server.MsgCollection.MsgManager;
import Server.MsgCollection.MsgManagerInter;
import Server.UserCollection.UserDB;
import Tools.Bulider.BufferedReaderBuilder;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

/* �̳߳���ʵ����Runnable�ӿ� */
class SingleServer implements Runnable {

	private Socket socket;
	private UserDB users;
	private MsgManagerInter msgManager;

	public SingleServer(Socket socket, UserDB users) {
		this.socket = socket;
		this.users = users;
		this.msgManager = null;
	}

	public void setMsgManager(MsgManagerInter msgManager) {
		this.msgManager = msgManager;
	}

	// ����ʵ��ʱ�Զ�����
	public void run() {
		String name = null;
		try {
			Director director = new Director(new PrintWriterBuilder(socket));
			PrintWriter writer = (PrintWriter) director.construct();

			director = new Director(new BufferedReaderBuilder(socket));
			BufferedReader reader = (BufferedReader) director.construct();

			// �������û���
			// ��������Ƿ��ظ�
			while (true) {
				// writer.println("�������ǳƣ�");
				// ����һ������
				name = reader.readLine();

				// ��Ӧһ�� �ɹ� ���� ʧ��
				if (users.getSocket(name) != null) {
					writer.println("ʧ��");
					continue;
				} else {
					// ���û��ǳƺ����׽�����ӵ�HashMap
					users.addUser(name, socket);
					writer.println("�ɹ�");

					System.out.println("��ע���û���" + name);

					// ѭ�����յ��û�����Ϣ������Ϣ����msgManager
					while (true) {
						String msg = reader.readLine();
						msgManager.addMsg(msg);
					}
				}
			}
			// reader.close();
			// writer.close();
		} catch (Exception e) {
			System.out.println(socket.getInetAddress() + "���ߣ�");

			//ǿ��ɾ���쳣�����û�
			if (name != null)
				users.deleteUser(name);

			System.out.println(e);
		} finally {
			System.out.println("��������ͻ�" + socket.getInetAddress() + "ͨ�Ž�����");
			try {
				socket.close();// ��ǰ�����ӵĿͻ���
			} catch (IOException e) {
				System.out.println("��������ͻ�" + socket.getInetAddress()
						+ "ͨ��δ��ȷ�رգ�");
			}
		}
	}

}
