package cn.bupt.ji.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

/** �̳߳���ʵ����Runnable�ӿ� */
class SingleServer implements Runnable {
	UserDB users;
	private Socket socket;

	// ���췽��
	public SingleServer(UserDB users, Socket socket) {
		this.users = users;
		this.socket = socket;
	}

	/** ˽�Ŀͻ��ˣ�from�� �� name��from����friendSocket��to�� */
	public void Client(BufferedReader reader, String name, Socket friendSocket) {
		try {
			System.out.println(name + "��" + friendSocket.getInetAddress()
					+ "����������"); 
			
			/* ��Ŀ���û����׽��ִ��������� */
			OutputStream socketOut = friendSocket.getOutputStream();
			PrintWriter pw = new PrintWriter(socketOut, true);// �����ã�����

			String sendmsg = "";
			
			while (!sendmsg.equals("bye")) {
				String str = reader.readLine();// ������BufferedReader���ͻ��˷�����������
				sendmsg = str;
				str = name + " ˵��" + str;

				pw.println(str);// ��Ŀ��ͻ��˻�������������
				System.out.println(name + " ˵��" + sendmsg);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("�û��˳�˽����");
		return;
	}

	// ����ʵ��ʱ�Զ�����
	public void run() {
		InputStream input;
		try {
			// �����������������ӿͻ��˵�socket��
			input = socket.getInputStream();

			InputStreamReader isreader = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(isreader);// Buffer�Ķ�ȡ��

			OutputStream socketOut = socket.getOutputStream();// ����������Ϣ���ظ��ͻ��ˣ�
			PrintWriter pw = new PrintWriter(socketOut, true);

			pw.println("�������ǳƣ�");
			// ��ȡ�û�����ĵ�һ���ַ���Ϊ���û����ǳ�
			String name = reader.readLine();

			// ���û��ǳƺ����׽�����ӵ�HashMap
			users.addUser(name, socket);
			pw.println("�û������óɹ���\n"); 
			
			// ѭ����ȡ�û��������Ϣ����������Ӧ�Ļ�Ӧ
			while (true) {
				pw.println("1.��Ⱥ�ġ�����Ⱥ��\n" + "2.��˽�ġ�����˽��\n" + "3.��List����ʾ���ߺ���\n"
						+ "4.��exit���˳�\n");
				String str = reader.readLine();
				System.out.println(name + " ѡ��" + str);

				switch (str) {
				case "4":
					System.out.println(socket.getInetAddress() + "�Ѿ��˳���");
					// �û��˳������û���Ϣ��HashMap��ɾ��
					users.deleteUser(name);
					break;

				/* Ⱥ�ľ��ǽ�һ����Ϣ�������пͻ� */
				case "1":
					// ���� �ֲ����� �ĵ�����
					// getSockets()���ص�ǰ�� Map
					pw.println("���ӳɹ������롾Ⱥ�ġ�״̬����\n");
					
					String sendmsg = "";
					while (!sendmsg.equals("bye")) {
						String stri = reader.readLine();// BufferedReader�ͻ��˷�����������
						sendmsg = stri;
						System.out.println(name + " ˵��" + sendmsg);
						stri = name + " ˵��" + stri;//����Ŀ��ͻ��˵�

						Iterator<String> iter = users.getSockets().keySet()
								.iterator();
						
						while (iter.hasNext()) {//��mapָ�ص�һ��������
							String key = iter.next();
							// ����Ŀ��ͻ���socket������
							Socket friendSocket = users.getSocket(key);
							
							OutputStream socketOutMul = friendSocket
									.getOutputStream();
							PrintWriter pwTo = new PrintWriter(socketOutMul,
									true);
							
							pwTo.println(stri);
							pwTo.flush();
							
							//pwTo.close();
						}
					}
					break;

				case "2":
					pw.println("��������ѵ��ǳƣ�");
					String friend = reader.readLine();

					// ����Ŀ��ͻ���socket������
					Socket friendSocket = users.getSocket(friend);// ��ֵ�Ƚ�
					if (friendSocket == null) {
						pw.println("���Ѳ�����!");
					} else {
						pw.println("���ӳɹ������롾˽�ġ�״̬����\n");
						// Ŀ��ͻ�������,����˽�Ľ���
						Client(reader, name, friendSocket);
					}
					break;

				case "3":
					pw.println(users.GetAllUsers());
					break;

				default:
					pw.println("������������޷�������");
					break;
				}
			}
			// reader.close();
			// isreader.close();
			// input.close();
			// pw.close();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			System.out.println("��ͻ�" + socket.getInetAddress() + "ͨ�Ž�����");
			try {
				socket.close();//��ǰ�����ӵĿͻ���
			} catch (IOException e) {
				System.out
						.println("��ͻ�" + socket.getInetAddress() + "ͨ��δ��ȷ�رգ�");
			}
		}
	}
}
