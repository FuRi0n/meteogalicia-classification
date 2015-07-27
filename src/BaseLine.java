/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author julio.janeiro
 */
public class BaseLine {

    private final String variable;
    private Integer correct;
    private Integer total;

    public BaseLine(String variable) {
        this.variable = variable;
        this.correct = 0;
        this.total = 0;
    }

    public void addCorrect() {
        this.correct++;
        this.total++;
    }

    public void addIncorrect() {
        this.total++;
    }

    public Integer getCorrect() {
        return correct;
    }

    public Integer getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return variable + ": " + (correct / (float) total) * 100 + "%";
    }
}
