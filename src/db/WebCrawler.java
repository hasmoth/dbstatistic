package db;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author thomas
 *
 */
public class WebCrawler {
	// date and time formats
	static private SimpleDateFormat tformat = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat dformat = new SimpleDateFormat ("dd.MM.yy");
	Date dNow = new Date();
	
	private Document doc_;
	private Elements ele_;
	private String station_;
	private String eva_;
	private Vector<TrainInstance> instances_ = new Vector<TrainInstance>();
	private Vector<TrainInstance> tmp_ = new Vector<TrainInstance>();
	final private String csv_ = "/home/thomas/documents/java-workspace/DBStatistic/D_Bahnhof_2017_09.csv";
	
	public WebCrawler() {
		this.station_ = "Frankfurt(Main)Hbf";
		this.eva_ = "8000105";
	}
	public WebCrawler (String station) {
		this.station_ = station;
		this.eva_ = getEVA(station_);
	}
	
	/**
	 * Invokes the actual parser,
	 * returns if object wasn't instantiated correctly. 
	 */
	public void pullData() {
		if (this.station_.isEmpty() || this.eva_.isEmpty()) return;
		try {
			parsePage();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Getter for collected instances
	 * @return vector of TrainInstance
	 */
	public Vector<TrainInstance> getInstances() {
		return instances_;
	}
	
	@Override
	public String toString() {
		String o = new String();
		for (TrainInstance instance : instances_) {
			o = o + instance + "\n";
		}
		return "WebCrawler [\n" + station_ + " " + new Date() + "\n" + o + "]";
	}
	// privates
	private void parsePage() throws InterruptedException {
		dNow = new Date();
		String nowDate = dformat.format(dNow);
		String nowTime = tformat.format(dNow);
		try {
			doc_ = Jsoup.connect(
					"https://reiseauskunft.bahn.de/bin/bhftafel.exe/dn?ld=15082&country=DEU&protocol=https:&seqnr=2&ident=fx.02634082.1469799492&rt=1"
					+ "&input=" + station_ + "%" + eva_
					+ "&time=" + nowTime + "&date=" + nowDate
					+ "&ld=15082&productsFilter=1111100000&start=1&boardType=dep&")
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:47.0) Gecko/20100101 Firefox/47.0").get();
		} catch (IOException e) {
			System.out.println("io - " + e);
		}
		ele_ = doc_.select("tr[id~=journeyRow]");
		try {
			parseElements();
		} catch (ParseException e) {
			System.out.println("parsePage - " + e);
		}
		Thread.sleep(60000);
	}
	private void parseElements() throws ParseException {
		TrainType tt;
		Date org = new Date();
		Date ris = new Date();
		String reason = new String("");
		String terminus = new String();
		for(Element elem : ele_) { // loop Elements and get basic types
			if (elem != null) {
				tt = new TrainType(elem.getElementsByClass("train").text());
				try {
					org = tformat.parse(elem.getElementsByClass("time").text());
				} catch (ParseException e) {
					System.out.println("parseElements - org " + e);
				}
				terminus = elem.getElementsByClass("route").select("span").first().getElementsByClass("bold").text();

				ArrayList<String> tmp = parseRis(elem.getElementsByClass("ris"));
				if (tmp.size() > 0) {
					try {
						ris = tformat.parse(tmp.get(0));
					} catch (ParseException e) {
						ris = org;
						reason = tmp.get(0);
					}
					if (tmp.size() == 2)
						reason = tmp.get(1);
				} else {
					ris = org;
				}
				TrainInstance ti = new TrainInstance(dNow, tt, org, ris, terminus, reason, station_);
				// TODO: if a delayed train has left the station, it's actual arrival time might be
				// reset to the original arrival time resulting in a zero delay. Hence we must be 
				// mindful of duplicates!
				int i = tmp_.indexOf(ti);
				if (i < 0)
					instances_.add(ti);
				else {
					// add only if dly hasn't suddenly changed after the scheduled time has passed
					if(!(tmp_.get(i).getDelay() > 0 && ti.getDelay() == 0) && (dNow.compareTo(org) > 0))
						instances_.add(ti);
				}
				reason = "";
			}
		}
		// copy collected instances to tmp_
		tmp_ = instances_;
	}
	private ArrayList<String> parseRis(Elements ris) {
		ArrayList<String> tmp = new ArrayList<String>();
		// ris can contain a string HH:mm, a string HH:mm plus text, just text or nothing
		if (ris.select("span").size()>0 && !ris.select("span").first().text().isEmpty()) {
			tmp.add(ris.select("span").first().text());
			if (ris.select("span").size()==2)
				tmp.add(ris.select("span").last().text());
		}
		return tmp;
	}
	private String getEVA(String station) {
		BufferedReader br = null;
		String line = "";
        String cvsSplitBy = ";";
        String eva = new String();
	
		try {
            br = new BufferedReader(new FileReader(csv_));
            while ((line = br.readLine()) != null) {
                String[] entry = line.split(cvsSplitBy);
                if (entry[3].equals(station)) {
                	eva = entry[0];
                	System.out.println("EVA [station=" + entry[3] + " , eva=" + entry[0] + "]");
                	break;
                }
            }
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return eva;
	}
}
