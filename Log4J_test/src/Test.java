import org.apache.log4j.Logger;

import Work.WorkServiceImpl;
import Work.WorkService;
import Bussiness.BussinessService;
import Bussiness.BussinessServiceImpl;
import Log.LogInvoHandler;

public class Test {
	// ���loggerֻ��Ϊ�˴�ӡ�ָ���
	public static Logger logger = Logger.getLogger(Test.class.getSimpleName());

	public static void main(String[] args) {

		// ���ط�����
		BussinessService bs = LogInvoHandler
				.getProxyInstance(BussinessServiceImpl.class);
		bs.login("zhangsan", "123456");
		bs.find();

		WorkService ws = LogInvoHandler.getProxyInstance(WorkServiceImpl.class);
		ws.work();
		ws.sleep();

		BussinessService bss = LogInvoHandler
				.getProxyInstance(BussinessServiceImpl.class);
		bss.login("lisi", "654321");
		bss.find();

		logger.debug(" debug ");
		logger.error(" error ");

	}
}