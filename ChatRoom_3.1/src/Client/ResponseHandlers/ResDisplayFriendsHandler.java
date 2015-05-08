package Client.ResponseHandlers;

import java.io.IOException;
import java.net.Socket;

import Client.Pages.HomePage;

public class ResDisplayFriendsHandler extends ResponseHandler {

	public ResDisplayFriendsHandler(Socket socket) {
		super(socket);
	}

	private HomePage homePage;

	private String friendListJsonTemp;

	// �ṩ��homePageʹ�õķ�����˫��������called
	public void updateFriendListStr() {
		homePage.setFriendListStr(friendListJsonTemp);
	}

	@Override
	public void handleResponse() {
		try {
			homePage = HomePage.getInstance(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		friendListJsonTemp = super.responseMsg;

		// ֪ͨhomePage���º����б�
		homePage.updateFriendList();

	}

}
