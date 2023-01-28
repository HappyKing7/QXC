package Start;

import Bean.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class demo {
	public static void main(String[] args) {
		demo1();
	}

	static void demo1(){
		JFrame jFrame = new JFrame();

		String[] columnNames = {"SHUZI","ZIMU","YINGYU"};
/*		//String[][] tableData = {{"","a","one"},{"2","b","two"},{"3","c","three"}};
		//String[][] tableData = {{"<html><span style='color: red'>1471</span> 1473 1477 1481 1483 1487 1491 1493 1497 1571 1573 1577 1581 1583 1587 1591 1593 1597 1671 1673 1677 1681 1683 1687 1691 1693 1697 2471 2473 2477 2481 2483 2487 2491 2493 2497 2571 2573 2577 2581 2583 2587 2591 2593 2597 2671 2673 2677 2681 2683 2687 2691 2693 2697 3471 3473 3477 3481 3483 3487 3491 3493 3497 3571 3573 3577 3581 3583 3587 3591 3593 3597 3671 3673 3677 3681 3683 3687 3691 3693 3697</html>","a","one"},{"2","b","two"},{"3","c","three"}};

		DefaultTableModel defaultTableModel = new DefaultTableModel(tableData,columnNames);
		JTable jTable = new JTable(defaultTableModel);

		JScrollPane jScrollPane = new JScrollPane(jTable);
		jFrame.add(jScrollPane,BorderLayout.CENTER);*/
		JLabel jLabel = new JLabel("<html><span style='color: red'>1471</span></html>");
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		jLabel.setVerticalAlignment(JLabel.CENTER);
		jFrame.add(jLabel,BorderLayout.NORTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jFrame.setBounds((screenSize.width - 1500) / 2, (screenSize.height - 800) / 2, 1500, 800);
		jFrame.setVisible(true);

		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	//514
	//511
}
