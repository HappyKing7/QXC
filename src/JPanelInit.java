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

	public JPanel initJPanel(String choiceLable, String priceLable, Choice choice, JTextField jTextField){
		Font font = new Font("宋体",Font.BOLD,25);
		JPanel panel = new JPanel();

		JLabel priceJLabel = new JLabel(priceLable);
		priceJLabel.setFont(font);
		panel.add(priceJLabel);
		panel.add(jTextField);

		JLabel choiceJLabel = new JLabel(choiceLable);
		choiceJLabel.setFont(font);
		choiceJLabel.setText(choiceLable);
		panel.add(choiceJLabel);
		panel.add(choice);
		return panel;
	}
}
