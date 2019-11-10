package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLiteJDBC extends DbConnector {
    protected Connection c = null;
    protected String dbName = "data/dbstat.db";
    private Statement stmt = null;

    /**
     * Default Constructor
     */
    public SQLiteJDBC() {
        super();
        dbConnector(dbName);
        createTable(c);
    }

    public SQLiteJDBC(String name) {
        super();
        dbConnector(name);
        createTable(c);
    }

    private void dbConnector(String name) {
        if (c != null)
            return;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + name);
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
                        + " (LINE     TEXT(10)  NOT NULL, "
                        + " TERM      TEXT(50)  NOT NULL, "
                        + " STATION   TEXT(50)  NOT NULL, "
                        + " TIME      TEXT(10)  NOT NULL, "
                        + " DATE      DATE      NOT NULL, " 
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
        // dbConnector();
    	// TODO: SELECT line,delay FROM DBSTAT WHERE line LIKE 'S%';
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM DBSTAT;");
            ArrayList<Integer> delay_ = new ArrayList<>();
            while (rs.next()) {
                delay_.add(rs.getInt("DELAY"));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return "";
    }

}
