package test;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class JFileFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private Socket socket = null;
	
	private JTextField jtfFile;

	public JFileFrame() {
		setLocation(500, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 150);
		setVisible(true);
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

	}

	// �����ť�¼�
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
		byte[] b = new byte[1024];

		File f = new File(filePath);
		try {
			// ���������
			DataOutputStream dout = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			// �ļ�������
			FileInputStream fr = new FileInputStream(f);

			System.out.println("��ʼ�����ļ�");
			
			int n = fr.read(b);
			while (n != -1) { // ��������д������
				dout.write(b, 0, n);
				dout.flush(); // �ٴζ�ȡn�ֽ�
				n = fr.read(b);
			}
			
			System.out.println("�ļ��������");
			
			// �ر���
			fr.close();
			dout.close();

			JOptionPane.showMessageDialog(this, "�ϴ��ɹ���");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// ��� ˫������
	public class BtnBrowse_ActionAdapter implements ActionListener {
		private JFileFrame adaptee;

		BtnBrowse_ActionAdapter(JFileFrame adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.btnBrowse_actionPerformed(e);
		}
	}

	// �ϴ� ˫������
	public class BtnUpload_ActionAdapter implements ActionListener {
		private JFileFrame adaptee;

		BtnUpload_ActionAdapter(JFileFrame adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.btnUpload_actionPerformed(e);
		}
	}


	// ���ӷ�����
	public void runJFileFrame() {
		try {
			socket = new Socket("localhost", 1234);
			System.out.println("���ӵ�������");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		JFileFrame frame = new JFileFrame();
		frame.runJFileFrame();
	}
}
