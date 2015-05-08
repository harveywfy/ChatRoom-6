package Client.Pages;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

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
import Client.MsgTrans;
import Client.ResponseHandlers.ResDisplayFriendsHandler;
import Client.ResponseHandlers.ResponseHandler;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class HomePage extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTree tree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode rootNode;

	private Socket socket;
	private PrintWriter writer; // �������ڲ�����Ҫʹ��

	// ��ǰ�û�
	private String userName;

	public void setUser(String user) {
		this.userName = user;
	}

	// ������ˢ�º����б������
	private ResponseHandler handler;

	public void setResponseHandler(ResDisplayFriendsHandler handler) {
		this.handler = handler;
	}

	// �����б�ԭJson��
	private String friendListStr;

	public void setFriendListStr(String s) {
		this.friendListStr = s;
	}

	// ����ģʽ
	private static HomePage s_homePage;

	public static HomePage getInstance(Socket socket) throws IOException {
		if (s_homePage == null) {
			s_homePage = new HomePage(socket);
		} else {
			System.out.println("��ҳ�Ѿ����ڣ������Ѵ��ڵ�ʵ��");
		}
		return s_homePage;
	}

	private HomePage(Socket socket) {
		this.socket = socket;

		this.userName = null;
		this.handler = null;
		this.friendListStr = null;

		Director director = new Director(new PrintWriterBuilder(socket));
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// paintWaitAction(); //�ⲿָ��
	}

	// caller
	public void updateFriendList() {
		System.out.println("ˢ�º����б�");

		rootNode.removeAllChildren();

		// ֪ͨhandler�����Լ��ĺ����б�
		((ResDisplayFriendsHandler) handler).updateFriendListStr();

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
			String name = (String) it.next();// ��ʵ��

			leafTreeNode = new DefaultMutableTreeNode(name);
			rootNode.add(leafTreeNode);
		}
		model.reload();
	}

	public void paintWaitAction() {
		setLocation(500, 170);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 540);
		// setVisible(true);
		setLayout(new BorderLayout());

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

			setVisible(true);

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
						String friend = (String) object; // ��ʵ��

						// �Լ����Լ���������
						if (!friend.equals(userName)) {
							ChatBox chatbox = ChatBox.getInstance(socket,
									userName, friend);
							chatbox.setTitle("˽�Ĵ���");
						}
					}
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