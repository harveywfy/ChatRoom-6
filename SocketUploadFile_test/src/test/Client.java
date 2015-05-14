package test;
import java.net.*;
import java.io.*;

public class Client {

	private Socket socket;

	public Client() {
		socket = null;
	}

	public void runClient() {
		try {
			socket = new Socket(InetAddress.getLocalHost(), 1234);
			System.out.println("���ӵ�������");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �����ļ�
	public void sendFile(String str) {
		byte[] b = new byte[1024];

		File f = new File(str);
		try {
			// ���������
			DataOutputStream dout = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			// �ļ�������
			FileInputStream fr = new FileInputStream(f);

			System.out.println("��ʼ�����ļ�");
			
			int n = fr.read(b); 
			while (n != -1) { // ��������д������
				dout.write(b, 0, n);
				dout.flush(); // �ٴζ�ȡn�ֽ�
				n = fr.read(b);
			}
			
			System.out.println("�ļ��������");
			
			// �ر���
			fr.close();
			dout.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Client client = new Client();
		client.runClient();
		client.sendFile("C://Users//JiHongwei//Desktop//Client.jpg");

	}

}
