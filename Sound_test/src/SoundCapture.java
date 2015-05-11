import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * javaSound API ��Ƶ��¼��
 * 
 * ��Ƶ�������ࣺTargetDataLine ���������豸
 * ʵ����Line�ӿڡ�Line�ӿ������ر�/���豸��ע���¼����������Լ��ṩһЩ������������Ч���Ķ���
 */

public class SoundCapture extends Thread {

	private TargetDataLine targetDataLine;// �����豸

	private Socket socket;
	private BufferedOutputStream captrueOutputStream;// ���¼�Ƶ�����

	public SoundCapture(Socket socket) throws IOException {
		this.socket = socket;
	}

	public void run() {
		// ��Ƶ�����ݸ�ʽ
		// ��Ƶ���ݡ���Ҳ���Ǵ�TargetDataLine������SourceDataLine��������ݣ����������Ƶ��ʽ�ı�׼
		// AudioFormat(float sampleRate,
		// int sampleSizeInBits,
		// int channels,
		// boolean signed,
		// boolean bigEndian��
		AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
		try {
			captrueOutputStream = new BufferedOutputStream(
					socket.getOutputStream());

			// 1.ȡ�������豸��Ϣ
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// 2.ȡ�������豸
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

			// 3.�������豸
			targetDataLine.open(format, targetDataLine.getBufferSize());

			// 4.��ʼ¼��
			targetDataLine.start();

			byte[] data = new byte[1024];
			while (true) {
				// 5.��ȡ¼������
				int cnt = targetDataLine.read(data, 0, 128);// ȡ���ݣ�1024���Ĵ�Сֱ�ӹ�ϵ��������ٶȣ�һ��ԽСԽ��

				if (cnt > 0) {
					// 6.����¼������
					captrueOutputStream.write(data, 0, cnt);
					captrueOutputStream.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			targetDataLine.stop();
			targetDataLine.close();
			targetDataLine = null;

			try {
				captrueOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
