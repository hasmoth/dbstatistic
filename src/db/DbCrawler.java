package db;
import java.text.ParseException;

public class DbCrawler {
	static volatile boolean keepRunning = true;
	
	public static void main(String[] args) throws ParseException {
				
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        keepRunning = false;
				System.out.println("Bye.");
		    }
		});

		ConnectorFactory connFactory = new ConnectorFactory();
		DbConnector conn = null;
		try {
			conn = connFactory.getConnector();
		} catch (NullPointerException e) {
			return;
		}
		
		WebCrawler crawl = new WebCrawler();
		while (true && keepRunning) {
			crawl.pullData();
			
			for (TrainInstance inst : crawl.getInstances()) {
				conn.insertRow(inst);
			}
			crawl.getInstances().clear();
		}
	}
}
