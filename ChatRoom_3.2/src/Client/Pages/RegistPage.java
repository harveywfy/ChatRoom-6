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
import Client.ResponseHandlers.ResRegistHandler;
import Client.ResponseHandlers.ResponseHandler;
import Client.ResponseHandlers.SetNameHandler;
import Tools.JsonTrans;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class RegistPage extends JFrame {
	private static final long serialVersionUID = 1L;

	private Socket socket;
	private String userName;

	private PrintWriter writer;

	// ע�������
	private ResponseHandler handler;

	public void setResponseHandler(ResRegistHandler handler) {
		this.handler = handler;
	}

	// ע��ɹ���־��˫��������handler
	private String registFlag;

	public void setRegistFlag(String flag) {
		this.registFlag = flag;
	}

	public JTextField jtfName = new JTextField(4); // ����Ϊ���������뽹��

	// ����ģʽ
	private static RegistPage s_registPage;

	public static RegistPage getInstance(Socket socket) {
		if (s_registPage == null) {
			s_registPage = new RegistPage(socket);
		} else {
			System.out.println("ע��ҳ���Ѿ����ڣ������Ѵ��ڵ�ʵ��");
		}
		return s_registPage;
	}

	private RegistPage(Socket socket) {
		this.socket = socket;

		// ����������͵�¼����
		Director director = new Director(new PrintWriterBuilder(this.socket));
		try {
			writer = (PrintWriter) director.construct();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public void paintWaitAction() {
		setLocation(500, 220);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(300, 200);
		setTitle("ע��");
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

		JButton jbtCancel = new JButton("ȡ��");
		JButton jbtRegist = new JButton("ע��");
		jpButton.add(jbtCancel);
		jpButton.add(jbtRegist);

		add(jpButton, BorderLayout.SOUTH);

		setVisible(true);

		// ע��
		jbtRegist.addActionListener(new ActionListener() {
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
					msgTrans.setMsgNum("5");
					msgTrans.setWords(userName);
					String sendMsg = msgTrans.getResult();
					// ���Client��ͷ
					String jsonOut = JsonTrans.buildJson("msg", sendMsg);

					writer.println(jsonOut);
					writer.flush();
				}// end of else
			}
		});// end of jbtOK

		// ȡ��
		jbtCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �رյ�ǰҳ
				System.out.println("ȡ��ע��");
				s_registPage.dispose();
			}
		});

		addWindowListener(new WindowAdapter() // �رմ���
		{
			public void windowClosing(WindowEvent e) {
				System.out.println("�ر�ע��ҳ");
			}
		});
	}

	public void regist() {
		((ResRegistHandler) handler).updateRegstFlag();

		// ����registFlag����
		JSONObject json = (JSONObject) JsonTrans.parseJson(registFlag, "res");
		String flag = json.getString("content");

		if (flag.equals("true")) {
			JOptionPane.showMessageDialog(null, "ע��ɹ���");

			s_registPage.dispose();// �رյ�ǰҳ��
		} else {
			// ע��ʧ��
			JOptionPane.showMessageDialog(null, "���û��Ѿ�ע��");
		}
	}
}