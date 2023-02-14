package Enum;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum TypeEnum {
	ALL("全选", 0),
	ZHIXUAN("直选", 0),
	ZUXUAN("组选", 0),
	SF("双飞",0),
	ZL("组六", 0),
	ZS("组三", 0),
	DD("独胆", 0),
	DZ("对子", 0),
	SFZL("双飞组六", 0),
	SFZS("双飞组三", 0),
	YMDW("一码定位", 0),
	EMDW("二码定位", 0),
	BZQB("豹子全包",0),
	ZLSM("组六四码",0),
	ZLWM("组六五码",0),
	ZLLM("组六六码",0),
	ZLQIM("组六七码", 0),
	ZLBM("组六八码", 0),
	ZLJM("组六九码", 0),
	ZLQB("组六全包", 0),
	ZSEM("组三二码", 0),
	ZSSANM("组三三码", 0),
	ZSSIM("组三四码", 0),
	ZSWM("组三五码", 0),
	ZSLM("组三六码", 0),
	ZSQIM("组三七码", 0),
	ZSBM("组三八码", 0),
	ZSJM("组三九码", 0),
	ZSQB("组三全包", 0),
	DZQB("对子全包", 0),
	KD("跨度", 0),
	KD0("跨度0", 0),
	KD1("跨度1",0),
	KD2("跨度2", 0),
	KD3("跨度3",0),
	KD4("跨度4",0),
	KD5("跨度5",0),
	KD6("跨度6",0),
	KD7("跨度7",0),
	KD8("跨度8",0),
	KD9("跨度9",0),
	HZ("和值",0),
	HZ0("和值0",0),
	HZ1("和值1",0),
	HZ2("和值2",0),
	HZ3("和值3",0),
	HZ4("和值4",0),
	HZ5("和值5",0),
	HZ6("和值6",0),
	HZ7("和值7",0),
	HZ8("和值8",0),
	HZ9("和值9",0),
	HZ10("和值10",0),
	HZ11("和值11",0),
	HZ12("和值12",0),
	HZ13("和值13",0),
	HZ14("和值14",0),
	HZ15("和值15",0),
	HZ16("和值16",0),
	HZ17("和值17",0),
	HZ18("和值18",0),
	HZ19("和值19",0),
	HZ20("和值20",0),
	HZ21("和值21",0),
	HZ22("和值22",0),
	HZ23("和值23",0),
	HZ24("和值24",0),
	HZ25("和值25",0),
	HZ26("和值26",0),
	HZ27("和值27",0),
	HZDA("和值大",0),
	HZX("和值小",0),
	HZDAN("和值单",0),
	HZS("和值双",0),
	FS("复式",0),
	FSSANM("复式三码", 0),
	FSSIM("复式四码", 0),
	FSWM("复式五码", 0),
	FSLM("复式六码", 0),
	FSQIM("复式七码", 0),
	FSBM("复式八码", 0),
	FSJM("复式九码", 0);

	private static final Map<Integer,TypeEnum> ENUM_MAP = new HashMap<>();
	private final String label;
	private int val;

	static {
		int no = 1;
		for(TypeEnum typeEnum:TypeEnum.values()) {
			typeEnum.setVal(no);
			no = no + 1;
		}

		for (TypeEnum typeEnum : TypeEnum.values()) {
			ENUM_MAP.put(typeEnum.getVal(),typeEnum);
		}
	}

	TypeEnum(String label,int val) {
		this.val = val;
		this.label = label;
	}

	public int getVal() {
		return val;
	}

	public String getLabel() {
		return label;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public static TypeEnum getEnumByVal(int val) {
		if (Objects.isNull(val)) {
			return null;
		}
		return ENUM_MAP.get(val);
	}

	public static String getLabelByVal(int val) {
		TypeEnum typeEnum = getEnumByVal(val);
		if (Objects.isNull(typeEnum)) {
			return null;
		}
		return typeEnum.getLabel();
	}

}
