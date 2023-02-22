package Enum;

public enum ZhiXuanAndTimesEnum {
	ZHIXUANONE("直选1单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.ONE.getLabel(),1),
	ZHIXUANTWO("直选2单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.TOW.getLabel(),2),
	ZHIXUANTHREE("直选3单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.THREE.getLabel(),3),
	ZHIXUANFOUR("直选4单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.FOUR.getLabel(),4),
	ZHIXUANFIVE("直选5单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.FIVE.getLabel(),5),
	ZHIXUANSIX("直选6单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.SIX.getLabel(),6),
	ZHIXUANSEVEN("直选7单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.SEVEN.getLabel(), 7),
	ZHIXUANEIGHT("直选8单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.EIGHT.getLabel(), 8),
	ZHIXUANNINE("直选9单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.NINE.getLabel(), 9),
	ZHIXUANTEN("直选10单",TypeEnum.ZHIXUAN.getLabel(),TimesEnum.TEN.getLabel(), 10);


	private final String desc;
	private final String type;
	private final String timesDesc;
	private final Integer times;

	ZhiXuanAndTimesEnum(String desc, String type, String timesDesc, Integer times) {
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
