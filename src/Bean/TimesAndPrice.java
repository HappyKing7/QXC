package Bean;

import java.util.List;

public class TimesAndPrice {
	private int priceFlag;
	private int group;
	private float price;
	private List<Float> priceList;

	public int getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(int priceFlag) {
		this.priceFlag = priceFlag;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Float> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<Float> priceList) {
		this.priceList = priceList;
	}
}
