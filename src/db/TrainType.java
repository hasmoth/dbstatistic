package db;

public class TrainType {
	private int line_;
	private TRAIN train_;
	
	public TrainType() {
		line_ = 0;
		train_ = TRAIN.N_K_T;
	}
	
	/**
	 * Try to split input string and convert to train type TRAIN.
	 * If we recognize the train type try to parse the line number.
	 * 
	 * @param type input string
	 */
	public TrainType(String type) {
		String[] input = type.split(" ");
		train_ = convertType(input[0]);
		if (input.length > 1 && train_!=TRAIN.N_K_T) {
			try {
				line_ = Integer.parseInt(input[1]);
			} catch (NumberFormatException e) {
				String[] tmp = input[1].split("(?<=\\D)(?=\\d)");
				if (tmp.length > 1)
					line_ = Integer.parseInt(tmp[1]);
			}
		}
	}
	public enum TRAIN {
		S,
		RB,
		RE,
		VIA,
		IC,
		EC,
		ICE,
		N_K_T
	}
	public void setTrainType(String type, int line) {
		train_ = convertType(type);
		line_ = line;
	}
	public TrainType getTrainType() {
		return this;
	}
	
	@Override
	public String toString() {
		return train_ + "" + line_;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		TrainType input = (TrainType) obj;
		return (input.train_ == this.train_) && (input.line_ == this.line_);
	}

	private TRAIN convertType(String type) {
		TRAIN tt = TRAIN.N_K_T;
		
		switch (type) {
			case "S":
				tt = TRAIN.S;
				break;
			case "RB":
				tt = TRAIN.RB;
				break;
			case "RE":
				tt = TRAIN.RE;
				break;
			case "VIA":
				tt = TRAIN.VIA;
				break;
			case "IC":
				tt = TRAIN.IC;
				break;
			case "EC":
				tt = TRAIN.EC;
				break;
			case "ICE":
				tt = TRAIN.ICE;
				break;
			default:
				tt = TRAIN.N_K_T;
		}
		return tt;
	}
}
