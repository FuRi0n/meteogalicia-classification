
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    private final List<Prediction> predictions;

    public ArffGenerator(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public void generateFiles(int type) {
        if (!predictions.isEmpty()) {
            generateFile(type, TimeFrame.MORNING);
            generateFile(type, TimeFrame.AFTERNOON);
            generateFile(type, TimeFrame.NIGHT);
        }
    }

    private void generateFile(int type, int timeFrame) {
        List<TimeFrame> temporalData = new ArrayList<>();
        for (Prediction p : predictions) {
            if (timeFrame == TimeFrame.MORNING) {
                temporalData.add(p.getMorning());
            } else if (timeFrame == TimeFrame.AFTERNOON) {
                temporalData.add(p.getAfternoon());
            } else {
                temporalData.add(p.getNight());
            }
        }
        if (type == CIELO) {
            FastVector attrs = generateCieloHeaders(temporalData.get(0).getCieloMS().size());
            Instances data = generateCieloData(temporalData, attrs);
            if (timeFrame == TimeFrame.MORNING) {
                saveFile("EstadoCieloMañana.arff", data);
            } else if (timeFrame == TimeFrame.AFTERNOON) {
                saveFile("EstadoCieloTarde.arff", data);
            } else if (timeFrame == TimeFrame.NIGHT) {
                saveFile("EstadoCieloNoche.arff", data);
            }
        } else if (type == VIENTO) {
            FastVector attrs = generateVientoHeaders(temporalData.get(0).getModuloVientoMS().size());
            Instances data = generateVientoData(temporalData, attrs);
            if (timeFrame == TimeFrame.MORNING) {
                saveFile("VientoMañana.arff", data);
            } else if (timeFrame == TimeFrame.AFTERNOON) {
                saveFile("VientoTarde.arff", data);
            } else if (timeFrame == TimeFrame.NIGHT) {
                saveFile("VientoNoche.arff", data);
            }
        } else {
            System.out.println("Error: Temperature, can't classify numeric values.");
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

    private Instances generateCieloData(List<TimeFrame> temporalData, FastVector attrs) {
        Instances data = new Instances("Estado del cielo", attrs, 0);
        FastVector cieloMSVector = getCieloMSVector();
        FastVector cieloMGVector = getCieloMGVector();
        for (TimeFrame t : temporalData) {
            double[] vals = new double[data.numAttributes()];
            for (int i = 0; i < temporalData.get(0).getCieloMS().size(); i++) {
                vals[i] = cieloMSVector.indexOf(t.getCieloMS().get(i));
            }
            vals[vals.length - 1] = cieloMGVector.indexOf(String.valueOf(t.getCieloMG()));
            data.add(new Instance(1, vals));
        }
        return data;
    }

    private Instances generateVientoData(List<TimeFrame> temporalData, FastVector attrs) {
        Instances data = new Instances("Viento", attrs, 0);
        FastVector moduloVientoMSVector = getModuloVientoMSVector();
        FastVector direccionVientoMSVector = getDireccionVientoMSVector();
        FastVector vientoMGVector = getVientoMGVector();
        for (TimeFrame t : temporalData) {
            double[] vals = new double[data.numAttributes()];
            for (int i = 0; i < temporalData.get(0).getModuloVientoMS().size(); i++) {
                vals[i * 2] = moduloVientoMSVector.indexOf(t.getModuloVientoMS().get(i));
                vals[i * 2 + 1] = direccionVientoMSVector.indexOf(t.getDireccionVientoMS().get(i));
            }
            vals[vals.length - 1] = vientoMGVector.indexOf(String.valueOf(t.getVientoMG()));
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
