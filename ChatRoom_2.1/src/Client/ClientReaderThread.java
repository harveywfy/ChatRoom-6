package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

//�ͻ��˵Ķ�ȡ�߳�
public class ClientReaderThread extends Thread{

	private Socket socket;
	
	public ClientReaderThread(Socket socket){
		this.socket = socket;	
	}
	
	public void run(){
		//ָ����
		Director director = new Director(new SocketReaderBuilder(socket));
		try {
			//ʹ��ָ��������reader
			BufferedReader reader = (BufferedReader) director.construct();
			
			String receiveMsg = "";
            while(!receiveMsg.equalsIgnoreCase("4")){
                receiveMsg = reader.readLine();
                System.out.println(receiveMsg);
            }
            reader.close();
		}catch(SocketException e){
			System.out.print("�ͻ���socket�ѹرգ�");
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
}
