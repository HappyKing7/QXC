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

	static void showTodaySummary(List<ShowSummaryList> showSummaryLists, String fileName, GlobalVariable globalVariable){
		Frame frame = new Frame("今日汇总");
		JLabel title = new JLabel(fileName+"数据",JLabel.CENTER);
		title.setFont(fontEnum.todaySummaryTitleFont);
		JPanel panel =  new JPanel();
		for (int i = 0; i < showSummaryLists.size(); i++) {
			ShowSummaryList ssl = showSummaryLists.get(i);
			JPanel resultPanel = new JPanel(new GridLayout(ssl.getShowSummaryList().size(),7));

			JLabel firstTotalMoneyLabel = new JLabel(ssl.getTotalMoney());
			firstTotalMoneyLabel.setFont(fontEnum.todaySummaryFont);
			for (int j = 0; j < ssl.getShowSummaryList().size(); j++) {
				ShowSummary ss = ssl.getShowSummaryList().get(j);
				if(j==0){
					JLabel noLabel = new JLabel(ssl.getNo() + "     " + ssl.getTotalPrice());
					noLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstSerialNumberLabel = new JLabel();
					if(ss.getSerialNumber().length()<= 30){
						firstSerialNumberLabel.setText(ss.getSerialNumber());
					}else {
						firstSerialNumberLabel.setText(function.numberWrap(ss.getSerialNumber(),5));
					}
					firstSerialNumberLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstDetailLabel = new JLabel(ss.getDetail());
					firstDetailLabel.setFont(fontEnum.todaySummaryFont);
/*					JLabel firstTotalPriceLabel = new JLabel(ssl.getTotalPrice());
					firstTotalPriceLabel.setFont(fontEnum.todaySummaryFont);*/
					JLabel firstNoteLabel = new JLabel(ssl.getNote());
					firstNoteLabel.setFont(fontEnum.todaySummaryFont);

					resultPanel.add(noLabel);
					resultPanel.add(firstSerialNumberLabel);
					resultPanel.add(new JLabel());
					resultPanel.add(firstDetailLabel);
					resultPanel.add(new JLabel());
					resultPanel.add(firstNoteLabel);
					//resultPanel.add(firstTotalPriceLabel);
					if(i==0){
						resultPanel.add(firstTotalMoneyLabel);
					}else{
						resultPanel.add(new JLabel());
					}
				}else {
					JLabel serialNumberLabel = new JLabel();
					if(ss.getSerialNumber().length()<= 30){
						serialNumberLabel.setText(ss.getSerialNumber());
					}else {
						serialNumberLabel.setText(function.numberWrap(ss.getSerialNumber(),5));
					}
					serialNumberLabel.setFont(fontEnum.todaySummaryFont);
					JLabel detailLabel = new JLabel(ss.getDetail());
					detailLabel.setFont(fontEnum.todaySummaryFont);

					resultPanel.add(new JLabel());
					resultPanel.add(serialNumberLabel);
					resultPanel.add(new JLabel());
					resultPanel.add(detailLabel);
					//resultPanel.add(new JLabel());
					resultPanel.add(new JLabel());
					resultPanel.add(new JLabel());
					resultPanel.add(new JLabel());
				}
				panel.add(resultPanel);
			}
			panel.add(new JLabel(" "));
		}
		BoxLayout layout=new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		JScrollPane jScrollPane = new JScrollPane(panel);
		frame.add(jScrollPane,BorderLayout.CENTER);
		frame.add(title,BorderLayout.NORTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		/*frame.setBounds((screenSize.width - 1500) / 2, (screenSize.height - 800) / 2, 1500, 800);
		frame.setVisible(true);*/
		Rectangle bounds = new Rectangle(screenSize);
		frame.setBounds(bounds);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
