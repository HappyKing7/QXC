package Enum;

public enum PriceEnum {
	TOW(2),
	TEN(10);

	private final int val;

	PriceEnum(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

}
