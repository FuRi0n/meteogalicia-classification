
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            File f = new File(args[0]);
            List<Prediction> predictions = new ArrayList<>();
            try {
                Scanner s = new Scanner(f);
                while (s.hasNextLine()) {
                    String[] values = s.nextLine().split(";");
                    String id = values[0];
                    LocalDate date = LocalDate.parse(values[1], DateTimeFormatter.ISO_DATE_TIME);
                    List<String> cieloMS = Arrays.asList(values[2].split(","));
                    List<Float> moduloVientoMS = Arrays.asList(values[3].split(",")).stream().map(str -> Float.parseFloat(str)).collect(Collectors.toList());
                    List<Float> direccionVientoMS = Arrays.asList(values[4].split(",")).stream().map(str -> Float.parseFloat(str)).collect(Collectors.toList());
                    List<Integer> temperaturaMS = Arrays.asList(values[5].split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    List<Integer> cieloMG = Arrays.asList(values[6].split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    List<Integer> vientoMG = Arrays.asList(values[7].split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    List<Integer> temperaturaMG = Arrays.asList(values[8].split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    predictions.add(new Prediction(id, date, cieloMS, moduloVientoMS, direccionVientoMS, temperaturaMS, cieloMG, vientoMG, temperaturaMG));
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error: No se encuentra el fichero.");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!predictions.isEmpty()) {
                //Cabecera
                FastVector attrs = new FastVector();
                FastVector cieloMSVector = getCieloMSVector(predictions);
                for (int i = 1; i <= 25; i++) {
                    attrs.addElement(new Attribute("cieloMS" + i, cieloMSVector));
                    attrs.addElement(new Attribute("moduloVientoMS" + i));
                    attrs.addElement(new Attribute("direccionVientoMS" + i));
                    attrs.addElement(new Attribute("temperaturaMS" + i));
                }
                FastVector cieloMGVector = getCieloMGVector(predictions);
                attrs.addElement(new Attribute("class", cieloMGVector));
                //Datos
                Instances data = new Instances("Estado del cielo", attrs, 0);
                for (Prediction p : predictions) {
                    double[] vals = new double[data.numAttributes()];
                    for (int i = 0; i <= 24; i++) {
                        vals[i * 4] = cieloMSVector.indexOf(p.getCieloMS().get(i));
                        vals[i * 4 + 1] = p.getModuloVientoMS().get(i);
                        vals[i * 4 + 2] = p.getDireccionVientoMS().get(i);
                        vals[i * 4 + 3] = p.getTemperaturaMS().get(i);
                    }
                    String str = p.getCieloMG().stream().map(i -> String.valueOf(i)).collect(Collectors.joining());
                    vals[vals.length - 1] = cieloMGVector.indexOf(str);
                    data.add(new Instance(1, vals));
                }
                ArffSaver saver = new ArffSaver();
                saver.setInstances(data);
                try {
                    saver.setFile(new File("EstadoCielo.arff"));
                    saver.writeBatch();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            System.err.println("Error: Se debe indicar el nombre del fichero a leer.");
        }
    }

    public static FastVector getCieloMSVector(List<Prediction> predictions) {
        FastVector cieloMSVector = new FastVector();
        for (Prediction p : predictions) {
            for (String v : p.getCieloMS()) {
                if (!cieloMSVector.contains(v)) {
                    cieloMSVector.addElement(v);
                }
            }
        }
        return cieloMSVector;
    }

    public static FastVector getCieloMGVector(List<Prediction> predictions) {
        FastVector cieloMGVector = new FastVector();
        for (Prediction p : predictions) {
            String val = "";
            for (Integer v : p.getCieloMG()) {
                val += v;
            }
            if (!cieloMGVector.contains(val)) {
                cieloMGVector.addElement(val);
            }
        }
        return cieloMGVector;
    }
}
