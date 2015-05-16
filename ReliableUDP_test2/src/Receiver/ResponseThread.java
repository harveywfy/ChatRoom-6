package Receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Flyweight.Msg;

public class ResponseThread extends Thread {

	private ReceiveWindow window;
	private DatagramSocket dataSocket;
	private int pointer;// �ڴ�ɾ�������
	private int preDelete[]; // Ԥɾ�����ļ���

	public ResponseThread(DatagramSocket dataSocket, ReceiveWindow window) {
		this.dataSocket = dataSocket;
		this.window = window;
		this.pointer = 0;
		this.preDelete = new int[window.getWindowSize()];

		for (int i = 0; i < window.getWindowSize(); i++) {
			preDelete[i] = -1;
		}

		System.out.println("�������ظ��߳�����������");
	}

	public void run() {
		try {
			while (true) {
				// System.out.println("hahahahahha");

				// ����ͣ�Ļ���ִ�У�������������������������������
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				DatagramPacket packet = window.peek();

				if (packet != null) {
					Msg msg = new Msg(packet.getData());

					// ����ȷ�ϰ�
					String a = String.valueOf(msg.getID());
					packet.setData(a.getBytes());
					dataSocket.send(packet);
					System.out.println("�ظ�ȷ����Ϣ��" + msg.getID());

					// ȷ��֮��İ�����Ԥɾ���İ�
					int position = msg.getID() % window.getWindowSize();
					preDelete[position] = msg.getID();

					System.out.println("pointer:" + pointer);

					// ɾ������������(ѭ�����ͷһ����position��ʼ��[4][5][6][3])
					for (int i = position; i < position
							+ window.getWindowSize(); i++) {
						if (pointer == preDelete[i % window.getWindowSize()]) {
							window.delete(pointer);
							pointer = (pointer + 1) % window.getNumScope(); // ��һ���ڴ�ɾ�������
							preDelete[i % window.getWindowSize()] = -1; // ����
						}
					}

				}// end of if
			}// end of while
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}