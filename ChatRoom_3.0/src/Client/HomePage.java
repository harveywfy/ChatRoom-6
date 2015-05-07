package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.sf.json.JSONObject;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class HomePage extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode;

	private PrintWriter writer; // �������ڲ�����Ҫʹ��

	private Map<String, JFrame> chatBoxesMap;// ��ֹ��ͬһ�����ظ�����chatBox

	private Socket socket;
	private String userName;
	private String friendListStr; // �����б�ԭJson��

	private ResponseManager manager; // ��Ҫ����ע��

	public void setManager(ResponseManager manager) {
		this.manager = manager;
	}

	public void setFriendListStr(String s) {
		this.friendListStr = s;
	}

	// ������Ϣ������publisher����ChatBox
	private Queue<String> msgQ;

	// Ϊ����manager������Ϣ
	public Queue<String> getMsgQueue() {
		return msgQ;
	}

	// Ⱥ����Ϣ
	private Queue<String> msgGroupQ;

	public Queue<String> getMsgGroupQueue() {
		return msgGroupQ;
	}

	public HomePage(Socket socket, String userName) {
		this.friendListStr = null;
		this.socket = socket;
		this.userName = userName;
		this.manager = null;

		setLocation(500, 170);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 540);
		setVisible(true);

		setLayout(new BorderLayout());

		this.chatBoxesMap = new HashMap<String, JFrame>();
		this.msgQ = new LinkedBlockingQueue<String>();
		this.msgGroupQ = new LinkedBlockingQueue<String>();

		Director director = new Director(new PrintWriterBuilder(socket));
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//paintWaitAction();

		ResConsumeThread thread = new ResConsumeThread();
		thread.setDaemon(true); // ����Ϊ�ػ�����
		thread.start();

		ResConsumeGroupThread thread1 = new ResConsumeGroupThread();
		thread1.setDaemon(true); // ����Ϊ�ػ�����
		thread1.start();
	}

	// ��ͬ�����촰���з���������Ϣ
	public class ResConsumeThread extends Thread {
		public void run() {
			String jsonStr = null;
			while (true) {
				try {
					// ��queue��ȡ��string
					jsonStr = ((LinkedBlockingQueue<String>) msgQ).take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// ����publisher����
				JSONObject json = (JSONObject) JsonTrans.parseJson(jsonStr,
						"res");
				String friName = json.getString("publisher");
				String content = json.getString("content");

				// ��map����publisher
				ChatBox chatbox = (ChatBox) chatBoxesMap.get(friName);
				if (chatbox != null) {
					// ����Ϣ��ӵ�chatBox����Ϣ������
					chatbox.getMsgQueue().offer(content);
					chatbox.setChatBoxesMap(chatBoxesMap);
				} else {
					try {
						chatbox = new ChatBox(socket, userName, friName);
						chatbox.setChatBoxesMap(chatBoxesMap);

						// ���½���chatBox����map
						chatBoxesMap.put(friName, chatbox);

					} catch (IOException e) {
						e.printStackTrace();
					}
					chatbox.getMsgQueue().offer(content);
				}
			}// end of while
		}
	}

	// ��Ⱥ�Ĵ����з���������Ϣ
	public class ResConsumeGroupThread extends Thread {
		public void run() {
			String jsonStr = null;
			while (true) {
				try {
					// ��queue��ȡ��string
					jsonStr = ((LinkedBlockingQueue<String>) msgGroupQ).take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println("��homepage��Ⱥ��queue��ȡ����Ϣ");

				// ����ģʽ�µ�groupChatBox
				GroupChatBox groupBox = GroupChatBox.getInstance(socket,
						userName);

				// ֱ�Ӵ���ԭʼ��
				groupBox.getMsgQueue().offer(jsonStr);

			}// end of while
		}
	}

	// caller
	public void updateFriendList() {
		System.out.println("ˢ�º����б�");

		rootNode.removeAllChildren();

		// ֪ͨResponseManager�����Լ��ĺ����б�
		manager.updateFriendListStr();

		// ������������ͷ
		JSONObject json1 = (JSONObject) JsonTrans.parseJson(friendListStr,
				"res");
		String content = json1.getString("content");// ȡ��content����

		// ����content����
		JSONObject userMap = (JSONObject) JsonTrans.parseJson(content,
				"userMap");

		DefaultMutableTreeNode leafTreeNode;

		Iterator<?> it = userMap.keys();
		while (it.hasNext()) {
			String name = (String) it.next();

			leafTreeNode = new DefaultMutableTreeNode(name);
			rootNode.add(leafTreeNode);
		}
		model.reload();
	}

	public void paintWaitAction() {
		try {
			JLabel jlbUserName = new JLabel("�û�����" + userName);

			// ����map����
			rootNode = new DefaultMutableTreeNode("����");

			// ���������������б�
			MsgTrans msgTrans = new MsgTrans();
			msgTrans.setPublisher(userName);
			msgTrans.setMsgNum("1");
			String sendMsg = msgTrans.getResult();

			// ��ӿͻ��˰�ͷ
			String a = JsonTrans.buildJson("msg", sendMsg);

			writer.println(a);
			writer.flush();

			tree = new JTree(rootNode);
			model = (DefaultTreeModel) tree.getModel();

			JScrollPane jsp = new JScrollPane(tree);

			JButton jbtUpdate = new JButton("ˢ��");
			JButton jbtGroupChat = new JButton("Ⱥ��");

			JPanel jpSouth = new JPanel();
			jpSouth.add(jbtUpdate);
			jpSouth.add(jbtGroupChat);

			add(jlbUserName, BorderLayout.NORTH);
			add(jsp, BorderLayout.CENTER);
			add(jpSouth, BorderLayout.SOUTH);

			// ˢ�°�ť�¼�
			jbtUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���������������б�
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(userName);
					msgTrans.setMsgNum("1");

					String sendMsg = msgTrans.getResult();

					// ��ӿͻ��˰�ͷ
					String a = JsonTrans.buildJson("msg", sendMsg);

					writer.println(a);
					writer.flush();
				}
			});

			// Ⱥ�İ�ť�¼�
			jbtGroupChat.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ����ģʽ�µ�groupChatBox
					GroupChatBox groupBox = GroupChatBox.getInstance(socket,
							userName);
					groupBox.setTitle("Ⱥ�Ĵ���");
				}
			});

			// ��Ҷ�ڵ�ĵ���¼�
			tree.addTreeSelectionListener(new TreeSelectionListener() {

				public void valueChanged(TreeSelectionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
							.getLastSelectedPathComponent();
					if (node == null)
						return;

					Object object = node.getUserObject();
					if (node.isLeaf()) {
						String friend = (String) object;

						// �Լ����Լ���������
						if (!friend.equals(userName)) {

							System.out.println("�û���" + friend + "����������");

							// �Ȳ���
							ChatBox chatbox = (ChatBox) chatBoxesMap
									.get(friend);
							if (chatbox == null) {
								try {
									chatbox = new ChatBox(socket, userName,
											friend);

									// �����chatBox����map���棬��ֹ�ظ�����
									chatBoxesMap.put(friend, chatbox);

									// ע��map
									chatbox.setChatBoxesMap(chatBoxesMap);

								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}// end of if (chatbox == null)
							chatbox.setTitle("˽�Ĵ���");
						}// end of if (!friend.equals(userName))
					}// end of if (node.isLeaf())
				}
			});

			addWindowListener(new WindowAdapter() // �رմ���
			{
				public void windowClosing(WindowEvent e) {
					// ��װ��Ϣ
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(userName);
					msgTrans.setMsgNum("4");

					String sendMsg = msgTrans.getResult();

					// ��ӿͻ��˰�ͷ
					String a = JsonTrans.buildJson("msg", sendMsg);

					writer.println(a);
					writer.flush();

					// �ر��߳�ResConsumeThread->�Ѿ������߳�����Ϊ�ػ�����
					// thread.stop();
					// thread1.stop();

					System.out.println("�ر���ҳ");

					System.exit(0); // ǿ�йر������߳�
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}