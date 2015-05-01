package Server;

public abstract class RequestHandler {

	protected UserDB users;
	protected String name;
	
	public RequestHandler(UserDB users, String name){
		this.users = users;
		this.name = name;
	}
	
	//��������
	public abstract void handleRequest();
}
