package Server;

public class LogoutHandler extends RequestHandler {

	public LogoutHandler(UserDB users, String name) {
		super(users, name);
	}

	@Override
	public void handleRequest() {
		// �û��˳������û���Ϣ��HashMap��ɾ��
		users.deleteUser(name);

	}

}
