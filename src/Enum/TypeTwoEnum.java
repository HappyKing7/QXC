package Enum;

public enum TypeTwoEnum
{
	TICAI("体彩", 0),
	FUCAI("福彩", 1);

	private final String label;
	private final int val;

	TypeTwoEnum(String label, int val) {
		this.val = val;
		this.label = label;
	}

	public int getVal() {
		return val;
	}
	public String getLabel() {
		return label;
	}
}
