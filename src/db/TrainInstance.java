package db;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

public class TrainInstance {
	// date and time formats
	public static SimpleDateFormat time = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat date = new SimpleDateFormat ("dd.MM.yy");
	private SimpleDateFormat mysqldate = new SimpleDateFormat ("yyyy-MM-dd");

	private Date date_;
	private TrainType type_ = new TrainType(); // line
	private Date org_; // scheduled arrival time
	private Date act_; // suggested arrival time
	private long dly_;  // delay [Minutes]
	private String reason_; // optional reason phrase
	private String terminus_;
	private String station_; // station of request
	
	public TrainInstance(Date date, TrainType type, Date org, Date act, String terminus, String reason, String station) {
		this.date_ = date;
		this.type_ = type;
		this.org_ = org;
		this.act_ = act;
		this.dly_ = parseDelay_(act, org);
		this.terminus_ = terminus;
		this.reason_ = reason;
		this.station_ = station;
	}
	// Special CTOR
	public TrainInstance(Date date, String type, String org_time, String act_time, String terminus, String reason, String station) throws ParseException {
		this(date, new TrainType(type), time.parse(org_time), time.parse(act_time), terminus, reason, station);
	}
	
	public TrainInstance getTrainInstance() {
		return this;
	}
	
	public void setTrainInstance(Date date, TrainType type, Date org, Date act, int dly, String terminus, String station) {
		this.date_ = date;
		this.type_ = type;
		this.org_ = org;
		this.act_ = act;
		this.dly_ = parseDelay_(act, org);
		this.terminus_ = terminus;
		this.station_ = station;
	}
	
	public String getDBString() {
		// reflects DB table LINE,TERM,STATION,TIME,DATE,DELAY,REASON
		return "'" + type_ + "','" + terminus_ + "','" + station_ + "','" + time.format(org_) + "','" + mysqldate.format(date_) + "'," + dly_ + ",'" + reason_ + "'";
	}
	
	public long getDelay() {
		return this.dly_;
	}
	
	@Override
	public String toString() {
		return "TrainInstance [date_=" + date.format(date_) + 
				", type_=" + type_ + 
				", org_=" + time.format(org_) + 
				", act_=" + time.format(act_) + 
				", dly_=" + dly_ + 
				", terminus_=" + terminus_ + 
				", reason=" + reason_ +
				", station=" + station_ +
				"]";
	}
	@Override
    public boolean equals(Object obj) {
		if (this == obj) return true;
		//if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		TrainInstance other = (TrainInstance) obj;
		if (!date.format(other.date_).equals(date.format(this.date_))) return false;
		if (!other.type_.equals(this.type_)) return false;
		if (!time.format(other.org_).equals(time.format(this.org_)))return false;
		if (!other.station_.equals(this.station_)) return false;
		if (!other.terminus_.equals(this.terminus_)) return false;
		
		return true;
	}
	// private
	/*
	 * return difference in minutes
	 */
	private long parseDelay_(Date act, Date org) {
		long delay_ = 0;
		delay_ = act.getTime() - org.getTime();
		if (delay_ < 0)
			delay_ = delay_ + 24*60*60*1000;
		return delay_/1000/60; // [Minutes]
	}
}
