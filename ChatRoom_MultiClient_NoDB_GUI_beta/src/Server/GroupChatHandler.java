package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

import Client.Director;
import Client.BufferedReaderBuilder;
import Client.PrintWriterBuilder;

public class GroupChatHandler extends RequestHandler {

	public GroupChatHandler(UserDB users, String name) {
		super(users, name);
	}

	@Override
	public void handleRequest() {
		try {
			// ��ǰ�û���writer
			Director director = new Director(new PrintWriterBuilder(
					users.getSocket(name)));
			PrintWriter writerU = (PrintWriter) director.construct();

			// ��ǰ�û���reader
			director = new Director(new BufferedReaderBuilder(
					users.getSocket(name)));
			BufferedReader readerU = (BufferedReader) director.construct();

			writerU.println("���ӳɹ������롾Ⱥ�ġ�״̬\n");

			String sendmsg = "";
			while (!sendmsg.equals("bye")) {
				String stri = readerU.readLine();// BufferedReader�ͻ��˷�����������
				sendmsg = stri;
				System.out.println(name + " ˵��" + sendmsg);
				stri = name + " ˵��" + stri;// ����Ŀ��ͻ��˵�

				Iterator<String> iter = users.getSocketsMap().keySet()
						.iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					// ����Ŀ��ͻ���socket������
					Socket friendSocket = users.getSocket(key);

					director = new Director(new PrintWriterBuilder(
							friendSocket));
					PrintWriter writerF = (PrintWriter) director.construct();

					writerF.println(stri);
					writerF.flush();
				}
			}
			//close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
