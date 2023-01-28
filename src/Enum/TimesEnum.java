package Enum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum TimesEnum {
	ONE("1倍", 1),
	TOW("2倍", 2),
	THREE("3倍", 3),
	FOUR("4倍", 4),
	FIVE("5倍", 5),
	SIX("6倍", 6),
	SEVEN("7倍", 7),
	EIGHT("8倍", 8),
	NINE("9倍", 9),
	TEN("10倍", 10);

	private final String label;
	private final int val;

	private static final Map<String,TimesEnum> STATUS_2_ENUM_MAP = new HashMap<>();
	private static final Map<Integer,TimesEnum> STATUS_3_ENUM_MAP = new HashMap<>();

	static {
		for (TimesEnum timesEnum : TimesEnum.values()) {
			STATUS_2_ENUM_MAP.put(timesEnum.getLabel(),timesEnum);
		}

		for (TimesEnum timesEnum : TimesEnum.values()) {
			STATUS_3_ENUM_MAP.put(timesEnum.getVal(),timesEnum);
		}
	}

	TimesEnum(String label, int val) {
		this.val = val;
		this.label = label;
	}

	public int getVal() {
		return val;
	}
	public String getLabel() {
		return label;
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
}
