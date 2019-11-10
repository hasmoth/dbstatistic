package db;

public abstract class DbConnector {
	public abstract void insertRow(TrainInstance train);
	public abstract String fetchRow();
}
