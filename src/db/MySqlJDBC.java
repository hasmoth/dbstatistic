package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySqlJDBC extends DbConnector {
	protected Connection c = null;
    protected String dbName = "localhost/dbstat";
    private Statement stmt = null;
	
	public MySqlJDBC() {
		dbConnector(dbName);
		createTable(c);
	}
	
	public MySqlJDBC(String name) {
		dbConnector(name);
		createTable(c);
	}

	@Override
    public void insertRow(TrainInstance train) {
        try {
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = " REPLACE INTO DBSTAT (LINE,TERM,STATION,TIME,DATE,DELAY,REASON) "
                    + " VALUES (" + train.getDBString() + ");";

            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        } catch (Exception e) {
            System.err.println("insertRow: " + e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
	@Override
	public String fetchRow() {
		// TODO Auto-generated method stub
		return null;
	}
	// privates
    private void dbConnector(String name) {
        if (c != null)
            return;
        try {
            c = DriverManager.getConnection("jdbc:mysql://" + name + "?"
                    + "user=sqluser&password=sqluserpw");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully!");
    }
    private void createTable(Connection db) {
        try {
            if (db != null) {
                stmt = db.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS DBSTAT "
                        + " (LINE     VARCHAR(10)  	NOT NULL, "
                        + " TERM      TEXT(50)  	NOT NULL, "
                        + " STATION   TEXT(50)  	NOT NULL, "
                        + " TIME      VARCHAR(10)  	NOT NULL, "
                        + " DATE      DATE      	NOT NULL, " 
                        + " DELAY     INT, "
                        + " REASON    TEXT(50), "
                        + " PRIMARY KEY (DATE, TIME, LINE));";
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
