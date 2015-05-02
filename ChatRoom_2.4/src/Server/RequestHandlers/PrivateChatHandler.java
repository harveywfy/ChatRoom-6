package Server.RequestHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import net.sf.json.JSONObject;
import Server.ResTrans;
import Server.UserCollection.UserDB;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class PrivateChatHandler extends RequestHandler {

	public PrivateChatHandler(UserDB users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");

		String name = json.getString("publisher");// ��������Ϣ�ķ�����
		String friendName = json.getString("receiver");// ��������Ϣ�Ľ�����
		String words = json.getString("words");// ��������Ϣ�Ľ�����

		System.out.println("˽����Ϣ");
		String str = name + " �� " + friendName + " ˵��" + words;
		System.out.println(str);

		Socket friendSocket = users.getSocket(friendName);
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
