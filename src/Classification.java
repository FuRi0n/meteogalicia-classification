
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author julio.janeiro
 */
public class Classification {

    Classifier cls;
    String file;
    Integer folds;
    Integer seed;

    public Classification(Classifier cls, String file, Integer folds, Integer seed) {
        this.cls = cls;
        this.file = file;
        this.folds = folds;
        this.seed = seed;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Double classify() {
        try {
            // load data
            Instances data = ConverterUtils.DataSource.read(file);
            data.setClassIndex(data.numAttributes() - 1);
            // perform cross-validation
            Evaluation eval = new Evaluation(data);
            eval.crossValidateModel(cls, data, folds, new Random(seed));
            return eval.pctCorrect()/(double)100;
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void saveModel() {
        try {
            Classifier clsCopy = Classifier.makeCopy(cls);
            Instances data = ConverterUtils.DataSource.read(file);
            data.setClassIndex(data.numAttributes() - 1);
            clsCopy.buildClassifier(data);
            SerializationHelper.write(file.split("\\.")[0] + ".model", clsCopy);
        } catch (Exception ex) {
            Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
