package Client;

import java.net.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Tools.Bulider.BufferedReaderBuilder;
import Tools.Bulider.Director;
import Tools.Bulider.PrintWriterBuilder;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;

	private Socket socket;

	private JTextField jtfIP = new JTextField(4);
	private JTextField jtfPort = new JTextField(4);
	public JTextField jtfName = new JTextField(4);
	// public������Ϊ���ڹ��캯������������뽹�㣬�ؼ�û�л��Ƴ�ʱ�޷���ȡ����

	private String serverIP; //�����ڲ�����
	private int serverPort;
	private String clientName;

	public Client() {
		socket = null;
		setTitle("ChatBox");
		setLayout(new BorderLayout());

		JPanel jpInput = new JPanel(new GridLayout(4, 2));

		JLabel jlbIP = new JLabel("ServerIP:");
		JLabel jlbPort = new JLabel("Port:");
		JLabel jlbName = new JLabel("Name:");

		jtfIP.setText("localhost");
		jtfPort.setText("8888");
		jtfName.setText("����ΰ");

		jpInput.add(jlbIP);
		jpInput.add(jtfIP);

		jpInput.add(jlbPort);
		jpInput.add(jtfPort);

		jpInput.add(jlbName);
		jpInput.add(jtfName);

		add(jpInput, BorderLayout.NORTH);

		JButton jbtOK = new JButton("Connect");
		add(jbtOK, BorderLayout.SOUTH);

		jbtOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverIP = jtfIP.getText();
				clientName = jtfName.getText();

				// �������롱�ж�
				if (jtfIP.getText().equals(null)
						|| jtfPort.getText().length() < 1
						|| jtfName.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "������Ϣ����Ϊ��");
				} else {
					// �жϲ�Ϊ��֮����ת��
					serverPort = Integer.parseInt(jtfPort.getText());

					try {
						// �������������������
						socket = new Socket(serverIP, serverPort);

						// �������ע�����û�
						Director director = new Director(
								new PrintWriterBuilder(socket));
						PrintWriter writer = (PrintWriter) director.construct();

						director = new Director(new BufferedReaderBuilder(
								socket));
						BufferedReader reader = (BufferedReader) director
								.construct();

						// ���� username
						writer.println(clientName);
						// ����һ���ַ���
						String result = reader.readLine();

						if (!result.equals("�ɹ�")) {
							JOptionPane.showMessageDialog(null, "�û�����ע�ᣡ");
						} else {
							dispose();
							try {
								System.out.println("�û�ע��ɹ���");
								
								ClientAdmin admin = new ClientAdmin(socket, clientName);
								admin.runAdmin();

							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}// end of else
			}
		});// end of jbtOK
		
		addWindowListener(new WindowAdapter() // �رմ���
		{
			public void windowClosing(WindowEvent e) {
				System.out.println("�رյ�¼ҳ");
			}
		});
	}

	public static void main(String[] args) throws IOException {

		Client frame = new Client();
		frame.setLocation(500, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 150);
		frame.setVisible(true);

		//ֻ���ڹ��캯�����������뽹��
		frame.jtfName.requestFocus();

	}
}