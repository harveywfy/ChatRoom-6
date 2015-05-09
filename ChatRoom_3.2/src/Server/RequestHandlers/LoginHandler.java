package Server.RequestHandlers;

import java.io.IOException;
import java.io.PrintWriter;

import net.sf.json.JSONObject;
import Server.ResTrans;
import Server.UserCollection.UserList;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class LoginHandler extends RequestHandler {

	public LoginHandler(UserList users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");
		String name = json.getString("publisher");// ��������Ϣ�ķ����ߣ���ʱ���֣�
		String formalName = json.getString("words");

		System.out.println(name + " ��¼");

		// ָ����
		Director director = new Director(new PrintWriterBuilder(
				super.users.getSocket(name)));
		try {
			// ʹ��ָ��������writer
			PrintWriter writer = (PrintWriter) director.construct();

			String content = null;
			// �Ƿ�ע��
			if (users.isRegister(formalName)) {
				// �Ƿ�����
				if (null == users.getOnlineUsers().get(formalName))
					content = "true"; // ˵�����Ե�¼
				else
					content = "false";
			} else
				content = "NOUSER";

			/** ����ǰ�û����뵽������ʽ�û��� */
			if (content.equals("true")) {
				users.loginFormalUser(formalName, name);
			}

			// ���ַ����������Ҫ�ͻ��˽�������ʽ
			ResTrans trans = new ResTrans();
			trans.setMsgNum("6");
			trans.setContent(content);
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
