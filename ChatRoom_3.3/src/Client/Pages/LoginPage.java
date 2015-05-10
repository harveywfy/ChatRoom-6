package Client.Pages;

import java.awt.BorderLayout;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.json.JSONObject;
import Client.MsgTrans;
import Client.ResponseHandlers.ResLoginHandler;
import Client.ResponseHandlers.ResRegistHandler;
import Client.ResponseHandlers.ResponseHandler;
import Client.ResponseHandlers.SetNameHandler;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class LoginPage extends JFrame {
	private static final long serialVersionUID = 1L;

	private Socket socket;
	private String userName;

	private PrintWriter writer;

	// ע���¼���������
	private ResponseHandler handler;

	public void setResponseHandler(ResLoginHandler handler) {
		this.handler = handler;
	}

	// ����ע��ҳע�봦�������
	private ResponseHandler agentHandler;

	public void setRegistHandler(ResRegistHandler handler) {
		this.agentHandler = handler;
	}

	// ��¼�ɹ�����homePage����Ϊ�ɼ�
	private HomePage homePage;

	public void setHomePage(HomePage home) {
		this.homePage = home;
	}

	// ��¼�ɹ���־��˫��������handler
	private String loginFlag;

	public void setLoginFlag(String flag) {
		this.loginFlag = flag;
	}

	public JTextField jtfName = new JTextField(4); // ֻ���ڻ�����ɺ��������뽹��

	// ����ģʽ
	private static LoginPage s_loginPage;

	public static LoginPage getInstance(Socket socket) {
		if (null == s_loginPage) {
			s_loginPage = new LoginPage(socket);
		} else {
			System.out.println("��¼�����Ѿ����ڣ������Ѵ��ڵ�ʵ��");
		}
		return s_loginPage;
	}

	private LoginPage(Socket socket) {
		this.socket = socket;
		this.loginFlag = null;

		// ����������͵�¼����
		Director director = new Director(new PrintWriterBuilder(this.socket));
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		// paintWaitAction();
	}

	public void paintWaitAction() {
		setLocation(500, 220);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 200);
		setTitle("��¼");
		setLayout(new BorderLayout());

		// ��
		JPanel jpInput = new JPanel(new GridLayout(1, 2));

		JLabel jlbName = new JLabel("�û���:");
		jtfName.setText("����ΰ");

		jpInput.add(jlbName);
		jpInput.add(jtfName);

		add(jpInput, BorderLayout.NORTH);

		// ��
		JPanel jpButton = new JPanel(new GridLayout(1, 2));

		JButton jbtLogin = new JButton("��¼");
		JButton jbtRegist = new JButton("ע��");
		jpButton.add(jbtLogin);
		jpButton.add(jbtRegist);

		add(jpButton, BorderLayout.SOUTH);

		setVisible(true);
		
		// ��¼
		jbtLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �������롱�ж�
				if (jtfName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "������Ϣ����Ϊ��");
				} else {
					// ��ȡ���������
					userName = jtfName.getText();

					// �����ݴ��
					MsgTrans msgTrans = new MsgTrans();
					msgTrans.setPublisher(SetNameHandler.getTempName());
					msgTrans.setMsgNum("6");
					msgTrans.setWords(userName);
					String sendMsg = msgTrans.getResult();
					// ���Client��ͷ
					String jsonOut = JsonTrans.buildJson("msg", sendMsg);

					writer.println(jsonOut);
					writer.flush();
					
				}// end of else			
			}
		});// end of jbtOK

		// ע��
		jbtRegist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ��ת��ע��ҳ��
				RegistPage registPage = RegistPage.getInstance(socket);
				registPage.setResponseHandler((ResRegistHandler) agentHandler);
				registPage.paintWaitAction();
			}
		});// end of jbtOK

		addWindowListener(new WindowAdapter() // �رմ���
		{
			public void windowClosing(WindowEvent e) {
				System.out.println("�رյ�¼ҳ");
			}
		});
	}

	public void login() {
		((ResLoginHandler) handler).updateLoginFlag();

		// ����loginFlag����
		JSONObject json = (JSONObject) JsonTrans.parseJson(loginFlag, "res");
		String flag = json.getString("content");

		if (flag.equals("true")) {
			// ��¼�ɹ�
			homePage.setVisible(true);
			homePage.setUser(userName);
			homePage.paintWaitAction();

			SetNameHandler.setRealName(userName);
			
			dispose(); //�رմ���
		} else if (flag.equals("false")) {
			// �û��Ѿ���½
			JOptionPane.showMessageDialog(null, "���û��Ѿ���¼");
		} else {
			// �û�δע��
			JOptionPane.showMessageDialog(null, "δע���û�");
		}
	}

}
