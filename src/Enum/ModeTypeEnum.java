package Enum;

public enum ModeTypeEnum {
	CREATE("创建", 0),
	UPDATE("刷新", 1);

	private String label;
	private final int val;

	ModeTypeEnum(String label, int val) {
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
