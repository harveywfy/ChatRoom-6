package Server;

//����Ϣ����ɻظ�Client����ʽ
public class ResTrans {
	private String publisher;
	private String msgNum;
	private String content;

	public ResTrans() {
		this.publisher = null;
		this.msgNum = null;
		this.content = null;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setMsgNum(String msgNum) {
		this.msgNum = msgNum;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResult() {
		String jsonStr = "{\'publisher\': \'" + publisher + "\', "
				+ "\'msgNum\': \'" + msgNum + "\', " 
				+ "\'content\': \'" + content + "\' " + "}";

		// ��֪��Ϊʲô ˫���� ת�岻����
		// String jsonStr = "{\'publisher\": \"" + publisher + "\", "
		// + "\"msgNum\": \"" + msgNum + "\", "
		// + "\"content\": \"" + content + "\" " + "}";

		return jsonStr;
	}
}
