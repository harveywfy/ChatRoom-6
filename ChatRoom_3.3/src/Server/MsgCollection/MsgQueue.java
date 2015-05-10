package Server.MsgCollection;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MsgQueue extends MsgCollection {

	private Queue<String> queue = new LinkedBlockingQueue<String>();

	private static MsgQueue s_msgQueue;

	private MsgQueue() {

	}

	public static MsgQueue getInstance() {
		if (s_msgQueue == null) {
			s_msgQueue = new MsgQueue();
			System.out.println("��������Ϣ���д����ɹ����ȴ�������Ϣ������");
		} else {
			System.out.println("��Ϣ�����Ѿ����ڣ������Ѵ��ڵ�ʵ��");
		}
		return s_msgQueue;
	}

	@Override
	public  void addMsg(String msg) {
		queue.offer(msg); // ���һ��Ԫ�ز�����true����������������򷵻�false
	}

	@Override
	public  String getMsg() {
		String result = null;
		try {
			result = ((LinkedBlockingQueue<String>) queue).take();// pull�Ƴ������ʶ���ͷ����Ԫ�أ��������Ϊ�գ��򷵻�null
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * put ���һ��Ԫ�� �����������������; take �Ƴ������ض���ͷ����Ԫ�� �������Ϊ�գ�������
	 */

}
