package it.algos.vaadwiki.backend.wrapper;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 11:33
 */
public class WMap<String, List> extends LinkedHashMap<String, List> {

    public String titolo;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

}
