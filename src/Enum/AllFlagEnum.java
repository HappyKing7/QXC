package Enum;

public enum AllFlagEnum {
	ALL("全部", 0),
	NOTALL("非全部", 1);

	private final String label;
	private final int val;

	AllFlagEnum(String label, int val) {
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
