package Enum;

public enum HeZhiEnum {
	HZDA(TypeEnum.HZDA.getLabel(),0),
	HZX(TypeEnum.HZX.getLabel(), 1),
	HZDAN(TypeEnum.HZDAN.getLabel(), 2),
	HZS(TypeEnum.HZS.getLabel(), 3);

	private final String label;
	private final int val;

	HeZhiEnum(String label, int val) {
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
