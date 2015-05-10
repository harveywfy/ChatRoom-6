package Server.UserCollection;

import java.io.Serializable;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class UserPersistence extends UserList implements Serializable {

	private static final long serialVersionUID = 1L;
	private Connection con;

	// �����û���
	private OnlineUserList onlineUserList;

	// ���ӱ�
	private ConnectUserList connectUserList;

	// �ļ��������ӱ�
	private FileSocketList fileSocketList;

	// �������ӱ�
	private SoundSocketList soundSocketList;

	public UserPersistence() {
		onlineUserList = new OnlineUserList();
		connectUserList = new ConnectUserList();
		fileSocketList = new FileSocketList();
		soundSocketList = new SoundSocketList();
		
		con = null;
		ConnectToDB();
	}

	// �������ݿ�
	public void ConnectToDB() {
		String driveName = "com.mysql.jdbc.Driver";// ����������
		String databaseURL = "jdbc:mysql://localhost/chat_room";// URLָ��Ҫ���ʵ����ݿ���
		String user = "root";// MySQL����ʱ���û���
		String password = "root";// MySQL����ʱ������

		try {
			Class.forName(driveName);// ��������
			System.out.println("�ɹ��������ݿ���������");
			con = DriverManager.getConnection(databaseURL, user, password);// ����MySQL���ݿ�
			System.out.println("�������ݿ�ɹ���");
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}

	// �Ͽ����ݿ�
	public void CutConnection(Connection con) throws SQLException {
		try {
			// if (result != null)
			// ;
			if (con != null)
				;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (result != null)
			// result.close();
			con.close();
			System.out.print("���ݿ�رճɹ���");
		}
	}

	// ��������
	private boolean Insert(String name) {
		try {
			String insertsql = "insert into user(user_id, user_name)"
					+ " values(?,?)";
			PreparedStatement ps = con.prepareStatement(insertsql);

			ps.setInt(1, 0);// ���ݿ�����
			ps.setString(2, name);

			int result = ps.executeUpdate();
			// ps.executeUpdate();�޷��ж��Ƿ��Ѿ�����
			if (result > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// ɾ������
	private boolean Delete(String name) {
		try {
			String delsql = "delete from user where user_name = \'" + name
					+ "\'";
			PreparedStatement ps = con.prepareStatement(delsql);
			int result = ps.executeUpdate(delsql);
			if (result > 0)
				return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	// ��ѯ����
	private ResultSet Select(String sql) {
		ResultSet result = null;
		try {
			Statement statement = con.createStatement();
			result = statement.executeQuery(sql);// sql:"select * from item"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** ������������������������������������������������������������������������������������������������������������������������������������������������ */

	// ������ʽ�û�
	@Override
	public void loginFormalUser(String name, String linkName) {
		onlineUserList.loginFormalUser(name, linkName);
	}

	// ������ʽ�û�
	@Override
	public void logoutFormalUser(String name) {
		onlineUserList.logoutFormalUser(name);
	}

	// �������ߺ���<��ʱ���֣� ����>
	@Override
	public HashMap<String, String> getOnlineUsers() {
		return onlineUserList.getOnlineUsersMap();
	}

	/** ������������������������������������������������������������������������������������������������������������������������������������������������ */

	// ������ʱ�û�
	@Override
	public Socket getSocket(String name) {
		return connectUserList.getSocket(name);
	}

	// ������ʱ�û�
	@Override
	public void loginUser(String name, Socket socket) {
		connectUserList.loginUser(name, socket);
	}

	// ������ʱ�û�
	@Override
	public void logoutUser(String name) {
		connectUserList.logoutUser(name);
	}

	// �洢����������ڽ������ӵ��û� <��ʱ���֣�socket>
	@Override
	public HashMap<String, Socket> getSocketsMap() {
		return connectUserList.getSocketsMap();
	}

	/** ������������������������������������������������������������������������������������������������������������������������������������������������ */

	// �ļ�socket
	@Override
	public void loginFileSocket(String linkName, Socket socket) {
		fileSocketList.loginFileSocket(linkName, socket);
	}

	@Override
	public void logoutFileSocket(String linkName) {
		fileSocketList.logoutFileSocket(linkName);
	}

	@Override
	public HashMap<String, Socket> getFileSocketsMap() {
		return fileSocketList.getFileSocketsMap();
	}

	/** ������������������������������������������������������������������������������������������������������������������������������������������������ */

	// �Ƿ�ע��
	@Override
	public boolean isRegister(String name) {
		ResultSet result = Select("select user_name from user where user_name = \'"
				+ name + "\'");
		try {
			// result.next()ȡ��������falser
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ע���û�
	@Override
	public void registUser(String name) {
		Insert(name);
		System.out.println("��ע���û���" + name);
	}

	// ע���û�
	@Override
	public void deleteUser(String name) {
		Delete(name);
		System.out.println("ע���û���" + name);
	}

	/** ������������������������������������������������������������������������������������������������������������������������������������������������ */

	// ����socket
	@Override
	public void loginSoundSocket(String linkName, Socket socket) {
		soundSocketList.loginSoundSocket(linkName, socket);
	}

	@Override
	public void logoutSoundSocket(String linkName) {
		soundSocketList.logoutSoundSocket(linkName);
	}

	@Override
	public HashMap<String, Socket> getSoundSocketsMap() {
		return soundSocketList.getSoundSocketsMap();
	}

}

/*
 * CREATE SCHEMA IF NOT EXISTS `chat_room` DEFAULT CHARACTER SET utf8 ; USE
 * `chat_room` ;
 * 
 * -- ----------------------------------------------------- -- Table
 * `chat_room`.`user` -- -----------------------------------------------------
 * CREATE TABLE IF NOT EXISTS `chat_room`.`user` ( `user_id` INT(11) NOT NULL
 * AUTO_INCREMENT, `user_name` VARCHAR(45) NULL DEFAULT NULL, PRIMARY KEY
 * (`user_id`)) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;
 */
