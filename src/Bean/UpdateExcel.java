package Bean;

public class UpdateExcel {
	private String no;
	private Integer towNo;
	private String totalPrice;
	private String totalMoney;
	private String note;
	private ShowSummary ss;

	public String getNo() {
		return no;
	}

	public void setNo(String oneNo) {
		this.no = oneNo;
	}

	public Integer getTowNo() {
		return towNo;
	}

	public void setTowNo(Integer towNo) {
		this.towNo = towNo;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNote() {
		return note;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ShowSummary getSs() {
		return ss;
	}

	public void setSs(ShowSummary ss) {
		this.ss = ss;
	}
}