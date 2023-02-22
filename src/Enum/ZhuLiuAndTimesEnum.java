package Enum;

public enum ZhuLiuAndTimesEnum {
	ZHULIUONE("组六1倍",TypeEnum.ZL.getLabel(),TimesEnum.ONE.getLabel(),1),
	ZHULIUWO("组六2倍",TypeEnum.ZL.getLabel(),TimesEnum.TOW.getLabel(),2),
	ZHULIUTHREE("组六3倍",TypeEnum.ZL.getLabel(),TimesEnum.THREE.getLabel(),3),
	ZHULIUFOUR("组六4倍",TypeEnum.ZL.getLabel(),TimesEnum.FOUR.getLabel(),4),
	ZHULIUFIVE("组六5倍",TypeEnum.ZL.getLabel(),TimesEnum.FIVE.getLabel(),5),
	ZHULIUSIX("组六6倍",TypeEnum.ZL.getLabel(),TimesEnum.SIX.getLabel(),6),
	ZHULIUSEVEN("组六7倍",TypeEnum.ZL.getLabel(),TimesEnum.SEVEN.getLabel(),7),
	ZHULIUEIGHT("组六8倍",TypeEnum.ZL.getLabel(),TimesEnum.EIGHT.getLabel(),8),
	ZHULIUNINE("组六9倍",TypeEnum.ZL.getLabel(),TimesEnum.NINE.getLabel(),9),
	ZHULIUTEN("组六10倍",TypeEnum.ZL.getLabel(),TimesEnum.TEN.getLabel(),10);


	private final String desc;
	private final String type;
	private final String timesDesc;
	private final Integer times;

	ZhuLiuAndTimesEnum(String desc, String type, String timesDesc, Integer times) {
		this.desc = desc;
		this.type = type;
		this.timesDesc = timesDesc;
		this.times = times;
	}

	public String getDesc() {
		return desc;
	}

	public String getType() {
		return type;
	}

	public String getTimesDesc() {
		return timesDesc;
	}

	public Integer getTimes() {
		return times;
	}
}
