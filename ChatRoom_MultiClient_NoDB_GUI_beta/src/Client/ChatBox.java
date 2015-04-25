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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatBox extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField jtfMsg = new JTextField(4);
	private JTextArea jtaMsg = new JTextArea();

	public ChatBox(final Socket socket, String friendName) throws IOException {

		runChatingThread(socket);

		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		this.setVisible(true);

		setTitle("ChatBox");
		setLayout(new BorderLayout());

		JLabel jlbFriendName = new JLabel("FriendName:" + friendName);

		jtaMsg.setEnabled(false);
		jtaMsg.setLineWrap(true);// �����Զ����й���
		jtaMsg.setWrapStyleWord(true);// ������в����ֹ���
		// jtaMsg.selectAll();

		JPanel jpList = new JPanel(new GridLayout(2, 1));
		jpList.add(jtaMsg);
		jpList.add(jtfMsg);

		JButton jbtSend = new JButton("Send");

		add(jlbFriendName, BorderLayout.NORTH);
		add(jpList, BorderLayout.CENTER);
		add(jbtSend, BorderLayout.SOUTH);

		jbtSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sendMsg = jtfMsg.getText();
				try {
					System.out.println(sendMsg);
					// sender.send(sendMsg);
					jtfMsg.grabFocus();// ��ý���
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		jtfMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("nimei\n");
				String sendMsg = jtfMsg.getText();
				try {
					System.out.println(sendMsg);
					// sender.send(sendMsg);
					jtfMsg.grabFocus();// ��ý���
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});

		addWindowListener(new WindowAdapter() // Ϊ�˹رմ���
		{
			public void windowClosing(WindowEvent e) {
				Director director = new Director(new PrintWriterBuilder(socket));
				PrintWriter writer;
				try {
					writer = (PrintWriter) director.construct();
					// ����Ӧ�÷�һ����Ҫ�����������������ַ���������
					writer.println("bye");
					writer.flush();
					
					closeChatingThread();
					
					System.out.println("�˳�˽��");

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private Thread readerThread = null;
	private Thread writerThread = null;
	
	private void runChatingThread(final Socket socket) {
		try {
			// �½����߳�
			readerThread = new ClientReaderThread(socket);
			readerThread.start();

			// �½�д�߳�
			writerThread = new ClientWriterThread(socket);
			writerThread.start();

		} catch (Exception e) {
			System.out.print(e);
		} finally {
			// socket.close();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void closeChatingThread(){
		readerThread.stop();
		writerThread.stop();
	}
}
