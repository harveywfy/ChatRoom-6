package cn.bupt.ji;

public class Client {
	public static void main(String[] args) {
		//����һ���߳�
		Customthread thread2 = new Customthread();
		thread2.start();
		
		//���߳�
		while(true){
			try {
				Thread.sleep(1000);
				System.out.println("�������߳�");	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		}//end of while
	}

}
