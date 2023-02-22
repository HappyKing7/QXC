package Enum;

public enum QuanBaoEnum {
	BZQB(TypeEnum.BZQB.getLabel(),0),
	ZLQB(TypeEnum.ZLQB.getLabel(), 1),
	ZSQB(TypeEnum.ZSQB.getLabel(), 2),
	DZQB(TypeEnum.DZQB.getLabel(), 3);

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
