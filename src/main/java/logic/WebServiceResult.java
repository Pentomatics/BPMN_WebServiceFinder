package logic;

/**
 * Created by Steffen on 22.05.2017.
 */
public class WebServiceResult {

    private String URL,name;
    private int hits;
    private double recall,precision,fmeasure;
    
	public WebServiceResult(String URL, int hits) {
        this.URL = URL;        
        this.hits = hits;
        
        name = URL.substring(0, URL.indexOf(".asmx"));
        name = name.substring(name.lastIndexOf("/")+1);
    }

    public String getURL() {
        return URL;
    }    
    
    public String getName(){
    	return name;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getFmeasure() {
		return fmeasure;
	}

	public void setFmeasure(double fmeasure) {
		this.fmeasure = fmeasure;
	}
}
