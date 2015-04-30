import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeMap {

	public JTreeMap() {

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("�����");// �Զ�����ڵ�

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("С��", 1);
		map.put("С��", 1);
		map.put("Сΰ", 1);

		DefaultMutableTreeNode leafTreeNode;
		
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			//Object val = map.get(key);
			
			leafTreeNode = new DefaultMutableTreeNode(key);
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
