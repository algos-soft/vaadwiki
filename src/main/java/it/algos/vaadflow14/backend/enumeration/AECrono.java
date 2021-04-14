package it.algos.vaadflow14.backend.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 29-nov-2020
 * Time: 07:09
 */
public enum AECrono {
    secolo, anno, mese, giorno;

    public static List<AECrono> get() {
        List<AECrono> lista = new ArrayList<>();

        for (AECrono crono : AECrono.values()) {
            lista.add(crono);
        }

        return lista;
    }
    public static List<String> getValue() {
        List<String> lista = new ArrayList<>();

        for (AECrono crono : AECrono.values()) {
            lista.add(crono.name());
        }

        return lista;
    }

}
