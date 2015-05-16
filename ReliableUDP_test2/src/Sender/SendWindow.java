package Sender;

import java.net.DatagramPacket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Flyweight.Msg;

public class SendWindow {

	private int windowSize;
	private DatagramPacket[] buffer; 

	private Lock lock = new ReentrantLock();// ����:�����ɾ������

	public SendWindow(int windowSize) {
		this.windowSize = windowSize;
		buffer = new DatagramPacket[windowSize];

		System.out.println("���Ͷ˴��ڴ����ɹ������ڴ�С��" + windowSize);
	}

	/** ���������������������������������������������������������������������������������������������������������������� */

	// �����Ƿ�����
	public boolean isFull() {
		boolean flag = true;
		for (int i = 0; i < windowSize; i++) {
			if (buffer[i] == null) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	// ����һ��������
	public void add(DatagramPacket p) {
		lock.lock();

		Msg msg = new Msg(p.getData());
		int msgID = msg.getID();
		int pointer = msgID % windowSize;// ����λ��

		// ��ǰ�������Ƿ�������
		if (buffer[pointer] == null) {
			buffer[pointer] = p;

			System.out.println("���ڲ������" + msgID);
			printWindowContent();// //////////////////////////////
		}

		lock.unlock();
	}

	// ɾ��һ��������
	public void delete(int ACK) {
		lock.lock();

		int position = ACK % windowSize; // ɾ����λ��
		if (buffer[position] != null) {
			buffer[position] = null;

			System.out.println("����ɾ������" + ACK);
			printWindowContent();// //////////////////////////////
		}
		lock.unlock();
	}

	private int pointer = 0;
	
	// �鿴һ������Ԫ��
	public DatagramPacket peek() {		
		if (buffer[pointer] != null) {
			int position = pointer;

			Msg msg = new Msg(buffer[position].getData());
			System.out.println("peek()��" + msg.getID());

			pointer = (pointer + 1) % windowSize;
			return buffer[position];
		}
		return null;
	}

	/** ���������������������������������������������������������������������������������������������������������������� */

	public void printWindowContent() {
		for (int i = 0; i < windowSize; i++) {
			if (buffer[i] != null) {
				Msg msg = new Msg(buffer[i].getData());
				System.out.println("���������ݰ���" + msg.getID() + "��");
			} else {
				System.out.println("���������ݰ���" + buffer[i] + "��");
			}
		}
	}

	public int getWindowSize() {
		return windowSize;
	}

	public int getNumScope() {
		return windowSize * 2;
	}

}
