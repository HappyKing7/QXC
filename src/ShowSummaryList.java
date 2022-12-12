import java.util.List;

public class ShowSummaryList {
	private String no;
	private List<ShowSummary> showSummaryList;
	private String totalPrice;
	private Integer size;
	private String totalMoney;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public List<ShowSummary> getShowSummaryList() {
		return showSummaryList;
	}

	public void setShowSummaryList(List<ShowSummary> showSummaryList) {
		this.showSummaryList = showSummaryList;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
}
