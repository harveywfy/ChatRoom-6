import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
	public static void main(String[] args){

		// �����̳߳أ���������н�������ӵ����У�Ȼ���ڴ����̺߳��Զ�������Щ����
		ExecutorService exec = Executors.newCachedThreadPool();
		// �½��߳�
		exec.execute(new SingleThread(1));
		exec.execute(new SingleThread(2));
		exec.execute(new SingleThread(3));

		//exec.close();
	}
}
