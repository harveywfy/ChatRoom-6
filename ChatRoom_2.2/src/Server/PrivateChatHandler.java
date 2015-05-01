package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Client.Director;
import Client.BufferedReaderBuilder;
import Client.PrintWriterBuilder;

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
			Director director = new Director(new PrintWriterBuilder(userSocket));
			PrintWriter writerU = (PrintWriter) director.construct();

			// ��ǰ�û���reader
			director = new Director(new BufferedReaderBuilder(userSocket));
			BufferedReader readerU = (BufferedReader) director.construct();

			writerU.println("Server�ѽ���˽��״̬");//Ϊ����Clientͬ��״̬��
			
			// writerU.println("�������������");
			String friendName = readerU.readLine();

			Socket friendSocket = users.getSocket(friendName);
			System.out.println(name + " <-> " + friendName);
			if(friendName.equals("bye"))
				System.out.println("�����ˣ�û����ȷ���յ�friendName��");

			if (friendSocket == null) {
				writerU.println("���Ѳ�����!"); //�ͻ���Ϊʲôû�нӵ���仰������
				
				System.out.println("û���ҵ�friendSocket��");
			} else {
				writerU.println("���ӳɹ������롾˽�ġ�״̬");

				System.out.println(name + "��" + friendName + "����������");

				// �û����ѵ�writer
				director = new Director(new PrintWriterBuilder(friendSocket));
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

				}//end of while
				System.out.println(name + "��" + friendName + "�˳�������");
			}//end of else

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
