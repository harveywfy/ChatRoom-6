import java.net.Socket;

public class Client {

	public void runClient() {
		try {
			Socket client = new Socket("127.0.0.1", 6000);
			System.out.println("�ͻ�������");

			// ����capture¼������
			SoundCapture cap = new SoundCapture(client);
			cap.run();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.runClient();
	}
}
