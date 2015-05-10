package Server.RequestHandlers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import net.sf.json.JSONObject;
import Server.ResTrans;
import Server.UserCollection.UserList;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;
import Tools.MapKey;

public class SoundChatHandler extends RequestHandler {

	private Socket soundSocket;
	private Socket soundFriSocket;

	// ��Ҫһ��map��Ӧ��ͬ�����̣������򿪡��ر�ʱ��֪��Ҫ�����ĸ�
	private HashMap<MapKey, SoundTransProcess> processMap;

	public SoundChatHandler(UserList users) {
		super(users);
		processMap = new HashMap<MapKey, SoundTransProcess>();// MapKey(userName,friendName)
	}

	@Override
	public void handleRequest() {
		JSONObject json = (JSONObject) JsonTrans.parseJson(super.requestMsg,
				"msg");
		String name = json.getString("publisher");// ��������Ϣ�ķ�����
		String friendName = json.getString("receiver");

		// user��������
		String linkName = users.getOnlineUsers().get(name);
		Socket socket = users.getSocket(linkName);
		soundSocket = users.getSoundSocketsMap().get(linkName);

		// friend��������
		String linkFriName = users.getOnlineUsers().get(friendName);
		Socket friendSocket = users.getSocketsMap().get(linkFriName);
		soundFriSocket = users.getSoundSocketsMap().get(linkFriName);

		System.out.println(name + " �� " + friendName + " ������������");

		try {
			/* ֪ͨ������׼�����ա��������� */
			Director director = new Director(new PrintWriterBuilder(
					friendSocket));
			PrintWriter writerF = (PrintWriter) director.construct();

			// ���ַ����������Ҫ�ͻ��˽�������ʽ
			ResTrans trans = new ResTrans();
			trans.setPublisher(name);
			trans.setMsgNum("10");
			String result = trans.getResult();

			// ��ӷ������İ�ͷ
			String output = JsonTrans.buildJson("res", result);

			writerF.println(output);
			writerF.flush();

			/* ֪ͨ������׼�����͡��������� */
			director = new Director(new PrintWriterBuilder(socket));
			writerF = (PrintWriter) director.construct();

			// ���ַ����������Ҫ�ͻ��˽�������ʽ
			trans = new ResTrans();
			trans.setPublisher(friendName);
			trans.setMsgNum("10");
			result = trans.getResult();

			// ��ӷ������İ�ͷ
			output = JsonTrans.buildJson("res", result);

			writerF.println(output);
			writerF.flush();

			MapKey key = new MapKey(name, friendName);
			SoundTransProcess process = processMap.get(key);
			if (null == process) {
				process = new SoundTransProcess();
				//����map
				processMap.put(key, process);
			}
			process.runProcess();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class SoundTransProcess {

		public SoundTransProcess() {
		}

		public void runProcess() {
			// ����ת���߳�
			thread = new SoundTransThread();
			thread.setDaemon(true);
			thread.start();

			// ����ת���߳�2
			// thread2 = new SoundTransThread2();
			// thread2.setDaemon(true);
			// thread2.start();
		}

		private SoundTransThread thread;

		// private SoundTransThread2 thread2;

		// ת���������� ������->������
		public class SoundTransThread extends Thread {
			public void run() {
				byte[] buffer = new byte[1024];

				try {
					BufferedOutputStream outputStream = new BufferedOutputStream(
							soundSocket.getOutputStream());
					BufferedInputStream inputStream = new BufferedInputStream(
							soundFriSocket.getInputStream());

					int n = 0;
					while ((n = inputStream.read(buffer, 0, buffer.length)) != -1) {
						if (n > 0) {
							outputStream.write(buffer, 0, n);
							outputStream.flush();
						}
					}// end of while
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// ת���������� ������->������
		public class SoundTransThread2 extends Thread {
			public void run() {
				byte[] buffer = new byte[1024];

				try {
					BufferedOutputStream outputStream = new BufferedOutputStream(
							soundFriSocket.getOutputStream());
					BufferedInputStream inputStream = new BufferedInputStream(
							soundSocket.getInputStream());

					int n = 0;
					while ((n = inputStream.read(buffer, 0, buffer.length)) != -1) {
						if (n > 0) {
							outputStream.write(buffer, 0, n);
							outputStream.flush();
						}
					}// end of while
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@SuppressWarnings("deprecation")
		public void stopSoundChatThreads() {
			thread.stop();
			// thread2.stop();
		}
	}

	public void stopTransProcess(MapKey key) {
		SoundTransProcess process = processMap.get(key);

		if (null != process) {
			process.stopSoundChatThreads();
			// �Ƴ�
			processMap.remove(key);
		}
	}

}
