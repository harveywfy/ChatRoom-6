package test;

import java.net.*;
import java.io.*;

public class Server {

	private Socket socket = null;

	public void runServer() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(1234);
			System.out.println("��������������ʼ����������");
			
			socket = server.accept();
			System.out.println("���ܿͻ���" + socket.getInetAddress()
					+ "���������󣬿�ʼͨ�š�����");
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	public void getFile() {
		byte[] b = new byte[1024];
		
		try { 
			DataInputStream din = new DataInputStream(new BufferedInputStream(
					socket.getInputStream())); 
			// ����Ҫ������ļ�
			File f = new File("C://Users//JiHongwei//Desktop//copy.jpg");

			RandomAccessFile fw = new RandomAccessFile(f, "rw");
			
			System.out.println("׼�������ļ�");
			
			// ���ļ���д��0~num���ֽ�
			int num = din.read(b); //����
			while (num != -1) { 
				fw.write(b, 0, num); // ����num���ֽ��ٴ�д���ļ�
				fw.skipBytes(num); // ��ȡnum���ֽ�
				num = din.read(b);
			} 
			
			System.out.println("�����ļ����");
			
			// �ر����룬�����
			din.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Server server = new Server();
		server.runServer();
		server.getFile();
	}

}
