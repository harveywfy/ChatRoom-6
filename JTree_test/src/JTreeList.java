import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeList {

	public JTreeList() {

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("�����");// �Զ�����ڵ�

		ArrayList<String> list = new ArrayList<String>();
		list.add("С��");
		list.add("С��");
		list.add("Сΰ");

		DefaultMutableTreeNode leafTreeNode;
		for (String x : list) {
			leafTreeNode = new DefaultMutableTreeNode(x);
			rootNode.add(leafTreeNode);
		}

		final JTree tree = new JTree(rootNode);// ����е���

		JFrame f = new JFrame("JTreeDemo");
		f.add(tree);
		f.setSize(300, 300);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ���ѡ���¼�
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();

				if (node == null)
					return;

				Object object = node.getUserObject();
				if (node.isLeaf()) {
					String user = (String) object;
					System.out.println("��ѡ���ˣ�" + user.toString());
				}

			}
		});
	}
}
