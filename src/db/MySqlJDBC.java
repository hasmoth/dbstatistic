package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySqlJDBC extends DbConnector {
	public MySqlJDBC() {
		// TODO Auto-generated constructor stub
	}
	
	public MySqlJDBC(String dbName) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void insertRow(TrainInstance train) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String fetchRow() {
		// TODO Auto-generated method stub
		return null;
	}
}
