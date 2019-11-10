package db;
import java.text.ParseException;

public class DbCrawler {
	
	public static void main(String[] args) throws ParseException {
		
		ConnectorFactory connFactory = new ConnectorFactory();
		DbConnector conn = null;
		try {
			//conn = connFactory.getConnector("SQLITE");
			conn = connFactory.getConnector("MYSQL", "192.168.1.100/dbstat");
		} catch (NullPointerException e) {
			return;
		}
		
		WebCrawler crawl = new WebCrawler();
		while (true) {
			crawl.pullData();
			
			for (TrainInstance inst : crawl.getInstances()) {
				conn.insertRow(inst);
			}
			crawl.getInstances().clear();
		}
	}
}
