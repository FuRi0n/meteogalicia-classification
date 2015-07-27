
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author julio.janeiro
 */
public class BaseLineGenerator {

    private final List<Prediction> predictions;
    private final List<BaseLine> baselines;

    public BaseLineGenerator(List<Prediction> predictions) {
        this.predictions = predictions;
        baselines = new ArrayList<>();
    }

    public void generate() {
        BaseLine cieloMañana = new BaseLine("Cielo Mañana");
        BaseLine cieloTarde = new BaseLine("Cielo Tarde");
        BaseLine cieloNoche = new BaseLine("Cielo Noche");
        BaseLine vientoMañana = new BaseLine("Viento Mañana");
        BaseLine vientoTarde = new BaseLine("Viento Tarde");
        BaseLine vientoNoche = new BaseLine("Viento Noche");
        BaseLine temperaturaMin = new BaseLine("Temperatura Min");
        BaseLine temperaturaMax = new BaseLine("Temperatura Max");
        for (Prediction p : predictions) {
            if (getCieloMSMode(p.getMorning()).equals(p.getMorning().getCieloMGText())) {
                cieloMañana.addCorrect();
            } else {
                cieloMañana.addIncorrect();
            }
            if (getCieloMSMode(p.getAfternoon()).equals(p.getAfternoon().getCieloMGText())) {
                cieloTarde.addCorrect();
            } else {
                cieloTarde.addIncorrect();
            }
            if (getCieloMSMode(p.getNight()).equals(p.getNight().getCieloMGText())) {
                cieloNoche.addCorrect();
            } else {
                cieloNoche.addIncorrect();
            }
            if (getVientoMGValue(p.getMorning()).equals(p.getMorning().getVientoMG())) {
                vientoMañana.addCorrect();
            } else {
                vientoMañana.addIncorrect();
            }
            if (getVientoMGValue(p.getAfternoon()).equals(p.getAfternoon().getVientoMG())) {
                vientoTarde.addCorrect();
            } else {
                vientoTarde.addIncorrect();
            }
            if (getVientoMGValue(p.getNight()).equals(p.getNight().getVientoMG())) {
                vientoNoche.addCorrect();
            } else {
                vientoNoche.addIncorrect();
            }
            Integer min = p.getTemperaturaMS().stream().min(Integer::compare).get();
            Integer max = p.getTemperaturaMS().stream().max(Integer::compare).get();
            if (p.getTemperaturaMG().get(0).equals(min)) {
                temperaturaMin.addCorrect();
            } else {
                temperaturaMin.addIncorrect();
            }
            if (p.getTemperaturaMG().get(1).equals(max)) {
                temperaturaMax.addCorrect();
            } else {
                temperaturaMax.addIncorrect();
            }
        }
        baselines.add(cieloMañana);
        baselines.add(cieloTarde);
        baselines.add(cieloNoche);
        baselines.add(vientoMañana);
        baselines.add(vientoTarde);
        baselines.add(vientoNoche);
        baselines.add(temperaturaMin);
        baselines.add(temperaturaMax);
    }

    public String getCieloMSMode(TimeFrame t) {
        Map<String, Long> counting = t.getCieloMS().stream().collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        String max = "";
        Integer maxCount = 0;
        for (Map.Entry<String, Long> count : counting.entrySet()) {
            if (count.getValue() > maxCount) {
                max = count.getKey();
                maxCount = count.getValue().intValue();
            }
        }
        return max;
    }

    public String getModuloVientoMSText(TimeFrame t) {
        Double mv = t.getModuloVientoMS().stream().mapToDouble(v -> Double.valueOf(v)).average().getAsDouble();
        if (mv <= 5) {
            return "CALMA";
        } else if (mv > 5 && mv <= 20) {
            return "DEBIL";
        } else if (mv > 20 && mv <= 40) {
            return "MODERADO";
        } else if (mv > 40 && mv <= 70) {
            return "FUERTE";
        } else if (mv > 70 && mv <= 120) {
            return "MUY_FUERTE";
        } else {
            return "HURACANADOS";
        }
    }

    public String getDireccionVientoMSMode(TimeFrame t) {
        Map<String, Long> counting = t.getDireccionVientoMS().stream().collect(Collectors.groupingBy(String::toString, Collectors.counting()));
        String max = "";
        Integer maxCount = 0;
        for (Map.Entry<String, Long> count : counting.entrySet()) {
            if (count.getValue() > maxCount) {
                max = count.getKey();
                maxCount = count.getValue().intValue();
            }
        }
        return max;
    }

    public Integer getVientoMGValue(TimeFrame t) {
        String mv = getModuloVientoMSText(t);
        String dv = getDireccionVientoMSMode(t);
        return getVientoMGValue(mv, dv);
    }

    public Integer getVientoMGValue(String mv, String dv) {
        if (mv.equals("CALMA")) {
            return 299;
        } else {
            switch (dv) {
                case "NORTE":
                    switch (mv) {
                        case "DEBIL":
                            return 301;
                        case "MODERADO":
                            return 309;
                        case "FUERTE":
                            return 317;
                        case "MUY_FUERTE":
                            return 325;
                        case "HURACANADOS":
                            return 325;
                    }
                case "NORDESTE":
                    switch (mv) {
                        case "DEBIL":
                            return 302;
                        case "MODERADO":
                            return 310;
                        case "FUERTE":
                            return 318;
                        case "MUY_FUERTE":
                            return 326;
                        case "HURACANADOS":
                            return 326;
                    }
                case "ESTE":
                    switch (mv) {
                        case "DEBIL":
                            return 303;
                        case "MODERADO":
                            return 311;
                        case "FUERTE":
                            return 319;
                        case "MUY_FUERTE":
                            return 327;
                        case "HURACANADOS":
                            return 327;
                    }
                case "SUDESTE":
                    switch (mv) {
                        case "DEBIL":
                            return 304;
                        case "MODERADO":
                            return 312;
                        case "FUERTE":
                            return 320;
                        case "MUY_FUERTE":
                            return 328;
                        case "HURACANADOS":
                            return 328;
                    }
                case "SUR":
                    switch (mv) {
                        case "DEBIL":
                            return 305;
                        case "MODERADO":
                            return 313;
                        case "FUERTE":
                            return 321;
                        case "MUY_FUERTE":
                            return 329;
                        case "HURACANADOS":
                            return 329;
                    }
                case "SUDOESTE":
                    switch (mv) {
                        case "DEBIL":
                            return 306;
                        case "MODERADO":
                            return 314;
                        case "FUERTE":
                            return 322;
                        case "MUY_FUERTE":
                            return 330;
                        case "HURACANADOS":
                            return 330;
                    }
                case "OESTE":
                    switch (mv) {
                        case "DEBIL":
                            return 307;
                        case "MODERADO":
                            return 315;
                        case "FUERTE":
                            return 323;
                        case "MUY_FUERTE":
                            return 331;
                        case "HURACANADOS":
                            return 331;
                    }
                case "NOROESTE":
                    switch (mv) {
                        case "DEBIL":
                            return 308;
                        case "MODERADO":
                            return 316;
                        case "FUERTE":
                            return 324;
                        case "MUY_FUERTE":
                            return 332;
                        case "HURACANADOS":
                            return 332;
                    }
                default:
                    return 300;
            }
        }
    }

    public String asText(Boolean header) {
        String text = "";
        for (BaseLine b : baselines) {
            if (header) {
                text += b.toString() + "\n";
            } else {
                text += (b.getCorrect() / (float) b.getTotal()) * 100 + "%\n";
            }
        }
        return text;
    }
}
