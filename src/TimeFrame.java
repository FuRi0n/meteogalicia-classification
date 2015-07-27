
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
public class TimeFrame {

    //Mañá: das 7 ás 14 h. en horario de inverno e das 6 ás 14 h. en horario de verán.	
    //Tarde: das 14 ás 20 h. en horario de inverno e das 14 ás 21 h. en horario de verán.	
    //Noite: das 20 ás 7 h. do día seguinte en horario de inverno e das 21 ás 6 h. do día seguinte en horario de verán.
    public static final int MORNING = 0;
    public static final int AFTERNOON = 1;
    public static final int NIGHT = 2;

    private List<String> cieloMS;
    private List<Float> moduloVientoMS;
    private List<String> direccionVientoMS;
    private Integer cieloMG;
    private Integer vientoMG;

    public TimeFrame(List<String> cieloMS, List<Float> moduloVientoMS, List<String> direccionVientoMS, List<Integer> temperaturaMS, List<Integer> cieloMG, List<Integer> vientoMG, Boolean summer, Integer timeFrame) {
        if (summer) {
            if (timeFrame == MORNING) {
                this.cieloMS = cieloMS.subList(0, 8);
                this.moduloVientoMS = moduloVientoMS.subList(0, 8);
                this.direccionVientoMS = direccionVientoMS.subList(0, 8);
                this.cieloMG = cieloMG.get(0);
                this.vientoMG = vientoMG.get(0);
            } else if (timeFrame == AFTERNOON) {
                this.cieloMS = cieloMS.subList(8, 15);
                this.moduloVientoMS = moduloVientoMS.subList(8, 15);
                this.direccionVientoMS = direccionVientoMS.subList(8, 15);
                this.cieloMG = cieloMG.get(1);
                this.vientoMG = vientoMG.get(1);
            } else {
                this.cieloMS = cieloMS.subList(15, 24);
                this.moduloVientoMS = moduloVientoMS.subList(15, 24);
                this.direccionVientoMS = direccionVientoMS.subList(15, 24);
                this.cieloMG = cieloMG.get(2);
                this.vientoMG = vientoMG.get(2);
            }
        } else {
            if (timeFrame == MORNING) {
                this.cieloMS = cieloMS.subList(1, 8);
                this.moduloVientoMS = moduloVientoMS.subList(1, 8);
                this.direccionVientoMS = direccionVientoMS.subList(1, 8);
                this.cieloMG = cieloMG.get(0);
                this.vientoMG = vientoMG.get(0);
            } else if (timeFrame == AFTERNOON) {
                this.cieloMS = cieloMS.subList(8, 14);
                this.moduloVientoMS = moduloVientoMS.subList(8, 14);
                this.direccionVientoMS = direccionVientoMS.subList(8, 14);
                this.cieloMG = cieloMG.get(1);
                this.vientoMG = vientoMG.get(1);
            } else {
                this.cieloMS = cieloMS.subList(14, 25);
                this.moduloVientoMS = moduloVientoMS.subList(14, 25);
                this.direccionVientoMS = direccionVientoMS.subList(14, 25);
                this.cieloMG = cieloMG.get(2);
                this.vientoMG = vientoMG.get(2);
            }
        }
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

    public List<String> getDireccionVientoMS() {
        return direccionVientoMS;
    }

    public void setDireccionVientoMS(List<String> direccionVientoMS) {
        this.direccionVientoMS = direccionVientoMS;
    }

    public Integer getCieloMG() {
        return cieloMG;
    }

    public void setCieloMG(Integer cieloMG) {
        this.cieloMG = cieloMG;
    }

    public Integer getVientoMG() {
        return vientoMG;
    }

    public void setVientoMG(Integer vientoMG) {
        this.vientoMG = vientoMG;
    }
    
    public List<String> getModuloVientoMSText() {
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

    public String getCieloMGText() {
        switch (cieloMG) {
            case 101:
                return "SUNNY";
            case 102:
                return "HIGH_CLOUDS";
            case 103:
                return "PARTLY_CLOUDY";
            case 104:
                return "OVERCAST";
            case 105:
                return "CLOUDY";
            case 106:
                return "FOG";
            case 107:
                return "SHOWERS";
            case 108:
                return "OVERCAST_AND_SHOWERS";
            case 109:
                return "INTERMITENT_SNOW";
            case 110:
                return "DRIZZLE";
            case 111:
                return "RAIN";
            case 112:
                return "SNOW";
            case 113:
                return "STORMS";
            case 114:
                return "MIST";
            case 115:
                return "FOG_BANK";
            case 116:
                return "MID_CLOUDS";
            case 117:
                return "WEAK_RAIN";
            case 118:
                return "WEAK_SHOWERS";
            case 119:
                return "STORM_THEN_CLOUDY";
            case 120:
                return "MELTED_SNOW";
            case 121:
                return "RAIN_HAIL";
        }
        return "";
    }
}
