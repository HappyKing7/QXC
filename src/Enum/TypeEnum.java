package Enum;

public enum TypeEnum {
	ZX("直选", 1),
	ZL("组六", 2),
	ZS("组三", 3),
	DD("独胆", 4),
	SFZL("双飞组六", 5),
	SFZS("双飞组三", 6),
	YMDW("一码定位", 7),
	EMDW("二码定位", 8),
	BZQB("豹子全包", 9),
	ZLSM("组六四码", 10),
	ZLWM("组六五码", 11),
	ZLLM("组六六码", 12),
	ZLQIM("组六七码", 13),
	ZLBM("组六八码", 14),
	ZLJM("组六九码", 15),
	ZLQM("组六全包", 16),
	ZSEM("组三二码", 17),
	ZSSANM("组三三码", 18),
	ZSSIM("组三四码", 19),
	ZSWM("组三五码", 20),
	ZSLM("组三六码", 21),
	ZSQIM("组三七码", 22),
	ZSBM("组三八码", 23),
	ZSJM("组三九码", 24),
	ZSQM("组三全包", 25),
	KDL("跨度0", 26),
	KDEL("跨度2", 26),
	KDSANL("跨度3", 26),
	KDSIL("跨度4", 26),
	KDWL("跨度5", 26),
	KDLL("跨度6", 26),
	KDQL("跨度7", 26),
	KDBL("跨度8", 26),
	KDJL("跨度9", 26);

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
