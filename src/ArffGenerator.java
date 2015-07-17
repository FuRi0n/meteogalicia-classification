
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author julio.janeiro
 */
public class ArffGenerator {

    public static final int CIELO = 0;
    public static final int VIENTO = 1;
    public static final int TEMPERATURA = 2;

    private List<Prediction> predictions;

    public ArffGenerator(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public void generateFiles(int type) {
        if (!predictions.isEmpty()) {
            if (type == CIELO) {
                //Headers
                FastVector morningAttrs = generateCieloHeaders(predictions.get(0).getMorning().getCieloMS().size());
                FastVector afternoonAttrs = generateCieloHeaders(predictions.get(0).getAfternoon().getCieloMS().size());
                FastVector nightAttrs = generateCieloHeaders(predictions.get(0).getNight().getCieloMS().size());
                //Data
                Instances morningData = generateCieloData(TimeFrame.MORNING, morningAttrs);
                Instances afternoonData = generateCieloData(TimeFrame.AFTERNOON, afternoonAttrs);
                Instances nightData = generateCieloData(TimeFrame.NIGHT, nightAttrs);
                //Save
                saveFile("EstadoCieloMañana.arff", morningData);
                saveFile("EstadoCieloTarde.arff", afternoonData);
                saveFile("EstadoCieloNoche.arff", nightData);
            } else if (type == VIENTO) {
                //Headers
                FastVector morningAttrs = generateVientoHeaders(predictions.get(0).getMorning().getModuloVientoMS().size());
                FastVector afternoonAttrs = generateVientoHeaders(predictions.get(0).getAfternoon().getModuloVientoMS().size());
                FastVector nightAttrs = generateVientoHeaders(predictions.get(0).getNight().getModuloVientoMS().size());
                //Data
                Instances morningData = generateVientoData(TimeFrame.MORNING, morningAttrs);
                Instances afternoonData = generateVientoData(TimeFrame.AFTERNOON, afternoonAttrs);
                Instances nightData = generateVientoData(TimeFrame.NIGHT, nightAttrs);
                //Save
                saveFile("VientoMañana.arff", morningData);
                saveFile("VientoTarde.arff", afternoonData);
                saveFile("VientoNoche.arff", nightData);
            } else {
                System.out.println("Error: Temperature, can't classify numeric values.");
            }
        }
    }

    private void saveFile(String name, Instances data) {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        try {
            saver.setFile(new File(name));
            saver.writeBatch();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private FastVector generateCieloHeaders(Integer size) {
        FastVector attrs = new FastVector();
        FastVector cieloMSVector = getCieloMSVector();
        for (int i = 1; i <= size; i++) {
            attrs.addElement(new Attribute("cieloMS" + i, cieloMSVector));
        }
        attrs.addElement(new Attribute("cieloMG", getCieloMGVector()));
        return attrs;
    }

    private FastVector generateVientoHeaders(Integer size) {
        FastVector attrs = new FastVector();
        FastVector moduloVientoMSVector = getModuloVientoMSVector();
        FastVector direccionVientoMSVector = getDireccionVientoMSVector();
        for (int i = 1; i <= size; i++) {
            attrs.addElement(new Attribute("moduloVientoMS" + i, moduloVientoMSVector));
            attrs.addElement(new Attribute("direccionVientoMS" + i, direccionVientoMSVector));
        }
        attrs.addElement(new Attribute("vientoMG", getVientoMGVector()));
        return attrs;
    }

    private Instances generateCieloData(Integer timeFrame, FastVector attrs) {
        Instances data = new Instances("Estado del cielo", attrs, 0);
        FastVector cieloMSVector = getCieloMSVector();
        FastVector cieloMGVector = getCieloMGVector();
        for (Prediction p : predictions) {
            double[] vals = new double[data.numAttributes()];
            if (timeFrame == TimeFrame.MORNING) {
                for (int i = 0; i < predictions.get(0).getMorning().getCieloMS().size(); i++) {
                    vals[i] = cieloMSVector.indexOf(p.getMorning().getCieloMS().get(i));
                }
                vals[vals.length - 1] = cieloMGVector.indexOf(String.valueOf(p.getMorning().getCieloMG()));
            } else if (timeFrame == TimeFrame.AFTERNOON) {
                for (int i = 0; i < predictions.get(0).getAfternoon().getCieloMS().size(); i++) {
                    vals[i] = cieloMSVector.indexOf(p.getAfternoon().getCieloMS().get(i));
                }
                vals[vals.length - 1] = cieloMGVector.indexOf(String.valueOf(p.getAfternoon().getCieloMG()));
            } else {
                for (int i = 0; i < predictions.get(0).getNight().getCieloMS().size(); i++) {
                    vals[i] = cieloMSVector.indexOf(p.getNight().getCieloMS().get(i));
                }
                vals[vals.length - 1] = cieloMGVector.indexOf(String.valueOf(p.getNight().getCieloMG()));
            }
            data.add(new Instance(1, vals));
        }
        return data;
    }

    private Instances generateVientoData(Integer timeFrame, FastVector attrs) {
        Instances data = new Instances("Viento", attrs, 0);
        FastVector moduloVientoMSVector = getModuloVientoMSVector();
        FastVector direccionVientoMSVector = getDireccionVientoMSVector();
        FastVector vientoMGVector = getVientoMGVector();
        for (Prediction p : predictions) {
            double[] vals = new double[data.numAttributes()];
            if (timeFrame == TimeFrame.MORNING) {
                for (int i = 0; i < predictions.get(0).getMorning().getModuloVientoMS().size(); i++) {
                    vals[i * 2] = moduloVientoMSVector.indexOf(p.getMorning().getModuloVientoMS().get(i));
                    vals[i * 2 + 1] = direccionVientoMSVector.indexOf(p.getMorning().getDireccionVientoMS().get(i));
                }
                vals[vals.length - 1] = vientoMGVector.indexOf(String.valueOf(p.getMorning().getVientoMG()));
            } else if (timeFrame == TimeFrame.AFTERNOON) {
                for (int i = 0; i < predictions.get(0).getAfternoon().getModuloVientoMS().size(); i++) {
                    vals[i * 2] = moduloVientoMSVector.indexOf(p.getAfternoon().getModuloVientoMS().get(i));
                    vals[i * 2 + 1] = direccionVientoMSVector.indexOf(p.getAfternoon().getDireccionVientoMS().get(i));
                }
                vals[vals.length - 1] = vientoMGVector.indexOf(String.valueOf(p.getAfternoon().getVientoMG()));
            } else {
                for (int i = 0; i < predictions.get(0).getNight().getModuloVientoMS().size(); i++) {
                    vals[i * 2] = moduloVientoMSVector.indexOf(p.getNight().getModuloVientoMS().get(i));
                    vals[i * 2 + 1] = direccionVientoMSVector.indexOf(p.getNight().getDireccionVientoMS().get(i));
                }
                vals[vals.length - 1] = vientoMGVector.indexOf(String.valueOf(p.getNight().getVientoMG()));
            }
            data.add(new Instance(1, vals));
        }
        return data;
    }

    private FastVector getCieloMSVector() {
        String[] values = {"SUNNY", "HIGH_CLOUDS", "PARTLY_CLOUDY", "OVERCAST", "CLOUDY",
            "FOG", "SHOWERS", "OVERCAST_AND_SHOWERS", "INTERMITENT_SNOW", "DRIZZLE", "RAIN",
            "SNOW", "STORMS", "MIST", "FOG_BANK", "MID_CLOUDS", "WEAK_RAIN", "WEAK_SHOWERS",
            "STORM_THEN_CLOUDY", "MELTED_SNOW", "RAIN HAIL"};
        FastVector cieloMSVector = new FastVector();
        for (String v : values) {
            cieloMSVector.addElement(v);
        }
        return cieloMSVector;
    }

    private FastVector getCieloMGVector() {
        String[] values = {"101", "102", "103", "104", "105", "106", "107", "108", "109",
            "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121"};
        FastVector cieloMGVector = new FastVector();
        for (String v : values) {
            cieloMGVector.addElement(v);
        }
        return cieloMGVector;
    }

    private FastVector getModuloVientoMSVector() {
        String[] values = {"CALMA", "DEBIL", "MODERADO", "FUERTE", "MUY_FUERTE", "HURACANADOS"};
        FastVector moduloVientoMSVector = new FastVector();
        for (String v : values) {
            moduloVientoMSVector.addElement(v);
        }
        return moduloVientoMSVector;
    }

    private FastVector getDireccionVientoMSVector() {
        String[] values = {"NORTE", "NORDESTE", "ESTE", "SUDESTE", "SUR", "SUDOESTE", "OESTE", "NOROESTE"};
        FastVector direccionVientoMSVector = new FastVector();
        for (String v : values) {
            direccionVientoMSVector.addElement(v);
        }
        return direccionVientoMSVector;
    }

    private FastVector getVientoMGVector() {
        String[] values = {"299", "300", "301", "302", "303", "304", "305", "306", "307", "308",
            "309", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "320",
            "321", "322", "323", "324", "325", "326", "327", "328", "329", "330", "331", "332"};
        FastVector cieloMGVector = new FastVector();
        for (String v : values) {
            cieloMGVector.addElement(v);
        }
        return cieloMGVector;
    }
}
