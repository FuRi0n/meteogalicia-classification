
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;

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
        if (args.length == 2) {
            if (args[0].endsWith("txt")) {
                List<Prediction> predictions = readFile(args[0]);
                if (!predictions.isEmpty()) {
                    Results res = classify(predictions, args[1]);
                    res.setRegression(regression(args[0]));
                    System.out.println(res.toString());
                } else {
                    System.err.println("Error: No se pueden leer los datos del fichero.");
                }
            } else if (args[0].endsWith("db")) {
                List<List<Prediction>> predictionsLists = readDB(args[0]);
                Results res = new Results();
                for (List<Prediction> predictions : predictionsLists) {
                    if (!predictions.isEmpty()) {
                        Results r = classify(predictions, args[1]);
                        r.setRegression(regression(args[0], predictions.get(0).getId()));
                        res.add(r);
                    } else {
                        System.err.println("Error: No se pueden leer los datos de la DB.");
                    }
                }
                System.out.println(res.toString());
            } else {
                System.err.println("Error: Extensión del fichero/DB no válido.");
            }
        } else {
            System.err.println("Parámetros: <nombre fichero/DB> <clasificador (J48/RF)>");
        }
    }

    public static List<Prediction> readFile(String file) {
        List<Prediction> predictions = new ArrayList<>();
        File f = new File(file);
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
        return predictions;
    }

    public static List<List<Prediction>> readDB(String db) {
        List<List<Prediction>> predictionsList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + db);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT DISTINCT ID_MG FROM METEO ORDER BY ID_MG");
            List<String> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getString("ID_MG"));
            }
            for (String id : ids) {
                List<Prediction> predictions = new ArrayList<>();
                ResultSet data = statement.executeQuery("SELECT * FROM METEO WHERE ID_MG = " + id);
                while (data.next()) {
                    LocalDate date = LocalDate.parse(data.getString("FECHA"), DateTimeFormatter.ISO_DATE_TIME);
                    List<String> cieloMS = Arrays.asList(data.getString("CIELO_MS").split(","));
                    List<Float> moduloVientoMS = Arrays.asList(data.getString("MODULOVIENTO_MS").split(",")).stream().map(str -> Float.parseFloat(str)).collect(Collectors.toList());
                    List<Float> direccionVientoMS = Arrays.asList(data.getString("DIRECCIONVIENTO_MS").split(",")).stream().map(str -> Float.parseFloat(str)).collect(Collectors.toList());
                    List<Integer> temperaturaMS = Arrays.asList(data.getString("TEMPERATURA_MS").split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    List<Integer> cieloMG = Arrays.asList(data.getString("CIELO_MG").split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    List<Integer> vientoMG = Arrays.asList(data.getString("VIENTO_MG").split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    List<Integer> temperaturaMG = Arrays.asList(data.getString("TEMPERATURA_MG").split(",")).stream().map(str -> Integer.parseInt(str)).collect(Collectors.toList());
                    predictions.add(new Prediction(id, date, cieloMS, moduloVientoMS, direccionVientoMS, temperaturaMS, cieloMG, vientoMG, temperaturaMG, true));
                }
                predictionsList.add(predictions);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return predictionsList;
    }

    public static Results classify(List<Prediction> predictions, String classifierName) {
        //Arff files for classification
        ArffGenerator generator = new ArffGenerator(predictions);
        generator.generateFiles(ArffGenerator.CIELO);
        generator.generateFiles(ArffGenerator.VIENTO);
        generator.generateFiles(ArffGenerator.TEMPERATURA);

        //BaseLines
        BaseLineGenerator bg = new BaseLineGenerator(predictions);
        bg.generate();
        List<Double> baseline = bg.asList();
        //System.out.println(bg.asText(false));

        //Classification
        Classifier c;
        switch (classifierName) {
            case "J48":
                c = new J48();
                break;
            case "RF":
                c = new RandomForest();
                break;
            default:
                c = new J48();
        }
        Classification cls = new Classification(c, "EstadoCieloMañana.arff", 10, 1);
        List<Double> classificationResults = new ArrayList<>();
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("EstadoCieloTarde.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("EstadoCieloNoche.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("VientoMañana.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("VientoTarde.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("VientoNoche.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("TemperaturaMin.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();
        cls.setFile("TemperaturaMax.arff");
        classificationResults.add(cls.classify());
        //cls.saveModel();

        return new Results(baseline, classificationResults);
    }

    public static List<Double> regression(String dataFile) {
        // Regression
        List<Double> res = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("python2.7", "Regression.py", dataFile);
        try {
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                res.add(Double.parseDouble(line));
            }
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public static List<Double> regression(String dataFile, String idMG) {
        // Regression
        List<Double> res = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("python2.7", "Regression.py", dataFile, idMG);
        try {
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                res.add(Double.parseDouble(line));
            }
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
}
