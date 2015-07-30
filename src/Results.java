
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author julio.janeiro
 */
public class Results {

    private List<Double> baseline;
    private List<Double> classification;
    private List<Double> regression;
    private Integer count;

    public Results(List<Double> baseline, List<Double> classification) {
        this.baseline = baseline;
        this.classification = classification;
        count = 1;
    }

    public Results() {
        this.baseline = new ArrayList<>();
        this.classification = new ArrayList<>();
        this.regression = new ArrayList<>();
        count = 0;
    }

    public List<Double> getRegression() {
        return regression;
    }
    
    public void setRegression(List<Double> regression) {
        this.regression = regression;
    }

    public void add(Results res) {
        if (baseline.isEmpty() && classification.isEmpty() && regression.isEmpty()) {
            baseline = res.baseline;
            classification = res.classification;
            regression = res.regression;
        } else {
            for (int i = 0; i < baseline.size(); i++) {
                baseline.set(i, baseline.get(i) + res.baseline.get(i));
            }
            for (int i = 0; i < classification.size(); i++) {
                classification.set(i, classification.get(i) + res.classification.get(i));
            }
            for (int i = 0; i < regression.size(); i++) {
                regression.set(i, regression.get(i) + res.regression.get(i));
            }
        }
        count++;
    }

    @Override
    public String toString() {
        String text = "";
        for (Double d : classification) {
            text += String.format("%.2f", (d/count)*100) + "%\n";
        }
        for (Double d : regression) {
            text += String.format("%.2f", (d/count)*100) + "%\n";
        }
        text += "\n";
        for (Double d : baseline) {
            text += String.format("%.2f", (d/count)*100) + "%\n";
        }
        return text;
    }

}
