package Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

//�ͻ��˵�д����
public class ClientWriterThread extends Thread {

	private Socket socket;

	public ClientWriterThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		//ָ����
		Director director = new Director(new SocketWriterBuilder(socket));
		
		// ʹ��Scanner�Ӽ��̶�������
		Scanner in = new Scanner(System.in);
 
		try {
			//ʹ��ָ��������һ��writer
			PrintWriter writer = (PrintWriter) director.construct();

			String sendMsg = "";

			while (!sendMsg.equalsIgnoreCase("4")) {
				sendMsg = in.next();
				writer.println(sendMsg);
				writer.flush();
			}
			System.out.println("bye!");
			writer.close();
			in.close();

		} catch(SocketException e){
			System.out.print("�ͻ���socket�ѹرգ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
