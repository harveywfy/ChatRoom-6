package Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Flyweight.Msg;

public class ReSendThread extends Thread {

	private DatagramSocket dataSocket;
	private SendWindow window;

	private String serverHost;
	private int serverPort;

	public ReSendThread(DatagramSocket dataSocket, SendWindow window) {
		this.dataSocket = dataSocket;
		this.window = window;

		System.out.println("�ͻ����ط��߳�����������");
	}

	public void run() {
		// �������ڣ���ĳ�������е�����ͣ������3��û��ɾ�����ط�������ݰ�
		while (true) {
			// ��window��ȡ�����ݰ�
			DatagramPacket packet = window.peek();
			if (packet != null) {
				Msg msg = new Msg(packet.getData());

				long cTime = System.currentTimeMillis();
				if (cTime - msg.getLastSendTime() > 3000) {
					send(msg);
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// ���Ͱ������������봰��
	public void send(Msg msg) {
		try {
			DatagramPacket packet = new DatagramPacket(msg.toByte(),
					msg.toByte().length, InetAddress.getByName(serverHost),
					serverPort);

			dataSocket.send(packet);
			System.out.println("���·������ݰ�:" + msg.getID());
			System.out.println("");

		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setServerHost(String host) {
		this.serverHost = host;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}
}
