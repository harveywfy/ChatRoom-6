package Client;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

//��socket��printWriter�Ľ�����
public class SocketWriterBuilder extends Builder{

	private Socket socket;
	
	public SocketWriterBuilder(){
		super();
		this.socket = null;
	}
	
	public SocketWriterBuilder(Socket socket){
		super();
		this.socket = socket;
	}
	
	public Object getResult() throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(socketOut, true);
		
		return pw;
	}
}
