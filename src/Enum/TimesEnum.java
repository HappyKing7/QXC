package Enum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum TimesEnum {
	ONE("1倍","一倍", 1),
	TOW("2倍","两倍", 2),
	THREE("3倍","三倍", 3),
	FOUR("4倍","四倍", 4),
	FIVE("5倍","五倍", 5),
	SIX("6倍","六倍", 6),
	SEVEN("7倍","七倍", 7),
	EIGHT("8倍","八倍", 8),
	NINE("9倍","九倍", 9),
	TEN("10倍","十倍", 10);


	private final String label;
	private final String clabel;
	private final int val;

	private static final Map<String,TimesEnum> STATUS_2_ENUM_MAP = new HashMap<>();
	private static final Map<Integer,TimesEnum> STATUS_3_ENUM_MAP = new HashMap<>();
	private static final Map<String,TimesEnum> STATUS_4_ENUM_MAP = new HashMap<>();

	static {
		for (TimesEnum timesEnum : TimesEnum.values()) {
			STATUS_2_ENUM_MAP.put(timesEnum.getLabel(),timesEnum);
			STATUS_3_ENUM_MAP.put(timesEnum.getVal(),timesEnum);
			STATUS_4_ENUM_MAP.put(timesEnum.getCLabel(),timesEnum);
		}
	}

	TimesEnum(String label, String clabel,int val) {
		this.val = val;
		this.clabel = clabel;
		this.label = label;
	}

	public int getVal() {
		return val;
	}
	public String getLabel() {
		return label;
	}
	public String getCLabel() {
		return clabel;
	}

	public static TimesEnum getEnumByLabel(String label) {
		if (Objects.isNull(label)) {
			return null;
		}
		return STATUS_2_ENUM_MAP.get(label);
	}
	public static Integer getValByLabel(String label) {
		TimesEnum timesEnum = getEnumByLabel(label);
		if (Objects.isNull(timesEnum)) {
			return null;
		}
		return timesEnum.getVal();
	}

	public static TimesEnum getEnumByVal(int val) {
		if (Objects.isNull(val)) {
			return null;
		}
		return STATUS_3_ENUM_MAP.get(val);
	}
	public static String getLabelByVal(int val) {
		TimesEnum timesEnum = getEnumByVal(val);
		if (Objects.isNull(timesEnum)) {
			return null;
		}
		return timesEnum.getLabel();
	}

	public static TimesEnum getEnumByCLabel(String clabel) {
		if (Objects.isNull(clabel)) {
			return null;
		}
		return STATUS_4_ENUM_MAP.get(clabel);
	}
	public static Integer getValByCLabel(String clabel) {
		TimesEnum timesEnum = getEnumByCLabel(clabel);
		if (Objects.isNull(timesEnum)) {
			return null;
		}
		return timesEnum.getVal();
	}
}
