package Enum;

public enum ZhuXuanAndTimesEnum {
	ZHUXUANONE("组选1组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.ONE.getLabel(),1),
	ZHUXUANTWO("组选2组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.TOW.getLabel(),2),
	ZHUXUANTHREE("组选3组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.THREE.getLabel(),3),
	ZHUXUANFOUR("组选4组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.FOUR.getLabel(),4),
	ZHUXUANFIVE("组选5组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.FIVE.getLabel(),5),
	ZHUXUANSIX("组选6组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.SIX.getLabel(),6),
	ZHUXUANSEVEN("组选7组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.SEVEN.getLabel(),7),
	ZHUXUANEIGHT("组选8组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.EIGHT.getLabel(),8),
	ZHUXUANNINE("组选9组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.NINE.getLabel(),9),
	ZHUXUANTEN("组选10组",TypeEnum.ZUXUAN.getLabel(),TimesEnum.TEN.getLabel(),10);

	private final String desc;
	private final String type;
	private final String timesDesc;
	private final Integer times;

	ZhuXuanAndTimesEnum(String desc, String type, String timesDesc, Integer times) {
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
