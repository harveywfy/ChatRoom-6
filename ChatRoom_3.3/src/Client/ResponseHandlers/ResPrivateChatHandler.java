package Client.ResponseHandlers;

import java.net.Socket;

import Client.Pages.ChatBox;
import Tools.JsonTrans;
import net.sf.json.JSONObject;

public class ResPrivateChatHandler extends ResponseHandler{

	public ResPrivateChatHandler(Socket socket) {
		super(socket);
	}

	@Override
	public void handleResponse() {
		System.out.println("�յ�˽����Ϣ");
		
		//����msgQ�е���Ϣ���ַ�����ͬ��chatBox��
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.responseMsg,
				"res");
		String friName = json.getString("publisher");
		String content = json.getString("content");

		// ��map���ҵ�chatBox
		ChatBox chatbox = ChatBox.getInstance(socket, SetNameHandler.getRealName(), friName);
		
		//����Ϣ����chatBox
		chatbox.getMsgQueue().offer(content);		
	}

}
