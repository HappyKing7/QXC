package Enum;

public enum ZhuXuanAndTimes {
	ZHUXUANONE("组选1组","组选","1倍",1),
	ZHUXUANTWO("组选2组","组选","2倍",2),
	ZHUXUANTHREE("组选3组","组选","3倍",3),
	ZHUXUANFOUR("组选4组","组选","4倍",4),
	ZHUXUANFIVE("组选5组","组选","5倍",5),
	ZHUXUANSIX("组选6组","组选","6倍",6),
	ZHUXUANSEVEN("组选7组","组选","7倍",7),
	ZHUXUANEIGHT("组选8组","组选","8倍",8),
	ZHUXUANNINE("组选9组","组选","9倍",9),
	ZHUXUANTEN("组选10组","组选","10倍",10);

	private String desc;
	private String type;
	private String timesDesc;
	private Integer times;

	ZhuXuanAndTimes(String desc,String type,String timesDesc,Integer times) {
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
