package com.ilyoan.mytrainresv.core;

public class Train {
	public String no;
	public String type;
	public String fromStation;
	public String fromDate;
	public String fromTime;
	public String toStation;
	public String toDate;
	public String toTime;
	public boolean hasSpecial;
	public boolean hasNormal;

	public Train(String no, String type, String fromStation, String fromDate, String fromTime,
			String toStation, String toDate, String toTime, boolean hasSpecial, boolean hasNormal) {
		this.no = no;
		this.type = type;
		this.fromStation = fromStation;
		this.fromDate = fromDate;
		this.fromTime = fromTime;
		this.toStation = toStation;
		this.toDate = toDate;
		this.toTime = toTime;
		this.hasSpecial = hasSpecial;
		this.hasNormal = hasNormal;
	}

	@Override
	public String toString() {
		Station station = Station.getInstance();
		return String.format("Train No:%s Type:%s [%s](%s %s) -> [%s](%s %s) S:%s, N:%s",
				this.no,
				this.type,
				station.getName(this.fromStation),
				this.fromDate,
				this.fromTime,
				station.getName(this.toStation),
				this.toDate,
				this.toTime,
				(this.hasSpecial ? "Y" : "N"),
				(this.hasNormal ? "Y" : "N"));
	}
}
