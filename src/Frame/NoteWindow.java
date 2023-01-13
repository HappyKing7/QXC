package Frame;

import Bean.*;
import Enum.*;
import Start.ComponentInit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoteWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static ComponentInit componentInit = new ComponentInit();

	public void showNoteWindow(GlobalVariable globalVariable){
		//备注
		JTextField noteJF=new JTextField(globalVariable.tickets.getNote(),70);
		noteJF.setFont(fontEnum.mainFont);

		Button noteButton = new Button("添加备注");
		noteButton.setFont(fontEnum.mainButtonFont);

		JPanel jPanel = componentInit.iniJPanel(new JPanel(),"备注",noteJF);
		jPanel.add(noteButton);

		Frame noteFrame = new Frame("备注");
		noteFrame.add(jPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//窗口化
		noteFrame.setBounds((screenSize.width - 1200) / 2, (screenSize.height - 120) / 2, 1200, 120);
		noteFrame.setVisible(true);

		//监听器
		noteFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				noteFrame.dispose();
			}
		});

		noteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				globalVariable.tickets.setNote(noteJF.getText());
				noteFrame.dispose();
			}
		});
	}
}
