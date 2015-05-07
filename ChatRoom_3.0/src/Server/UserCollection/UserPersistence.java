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

	// �洢�����û�
	private HashMap<String, Socket> socketsMap = new HashMap<String, Socket>();

	public UserPersistence() {
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
	private boolean Insert(String name, Socket socket) {
		try {
			String insertsql = "insert into user(user_id, user_name)"
					+ " values(?,?)";
			PreparedStatement ps = con.prepareStatement(insertsql);

			ps.setInt(1, 0);// ���ݿ�����
			ps.setString(2, name);

			int result = ps.executeUpdate();
			// ps.executeUpdate();�޷��ж��Ƿ��Ѿ�����
			if (result > 0) {
				System.out.println("��ע���û���" + name);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// // ɾ������
	// private boolean Delete(String delsql) {
	// try {
	// PreparedStatement ps = con.prepareStatement(delsql);
	// int result = ps.executeUpdate(delsql);
	// if (result > 0)
	// return true;
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// }
	// return false;
	// }

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
	
	// ��д�����෽��
	
	// ע���ͬʱ�����û�����
	@Override
	public void addUser(String name, Socket socket) {
		Insert(name, socket);

		// ����
		socketsMap.put(name, socket);
	}

	@Override
	public Socket getSocket(String name) {
		ResultSet result = Select("select user_name from user where user_name = \'"
				+ name + "\'");
		Socket socket = null;
		try {
			// �����Ϊ��
			if (!result.wasNull()) {
				socket = (Socket) socketsMap.get(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return socket;
	}

	// ���������ʵ�����߷���
	@Override
	public void deleteUser(String name) {
		ResultSet result = Select("select user_name from user where user_name = \'"
				+ name + "\'");
		try {
			if (!result.wasNull()) {
				socketsMap.remove(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getUsersList(String name) {
		// �Ѿ������ķ���������
		return null;
	}

	@Override
	public HashMap<String, Socket> getSocketsMap() {
		return socketsMap;
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
