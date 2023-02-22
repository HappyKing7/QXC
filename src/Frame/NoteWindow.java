package Frame;

import Bean.*;
import Enum.*;
import Function.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class NoteWindow {
	private final FontEnum fontEnum = new FontEnum();
	private final ComponentInit componentInit = new ComponentInit();

	public JFrame showNoteWindow(GlobalVariable globalVariable){
		//备注
		JTextField noteJF=new JTextField(globalVariable.tickets.getNote(),70);
		noteJF.setFont(fontEnum.mainFont);

		Button noteButton = new Button("添加备注");
		noteButton.setFont(fontEnum.mainButtonFont);

		JComboBox<String> noteComboBox = componentInit.noteQuicklySelect(globalVariable);

		JPanel jPanel = componentInit.iniJPanel(new JPanel(),"备注",noteJF);
		jPanel = componentInit.iniJPanel(jPanel,"",noteComboBox);
		jPanel.add(noteButton);

		JFrame noteFrame = new JFrame("备注");
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

		noteButton.addActionListener(e -> {
			globalVariable.tickets.setNote(noteJF.getText());
			noteFrame.dispose();
		});

		noteComboBox.addItemListener(e -> noteJF.setText(Objects.requireNonNull(noteComboBox.getSelectedItem()).toString()));
		noteComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				noteComboBox.setPopupVisible(true);
			}
		});

		return noteFrame;
	}
}
