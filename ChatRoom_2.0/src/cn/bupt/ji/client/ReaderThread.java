package cn.bupt.ji.client;

import java.io.*;
import java.net.*;

/**����һ������������ȡ���Է���������������*/
public class ReaderThread extends Thread {
	private Socket socket;
    
	public ReaderThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		InputStream input;
        try {
            input = socket.getInputStream();
            InputStreamReader isreader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isreader);
 
            String receiveMsg = "";
 
            while(!receiveMsg.equalsIgnoreCase("4")){
                receiveMsg = reader.readLine();
                System.out.println(receiveMsg);
            }
            reader.close();
        } catch (IOException e) {
			System.out.println(e);
		}
	}
}