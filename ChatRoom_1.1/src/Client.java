import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) throws IOException {
		// �������������������
		Socket socket = new Socket("localhost", 8888);

		try {
			// ������������������������
			DataInputStream dis = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));

			Thread msr = new ReaderThread(dis);
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
					System.out.println("�ͻ���˵��" + msg);
					
					dos.writeUTF(msg);
					dos.flush();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		} catch (SocketException e) {
			System.out.println(e);
		} finally {
			socket.close();
		}
	}
}
