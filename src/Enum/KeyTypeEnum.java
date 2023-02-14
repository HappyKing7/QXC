package Enum;

public enum KeyTypeEnum {
	LEIBIE("类别"),
	LEIXING("类型"),
	TIMES("倍数"),
	OTHER("其他");


	private final String label;

	KeyTypeEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
