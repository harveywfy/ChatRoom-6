package Client.ResponseHandlers;

import java.net.Socket;

import Client.Pages.LoginPage;

public class ResLoginHandler extends ResponseHandler {

	public ResLoginHandler(Socket socket) {
		super(socket);
	}

	private LoginPage loginPage;

	private String loginFlagTemp;

	public void updateLoginFlag() {
		loginPage.setLoginFlag(loginFlagTemp);
	}

	@Override
	public void handleResponse() {
		loginPage = LoginPage.getInstance(super.socket);

		loginFlagTemp = super.responseMsg;

		// ˫����������loginPage�ĵ�¼��־
		loginPage.login();
	}

}
