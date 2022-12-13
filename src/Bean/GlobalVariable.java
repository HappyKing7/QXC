package Bean;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalVariable {
	public Frame oneFrame = new Frame("单人汇总");
	public Frame mainFrame = new Frame("QXC");
	public AtomicInteger alllistNo =new AtomicInteger();
	public AtomicInteger ticketsNo =new AtomicInteger();
	public TicketList tickets = new TicketList();
	public String selectNo = "";
	public String filePath = "";
	public String selectPrice = "";

	public Frame getOneFrame() {
		return oneFrame;
	}

	public void setOneFrame(Frame oneFrame) {
		this.oneFrame = oneFrame;
	}

	public Frame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(Frame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public AtomicInteger getAlllistNo() {
		return alllistNo;
	}

	public void setAlllistNo(AtomicInteger alllistNo) {
		this.alllistNo = alllistNo;
	}

	public AtomicInteger getTicketsNo() {
		return ticketsNo;
	}

	public void setTicketsNo(AtomicInteger ticketsNo) {
		this.ticketsNo = ticketsNo;
	}

	public TicketList getTickets() {
		return tickets;
	}

	public void setTickets(TicketList tickets) {
		this.tickets = tickets;
	}

	public String getSelectNo() {
		return selectNo;
	}

	public void setSelectNo(String selectNo) {
		this.selectNo = selectNo;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSelectPrice() {
		return selectPrice;
	}

	public void setSelectPrice(String selectPrice) {
		this.selectPrice = selectPrice;
	}
}
