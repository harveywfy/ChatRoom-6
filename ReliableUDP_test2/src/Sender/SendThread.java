package Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Flyweight.Msg;

public class SendThread extends Thread {

	private DatagramSocket dataSocket;
	private SendWindow window;
	
	private int numScope;
	private String serverHost;
	private int serverPort;

	public SendThread(DatagramSocket dataSocket, SendWindow window) {
		this.dataSocket = dataSocket;
		this.window = window;

		System.out.println("�ͻ��˷����߳�����������");
	}

	public void run() {
		int pointer = 0;
		while (true) {
			Msg msg = new Msg(pointer);
			msg.setLastSendTime(System.currentTimeMillis());
			
			send(msg); // ����ʽ�ķ���һ���ᷢ��ȥ

			// �ƶ���ſռ�
			pointer = (++pointer) % numScope;

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

			// �жϴ����Ƿ�����
			while (true) {
				if (!window.isFull()) {
					System.out.println("");
					window.add(packet);//�ȷ���window�ٷ���
					
					dataSocket.send(packet);
					System.out.println("�������ݰ�:" + msg.getID());
					System.out.println("");
					break;
				}
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setNumScope(int s) {
		this.numScope = s;
	}

	public void setServerHost(String host) {
		this.serverHost = host;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}
}
