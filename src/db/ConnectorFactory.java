package db;

import java.io.IOException;
import java.util.Properties;

/**
 * Factory class to implement connectors to specific databases.
 * @author hasmoth
 * 
 */
public class ConnectorFactory {
	private DbConnector connector_ = null;
	private Properties prop = new Properties();
	private String dbmode = new String();
	private String dbname = new String();
	private String dbaddr = new String();
	
	private void initConnectorFactory() {
		try {
			prop = new PropertyValues().getPropValues();
		} catch (IOException e) {
			System.out.println("Exception: " + e);
		}
		dbmode = prop.getProperty("dbmode");
		dbname = prop.getProperty("dbname");
		dbaddr = prop.getProperty("dbaddr");
		System.out.println(dbmode + " " + dbname + " " + dbaddr);
	}
	public DbConnector getConnector() throws NullPointerException {
		initConnectorFactory();
		if (dbmode.equalsIgnoreCase("SQLITE")) {
			connector_ = new SQLiteJDBC(dbname);
		} else if (dbmode.equalsIgnoreCase("MYSQL")) {
			connector_ = new MySqlJDBC(dbname, dbaddr);
		}
		if (connector_ == null) throw new NullPointerException("DB connection failed");
		return connector_;
	}
}
