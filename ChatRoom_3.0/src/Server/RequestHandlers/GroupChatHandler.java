package Server.RequestHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

import net.sf.json.JSONObject;
import Server.ResTrans;
import Server.UserCollection.UserList;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class GroupChatHandler extends RequestHandler {

	public GroupChatHandler(UserList users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");

		String name = json.getString("publisher");// ��������Ϣ�ķ�����
		String words = json.getString("words");// ��������Ϣ������

		System.out.println("Ⱥ����Ϣ");
		System.out.println(name + "˵��" + words);

		try {
			// Ⱥ��ʱ��������������Ϣת���������û�
			Iterator<String> iter = users.getSocketsMap().keySet().iterator();
			Director director;
			PrintWriter writerF;
			while (iter.hasNext()) {
				String key = iter.next();

				//�����Լ�������Ϣ
				if (name.equals(key))
					continue;

				// ����Ŀ��ͻ���socket������
				Socket friendSocket = users.getSocket(key);

				director = new Director(new PrintWriterBuilder(friendSocket));
				writerF = (PrintWriter) director.construct();

				// ���ַ����������Ҫ�ͻ��˽�������ʽ
				ResTrans trans = new ResTrans();
				trans.setPublisher(name);
				trans.setMsgNum("3");
				trans.setContent(words);
				String result = trans.getResult();

				// ��ӷ������İ�ͷ
				String output = JsonTrans.buildJson("res", result);

				writerF.println(output);
				writerF.flush();

				System.out.println("ת����Ⱥ����Ϣ��" + output);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
