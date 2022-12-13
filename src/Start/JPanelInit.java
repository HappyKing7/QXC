package Start;

import javax.swing.*;
import java.awt.*;

public class JPanelInit {
	public JPanel initJPanel(String lable, JTextField jTextField){
		Font font = new Font("宋体",Font.BOLD,25);
		JPanel panel = new JPanel();
		JLabel jLabel = new JLabel(lable);
		jLabel.setFont(font);
		panel.add(jLabel);
		panel.add(jTextField);
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
