package Server.RequestHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import Server.ResTrans;
import Server.UserCollection.UserList;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;
import net.sf.json.JSONObject;

public class DisplayFriendsHandler extends RequestHandler {

	public DisplayFriendsHandler(UserList users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");
		String name = json.getString("publisher");// ��������Ϣ�ķ�����

		String userName = users.getOnlineUsers().get(name);	
		System.out.println(name + " ��������б�");

		// ָ����
		Director director = new Director(new PrintWriterBuilder(
				users.getSocket(userName))); 
		try {
			// ʹ��ָ��������writer
			PrintWriter writer = (PrintWriter) director.construct();

			// ��hashMapת����string��Ϊcontent
			JSONObject json1 = JSONObject.fromObject(users.getOnlineUsers());
			String userMapStr = JsonTrans.buildJson("userMap", json1);

			// ���ַ����������Ҫ�ͻ��˽�������ʽ
			ResTrans trans = new ResTrans();
			trans.setMsgNum("1");
			trans.setContent(userMapStr);

			String result = trans.getResult();

			// ��ӷ������İ�ͷ
			String output = JsonTrans.buildJson("res", result);

			writer.println(output);

			System.out.println("��ӦClient����ϢΪ��" + output);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
