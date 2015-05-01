package Client.ResCollection;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class ResponseMsgQueue extends ResponseMsgCollection{

	private Queue<String> queue = new LinkedBlockingQueue<String>();

	private static ResponseMsgQueue s_msgQueue;

	private ResponseMsgQueue() {

	}

	public static ResponseMsgQueue getInstance() {
		if (s_msgQueue == null) {
			s_msgQueue = new ResponseMsgQueue();
			System.out.println("��Ϣ���д����ɹ�");
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
			result = ((LinkedBlockingQueue<String>) queue).take();// ����ʽ��ȡ
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

}
