import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemPersistence {

	public Connection con;
	private ResultSet result;

	/* ���췽�� */
	public ItemPersistence() {
		con = null;
		result = null;
	}

	/* �������ݿ� */
	public Connection connectToDB() {
		String driveName = "com.mysql.jdbc.Driver";// ����������
		String databaseURL = "jdbc:mysql://localhost/market";// URLָ��Ҫ���ʵ����ݿ���
		String user = "root";// MySQL����ʱ���û���
		String password = "root";// MySQL����ʱ������
		// Connection con = null;//�����ﴴ���ֲ��������ǵ��˳�Ա����������
		
		try {
			Class.forName(driveName);// ��������
			System.out.println("�ɹ��������ݿ���������");
			con = DriverManager.getConnection(databaseURL, user, password);// ����MySQL���ݿ�
			System.out.println("�������ݿ�ɹ���");
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
		return con;
	}

	/* �Ͽ����ݿ� */
	public void CutConnection(Connection con) throws SQLException {
		try {
			if (result != null)
				;
			if (con != null)
				;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (result != null)
				result.close();
			con.close();
			System.out.print("���ݿ�رճɹ���");
		}
	}

	/* �������� */
	public boolean Insert(Item item) {
		try {
			String insertsql = "insert into Item(item_id,item_name)"
					+ " values(?,?)";
			PreparedStatement ps = con.prepareStatement(insertsql);

			ps.setInt(1, item.getID());
			ps.setString(2, item.getName());

			int result = ps.executeUpdate();
			// ps.executeUpdate();�޷��ж��Ƿ��Ѿ�����
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/* ɾ������ */
	public boolean Delet(int ID) {
		try {
			String delsql = "delete from Item where item_id=" + ID;

			PreparedStatement ps = con.prepareStatement(delsql);
			int result = ps.executeUpdate(delsql);
			if (result > 0)
				return true;
		} catch (SQLException ex) {
			Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}
	
	/* �޸���Ϣ */
	public boolean Update(int ID, String newName) {
		try {
			String upsql = "update Item set item_name='" + newName
					+ "' where item_id=" + ID;

			PreparedStatement ps = con.prepareStatement(upsql);
			int result = ps.executeUpdate();// ������������0
			if (result > 0)
				return true;
		} catch (SQLException ex) {
			Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	/* ��ѯ���� */
	public void Select(String sql) {
		try {
			Statement statement = con.createStatement();
			result = statement.executeQuery(sql);// sql:"select * from item"
			while (result.next()) {
				System.out.println(result.getString("item_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
