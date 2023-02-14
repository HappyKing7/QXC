package Enum;

public enum QuanBaoEnum {
	BZQB("豹子全包",0),
	ZLQB("组六全包", 1),
	ZSQB("组三全包", 2),
	DZQB("对子全包", 3);

	private final String label;
	private final int val;

	QuanBaoEnum(String label, int val) {
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
