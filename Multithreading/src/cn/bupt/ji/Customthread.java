package cn.bupt.ji;

//�½��߳�����Ҫ�̳�Thread���࣬����ʵ��run()�������÷����ڵ���start()���Զ�ִ��
public class Customthread extends Thread {
	public Customthread() {

	}

	public void run() {
		while(true){
			try {
				Thread.sleep(100);
				System.out.println("�����߳�2");	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}//end of while
	}
}
