package Enum;

public enum ZhiXuanAndTimes {
	ZHIXUANONE("直选1单","直选","1倍",1),
	ZHIXUANTWO("直选2单","直选","2倍",2),
	ZHIXUANTHREE("直选3单","直选","3倍",3),
	ZHIXUANFOUR("直选4单","直选","4倍",4),
	ZHIXUANFIVE("直选5单","直选","5倍",5),
	ZHIXUANSIX("直选6单","直选","6倍",6),
	ZHIXUANSEVEN("直选7单","直选","7倍",7),
	ZHIXUANEIGHT("直选8单","直选","8倍",8),
	ZHIXUANNINE("直选9单","直选","9倍",9),
	ZHIXUANTEN("直选10单","直选","10倍",10);


	private final String desc;
	private final String type;
	private final String timesDesc;
	private final Integer times;

	ZhiXuanAndTimes(String desc,String type,String timesDesc,Integer times) {
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
