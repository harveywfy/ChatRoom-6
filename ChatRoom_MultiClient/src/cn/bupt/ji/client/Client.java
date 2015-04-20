package cn.bupt.ji.client;

import java.net.*;
import java.util.Scanner;
import java.io.*;

/**���߳�����д���ݣ��½��߳�����������*/
public class Client {

	public static void main(String[] args) throws IOException {
		// �������������������
		Socket socket = new Socket("127.0.0.1", 8888);
		try {
			// ���������̡߳�������������
			Thread msr = new ReaderThread(socket);
			msr.start();
			
            //ʹ��Scanner�Ӽ��̶�������
			Scanner in = new Scanner(System.in);
			OutputStream out;
			try {
				//Output.write()
				out = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(out, true);

				String sendMsg = "";

				while (!sendMsg.equalsIgnoreCase("4")) {
					sendMsg = in.next();
					pw.println(sendMsg);
					pw.flush();
				}
				System.out.println("bye!");
				pw.close();
				in.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		} finally {
			socket.close();
		}
	}
}