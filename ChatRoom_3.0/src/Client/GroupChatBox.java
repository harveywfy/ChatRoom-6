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
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.json.JSONObject;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class GroupChatBox extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField jtfMsg = new JTextField();// ��Ϊ����Ϊ�������ڲ���
	private JTextArea jtaMsg = new JTextArea();

	private Socket socket;
	private String userName;

	private PrintWriter writer;

	private Queue<String> msgQ; // �洢��������content

	public Queue<String> getMsgQueue() {
		return msgQ;
	}

	private static GroupChatBox s_box;

	public static GroupChatBox getInstance(Socket socket, String userName) {
		if (s_box == null) {
			try {
				s_box = new GroupChatBox(socket, userName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Ⱥ�Ĵ����Ѿ����ڣ������Ѵ��ڵ�ʵ��");
		}
		return s_box;
	}

	private GroupChatBox(Socket socket, String userName) throws IOException {
		this.socket = socket;
		this.userName = userName;
		// this.friendName = friendName;

		this.setLocation(500, 220);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		this.setVisible(true);

		setTitle("GroupChatBox");
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

		UpdateTextAreaThread thread = new UpdateTextAreaThread();
		thread.setDaemon(true); // �ػ��߳�
		thread.start();
	}

	private void paintWaitAction() {
		JLabel jlbUserName = new JLabel("��ǰ�û�:" + userName);

		JPanel jpnTop = new JPanel();
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
				//��ȡ���������
				String sendMsg = jtfMsg.getText();
				try {
					//�����Ϣ
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(userName);
					msgTrans.setMsgNum("3");
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
					// �ر�֮������ٴ��½�
					s_box = null;

					System.out.println("�˳�Ⱥ��");
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
					// ȡ��json��
					String jsonStr = ((LinkedBlockingQueue<String>) msgQ)
							.take();

					// ������������ͷ
					JSONObject json = (JSONObject) JsonTrans.parseJson(jsonStr,
							"res");
					// ��������
					String friName = json.getString("publisher");
					String content = json.getString("content");

					String a = friName + " ˵��" + content;

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
