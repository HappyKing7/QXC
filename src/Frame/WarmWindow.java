package Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WarmWindow {
	public JFrame warmWindow(String warmInfo,Font warmInfoFont){
		JFrame frame = new JFrame("提示信息");
		JLabel jLabel = new JLabel(warmInfo,JLabel.CENTER);
		jLabel.setFont(warmInfoFont);
		jLabel.setForeground(Color.red);
		frame.add(jLabel,BorderLayout.CENTER);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		if (!warmInfo.contains("<html>")){
			frame.setBounds((screenSize.width - 1000) / 2, (screenSize.height - 150) / 2, 1000, 150);
		} else {
			frame.setBounds((screenSize.width - 1400) / 2, (screenSize.height - 180) / 2, 1400, 180);
		}
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		return frame;
	}
}
