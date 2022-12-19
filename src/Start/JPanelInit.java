package Start;

import javax.swing.*;
import java.awt.*;

public class JPanelInit {
	public JPanel addSpace(JPanel panel,int num){
		for (int i = 0; i < num; i++) {
			panel.add(new Label());
		}
		return panel;
	}
	public JPanel initJPanel(JPanel panel, String lable, JTextField jTextField){
		Font font = new Font("宋体",Font.BOLD,25);
		JLabel jLabel = new JLabel(lable);
		jLabel.setFont(font);
		panel.add(jLabel);
		panel.add(jTextField);
		return panel;
	}

	public JPanel iniJPanel(JPanel panel,String choiceLabel,Choice choiceInfo){
		Font font = new Font("宋体",Font.BOLD,25);
		JLabel timesChoiceJLabel = new JLabel(choiceLabel);
		timesChoiceJLabel.setFont(font);
		if(!choiceLabel.equals("")){
			panel.add(timesChoiceJLabel);
		}
		panel.add(choiceInfo);
		return panel;
	}

	public JPanel iniJPanel(JPanel panel,String label, JTextField jTextField){
		Font font = new Font("宋体",Font.BOLD,25);
		JLabel priceJLabel = new JLabel(label);
		priceJLabel.setFont(font);
		panel.add(priceJLabel);
		panel.add(jTextField);
		return panel;
	}

	public JPanel iniJPanel(JPanel panel,String label){
		Font font = new Font("宋体",Font.BOLD,25);
		JLabel priceJLabel = new JLabel(label);
		priceJLabel.setFont(font);
		panel.add(priceJLabel);
		return panel;
	}

	public JPanel initJPanel(String typeLabel, String priceLabel, String timesLabel,
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
	}
}
