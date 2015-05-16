package Receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import Flyweight.Msg;

public class ReceiveThread extends Thread {
	private DatagramSocket dataSocket;
	private ReceiveWindow window;

	public ReceiveThread(DatagramSocket dataSocket, ReceiveWindow window) {
		this.dataSocket = dataSocket;
		this.window = window;

		System.out.println("�����������߳�����������");
	}

	public void run() {
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		while (true) {
			try {
				dataSocket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			window.printExpectNum();

			// ������Msg
			Msg msg = new Msg(packet.getData());
			int num = msg.getID();
			System.out.println("���յ����ݰ���" + num);

			// ȡ��buffer������(�������ͬһ��buffer���ı�һ��ֵ�������������buffer�Ķ��󶼻�ı�)
			byte[] b = Arrays.copyOf(buffer, buffer.length);

			// ��ӵ�����
			if (!window.add(new DatagramPacket(b, b.length,
					packet.getAddress(), packet.getPort()))) {
				System.out.println("���ݰ���Ų�����������ڣ�������");
				System.out.println("");
			}

		}// end of while
	}

}
