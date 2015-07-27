
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import weka.classifiers.trees.J48;

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
            //Arff files for classification
            ArffGenerator generator = new ArffGenerator(predictions);
            generator.generateFiles(ArffGenerator.CIELO);
            generator.generateFiles(ArffGenerator.VIENTO);
            generator.generateFiles(ArffGenerator.TEMPERATURA);

            //BaseLines
            System.out.println("Baseline:");
            BaseLineGenerator bg = new BaseLineGenerator(predictions);
            bg.generate();
            System.out.println(bg.asText(false));
            
            //Classification
            System.out.println("Classification:");
            Classification cls = new Classification(new J48(), "EstadoCieloMañana.arff", 10, 1);
            cls.classify();
            cls.saveModel();
            cls.setFile("EstadoCieloTarde.arff");
            cls.classify();
            cls.saveModel();
            cls.setFile("EstadoCieloNoche.arff");
            cls.classify();
            cls.saveModel();
            cls.setFile("VientoMañana.arff");
            cls.classify();
            cls.saveModel();
            cls.setFile("VientoTarde.arff");
            cls.classify();
            cls.saveModel();
            cls.setFile("VientoNoche.arff");
            cls.classify();
            cls.saveModel();
            cls.setFile("TemperaturaMin.arff");
            cls.classify();
            cls.saveModel();
            cls.setFile("TemperaturaMax.arff");
            cls.classify();
            cls.saveModel();
            
            // Regression
            System.out.println("\nRegression:");
            ProcessBuilder pb = new ProcessBuilder("python2.7", "Regression.py", args[0]);
            try {
                Process p = pb.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while((line=br.readLine())!=null){
                    System.out.println(line);
                }
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("Error: Se debe indicar el nombre del fichero a leer.");
        }
    }
}
