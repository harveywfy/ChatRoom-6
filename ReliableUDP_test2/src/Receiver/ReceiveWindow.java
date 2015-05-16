package Receiver;

import java.net.DatagramPacket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Flyweight.Msg;

public class ReceiveWindow {
	private int windowSize;
	private DatagramPacket[] buffer; 

	private int[] expectNumArray; // ���������

	private Lock lock = new ReentrantLock();// ����:�����ɾ������ͬʱ����

	public ReceiveWindow(int windowSize) {
		this.windowSize = windowSize;
		buffer = new DatagramPacket[windowSize];
		pointer = 0;

		expectNumArray = new int[windowSize];
		// ��ʼʱ����0��1��2��3
		for (int i = 0; i < this.windowSize; i++) {
			expectNumArray[i] = i;
		}

		System.out.println("���ն˴��ڴ����ɹ������ڴ�С��" + windowSize);
	}

	/** ���������������������������������������������������������������������������������������������������������������� */

	// ����İ��Ƿ������������
	private boolean isExpected(DatagramPacket packet) {
		Msg msg = new Msg(packet.getData());
		int msgID = msg.getID();

		for (int i = 0; i < windowSize; i++) {
			if (msgID == expectNumArray[i]) {
				return true;
			}
		}
		return false;
	}

	// ����һ��������
	public boolean add(DatagramPacket p) {
		lock.lock();

		Msg msg = new Msg(p.getData());
		int msgID = msg.getID();

		if (isExpected(p)) {
			int position = msgID % windowSize;// ����λ��

			// ��ǰ�������Ƿ�������
			if (buffer[position] == null) {
				buffer[position] = p;
				System.out.println("���ڲ������" + msgID);

				printWindowContent();// //////////////////////////////
				lock.unlock();
				return true;
			}
		}
		lock.unlock();
		return false;
	}

	// ɾ��һ��������
	public void delete(int num) {
		lock.lock();

		int position = num % windowSize; // ɾ����λ��
		if (buffer[position] != null) {
			buffer[position] = null;

			System.out.println("����ɾ������" + num);
			printWindowContent();// //////////////////////////////

			// �ı��������(����������)
			expectNumArray[position] = (expectNumArray[position] + windowSize)
					% getNumScope();
		}
		lock.unlock();
	}

	private int pointer;

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

	private void printWindowContent() {
		for (int i = 0; i < windowSize; i++) {
			if (buffer[i] != null) {
				Msg msg = new Msg(buffer[i].getData());
				System.out.println("���������ݰ���" + msg.getID() + "��");
			} else {
				System.out.println("���������ݰ���" + null + "��");
			}
		}
		System.out.println("");
	}

	public void printExpectNum() {
		for (int i = 0; i < windowSize; i++) {
			System.out.println("-> �ڴ������ݰ���" + expectNumArray[i]);
		}
	}

	public int getWindowSize() {
		return windowSize;
	}

	public int getNumScope() {
		return windowSize * 2;
	}
}
