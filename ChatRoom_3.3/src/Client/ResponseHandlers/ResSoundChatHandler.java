package Client.ResponseHandlers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import net.sf.json.JSONObject;
import Client.Pages.ChatBox;
import Client.Pages.SoundChatBox;
import Tools.JsonTrans;

public class ResSoundChatHandler extends ResponseHandler {

	private Socket soundSocket;

	// �����Ѿ����ڵ����������߳�
	private Map<String, SoundChatProcess> soundChatThreadsMap;

	public ResSoundChatHandler(Socket socket, Socket soundSocket) {
		super(socket);
		this.soundSocket = soundSocket;
		soundChatThreadsMap = new HashMap<String, SoundChatProcess>();
	}

	@Override
	public void handleResponse() {
		System.out.println("��������");

		JSONObject json = (JSONObject) JsonTrans.parseJson(super.responseMsg,
				"res");
		String friendName = json.getString("publisher");

		// ��map���ҵ���Ӧ�ĺ���������������handler
		SoundChatProcess process = soundChatThreadsMap.get(friendName);
		if (null == process) {
			process = new SoundChatProcess(friendName);
			// ����
			soundChatThreadsMap.put(friendName, process);
		}
		process.runProcess();

	}

	// ��Ƶ�������
	public class SoundChatProcess {
		private String friendName;

		public SoundChatProcess(String friendName) {
			this.friendName = friendName;
		}

		public void runProcess() {
			// �õ�˽�Ĵ���ʵ��
			ChatBox c = ChatBox.getInstance(socket,
					SetNameHandler.getRealName(), friendName);
			c.setTitle("Sound Chat Box");

			// �õ��������촰��ʵ��
			SoundChatBox soundChatBox = SoundChatBox.getInstance(socket,
					SetNameHandler.getRealName(), friendName);
			soundChatBox.setTitle("Voice Chat");

			// �������촰�ڵġ���ʼ����ť����Ϊdisable
			soundChatBox.jbtOK.setEnabled(false);

			// �½��̣߳���������������
			thread = new SoundPlaybackThread();
			thread.setDaemon(true);
			thread.start();

			// �½��̣߳� ���������ɼ���
			thread2 = new SoundCaptureThread();
			thread2.setDaemon(true);
			thread2.start();
		}

		private SoundPlaybackThread thread;
		private SoundCaptureThread thread2;

		private SourceDataLine sourceDataLine;
		private TargetDataLine targetDataLine;

		public class SoundCaptureThread extends Thread {
			public void run() {
				AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
				try {
					BufferedOutputStream captrueOutputStream = new BufferedOutputStream(
							soundSocket.getOutputStream());

					// 1.ȡ�������豸��Ϣ
					DataLine.Info info = new DataLine.Info(
							TargetDataLine.class, format);

					// 2.ȡ�������豸
					targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

					// 3.�������豸
					targetDataLine.open(format, targetDataLine.getBufferSize());

					// 4.��ʼ¼��
					targetDataLine.start();

					byte[] data = new byte[1024];
					while (true) {
						if (targetDataLine != null) {
							// 5.��ȡ¼������
							int cnt = targetDataLine.read(data, 0, 128);// ȡ���ݣ�1024���Ĵ�Сֱ�ӹ�ϵ��������ٶȣ�һ��ԽСԽ��
							if (cnt > 0) {
								// 6.����¼������
								captrueOutputStream.write(data, 0, cnt);
								captrueOutputStream.flush();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private final int bufSize = 16384;

		public class SoundPlaybackThread extends Thread {
			public void run() {
				AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
				try {
					BufferedInputStream playbackInputStream = new BufferedInputStream(
							new AudioInputStream(soundSocket.getInputStream(),
									format, 2147483647));

					// 1.ȡ������豸��Ϣ
					DataLine.Info info = new DataLine.Info(
							SourceDataLine.class, format);

					// 2.ȡ������豸��AudioSystem���Ź��������ã�
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);

					// 3.������豸
					sourceDataLine.open(format, bufSize);

					// 4.��ʼ����
					sourceDataLine.start();

					byte[] data = new byte[1024];
					int cnt;
					// 6.��ȡ���ݵ���������
					while ((cnt = playbackInputStream
							.read(data, 0, data.length)) != -1) {
						if (cnt > 0) {
							if (sourceDataLine != null)
								// 7.���Ż������ݣ�ͨ����Դ�����н���Ƶ����д���Ƶ��
								sourceDataLine.write(data, 0, cnt);
						}

					}
					// Block�ȴ���ʱ���ݱ����Ϊ��
					sourceDataLine.drain();
					sourceDataLine.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}// end of class SoundPlaybackThread

		@SuppressWarnings("deprecation")
		public void stopSoundChatProcess() {
			if (null != sourceDataLine) {
				sourceDataLine.stop();
				sourceDataLine.close();
			}
			sourceDataLine = null;

			if (null != targetDataLine) {
				targetDataLine.stop();
				targetDataLine.close();
			}
			targetDataLine = null;

			thread.stop();
			thread2.stop();
		}
	}

	public void stopSoundChatThreads(String friendName) {
		// �ҵ���Ӧ�Ĵ������
		SoundChatProcess process = soundChatThreadsMap.get(friendName);
		if (null != process) {
			process.stopSoundChatProcess();
			// �Ƴ�
			soundChatThreadsMap.remove(friendName);
		}
	}

}
