package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

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
	private Socket fileSocket;
	private UserList users;
	private MsgManagerInter msgManager;

	public SingleServer(Socket socket, Socket fileSocket, UserList users) {
		this.socket = socket;
		this.fileSocket = fileSocket;
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

			/** ����ǰ���Ӵ���socket���ӱ���ļ�socket���� */
			users.loginUser(tempName, socket);
			users.loginFileSocket(tempName, fileSocket);

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
				users.logoutFileSocket(tempName);
				
				// ���������쳣���Ѿ���½���û����ñȿͻ���ͻȻ�ϵ磨�취��̫�ã�
				Iterator<?> it = users.getOnlineUsers().keySet().iterator();
				while (it.hasNext()) {
					String a = (String) it.next();
					if (users.getOnlineUsers().get(a).equals(tempName))
						users.logoutFormalUser(a);
				}
			}
			System.out.println("��ʣ�������û���" + users.getOnlineUsers().size());
			System.out.println("��ʣ����������" + users.getSocketsMap().size());
			System.out.println("��ʣ���ļ��ͻ������ӣ�" + users.getFileSocketsMap().size());

			System.out.println(e);
		} finally {
			System.out.println("��������ͻ�" + socket.getInetAddress() + "ͨ�Ž�����");
			try {
				socket.close();// ��ǰ�����ӵĿͻ���
				fileSocket.close();
			} catch (IOException e) {
				System.out.println("��������ͻ�" + socket.getInetAddress()
						+ "ͨ��δ��ȷ�رգ�");
			}
		}
	}

}
