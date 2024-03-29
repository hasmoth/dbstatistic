package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

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
        dbConnector("data/"+name+".db");
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
        System.out.println(getClass().getName() + ": Opened database successfully!");
    }

    private void createTable(Connection db) {
        try {
            if (db != null) {
                stmt = db.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS DBSTAT "
                        + " (LINE     TEXT(10)  NOT NULL, "
                        + " TERM      TEXT(50)  NOT NULL, "
                        + " STATION   TEXT(50)  NOT NULL, "
                        + " OTIME     TEXT(10)  NOT NULL, "
                        + " ODATE     DATE      NOT NULL, " 
                        + " DELAY     INT, "
                        + " REASON    TEXT(50), "
                        + " 'INSERT'  TEXT(50), "
                        + " PRIMARY KEY (ODATE, OTIME, LINE));";
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            System.err.println("createTable: " + e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    @Override
    public void insertRow(TrainInstance train) {
        try {
        	Date now = new Date();
            c.setAutoCommit(false);

            stmt = c.createStatement();
            String sql = " REPLACE INTO DBSTAT (LINE,TERM,STATION,OTIME,ODATE,DELAY,REASON,'INSERT') "
                    + " VALUES (" + train.getDBString() + ",'" + now.toString() + "');";

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
