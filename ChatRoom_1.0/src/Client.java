import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws IOException {
		// TODO �Զ����ɵķ������
		// �������������������
		Socket socket = new Socket("localhost", 8888);
		try {
			// �������ǡ��Է�����������������������read()������
			// ������ǡ��Լ���������������������write()д����
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));

			// ʹ��Scanner��ȡ�Լ����������
			Scanner scanner = new Scanner(System.in);

			boolean stop = false;
			while (!stop) {
				System.out.println("�����������εı߳���");

				double length = scanner.nextDouble();// ��ȡһ�������double

				out.writeDouble(length);// �������д��һ������
				out.flush();

				double area = in.readDouble();// ������������һ������
				System.out.println("�ӷ��������ܵ������������" + area);

				while (true) {
					System.out.println("��������Y/N��");

					String str = scanner.next();// ��ȡһ�������String
					if (str.equalsIgnoreCase("N")) {
						out.writeInt(0);
						out.flush();
						
						stop = true;
						
						break;
					} else if (str.equalsIgnoreCase("Y")) {
						out.writeInt(1);
						out.flush();
						
						break;
					}
				}
			}
			scanner.close();
		} finally {
			socket.close();
		}
	}
}
