
import java.time.LocalDate;
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
public class Prediction {

    String id;
    LocalDate date;
    List<String> cieloMS;
    List<Float> moduloVientoMS;
    List<Float> direccionVientoMS;
    List<Integer> temperaturaMS;
    List<Integer> cieloMG;
    List<Integer> vientoMG;
    List<Integer> temperaturaMG;

    public Prediction(String id, LocalDate date, List<String> cieloMS, List<Float> moduloVientoMS, List<Float> direccionVientoMS, List<Integer> temperaturaMS, List<Integer> cieloMG, List<Integer> vientoMG, List<Integer> temperaturaMG) {
        this.id = id;
        this.date = date;
        this.cieloMS = cieloMS;
        this.moduloVientoMS = moduloVientoMS;
        this.direccionVientoMS = direccionVientoMS;
        this.temperaturaMS = temperaturaMS;
        this.cieloMG = cieloMG;
        this.vientoMG = vientoMG;
        this.temperaturaMG = temperaturaMG;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getCieloMS() {
        return cieloMS;
    }

    public void setCieloMS(List<String> cieloMS) {
        this.cieloMS = cieloMS;
    }

    public List<Float> getModuloVientoMS() {
        return moduloVientoMS;
    }

    public void setModuloVientoMS(List<Float> moduloVientoMS) {
        this.moduloVientoMS = moduloVientoMS;
    }

    public List<Float> getDireccionVientoMS() {
        return direccionVientoMS;
    }

    public void setDireccionVientoMS(List<Float> direccionVientoMS) {
        this.direccionVientoMS = direccionVientoMS;
    }

    public List<Integer> getTemperaturaMS() {
        return temperaturaMS;
    }

    public void setTemperaturaMS(List<Integer> temperaturaMS) {
        this.temperaturaMS = temperaturaMS;
    }

    public List<Integer> getCieloMG() {
        return cieloMG;
    }

    public void setCieloMG(List<Integer> cieloMG) {
        this.cieloMG = cieloMG;
    }

    public List<Integer> getVientoMG() {
        return vientoMG;
    }

    public void setVientoMG(List<Integer> vientoMG) {
        this.vientoMG = vientoMG;
    }

    public List<Integer> getTemperaturaMG() {
        return temperaturaMG;
    }

    public void setTemperaturaMG(List<Integer> temperaturaMG) {
        this.temperaturaMG = temperaturaMG;
    }
}
