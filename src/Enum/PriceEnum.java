package Enum;

public enum PriceEnum {
	TOW(2),
	Ten(10),
	Hundred(100);

	private final int val;

	PriceEnum(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

}
