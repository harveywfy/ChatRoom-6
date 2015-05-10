package Client.Pages;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Client.MsgTrans;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class SoundChatBox extends JFrame {
	private static final long serialVersionUID = 1L;

	private Socket socket;
	private String userName;
	private String friendName;

	private PrintWriter writer;

	// ����ģʽ
	private static SoundChatBox s_soundChatBox;

	public static SoundChatBox getInstance(Socket socket, String userName,
			String friendName) {
		if (null == s_soundChatBox) {
			s_soundChatBox = new SoundChatBox(socket, userName, friendName);
		} else {
			System.out.println("�������촰���Ѿ����ڣ������Ѵ���ʵ��");
		}
		return s_soundChatBox;
	}

	private SoundChatBox(Socket socket, String userName, String friendName) {
		this.socket = socket;
		this.userName = userName;
		this.friendName = friendName;

		// ָ����
		Director director = new Director(new PrintWriterBuilder(this.socket));
		// ʹ��ָ��������һ��writer
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e) {
			e.printStackTrace();
		}

		paintWaitAction();
	}

	public JButton jbtOK; // Ϊ���ⲿ�����Ƿ�ɰ�

	private void paintWaitAction() {
		setLocation(600, 250);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(200, 100);
		// setVisible(true); //���д�����Ҫ��Ȼ�ؼ�һ��ʼ��ʾ��������

		setTitle("��������");
		setLayout(new GridLayout(3, 1));

		JLabel jbl = new JLabel("���ѣ�" + friendName);

		jbtOK = new JButton("��ʼ");
		JButton jbtCancel = new JButton("ֹͣ");

		add(jbl);
		add(jbtOK);
		add(jbtCancel);

		// ��ʼ
		jbtOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ť��Ϊdisable
				jbtOK.setEnabled(false);

				// ��Server����������������
				MsgTrans msgTrans = new MsgTrans();
				msgTrans.setPublisher(userName);
				msgTrans.setMsgNum("10");
				msgTrans.setReceiver(friendName);
				String sendMsg = msgTrans.getResult();
				// ���Client��ͷ
				String jsonOut = JsonTrans.buildJson("msg", sendMsg);

				writer.println(jsonOut);
				writer.flush();
			}
		});

		// ȡ��
		jbtCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ť��Ϊable
				jbtOK.setEnabled(true);

				// ��Server������ֹ������������
				MsgTrans msgTrans = new MsgTrans();
				msgTrans.setPublisher(userName);
				msgTrans.setMsgNum("11");
				msgTrans.setReceiver(friendName);
				msgTrans.setWords("cancel"); // �÷�����֪����ȡ�����ǹر�
				String sendMsg = msgTrans.getResult();
				// ���Client��ͷ
				String jsonOut = JsonTrans.buildJson("msg", sendMsg);

				writer.println(jsonOut);
				writer.flush();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("�ر��������촰��");

				if (s_soundChatBox != null) {
					// ��Server������ֹ������������
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(userName);
					msgTrans.setMsgNum("11");
					msgTrans.setReceiver(friendName);
					msgTrans.setWords("close"); // �÷�����֪����ȡ�����ǹر�
					String sendMsg = msgTrans.getResult();
					// ���Client��ͷ
					String jsonOut = JsonTrans.buildJson("msg", sendMsg);

					writer.println(jsonOut);
					writer.flush();
				}
				s_soundChatBox = null;
			}
		});

		setVisible(true);
	}

	public void closeWindows() {
		dispose();
		s_soundChatBox = null;
	}

}
