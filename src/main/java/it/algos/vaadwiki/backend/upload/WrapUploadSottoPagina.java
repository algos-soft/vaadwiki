package it.algos.vaadwiki.backend.upload;

import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.attivita.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 03-mar-2022
 * Time: 16:52
 */
public class WrapUploadSottoPagina {

    protected Attivita attivita;

    protected String wikiPaginaTitle;

    protected String wikiSottoTitolo;

    protected Map<String, List<String>> mappaUno;

    public WrapUploadSottoPagina(Attivita attivita, String wikiPaginaTitle, String wikiSottoTitolo, Map<String, List<String>> mappaUno) {
        this.attivita = attivita;
        this.wikiPaginaTitle = wikiPaginaTitle;
        this.wikiSottoTitolo = wikiSottoTitolo;
        this.mappaUno = mappaUno;
    }

    public Attivita getAttivita() {
        return attivita;
    }


    public String getWikiPaginaTitle() {
        return wikiPaginaTitle;
    }

    public String getWikiSottoTitolo() {
        return wikiSottoTitolo;
    }

    public Map<String, List<String>> getMappaUno() {
        return mappaUno;
    }

}
