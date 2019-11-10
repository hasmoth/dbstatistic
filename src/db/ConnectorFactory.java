package db;

/**
 * Factory class to implement connectors to specific databases.
 * @author hasmoth
 * 
 */
public class ConnectorFactory {
	private DbConnector connector_ = null;
	public DbConnector getConnector(String type) throws NullPointerException {
		if (type == null) return null;
		if (type.equalsIgnoreCase("SQLITE")) {
			connector_ = new SQLiteJDBC();
		} else if (type.equalsIgnoreCase("MYSQL")) {
			connector_ = new MySqlJDBC();
		}
		if (connector_ == null) throw new NullPointerException("DB connection failed");
		return connector_;
	}
	public DbConnector getConnector(String type, String dbName) throws NullPointerException {
		if (type == null) return null;
		if (type.equalsIgnoreCase("SQLITE")) {
			connector_ = new SQLiteJDBC(dbName);
		} else if (type.equalsIgnoreCase("MYSQL")) {
			connector_ = new MySqlJDBC(dbName);
		}
		if (connector_ == null) throw new NullPointerException("DB connection failed");
		return connector_;
	}
}
