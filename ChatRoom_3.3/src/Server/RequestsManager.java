package Server;

import java.util.Map;

import net.sf.json.JSONObject;
//import Server.MsgCollection.MsgManager;
import Server.MsgCollection.MsgManagerInter;
import Server.RequestHandlers.RequestHandler;
import Tools.JsonTrans;

public class RequestsManager extends Thread {

	private Map<?, ?> requestMap;
	private MsgManagerInter msgManager;

	public RequestsManager() {
		requestMap = null;
		msgManager = null;
	}

	public void setRequestMap(Map<?, ?> map) {
		this.requestMap = map;
	}

	public void setMsgManager(MsgManagerInter msgManager) {
		this.msgManager = msgManager;
	}

	public void run() {
		while (true) {
			String msg = msgManager.getMsg(); // ����ʽ��ȡ
			System.out.println("��ȡ������Client��������ϢΪ" + msg + "��");

			JSONObject json = (JSONObject) JsonTrans.parseJson(msg, "msg");
			String key = json.getString("msgNum");// ����������Ϣ���

			// ����ģʽ
			RequestHandler handler = (RequestHandler) requestMap.get(key);
			handler.setRequestMsg(msg);
			handler.handleRequest();
		}
	}
}
