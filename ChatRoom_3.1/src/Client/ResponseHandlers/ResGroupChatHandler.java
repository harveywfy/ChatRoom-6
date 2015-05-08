package Client.ResponseHandlers;

import java.net.Socket;

import Client.Pages.GroupChatBox;

public class ResGroupChatHandler extends ResponseHandler {

	public ResGroupChatHandler(Socket socket) {
		super(socket);
	}

	@Override
	public void handleResponse() {
		// �õ�ʵ��
		GroupChatBox chatbox = GroupChatBox.getInstance(socket,
				SetNameHandler.getRealName());

		// ����Ϣ����chatBox
		chatbox.getMsgQueue().offer(super.responseMsg);

	}

}
