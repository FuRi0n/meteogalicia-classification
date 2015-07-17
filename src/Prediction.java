
import java.time.LocalDate;
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
public class Prediction {

    private String id;
    private LocalDate date;
    private TimeFrame morning;
    private TimeFrame afternoon;
    private TimeFrame night;
    private List<Integer> temperaturaMS;
    private List<Integer> temperaturaMG;

    public Prediction(String id, LocalDate date, List<String> cieloMS, List<Float> moduloVientoMS, List<Float> direccionVientoMS, List<Integer> temperaturaMS, List<Integer> cieloMG, List<Integer> vientoMG, List<Integer> temperaturaMG, Boolean summer) {
        this.id = id;
        this.date = date;
        List<String> moduloVientoMSstr = transformModuloVientoMS(moduloVientoMS);
        List<String> direccionVientoMSstr = transformDireccionVientoMS(direccionVientoMS);
        morning = new TimeFrame(cieloMS, moduloVientoMSstr, direccionVientoMSstr, temperaturaMS, cieloMG, vientoMG, summer, TimeFrame.MORNING);
        afternoon = new TimeFrame(cieloMS, moduloVientoMSstr, direccionVientoMSstr, temperaturaMS, cieloMG, vientoMG, summer, TimeFrame.AFTERNOON);
        night = new TimeFrame(cieloMS, moduloVientoMSstr, direccionVientoMSstr, temperaturaMS, cieloMG, vientoMG, summer, TimeFrame.NIGHT);
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

    public TimeFrame getMorning() {
        return morning;
    }

    public void setMorning(TimeFrame morning) {
        this.morning = morning;
    }

    public TimeFrame getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(TimeFrame afternoon) {
        this.afternoon = afternoon;
    }

    public TimeFrame getNight() {
        return night;
    }

    public void setNight(TimeFrame night) {
        this.night = night;
    }

    public List<Integer> getTemperaturaMS() {
        return temperaturaMS;
    }

    public void setTemperaturaMS(List<Integer> temperaturaMS) {
        this.temperaturaMS = temperaturaMS;
    }

    public List<Integer> getTemperaturaMG() {
        return temperaturaMG;
    }

    public void setTemperaturaMG(List<Integer> temperaturaMG) {
        this.temperaturaMG = temperaturaMG;
    }

    private List<String> transformModuloVientoMS(List<Float> moduloVientoMS) {
        List<String> moduloVientoMSstr = new ArrayList<>();
        for (Float v : moduloVientoMS) {
            if (v <= 5) {
                moduloVientoMSstr.add("CALMA");
            } else if (v > 5 && v <= 20) {
                moduloVientoMSstr.add("DEBIL");
            } else if (v > 20 && v <= 40) {
                moduloVientoMSstr.add("MODERADO");
            } else if (v > 40 && v <= 70) {
                moduloVientoMSstr.add("FUERTE");
            } else if (v > 70 && v <= 120) {
                moduloVientoMSstr.add("MUY_FUERTE");
            } else {
                moduloVientoMSstr.add("HURACANADOS");
            }
        }
        return moduloVientoMSstr;
    }

    private List<String> transformDireccionVientoMS(List<Float> direccionVientoMS) {
        List<String> direccionVientoMSstr = new ArrayList<>();
        for (Float v : direccionVientoMS) {
            if (v > 337.5 && v <= 22.5) {
                direccionVientoMSstr.add("NORTE");
            } else if (v > 22.5 && v <= 67.5) {
                direccionVientoMSstr.add("NORDESTE");
            } else if (v > 67.5 && v <= 112.5) {
                direccionVientoMSstr.add("ESTE");
            } else if (v > 112.5 && v <= 157.5) {
                direccionVientoMSstr.add("SUDESTE");
            } else if (v > 157.5 && v <= 202.5) {
                direccionVientoMSstr.add("SUR");
            } else if (v > 202.5 && v <= 247.5) {
                direccionVientoMSstr.add("SUDOESTE");
            } else if (v > 247.5 && v <= 292.5) {
                direccionVientoMSstr.add("OESTE");
            } else {
                direccionVientoMSstr.add("NOROESTE");
            }
        }
        return direccionVientoMSstr;
    }
}
