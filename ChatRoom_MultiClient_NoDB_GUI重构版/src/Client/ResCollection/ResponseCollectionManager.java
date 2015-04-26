package Client.ResCollection;

public class ResponseCollectionManager {
private ResponseMsgCollection msgQueue;
	
	public ResponseCollectionManager(){
		msgQueue = ResponseMsgQueue.getInstance();
	}
	
	public  void addMsg(String msg) {
		msgQueue.addMsg(msg); 
	}

	public  String getMsg() {
		return msgQueue.getMsg();
	}
}
