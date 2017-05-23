package logic;

/**
 * Created by Steffen on 22.05.2017.
 */
public class WebServiceResult {

    private String name;
    private int hits;
    private int recall;
    private int precision;
    private int fmeasure;


    public WebServiceResult(String name, int hits) {
        this.name = name;
        this.hits = hits;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getRecall() {
        return recall;
    }

    public void setRecall(int recall) {
        this.recall = recall;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getFmeasure() {
        return fmeasure;
    }

    public void setFmeasure(int fmeasure) {
        this.fmeasure = fmeasure;
    }
}
