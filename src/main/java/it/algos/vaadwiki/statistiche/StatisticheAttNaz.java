package it.algos.vaadwiki.statistiche;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 18-nov-2019
 * Time: 18:48
 */
public abstract class StatisticheAttNaz extends Statistiche {

    protected List<String> lista;

    protected LinkedHashMap<String, MappaStatistiche> mappa;

    protected String codeLastUpload;

    protected String codeDurataUpload;


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheAttivita.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void postConstruct() {
        long inizio = System.currentTimeMillis();
        inizia();
        setLastUpload(inizio);
    }// end of method


    /**
     * Corpo della pagina <br>
     */
    @Override
    protected void elaboraBody() {
        String testo = VUOTA;
        int numBio = bioService.count();

        //--prima tabella
        testo += A_CAPO;
        testo += tabellaUsate(numBio);

        //--seconda tabella
        testo += tabellaNonUsate(numBio);

        testo += "==Note==";
        testo += A_CAPO;
        testo += "<references/>";

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Prima tabella <br>
     */
    protected String tabellaUsate(int numBio) {
        String testo = VUOTA;

        //--tabella
        testo += A_CAPO;
        testo += testoPrimaTabella(numBio);
        testo += A_CAPO;
        testo += inizioTabella();
        testo += colonnePrimaTabella();
        testo += corpoPrimaTabella();
        testo += fineTabella();
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /*
     * seconda tabella <br>
     */
    protected String tabellaNonUsate(int numBio) {
        String testo = VUOTA;

        //--tabella
        testo += A_CAPO;
        testo += testoSecondaTabella(numBio);
        testo += A_CAPO;
        testo += inizioTabella();
        testo += colonneSecondaTabella();
        testo += corpoSecondaTabella();
        testo += fineTabella();
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /*
     * testo descrittivo prima tabella <br>
     */
    protected String testoPrimaTabella(int numBio) {
        return VUOTA;
    }// fine del metodo


    private String inizioTabella() {
        String testo = "";

        testo += A_CAPO;
        testo += "{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF;\"";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String colonnePrimaTabella() {
        return VUOTA;
    }// fine del metodo


    protected String corpoPrimaTabella() {
        StringBuilder testo = new StringBuilder();
        int cont = 1;
        int k = 1;
        String riga;

        for (String chiave : lista) {
            riga = rigaPrimaTabella(chiave, cont);
            if (text.isValid(riga)) {
                testo.append(riga);
                k = k + 1;
                cont = k;
            }// end of if cycle
        }// end of for cycle

        return testo.toString();
    }// fine del metodo


    protected String rigaPrimaTabella(String plurale, int cont) {
        return VUOTA;
    }// fine del metodo


    private String fineTabella() {
        String testo = "";

        testo += "|}";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /*
     * testo descrittivo prima tabella <br>
     */
    protected String testoSecondaTabella(int numBio) {
        return VUOTA;
    }// fine del metodo


    protected String colonneSecondaTabella() {
        return VUOTA;
    }// fine del metodo


    protected String corpoSecondaTabella() {
        StringBuilder testo = new StringBuilder();
        int cont = 1;
        int k = 1;
        String riga;

        for (String plurale : lista) {
            riga = rigaSecondaTabella(plurale, cont);
            if (text.isValid(riga)) {
                testo.append(riga);
                k = k + 1;
                cont = k;
            }// end of if cycle
        }// end of for cycle

        return testo.toString();
    }// fine del metodo


    protected String rigaSecondaTabella(String plurale, int cont) {
        return VUOTA;
    }// fine del metodo


    /**
     * Registra nelle preferenze la data dell'ultimo upload effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo upload effettuato, in minuti <br>
     */
    protected void setLastUpload(long inizio) {
        int delta = 1000 * 60;
        LocalDateTime lastDownload = LocalDateTime.now();
        pref.saveValue(codeLastUpload, lastDownload);

        long fine = System.currentTimeMillis();
        long durata = fine - inizio;
        int minuti = 0;
        if (durata > delta) {
            minuti = (int) durata / delta;
        } else {
            minuti = 0;
        }// end of if/else cycle
        pref.saveValue(codeDurataUpload, minuti);
    }// end of method

}// end of class
