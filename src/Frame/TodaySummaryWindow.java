package Frame;

import Enum.*;
import Start.*;
import Bean.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class TodaySummaryWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static Function function = new Function();
	static void showTodaySummary(List<ShowSummaryList> showSummaryLists, GlobalVariable globalVariable){
		Frame frame = new Frame();
		JPanel panel =  new JPanel(new GridLayout(showSummaryLists.get(0).getSize(),1));
		for (int i = 0; i < showSummaryLists.size(); i++) {
			ShowSummaryList ssl = showSummaryLists.get(i);
			JPanel resultPanel = new JPanel(new GridLayout(ssl.getShowSummaryList().size(),5));
			JLabel firstTotalMoneyLabel = new JLabel(ssl.getTotalMoney());
			firstTotalMoneyLabel.setFont(fontEnum.todaySummaryFont);
			for (int j = 0; j < ssl.getShowSummaryList().size(); j++) {
				ShowSummary ss = ssl.getShowSummaryList().get(j);
				if(j==0){
					JLabel noLabel = new JLabel(ssl.getNo());
					noLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstSerialNumberLabel = new JLabel(ss.getSerialNumber());
					firstSerialNumberLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstDetailLabel = new JLabel(ss.getDetail());
					firstDetailLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstTotalPriceLabel = new JLabel(ssl.getTotalPrice());
					firstTotalPriceLabel.setFont(fontEnum.todaySummaryFont);

					resultPanel.add(noLabel);
					resultPanel.add(firstSerialNumberLabel);
					resultPanel.add(firstDetailLabel);
					resultPanel.add(firstTotalPriceLabel);
					if(i==0){
						resultPanel.add(firstTotalMoneyLabel);
					}else{
						JLabel emptyLabel0 = new JLabel();
						emptyLabel0.setFont(fontEnum.todaySummaryFont);
						resultPanel.add(emptyLabel0);
					}
				}else {
					JLabel serialNumberLabel = new JLabel(ss.getSerialNumber());
					serialNumberLabel.setFont(fontEnum.todaySummaryFont);
					JLabel detailLabel = new JLabel(ss.getDetail());
					detailLabel.setFont(fontEnum.todaySummaryFont);
					JLabel emptyLabel1 = new JLabel();
					emptyLabel1.setFont(fontEnum.todaySummaryFont);
					JLabel emptyLabel2 = new JLabel();
					emptyLabel2.setFont(fontEnum.todaySummaryFont);
					JLabel emptyLabel3 = new JLabel();
					emptyLabel2.setFont(fontEnum.todaySummaryFont);

					resultPanel.add(emptyLabel1);
					resultPanel.add(serialNumberLabel);
					resultPanel.add(detailLabel);
					resultPanel.add(emptyLabel2);
					resultPanel.add(emptyLabel3);
				}
				panel.add(resultPanel);
			}
		}
		JScrollPane jScrollPane = new JScrollPane(panel);
		frame.add(jScrollPane,BorderLayout.CENTER);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		frame.setBounds((screenSize.width - 1250) / 2, (screenSize.height - 800) / 2, 1250, 800);
		frame.setVisible(true);

		//注册监听器
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}
}
