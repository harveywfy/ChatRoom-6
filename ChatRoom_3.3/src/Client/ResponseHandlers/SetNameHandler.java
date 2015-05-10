package Client.ResponseHandlers;

import java.net.Socket;

import net.sf.json.JSONObject;
import Tools.JsonTrans;

public class SetNameHandler extends ResponseHandler {

	public SetNameHandler(Socket socket) {
		super(socket);
	}

	private static String s_tempName;

	public static String getTempName() {
		return s_tempName;
	}

	@Override
	public void handleResponse() {
		System.out.println("�ӵ����������ص�������");
		
		// ������������ͷ
		JSONObject json1 = (JSONObject) JsonTrans.parseJson(super.responseMsg,
				"res");
		String content = json1.getString("content");// ȡ��content����

		s_tempName = content;
	}

	private static String s_realName;
	
	public static void setRealName(String name){
		System.out.println("���õ�¼��");
		s_realName = name;
	}
	
	public static String getRealName(){
		return s_realName;
	}
	
}
