package cn.bupt.ji.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiServer {
	public static void main(String[] args) throws IOException {
		/* ����һ��UserDB�Ķ���ȫ��ʹ����Ϊһ���洢�����û����������ݿ� */
		UserDB users = new UserDB();

		ServerSocket server = new ServerSocket(8888);
		
		/* �����̳߳أ���������ӵ�runnable�У�Ȼ���ڴ����̺߳��Զ�������Щ���� */
		ExecutorService exec = Executors.newCachedThreadPool();

		try {
			System.out.println("��������������ʼ����������");
			
			while (true) {
				Socket socket = server.accept();
				System.out.println("���ܿͻ���" + socket.getInetAddress()
						+ "���������󣬿�ʼͨ�š�����");

				/* �����Ӻ��socket�½��߳� */
				exec.execute(new SingleServer(users, socket));
			}
		} finally {
			// exec.close();�̳߳�Ӧ����ʾ�عرգ�����Ϊʲôû�ж���close()������
			server.close();//ͨ�Ž����˾�Ȼû�йص�������������
		}
	}
}