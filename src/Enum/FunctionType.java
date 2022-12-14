package Enum;

public enum FunctionType {
	ZUHE("组合", 0),
	FEIZUHE("非组合", 1);

	private String label;
	private final int val;

	FunctionType(String label, int val) {
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
