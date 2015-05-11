import java.io.BufferedInputStream;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * javaSound API ��Ƶ�Ĳ���
 * 
 * ��Ƶ������ࣺSoundDataLine ��������豸
 * ʵ����Line�ӿڡ�Line�ӿ������ر�/���豸��ע���¼����������Լ��ṩһЩ������������Ч���Ķ���
 */

public class SoundPlayback extends Thread {

	private final int bufSize = 16384;

	private SourceDataLine sourceDataLine;// ��Ƶ���ݸ�ʽ����

	private Socket socket;
	private BufferedInputStream playbackInputStream;

	public SoundPlayback(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
		try {
			playbackInputStream = new BufferedInputStream(new AudioInputStream(
					socket.getInputStream(), format, 2147483647));

			// 1.ȡ������豸��Ϣ
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

			// 2.ȡ������豸��AudioSystem���Ź��������ã�
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);

			// 3.������豸
			sourceDataLine.open(format, bufSize);

			// 4.��ʼ����
			sourceDataLine.start();

			byte[] data = new byte[1024];
			int cnt;
			// 6.��ȡ���ݵ���������
			while ((cnt = playbackInputStream.read(data, 0, data.length)) != -1) {
				if (cnt > 0) {
					// 7.���Ż������ݣ�ͨ����Դ�����н���Ƶ����д���Ƶ��
					sourceDataLine.write(data, 0, cnt);
				}

			}
			// Block�ȴ���ʱ���ݱ����Ϊ��
			sourceDataLine.drain();
			sourceDataLine.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sourceDataLine.stop();
			sourceDataLine.close();
			sourceDataLine = null;
		}
	}

}
