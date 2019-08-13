package com.celio.fac.utils;

import java.math.BigDecimal;
import java.text.Normalizer;

public class Utils {

    public static String aredondaValor(String qtd, int scala) {
        if (qtd != null && !qtd.isEmpty()) {
            try {
                BigDecimal value = new BigDecimal(qtd);

                qtd = value.setScale(scala, BigDecimal.ROUND_UP).toString();
                String vt[] = qtd.split("[.]");
                switch (scala) {
                    case 1:
                        if (vt[1].startsWith("0")) {
                            qtd = vt[0];
                        }
                        break;
                    case 3:
                        if (vt[1].startsWith("000")) {
                            qtd = vt[0];
                        }
                        break;
                    default:
                        if (vt[1].startsWith("00")) {
                            qtd = vt[0];
                        }
                        break;
                }
            } catch (Exception e) {

            }
        }
        return qtd;
    }

    /**
     * @param str
     * @return
     */
    public static String removerAcento(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return inTrim(str);
    }

    /**
     * remove espacos extras dentros das strings
     *
     * @param str
     * @return
     */
    public static String inTrim(String str) {
        str = str.trim();
        char vt[] = str.toCharArray();
        boolean l = true;
        StringBuilder r = new StringBuilder();
        for (char c : vt) {

            if (c != ' ') {
                l = true;
            }

            if (l) {
                r.append(c);
                if (c == ' ') {
                    l = false;
                }
            }
        }
        return r.toString();
    }

    /**
     * @param value
     * @return
     */
    public static String getSimNao(String value) {
        if (value.equalsIgnoreCase("true")) {
            value = "Sim";
        } else if (value.equalsIgnoreCase("false")) {
            value = "Não";
        }
        return value;
    }

    /**
     * @param value
     * @return
     */
    public static String getTrueFalse(String value) {
        if (value.equalsIgnoreCase("Sim")) {
            value = "true";
        } else if (value.equalsIgnoreCase("Não")) {
            value = "false";
        }
        return value;
    }

    /**
     * @param segmento
     * @return
     */
    public static String getSemento(String segmento) {
        if (segmento.equalsIgnoreCase("panificacao")) {
            segmento = "Panificação";
        } else if (segmento.equalsIgnoreCase("confeitaria")) {
            segmento = "Confeitaria";
        } else if (segmento.equalsIgnoreCase("industria")) {
            segmento = "Indústria";
        }
        return segmento;
    }

    /**
     * @param segmento
     * @return
     */
    public static String getSementoBD(String segmento) {
        if (segmento.equalsIgnoreCase("Panificação")) {
            segmento = "panificacao";
        } else if (segmento.equalsIgnoreCase("Confeitaria")) {
            segmento = "confeitaria";
        } else if (segmento.equalsIgnoreCase("Indústria")) {
            segmento = "industria";
        }
        return segmento;
    }

}
