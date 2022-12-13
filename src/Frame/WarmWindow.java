package Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WarmWindow {
	public void warmWindow(String warmInfo,Font warmInfoFont){
		Frame frame=new Frame("提示信息");
		JLabel jLabel = new JLabel(warmInfo,JLabel.CENTER);
		jLabel.setFont(warmInfoFont);
		jLabel.setForeground(Color.red);
		frame.add(jLabel,BorderLayout.CENTER);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		frame.setBounds((screenSize.width - 1000) / 2, (screenSize.height - 150) / 2, 1000, 150);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}
}
