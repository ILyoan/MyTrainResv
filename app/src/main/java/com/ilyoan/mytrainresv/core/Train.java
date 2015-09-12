package com.ilyoan.mytrainresv.core;

public class Train {
	public String no;
	public String type;
	public String group;
	public String fromStation;
	public String fromDate;
	public String fromTime;
	public String toStation;
	public String toDate;
	public String toTime;
	public String runDate;
	public boolean hasSpecial;
	public boolean hasNormal;

	public Train(String no, String type, String group,
				 String fromStation, String fromDate, String fromTime,
			     String toStation, String toDate, String toTime, String runDate,
				 boolean hasSpecial, boolean hasNormal) {
		this.no = no;
		this.type = type;
		this.group = group;
		this.fromStation = fromStation;
		this.fromDate = fromDate;
		this.fromTime = fromTime;
		this.toStation = toStation;
		this.toDate = toDate;
		this.toTime = toTime;
		this.runDate = runDate;
		this.hasSpecial = hasSpecial;
		this.hasNormal = hasNormal;
	}

	public String TrainType() {
		if (this.type.equals("00")) return "KTX";
		if (this.type.equals("01")) return "새마을";
		if (this.type.equals("02")) return "무궁화";
		if (this.type.equals("03")) return "통근";
		if (this.type.equals("04")) return "누리로";
		if (this.type.equals("05")) return "전체";
		if (this.type.equals("06")) return "공항";
		if (this.type.equals("07")) return "산천";
		if (this.type.equals("09")) return "ITX";
		return this.type;
	}
	@Override
	public String toString() {
		Station station = Station.getInstance();
		return String.format("No:%s %s %s-%s-%s\n%s(%s:%s) -> %s(%s:%s) %s %s",
				this.no,
				TrainType(),
				this.fromDate.substring(0, 4),
				this.fromDate.substring(4, 6),
				this.fromDate.substring(6, 8),
				station.getName(this.fromStation),
				this.fromTime.substring(0, 2),
				this.fromTime.substring(2, 4),
				station.getName(this.toStation),
				this.toTime.substring(0, 2),
				this.toTime.substring(2, 4),
				this.hasNormal ? "일반실" : "",
				this.hasSpecial ? "특실" : ""
		);
	}
}
