package Server.RequestHandlers;

import net.sf.json.JSONObject;
//import Server.UserCollection.UserDB;
import Server.UserCollection.UserList;
import Tools.JsonTrans;

public class LogoutHandler extends RequestHandler {

	public LogoutHandler(UserList users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg, "msg");
		String name = json.getString("publisher");// ��������Ϣ�ķ�����
		
		// �û��˳������û���Ϣ��HashMap��ɾ��
		users.deleteUser(name);
		System.out.println(name + "�ɹ��˳�ϵͳ");
	}
}
