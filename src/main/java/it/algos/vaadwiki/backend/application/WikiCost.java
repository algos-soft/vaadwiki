package it.algos.vaadwiki.backend.application;

import it.algos.vaadflow14.backend.annotation.*;

import java.time.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 14-apr-2021
 * Time: 8:22
 * <p>
 * Classe astratta di costanti statiche <br>
 * Completa la classe FlowCost con le costanti statiche specifiche di questa applicazione <br>
 * Le costanti statiche sono sempre 'static final' (nell'ordine) <br>
 * <p>
 * Not annotated with @SpringComponent (astratta, inutile) <br>
 * Not annotated with @Scope (astratta, inutile) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file da wizard/scripts di algos <br>
 */
@AIScript(sovraScrivibile = false)
public abstract class WikiCost {

    public static final String TAG_WIKI_DATA = "wikiData";

    public static final String PREF_DATA_LAST_DOWLOAD_GENERE = "lastDownloadGenere";

    public static final String PREF_DATA_LAST_DOWLOAD_ATTIVITA = "lastDownloadAttivita";

    public static final String PREF_DATA_LAST_DOWLOAD_NAZIONALITA = "lastDownloadNazionalita";

    public static final String PREF_DATA_LAST_DOWLOAD_PROFESSIONE = "lastDownloadProfessione";

    public static final String PREF_DATA_LAST_DOWLOAD_NOME_DOPPIO = "lastDownloadNomeDoppio";

    public static final String PREF_DATA_LAST_DOWLOAD_BIOGRAFIE = "lastDownloadBiografie";
    public static final String PREF_DATA_LAST_UPLOAD_BIOGRAFIE = "lastDownloadBiografie";

    public static final LocalDateTime DATA_TIME = LocalDateTime.of(2021, 4, 15, 12, 30);

    public static final String KEY_MAP_GRAFFE_ESISTONO = "keyMapGraffeEsistono";

    public static final String KEY_MAP_GRAFFE_TYPE = "keyMapGraffeType";

    public static final String KEY_MAP_GRAFFE_NUMERO = "keyMapGraffeNumero";

    public static final String KEY_MAP_GRAFFE_VALORE_CONTENUTO = "keyMapGraffeValoreContenuto";

    public static final String KEY_MAP_GRAFFE_TESTO_PRECEDENTE = "keyMapGraffeTestoPrecedente";

    public static final String KEY_MAP_GRAFFE_NOME_PARAMETRO = "keyMapGraffeNomeParametro";

    public static final String KEY_MAP_GRAFFE_VALORE_PARAMETRO = "keyMapGraffeValoreParametro";

    public static final String KEY_MAP_GRAFFE_LISTA_WRAPPER = "keyMapGraffeListaWrapper";

    public static final String NO_CAT = "Non ci sono pagine nella categoria";

    public static final String NO_PAGES_CAT = "Non esiste la categoria";

}// end of static class