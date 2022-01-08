package it.algos.vaadwiki.backend.wrapper;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 11:33
 */
public class WMap {

    Map<String, List> mappa;

    public WMap(String key, List lista) {
        this.mappa = new LinkedHashMap<>();
        this.mappa.put(key, lista);
    }

    public Map<String, List> getMappa() {
        return mappa;
    }

    public void setMappa(Map<String, List> mappa) {
        this.mappa = mappa;
    }

}
