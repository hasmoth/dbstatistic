package db;

/**
 * Factory class to implement connectors to specific databases.
 * @author atoms.h
 * 
 */
public class ConnectorFactory {
	public DbConnector getConnector(String type) {
		if (type == null) return null;
		if (type.equalsIgnoreCase("SQLITE")) {
			return new SQLiteJDBC();
		} else if (type.equalsIgnoreCase("MYSQL")) {
			return new MySqlJDBC();
		}
		return null;
	}
	public DbConnector getConnector(String type, String dbName) {
		if (type == null) return null;
		if (type.equalsIgnoreCase("SQLITE")) {
			return new SQLiteJDBC(dbName);
		} else if (type.equalsIgnoreCase("MYSQL")) {
			return new MySqlJDBC(dbName);
		}
		return null;
	}
}
