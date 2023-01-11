package Frame;

import Enum.*;
import Start.*;
import Bean.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OneSummaryWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static Function function = new Function();
	private JPanel showOneSummaryJPanel = new JPanel();

	public JPanel showOneSummaryFrame(JPanel showOneSummaryPanel,GlobalVariable globalVariable){
		showOneSummaryJPanel = showOneSummaryPanel;
		showOneSummaryPanel.removeAll();
		JPanel panel = new JPanel(new GridLayout(globalVariable.tickets.getTicketList().size()+2,2));
		ButtonGroup btnGroup = new ButtonGroup();
		float totalPrice = 0;
		for (int i = 0; i < globalVariable.tickets.getTicketList().size(); i++) {
			JPanel resultPanel = new JPanel();
			String[] output = function.showSummaryWithNumber(globalVariable.tickets.getTicketList().get(i)).split("-");
			JLabel resultNumberLabel = new JLabel(output[0]);
			JLabel resultInfoLabel = new JLabel(output[1]);
			totalPrice = totalPrice + globalVariable.tickets.getTicketList().get(i).getTotalPrice();
			resultNumberLabel.setFont(fontEnum.oneSummaryFont);
			resultInfoLabel.setFont(fontEnum.oneSummaryFont);
			JRadioButton resultButton = new JRadioButton(String.valueOf(i+1));
			resultButton.setFont(fontEnum.oneSummaryFont);
			resultButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					globalVariable.selectNo = String.valueOf(Integer.valueOf(resultButton.getText()) - 1);
				}
			});
			btnGroup.add(resultButton);
			resultPanel.add(resultButton);
			resultPanel.add(new JPanel());
			resultPanel.add(resultNumberLabel);
			resultPanel.add(new JPanel());
			resultPanel.add(resultInfoLabel);
			panel.add(resultPanel);
		}
		JPanel totalPricePanel = new JPanel();
		JLabel totalPriceLabel = new JLabel("总共"+String.format("%.2f", Float.valueOf(totalPrice))+"元");
		totalPriceLabel.setFont(fontEnum.oneSummaryFont);
		totalPricePanel.add(totalPriceLabel);
		panel.add(totalPricePanel);

		showOneSummaryPanel.add(panel);
		Button update = new Button("修改");
		Button delete = new Button("删除");
		Button sumbit = new Button("下单");
		JPanel buttonPanel=new JPanel();
		buttonPanel.setFont(fontEnum.oneSummaryFont);
		buttonPanel.add(update);
		buttonPanel.add(delete);
		buttonPanel.add(sumbit);
		//panel.add(buttonPanel);

		showOneSummaryPanel.add(panel,BorderLayout.NORTH);

		return showOneSummaryPanel;
	}
}
