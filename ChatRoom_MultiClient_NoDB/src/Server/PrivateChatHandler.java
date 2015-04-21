package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Client.Director;
import Client.SocketReaderBuilder;
import Client.SocketWriterBuilder;

public class PrivateChatHandler extends RequestHandler {

	public PrivateChatHandler(UserDB users, String name) {
		super(users, name);
	}

	@Override
	public void handleRequest() {
		// ˽��ʱ��������ֻ������ղ�ת����Ϣ(��userSocket����Ϣת����friendSocket)
		Socket userSocket = users.getSocket(name);

		try {
			// ��ǰ�û���writer
			Director director = new Director(
					new SocketWriterBuilder(userSocket));
			PrintWriter writerU = (PrintWriter) director.construct();

			// ��ǰ�û���reader
			director = new Director(new SocketReaderBuilder(userSocket));
			BufferedReader readerU = (BufferedReader) director.construct();

			writerU.println("�������������");
			String friendName = readerU.readLine();

			Socket friendSocket = users.getSocket(friendName);
			if (friendSocket == null) {
				writerU.println("���Ѳ�����!");
			} else {
				writerU.println("���ӳɹ������롾˽�ġ�״̬\n");

				System.out.println(name + "��" + friendName + "����������");

				// �û����ѵ�writer
				director = new Director(new SocketWriterBuilder(friendSocket));
				PrintWriter writerF = (PrintWriter) director.construct();

				String sendmsg = "";
				while (!sendmsg.equals("bye")) {
					String str = null;
					try {
						str = readerU.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}

					sendmsg = str;
					str = name + " �� " + friendName + " ˵��" + str;

					writerF.println(str);// ��Ŀ��ͻ��˻�������������
					System.out.println(str);

				}// end of while
					// ��Ϊ����ͬһ��socket�����ã����Բ������ڲ��ر���
					// writerU.close();
				// readerU.close();
				// writerF.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
