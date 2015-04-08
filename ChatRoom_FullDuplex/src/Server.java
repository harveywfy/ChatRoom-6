
import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) throws IOException {
		// ����ServerSocket��������׳��쳣����
		// �����������׽��֣�������Ѿ�ռ�õĶ˿��ϴ����������׳��쳣
		ServerSocket server = new ServerSocket(8888);

		// server�����ɹ���ʹ��try-finallyȷ�����ۺ�����������Ƿ��쳣����������ȷ�ر�
		try {
			System.out.println("��������������ʼ����������");

			// �������׽��ּ������Կͻ��˵���������
			// accept()����һ����ͻ������Ӧ���׽��֣�����IOͨ��
			Socket socket = server.accept();
			// socket�����ɹ���ʹ��try-finallyȷ�����ۺ�����������Ƿ��쳣����������ȷ�ر�
			try {
				// ͨ��socket������÷���getInetAddress,���Ի�������ӵ���һ�˼������IP��ַ
				System.out.println("���ܿͻ���" + socket.getInetAddress()
						+ "���������󣬿�ʼͨ�š�����");

				// ��������������
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(socket.getInputStream()));
				//��������ȡ�����߳�
				Thread msr = new ReaderThread(in);
				msr.start();
				
				// �����������������д����
				DataOutputStream dos = new DataOutputStream(
						new BufferedOutputStream(socket.getOutputStream()));

				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);

				String msg;
				try {
					while (true) {
						msg = br.readLine();
						System.out.println("������˵��" + msg);
						
						dos.writeUTF(msg);
						dos.flush();
					}
				} catch (IOException e) {
					System.out.println(e);
				}
			} finally {
				socket.close();
				System.out.println("ͨ�Ž�����");
			}
		} finally {
			server.close();
		}
	}
}