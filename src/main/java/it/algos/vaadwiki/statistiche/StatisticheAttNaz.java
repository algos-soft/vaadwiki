package it.algos.vaadwiki.statistiche;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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

    protected List<String> listaPlurali;

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

        //--testo prima della tabella
        testo += inizioParagrafo(numBio);

        //--tabella
        testo += A_CAPO;
        testo += creaTabella();
        testo += A_CAPO;

        testo += "==Note==";
        testo += A_CAPO;
        testo += "<references/>";

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Testo descrittivo prima della tabella tabella <br>
     */
    protected String inizioParagrafo(int numBio) {
        return VUOTA;
    }// fine del metodo


    private String creaTabella() {
        String testo = "";

        testo += inizioTabella();
        testo += colonneTabella();
        testo += corpoTabella();
        testo += fineTabella();

        return testo;
    }// fine del metodo


    private String inizioTabella() {
        String testo = "";

        testo += A_CAPO;
        testo += "{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF;\"";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String colonneTabella() {
        return VUOTA;
    }// fine del metodo


    private String corpoTabella() {
        StringBuilder testo = new StringBuilder();
        int cont = 0;
        int k = 0;

        for (String plurale : listaPlurali) {
            k = k + 1;
            cont = k;
            testo.append(rigaTabella(plurale, cont));
        }// end of for cycle

        return testo.toString();
    }// fine del metodo


    protected String rigaTabella(String plurale, int cont) {
        return VUOTA;
    }// fine del metodo


    private String fineTabella() {
        String testo = "";

        testo += "|}";
        testo += A_CAPO;

        return testo;
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
