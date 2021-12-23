package it.algos.vaadflow14.backend.enumeration;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 29-nov-2020
 * Time: 07:08
 */
public enum AEGeografia {
    continente, stato, regione, provincia;

    public static List<AEGeografia> get() {
        List<AEGeografia> lista = new ArrayList<>();

        for (AEGeografia geo : AEGeografia.values()) {
            lista.add(geo);
        }

        return lista;
    }

    public static List<String> getValue() {
        List<String> lista = new ArrayList<>();

        for (AEGeografia geo : AEGeografia.values()) {
            lista.add(geo.name());
        }

        return lista;
    }

}
