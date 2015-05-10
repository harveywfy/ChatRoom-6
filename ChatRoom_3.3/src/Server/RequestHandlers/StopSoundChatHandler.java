package Server.RequestHandlers;

import java.io.PrintWriter;
import java.net.Socket;

import net.sf.json.JSONObject;
import Server.ResTrans;
import Server.UserCollection.UserList;
import Tools.JsonTrans;
import Tools.MapKey;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class StopSoundChatHandler extends RequestHandler {

	private SoundChatHandler soundChatHandler;

	public void setSoundChatHandler(SoundChatHandler soundChatHandler) {
		this.soundChatHandler = soundChatHandler;
	}

	public StopSoundChatHandler(UserList users) {
		super(users);
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");
		String name = json.getString("publisher");// ��������Ϣ�ķ�����
		String friendName = json.getString("receiver");
		String flag = json.getString("words"); // ȡ�����ǹر�

		// user��������
		String linkName = users.getOnlineUsers().get(name);
		Socket socket = users.getSocket(linkName);

		// friend��������
		String linkFriName = users.getOnlineUsers().get(friendName);
		Socket friendSocket = users.getSocketsMap().get(linkFriName);

		System.out.println(name + " �� " + friendName + " ��ֹ��������");

		try {
			/** ֪ͨ������ֹͣ�������� */
			Director director = new Director(new PrintWriterBuilder(socket));
			PrintWriter writerF = (PrintWriter) director.construct();

			// ���ַ����������Ҫ�ͻ��˽�������ʽ
			ResTrans trans = new ResTrans();
			trans.setPublisher(friendName);
			trans.setMsgNum("11");
			if (flag.equals("cancel"))
				trans.setContent(flag);
			else
				trans.setContent("close");
			String result = trans.getResult();
			// ��ӷ������İ�ͷ
			String output = JsonTrans.buildJson("res", result);

			writerF.println(output);
			writerF.flush();

			/** ֪ͨ������ֹͣ�������� */
			director = new Director(new PrintWriterBuilder(friendSocket));
			writerF = (PrintWriter) director.construct();

			// ���ַ����������Ҫ�ͻ��˽�������ʽ
			trans = new ResTrans();
			trans.setPublisher(name);
			trans.setMsgNum("11");
			if (flag.equals("cancel"))
				trans.setContent(flag);
			else
				trans.setContent("close");
			result = trans.getResult();
			// ��ӷ������İ�ͷ
			output = JsonTrans.buildJson("res", result);

			writerF.println(output);
			writerF.flush();

			Thread.sleep(1000);
			// �رշ���������ת���߳�
			soundChatHandler.stopTransProcess(new MapKey(name, friendName));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
