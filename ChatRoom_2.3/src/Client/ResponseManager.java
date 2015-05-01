package Client;

import java.net.Socket;

import net.sf.json.JSONObject;
import Client.ResCollection.ResponseCollectionManager;
import Tools.JsonTrans;

public class ResponseManager {

	private ResponseCollectionManager resCollection;
	private Socket socket;
	private String userName;

	private HomePage homePage;

	private String friendListJsonTemp;

	public ResponseManager(Socket socket, String userName) {
		this.resCollection = null;
		this.socket = socket;
		this.userName = userName;
	}

	// ��Ҫ����ע��
	public void setResponseCollectionManager(
			ResponseCollectionManager resCollection) {
		this.resCollection = resCollection;
	}

	public void runManager() {
		// ������ҳ����
		homePage = new HomePage(socket, userName);
		homePage.setManager(this);// ����ע��

		ResDistributeThread thread = new ResDistributeThread();
		thread.start();

	}

	// �ṩ��homePageʹ�õķ�����˫��������called
	public void updateFriendListStr() {
		homePage.setFriendListStr(friendListJsonTemp);
	}

	public class ResDistributeThread extends Thread {
		public void run() {
			while (true) {
				String msg = resCollection.getMsg(); // ����ʽ��ȡ
				System.out.println("ȡ������Server�Ļ�Ӧ��ϢΪ��" + msg);

				try {
					// ������������ͷ
					JSONObject json = (JSONObject) JsonTrans.parseJson(msg,
							"res");
					String key = json.getString("msgNum");// ����������Ϣ���

					// Ⱥ�ĵ�ʱ��������˵������ˣ�����������������
					
					// ������Ϣ��ŷַ���Ϣ������if-else�պ�һ��
					if (key.equals("1")) {
						// ���յ���1�����Ͱ�������һ���ֶ���ȴ�homePage���ø��·���
						friendListJsonTemp = msg;

						// ֪ͨhomePage���º����б�
						homePage.updateFriendList();
					}

					if (key.equals("2")) {
						homePage.getMsgQueue().offer(msg); // ˽����Ϣ
					}
					
					if (key.equals("3")) {
						homePage.getMsgGroupQueue().offer(msg); // Ⱥ����Ϣ
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
