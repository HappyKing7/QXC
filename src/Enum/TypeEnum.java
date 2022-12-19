package Enum;

public enum TypeEnum {
	ZHIXUAN("直选", 1),
	ZUXUAN("组选", 2),
	ZL("组六", 3),
	ZS("组三", 4),
	DD("独胆", 5),
	SFZL("双飞组六", 6),
	SFZS("双飞组三", 7),
	YMDW("一码定位", 8),
	EMDW("二码定位", 9),
	BZQB("豹子全包", 10),
	ZLSM("组六四码", 12),
	ZLWM("组六五码", 13),
	ZLLM("组六六码", 14),
	ZLQIM("组六七码", 15),
	ZLBM("组六八码", 16),
	ZLJM("组六九码", 17),
	ZLQM("组六全包", 18),
	ZSEM("组三二码", 19),
	ZSSANM("组三三码", 20),
	ZSSIM("组三四码", 21),
	ZSWM("组三五码", 22),
	ZSLM("组三六码", 23),
	ZSQIM("组三七码", 24),
	ZSBM("组三八码", 25),
	ZSJM("组三九码", 26),
	ZSQM("组三全包", 27),
	KDL("跨度0", 28),
	KDEL("跨度2", 29),
	KDSANL("跨度3", 30),
	KDSIL("跨度4", 31),
	KDWL("跨度5", 32),
	KDLL("跨度6", 33),
	KDQL("跨度7", 34),
	KDBL("跨度8", 35),
	KDJL("跨度9", 36);

	private String label;
	private final int val;

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
}
