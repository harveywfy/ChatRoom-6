package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Server.MsgCollection.MsgManagerInter;
import Server.UserCollection.UserList;
import Tools.ClientNameGenerator;
import Tools.JsonTrans;
import Tools.Bulider.BufferedReaderBuilder;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

/* �̳߳���ʵ����Runnable�ӿ� */
class SingleServer implements Runnable {

	private Socket socket;
	private UserList users;
	private MsgManagerInter msgManager;

	public SingleServer(Socket socket, UserList users) {
		this.socket = socket;
		this.users = users;
		this.msgManager = null;
	}

	public void setMsgManager(MsgManagerInter msgManager) {
		this.msgManager = msgManager;
	}

	// ����ʵ��ʱ�Զ�����
	public void run() {
		String tempName = null;
		try {
			Director director = new Director(new BufferedReaderBuilder(socket));
			BufferedReader reader = (BufferedReader) director.construct();

			director = new Director(new PrintWriterBuilder(socket));
			// ʹ��ָ��������writer
			PrintWriter writer = (PrintWriter) director.construct();

			// ����ǰ���ӵ�socket����һ������
			tempName = ClientNameGenerator.gen();

			// ����ʱ���ַ��͸��ͻ���
			ResTrans trans = new ResTrans();
			trans.setMsgNum("0");
			trans.setContent(tempName);
			String result = trans.getResult();
			// ��ӷ������İ�ͷ
			String output = JsonTrans.buildJson("res", result);

			writer.println(output);

			/** ����ǰ���Ӵ���socket���ӱ��� */
			users.loginUser(tempName, socket);

			// ѭ�����յ��û�����Ϣ������Ϣ����msgManager
			while (true) {
				String msg = reader.readLine();
				msgManager.addMsg(msg);
			}

			// reader.close();
			// writer.close();
		} catch (Exception e) {
			System.out.println(socket.getInetAddress() + "���ߣ�");

			/** ǿ������δ��¼���� */
			if (tempName != null) {
				users.logoutUser(tempName);
			}
			
			System.out.println("ʣ�������û���" + users.getOnlineUsers().size());
			System.out.println("ʣ����������" + users.getSocketsMap().size());

			// ��취ɾ���Ѿ���½���û������� -- ����û�е�¼�����û���
			// if (null != users.getOnlineUsers().get(tempName)) {
			// users.logoutFormalUser(tempName);
			// }

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
