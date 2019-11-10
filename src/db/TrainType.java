package db;

public class TrainType {
	private int line_;
	private TRAIN train_;
	
	public TrainType() {
		line_ = 0;
		train_ = TRAIN.NO_KNOWN_TYPE;
	}
	
	public TrainType(String type) {
		String[] input = type.split(" ");
		if (input.length > 1)
			try {
				line_ = Integer.parseInt(input[1]);
			} catch (NumberFormatException e) {
				String[] tmp = input[1].split("(?<=\\D)(?=\\d)");
				line_ = Integer.parseInt(tmp[1]);
			}
		train_ = convertType(input[0]);
	}
	public enum TRAIN {
		S,
		RB,
		RE,
		VIA,
		IC,
		ICE,
		NO_KNOWN_TYPE
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

	private TRAIN convertType(String type) {
		TRAIN tt = TRAIN.NO_KNOWN_TYPE;
		
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
			case "ICE":
				tt = TRAIN.ICE;
				break;
			default:
				tt = TRAIN.NO_KNOWN_TYPE;
		}
		return tt;
	}
}
