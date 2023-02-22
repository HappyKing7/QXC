package Function;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import Bean.GlobalVariable;
import Enum.*;

public class ComponentInit {
	private final FontEnum fontEnum = new FontEnum();
	private final NoteFunction noteFunction = new NoteFunction();
	Font font = new Font("宋体",Font.BOLD,25);

	public JPanel addSpace(JPanel panel,int num){
		for (int i = 0; i < num; i++) {
			panel.add(new Label());
		}
		return panel;
	}

	public JPanel initJPanel(JPanel panel, String lable, JTextArea jTextArea){
		JLabel jLabel = new JLabel(lable);
		jLabel.setFont(font);
		panel.add(jLabel);
		panel.add(jTextArea);
		return panel;
	}

	public JPanel initJPanel(JPanel panel, String lable, JScrollPane jScrollPane){
		JLabel jLabel = new JLabel(lable);
		jLabel.setFont(font);
		panel.add(jLabel);
		panel.add(jScrollPane);
		return panel;
	}


	public JPanel initJPanel(JPanel panel, String lable, JTextField jTextField){
		JLabel jLabel = new JLabel(lable);
		jLabel.setFont(font);
		panel.add(jLabel);
		panel.add(jTextField);
		return panel;
	}

	/*public JPanel iniJPanel(JPanel panel,String choiceLabel,Choice choiceInfo){
		Font font = new Font("宋体",Font.BOLD,25);
		JLabel timesChoiceJLabel = new JLabel(choiceLabel);
		timesChoiceJLabel.setFont(font);
		if(!choiceLabel.equals("")){
			panel.add(timesChoiceJLabel);
		}
		panel.add(choiceInfo);
		return panel;
	}*/

	public JPanel iniJPanel(JPanel panel,String choiceLabel,JComboBox<String> jComboBox){
		JLabel timesChoiceJLabel = new JLabel(choiceLabel);
		timesChoiceJLabel.setFont(font);
		if(!choiceLabel.equals("")){
			panel.add(timesChoiceJLabel);
		}
		panel.add(jComboBox);
		return panel;
	}

	public JPanel iniJPanel(JPanel panel,String choiceLabel,JPanel addPanel){
		JLabel timesChoiceJLabel = new JLabel(choiceLabel);
		timesChoiceJLabel.setFont(font);
		if(!choiceLabel.equals("")){
			panel.add(timesChoiceJLabel);
		}
		panel.add(addPanel);
		return panel;
	}

	public JPanel iniJPanel(JPanel panel,String label, JTextField jTextField){
		JLabel priceJLabel = new JLabel(label);
		priceJLabel.setFont(font);
		panel.add(priceJLabel);
		panel.add(jTextField);
		return panel;
	}

	public JPanel iniJPanel(JPanel panel,String label){
		JLabel priceJLabel = new JLabel(label);
		priceJLabel.setFont(font);
		panel.add(priceJLabel);
		return panel;
	}

	/*public JPanel initJPanel(String typeLabel, String priceLabel, String timesLabel,
							 Choice priceChoice,Choice typeChoice, Choice timesChoice,
							 JTextField jTextField){
		Font font = new Font("宋体",Font.BOLD,25);
		JPanel panel = new JPanel();

		JLabel priceJLabel = new JLabel(priceLabel);
		priceJLabel.setFont(font);
		panel.add(priceJLabel);
		panel.add(jTextField);
		panel.add(priceChoice);

		panel.add(new Label());
		JLabel timesChoiceJLabel = new JLabel(timesLabel);
		timesChoiceJLabel.setFont(font);
		timesChoiceJLabel.setText(timesLabel);
		panel.add(timesChoiceJLabel);
		panel.add(timesChoice);

		panel.add(new Label());
		JLabel typeChoiceJLabel = new JLabel(typeLabel);
		typeChoiceJLabel.setFont(font);
		typeChoiceJLabel.setText(typeLabel);
		panel.add(typeChoiceJLabel);
		panel.add(typeChoice);

		return panel;
	}*/

	public JButton jButtonInit(JButton jButton,String label){
		jButton.setText(label);
		jButton.setBorder(BorderFactory.createRaisedBevelBorder());
		jButton.setContentAreaFilled(false);
		if (!label.contains("<html>自动识别")){
			jButton.setFont(new Font("",Font.PLAIN,20));
			jButton.setPreferredSize(new Dimension(label.length()*30,jButton.getPreferredSize().height));
		} else {
			jButton.setFont(new Font("",Font.PLAIN,15));
			jButton.setPreferredSize(new Dimension(label.length()*3,jButton.getPreferredSize().height));
		}
		return jButton;
	}

	public void init(JTextField priceJF, JComboBox<String> timesComboBox, JTextField timesJF, JTextField typeJF,
					 JComboBox<String> typeComboBox, List<JRadioButton> functionList,List<JRadioButton> priceList){
		priceJF.setText(String.valueOf(PriceEnum.TOW.getVal()));
		timesComboBox.setSelectedItem(String.valueOf(TimesEnum.ONE.getLabel()));
		timesJF.setText(String.valueOf(TimesEnum.ONE.getVal()));
		typeJF.setText(TypeEnum.ZHIXUAN.getLabel());
		typeComboBox.setSelectedItem(TypeEnum.ZHIXUAN.getLabel());
		for (JRadioButton jRadioButton : functionList){
			if (jRadioButton.getText().equals(FunctionType.FEIZUHE.getLabel())){
				jRadioButton.setSelected(true);
			}
		}
		for (JRadioButton jRadioButton : priceList){
			if (jRadioButton.getText().equals(String.valueOf(PriceEnum.TOW.getVal()))){
				jRadioButton.setSelected(true);
			}
		}
	}

	public JComboBox<String> noteQuicklySelect(GlobalVariable globalVariable){
		List<String> noteList = noteFunction.readNoteExcel(globalVariable.filePath);
		JComboBox<String> noteComboBox = new JComboBox<>();
		noteComboBox.addItem("快速选择");
		for (String s : noteList) {
			noteComboBox.addItem(s);
			noteComboBox.setFont(fontEnum.mainFont);
		}
		return noteComboBox;
	}
}
