package Enum;

public enum ZhuLiuAndTimes {
	ZHULIUONE("组六1倍","组六","1倍",1),
	ZHULIUWO("组六2倍","组六","2倍",2),
	ZHULIUTHREE("组六3倍","组六","3倍",3),
	ZHULIUFOUR("组六4倍","组六","4倍",4),
	ZHULIUFIVE("组六5倍","组六","5倍",5),
	ZHULIUSIX("组六6倍","组六","6倍",6),
	ZHULIUSEVEN("组六7倍","组六","7倍",7),
	ZHULIUEIGHT("组六8倍","组六","8倍",8),
	ZHULIUNINE("组六9倍","组六","9倍",9),
	ZHULIUTEN("组六10倍","组六","10倍",10);


	private final String desc;
	private final String type;
	private final String timesDesc;
	private final Integer times;

	ZhuLiuAndTimes(String desc,String type,String timesDesc,Integer times) {
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
