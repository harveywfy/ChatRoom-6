package Client.Pages;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sf.json.JSONObject;
import Client.MsgTrans;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class SendFilePage extends JFrame {

	private static final long serialVersionUID = 1L;

	private Socket socket;
	private String userName; // ����
	private String friendName; // ����

	private JTextField jtfFile;

	// ����
	private static SendFilePage s_sendFilePage = null;

	public static SendFilePage getInstance(Socket socket, String userName,
			String friendName) {
		if (s_sendFilePage == null) {
			s_sendFilePage = new SendFilePage(socket, userName, friendName);
		} else {
			System.out.println("�����ļ�ҳ�Ѿ����ڣ������Ѵ���ʵ��");

			s_sendFilePage.socket = socket;
			s_sendFilePage.userName = userName;
			s_sendFilePage.friendName = friendName;
		}
		return s_sendFilePage;
	}

	private SendFilePage(Socket socket, String userName, String friendName) {
		this.socket = socket;
		this.userName = userName;
		this.friendName = friendName;

		paintWaitAction();
	}

	public void paintWaitAction() {
		setLocation(500, 250);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 150);
		setTitle("�ϴ��ļ�");
		setLayout(new GridLayout(3, 1));

		JButton jbtUpload = new JButton("�ϴ�");
		JButton jbtBrowse = new JButton("���");

		jtfFile = new JTextField();
		jtfFile.setEditable(false);

		add(jtfFile);
		add(jbtBrowse);
		add(jbtUpload);

		jbtBrowse.addActionListener(new BtnBrowse_ActionAdapter(this));

		jbtUpload.addActionListener(new BtnUpload_ActionAdapter(this));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("�ر��ϴ��ļ�����");

				s_sendFilePage = null;
			}
		});

		setVisible(true);
	}

	public void closeWindows() {
		dispose();
		s_sendFilePage = null;
	}

	// �����ť�¼�
	@SuppressWarnings("deprecation")
	public void btnBrowse_actionPerformed(ActionEvent e) {

		FileDialog fd = new FileDialog(this, "�ϴ��ļ�", FileDialog.LOAD);
		fd.show();
		String jfPath = fd.getDirectory() + fd.getFile();

		if ("null".equals(jfPath) == false) { // ����ļ���ѡ���
			jtfFile.setText(jfPath);

			// JOptionPane.showMessageDialog(null, fd.getFile(), "���������ִ�",
			// JOptionPane.ERROR_MESSAGE);
		}
	}

	// �ϴ���ť�¼�
	public void btnUpload_actionPerformed(ActionEvent e) {
		String filePath = jtfFile.getText();

		if ("".equals(filePath)) { // ����Ƿ�ѡ���ļ�
			JOptionPane.showMessageDialog(this, "��ѡ���ļ�", "��ʾ", 2);
			return;
		}
		
		String a = filePath.replaceAll("\\\\", "/");
		
		//String b = a.substring(a.lastIndexOf("/") + 1);
		//System.out.println(a);
		File f = new File(a);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("fileName", a);
		Long l = f.length();
		map.put("length", l.toString());
		JSONObject filePart = JSONObject.fromObject(map);

		String filePartJson = JsonTrans.buildJson("filePart", filePart);

		// ���ļ�����msgNum���͵�server
		MsgTrans msgTrans = new MsgTrans();
		msgTrans.setPublisher(userName);
		msgTrans.setReceiver(friendName);
		msgTrans.setMsgNum("8");
		msgTrans.setWords(filePartJson);

		String sendMsg1 = msgTrans.getResult();
		String jsonOut = JsonTrans.buildJson("msg", sendMsg1);

		// ָ����
		Director director = new Director(new PrintWriterBuilder(this.socket));
		// ʹ��ָ��������һ��writer
		PrintWriter writer = null;
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		writer.println(jsonOut);
		writer.flush();

		dispose();
	}

	// ��� ˫������
	public class BtnBrowse_ActionAdapter implements ActionListener {
		private SendFilePage adaptee;

		BtnBrowse_ActionAdapter(SendFilePage adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.btnBrowse_actionPerformed(e);
		}
	}

	// �ϴ� ˫������
	public class BtnUpload_ActionAdapter implements ActionListener {
		private SendFilePage adaptee;

		BtnUpload_ActionAdapter(SendFilePage adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.btnUpload_actionPerformed(e);
		}
	}

}
