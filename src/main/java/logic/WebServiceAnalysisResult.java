package logic;
/**
 * Created by Steffen on 19.05.2017.
 */
public class WebServiceAnalysisResult {

    private int hits;


    public WebServiceAnalysisResult() {}

    public WebServiceAnalysisResult(int hits) {
        this.hits = hits;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
}
