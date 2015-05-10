package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import net.sf.json.JSONObject;
import Client.Pages.HomePage;
import Client.Pages.LoginPage;
import Client.ResCollection.ResponseCollectionManager;
import Client.ResponseHandlers.ResDisplayFriendsHandler;
import Client.ResponseHandlers.ResLoginHandler;
import Client.ResponseHandlers.ResRegistHandler;
import Client.ResponseHandlers.ResponseHandler;
import Tools.JsonTrans;

public class ResponseManager {
	private Socket socket;

	// �ͻ�����Ϣ����
	private ResponseCollectionManager resCollection;

	// ��Ų�����
	private Map<?, ?> responseMap;

	public void setResponseMap(Map<?, ?> map) {
		this.responseMap = map;
	}

	public ResponseManager(Socket socket) {
		this.resCollection = null;
		this.socket = socket;
	}

	// ��Ҫ����ע��
	public void setResponseCollectionManager(
			ResponseCollectionManager resCollection) {
		this.resCollection = resCollection;
	}

	public void runManager() {
		// ������ҳ���ڣ�����Ϊ���ɼ�
		HomePage homePage = null;
		try {
			homePage = HomePage.getInstance(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		homePage.setVisible(false);
		homePage.setResponseHandler((ResDisplayFriendsHandler) responseMap
				.get("1"));
		// homePage.paintWaitAction();

		// ������¼ҳ
		LoginPage loginPage = LoginPage.getInstance(socket);
		loginPage.paintWaitAction();
		loginPage.setHomePage(homePage);
		loginPage.jtfName.requestFocus(); // ���뽹��
		loginPage.setResponseHandler((ResLoginHandler) responseMap.get("6"));
		loginPage.setRegistHandler((ResRegistHandler) responseMap.get("5")); // ��ע��ҳ��ע��handler

		ResDistributeThread thread = new ResDistributeThread();
		thread.setDaemon(true); // ����Ϊ�ػ�����
		thread.start();

	}

	public class ResDistributeThread extends Thread {
		public void run() {
			while (true) {
				String msg = resCollection.getMsg(); // ����ʽ��ȡ
				System.out.println("��ȡ������Server�Ļ�Ӧ��ϢΪ��" + msg + "��");

				try {
					// ������������ͷ
					JSONObject json = (JSONObject) JsonTrans.parseJson(msg,
							"res");
					String key = json.getString("msgNum");// ����������Ϣ���

					// ����ģʽ
					ResponseHandler handler = (ResponseHandler) responseMap
							.get(key);
					handler.setResponseMsg(msg);
					handler.handleResponse();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
