
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

				DataInputStream in = new DataInputStream(
						new BufferedInputStream(socket.getInputStream()));
				DataOutputStream out = new DataOutputStream(
						new BufferedOutputStream(socket.getOutputStream()));

				do {
					double length = in.readDouble();
					System.out.println("�ӿͻ����յ������α߳���" + length);
					
					double area = length * length;
					//
					out.writeDouble(area);
					out.flush();
					
					System.out.println("�����������" + area);
					
				} while (in.readInt() != 0);//
				
			} finally {
				socket.close();
				System.out.println("ͨ�Ž�����");
			}
		} finally {
			server.close();
		}
	}
}