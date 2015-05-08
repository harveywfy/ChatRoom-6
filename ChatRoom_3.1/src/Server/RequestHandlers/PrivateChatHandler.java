package Server.RequestHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import net.sf.json.JSONObject;
import Server.ResTrans;
//import Server.UserCollection.UserDB;
import Server.UserCollection.UserList;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class PrivateChatHandler extends RequestHandler {

	public PrivateChatHandler(UserList users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");

		String name = json.getString("publisher");// ��������Ϣ�ķ����ߣ���ʵ���֣�
		String friendName = json.getString("receiver");// ��������Ϣ�Ľ����ߣ���ʵ���֣�
		String words = json.getString("words");

		System.out.println("˽����Ϣ");
		String str = name + " �� " + friendName + " ˵��" + words;
		System.out.println(str);

		//����ʵ�����ҵ��������֣��ٴ����������ҵ�socket
		String friLinkName = users.getOnlineUsers().get(friendName);
		
		Socket friendSocket = users.getSocket(friLinkName);
		try {
			if (friendSocket == null) {
				System.out.println("û���ҵ�friendSocket��");
			} else {
				// ���ѵ�writer
				Director director = new Director(new PrintWriterBuilder(
						friendSocket));
				PrintWriter writerF = (PrintWriter) director.construct();

				// ���ַ����������Ҫ�ͻ��˽�������ʽ
				ResTrans trans = new ResTrans();
				trans.setPublisher(name);
				trans.setMsgNum("2");
				trans.setContent(words);

				String result = trans.getResult();

				// ��ӷ������İ�ͷ
				String output = JsonTrans.buildJson("res", result);

				writerF.println(output);
				writerF.flush();

				System.out.println("ת����˽����Ϣ��" + output);

			}// end of else

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
