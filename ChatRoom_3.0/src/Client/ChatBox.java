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

	private JTextField jtfMsg = new JTextField();// 设为属性为了匿名内部类
	private JTextArea jtaMsg = new JTextArea();

	private Socket socket;
	private String userName;
	private String friendName;

	private PrintWriter writer;

	private Map<String, JFrame> chatBoxesMap; // 从homePage注入，为了关闭时可以删除当前窗口

	public void setChatBoxesMap(Map<String, JFrame> chatBoxesMap) {
		this.chatBoxesMap = chatBoxesMap;
	}

	private Queue<String> msgQ; // 存储解析过的content

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

		// 指导者
		Director director = new Director(new PrintWriterBuilder(this.socket));
		// 使用指导者生成一个writer
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e) {
			e.printStackTrace();
		}

		paintWaitAction();

		// 关闭当前窗口时，守护线程也会关闭
		UpdateTextAreaThread thread = new UpdateTextAreaThread();
		thread.setDaemon(true);// 设置为守护线程
		thread.start();
	}

	private void paintWaitAction() {
		JLabel jlbUserName = new JLabel("当前用户:" + userName);
		JLabel jlbFriendName = new JLabel("Friend Name:" + friendName);

		JPanel jpnTop = new JPanel();
		jpnTop.add(jlbFriendName);
		jpnTop.add(jlbUserName);

		jtaMsg.setEnabled(false);
		jtaMsg.setLineWrap(true);// 激活自动换行功能
		jtaMsg.setWrapStyleWord(true);// 激活断行不断字功能
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
				// 获取输入框内容
				String sendMsg = jtfMsg.getText();
				try {
					// 将内容打包
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(userName);
					msgTrans.setReceiver(friendName);
					msgTrans.setMsgNum("2");
					msgTrans.setWords(sendMsg);

					String sendMsg1 = msgTrans.getResult();
					String jsonOut = JsonTrans.buildJson("msg", sendMsg1);

					writer.println(jsonOut);
					writer.flush();

					// 将自己发送的消息加到textArea
					String a = userName + " 说：" + sendMsg;

					String temp = jtaMsg.getText();
					String output = "";
					if (temp.equals(""))
						output = a;
					else
						output = temp + "\n" + a;
					jtaMsg.setText(output);
					jtaMsg.setCaretPosition(jtaMsg.getText().length());

					jtfMsg.grabFocus();// 获得焦点
					jtfMsg.setText("");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					// 关闭窗口就把当前chatBox从map中移除
					chatBoxesMap.remove(friendName);
					System.out.println("退出私聊");
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
					// 接到新消息就刷新消息窗口
					String content = ((LinkedBlockingQueue<String>) msgQ)
							.take();

					String a = friendName + " 说：" + content;

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
