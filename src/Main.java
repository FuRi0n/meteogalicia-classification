
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
                    predictions.add(new Prediction(id, date, cieloMS, moduloVientoMS, direccionVientoMS, temperaturaMS, cieloMG, vientoMG, temperaturaMG, true));
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error: No se encuentra el fichero.");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArffGenerator generator = new ArffGenerator(predictions);
            generator.generateFiles(ArffGenerator.CIELO);
            generator.generateFiles(ArffGenerator.VIENTO);
            generator.generateFiles(ArffGenerator.TEMPERATURA);
        } else {
            System.err.println("Error: Se debe indicar el nombre del fichero a leer.");
        }
    }  
}
