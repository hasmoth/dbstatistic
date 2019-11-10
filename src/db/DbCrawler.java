package db;
import java.text.ParseException;

public class DbCrawler {
	
	public static void main(String[] args) throws ParseException {
		
		ConnectorFactory connFactory = new ConnectorFactory();
		DbConnector conn = connFactory.getConnector("SQLITE");
		
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
