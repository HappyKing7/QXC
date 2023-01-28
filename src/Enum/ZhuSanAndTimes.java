package Enum;

public enum ZhuSanAndTimes {
	ZHUSANONE("组三1倍","组三","1倍",1),
	ZHUSANTWO("组三2倍","组三","2倍",2),
	ZHUSANTHREE("组三3倍","组三","3倍",3),
	ZHUSANFOUR("组三4倍","组三","4倍",4),
	ZHUSANFIVE("组三5倍","组三","5倍",5),
	ZHUSANSIX("组三6倍","组三","6倍",6),
	ZHUSANSEVEN("组三7倍","组三","7倍",7),
	ZHUSANEIGHT("组三8倍","组三","8倍",8),
	ZHUSANNINE("组三9倍","组三","9倍",9),
	ZHUSANTEN("组三10倍","组三","10倍",10);


	private final String desc;
	private final String type;
	private final String timesDesc;
	private final Integer times;

	ZhuSanAndTimes(String desc,String type,String timesDesc,Integer times) {
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
