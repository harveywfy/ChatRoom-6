package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class ChatBox extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField jtfMsg = new JTextField();// ��Ϊ����Ϊ�������ڲ���
	private JTextArea jtaMsg = new JTextArea();

	private Socket socket;
	private String userName;
	private String friendName;

	private PrintWriter writer;

	private Map<String, JFrame> chatBoxesMap; // ��homePageע�룬Ϊ�˹ر�ʱ����ɾ����ǰ����

	public void setChatBoxesMap(Map<String, JFrame> chatBoxesMap) {
		this.chatBoxesMap = chatBoxesMap;
	}

	private Queue<String> msgQ; // �洢��������content

	public Queue<String> getMsgQueue() {
		return msgQ;
	}

	public ChatBox(Socket socket, String userName, String friendName)
			throws IOException {
		this.socket = socket;
		this.userName = userName;
		this.friendName = friendName;

		this.setLocation(500, 220);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		this.setVisible(true);

		setTitle("ChatBox");
		setLayout(new BorderLayout());

		msgQ = new LinkedBlockingQueue<String>();

		// ָ����
		Director director = new Director(new PrintWriterBuilder(this.socket));
		// ʹ��ָ��������һ��writer
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e) {
			e.printStackTrace();
		}

		paintWaitAction();

		// �رյ�ǰ����ʱ���ػ��߳�Ҳ��ر�
		UpdateTextAreaThread thread = new UpdateTextAreaThread();
		thread.setDaemon(true);// ����Ϊ�ػ��߳�
		thread.start();
	}

	private void paintWaitAction() {
		JLabel jlbUserName = new JLabel("��ǰ�û�:" + userName);
		JLabel jlbFriendName = new JLabel("Friend Name:" + friendName);

		JPanel jpnTop = new JPanel();
		jpnTop.add(jlbFriendName);
		jpnTop.add(jlbUserName);

		jtaMsg.setEnabled(false);
		jtaMsg.setLineWrap(true);// �����Զ����й���
		jtaMsg.setWrapStyleWord(true);// ������в����ֹ���
		// jtaMsg.selectAll();

		JScrollPane jsp = new JScrollPane(jtaMsg);

		JPanel jpList = new JPanel(new GridLayout(2, 1));
		jpList.add(jsp);
		jpList.add(jtfMsg);

		JButton jbtSend = new JButton("Send");

		add(jpnTop, BorderLayout.NORTH);
		add(jpList, BorderLayout.CENTER);
		add(jbtSend, BorderLayout.SOUTH);

		jbtSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ȡ���������
				String sendMsg = jtfMsg.getText();
				try {
					// �����ݴ��
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(userName);
					msgTrans.setReceiver(friendName);
					msgTrans.setMsgNum("2");
					msgTrans.setWords(sendMsg);

					String sendMsg1 = msgTrans.getResult();
					String jsonOut = JsonTrans.buildJson("msg", sendMsg1);

					writer.println(jsonOut);
					writer.flush();

					// ���Լ����͵���Ϣ�ӵ�textArea
					String a = userName + " ˵��" + sendMsg;

					String temp = jtaMsg.getText();
					String output = "";
					if (temp.equals(""))
						output = a;
					else
						output = temp + "\n" + a;
					jtaMsg.setText(output);
					jtaMsg.setCaretPosition(jtaMsg.getText().length());

					jtfMsg.grabFocus();// ��ý���
					jtfMsg.setText("");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					// �رմ��ھͰѵ�ǰchatBox��map���Ƴ�
					chatBoxesMap.remove(friendName);
					System.out.println("�˳�˽��");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public class UpdateTextAreaThread extends Thread {
		public void run() {
			try {
				while (true) {
					// �ӵ�����Ϣ��ˢ����Ϣ����
					String content = ((LinkedBlockingQueue<String>) msgQ)
							.take();

					String a = friendName + " ˵��" + content;

					String temp = jtaMsg.getText();

					String output = "";
					if (temp.equals(""))
						output = a;
					else
						output = temp + "\n" + a;

					jtaMsg.setText(output);
					jtaMsg.setCaretPosition(jtaMsg.getText().length());
				}
			} catch (Exception e) {
				System.out.print(e);
			}
		}
	}

}
