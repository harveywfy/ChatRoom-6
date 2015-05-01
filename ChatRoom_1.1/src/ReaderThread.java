import java.io.*;

/** ����һ������������ȡ���Է��������������� */
public class ReaderThread extends Thread {
	private DataInputStream dis; // DataInput

	public ReaderThread(DataInputStream dis) {
		this.dis = dis;
	}

	public void run() {
		String msg;
		try {
			while (true) {
				/*
				 * readUTF()�÷�����ȡʹ�� UTF-8 �޸İ��ʽ����� Unicode �ַ����ı�ʾ��ʽ�� Ȼ���� String
				 * ����ʽ���ش��ַ�����
				 */
				msg = dis.readUTF();
				System.out.println("�Է�˵:" + msg);
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}