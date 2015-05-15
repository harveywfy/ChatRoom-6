import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UdpServer {

	private DatagramSocket ds = null; // �൱��client socket

	public UdpServer(String host, int port) {
		InetSocketAddress socketAddress = new InetSocketAddress(host, port);
		try {
			ds = new DatagramSocket(socketAddress);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("���������!");
	}

	private byte[] buffer = new byte[1024];
	private DatagramPacket packet = null; // ���յ��ı���

	// �������ݰ����÷���������߳�����.���ؽ��յ����ݴ���Ϣ
	public void receive() throws IOException {
		packet = new DatagramPacket(buffer, buffer.length);
		ds.receive(packet);

		String info = new String(packet.getData(), 0, packet.getLength());
		System.out.println("������Ϣ��" + info);
	}

	// ����Ӧ�����͸������.��Ӧ����
	public void response(String info) throws IOException {
		System.out.println("�ͻ��˵�ַ : " + packet.getAddress().getHostAddress()
				+ ",�˿ڣ�" + packet.getPort());

		DatagramPacket dp = new DatagramPacket(buffer, buffer.length,
				packet.getAddress(), packet.getPort());
		dp.setData(info.getBytes());

		ds.send(dp);
	}

	// �ر�udp������.
	public final void close() {
		try {
			ds.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		String serverHost = "127.0.0.1";
		int serverPort = 3344;

		UdpServer udpServerSocket = new UdpServer(serverHost, serverPort);

		while (true) {
			udpServerSocket.receive();
			udpServerSocket.response("���,sterning!");

		}
	}
}
