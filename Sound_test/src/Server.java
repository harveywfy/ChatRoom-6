import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public void runServer() {
		try {
			// ��������socket�˿ڷ�����
			ServerSocket serverSocket = new ServerSocket(6000);
			System.out.println("����������");
			
			Socket client = serverSocket.accept();

			// ����playback��������	
			SoundPlayback player = new SoundPlayback(client);
			player.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.runServer();
	}
}
