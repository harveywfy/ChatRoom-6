//�������̳߳�ʵ�������߳���
public class SingleThread implements Runnable {

	public SingleThread() {
		m_num = 0;
	}

	public SingleThread(int num) {
		m_num = num;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
				System.out.println("�����߳�" + m_num);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// end of while

	}

	private int m_num;
}
