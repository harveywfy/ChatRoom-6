package Sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ACKReceiverThread extends Thread {

	private SendWindow window;
	private DatagramSocket dataSocket;

	private int pointer;// �ڴ�ɾ�������
	private int preDelete[]; //Ԥɾ�����ļ���

	public ACKReceiverThread(DatagramSocket dataSocket, SendWindow window) {
		// �����������е�ַ�İ�
		this.dataSocket = dataSocket;
		this.window = window;

		this.pointer = 0;
		this.preDelete = new int[window.getWindowSize()];

		for (int i = 0; i < window.getWindowSize(); i++) {
			preDelete[i] = -1;
		}

		System.out.println("�ͻ���ACK�����߳�����������");
	}

	private byte[] buffer = new byte[1024];

	public void run() {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			// ѭ���ȴ�����ȷ�ϰ�
			while (true) {
				dataSocket.receive(packet);

				String info = new String(packet.getData(), 0,
						packet.getLength());
				int ACKnum = Integer.parseInt(info);
				System.out.println("���յ�ȷ����Ϣ��" + ACKnum);

				int position = ACKnum % window.getWindowSize();
				preDelete[position] = ACKnum;

				//System.out.println("pointer:" + pointer);

				// ɾ������������(ѭ�����ͷһ����position��ʼ��[4][5][6][3])
				for (int i = position; i < position + window.getWindowSize(); i++) {
					if (pointer == preDelete[i % window.getWindowSize()]) {
						window.delete(pointer);
						pointer = (pointer + 1) % window.getNumScope(); // ��һ���ڴ�ɾ�������
						preDelete[i % window.getWindowSize()] = -1; // ����
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
