package it.algos.wiki;

/**
 * Created by Gac on 05 ago 2015.
 * Using specific Templates (Entity, Domain, Modulo)
 */


import it.algos.vaadflow.backend.entity.AEntity;

import javax.persistence.Entity;
import java.util.Map;

@Entity
public class Pagina extends AEntity {

    private static String QUERY = "query";
    private static String PAGES = "pages";
    private static String MISSING = "missing";
    private static String CONTENT_MODEL = "contentmodel";
    private static String CONTENT_FORMAT = "contentformat";
    private static String EDIT_TOKEN = "edittoken";
    private static String LAST_REV_ID = "lastrevid";
    private static String LENGTH = "length";
    private static String NS = "ns";
    private static String PAGE_ID = "pageid";
    private static String PAGE_LANGUAGE = "pagelanguage";
    private static String REVISIONS = "revisions";
    private static String START_TIME_STAMP = "starttimestamp";
    private static String TITLE = "title";
    private static String TOUCHED = "touched";
    private static String TEXT = "text";

    private static String APICI = "\"";
    private static String PUNTI = ":";
    private static String GRAFFA_INI = "{";
    private static String GRAFFA_END = "}";
    private static String VIR = ",";

    /** nomi interni dei campi (ordine non garantito) */
    //--parametri wiki
    private int pageid;
    private String title;
    private int ns;

    private String contentmodel;
    private String contentformat;
    private String edittoken;
    private int lastrevid;
    private int length;
    private String pagelanguage;
    private String starttimestamp;
    private String touched;    //ultima visita effettuata da chicchessia sul server wiki - attualmente (27-10-13) non utilizzato
    private String testo; //contenuto completo della pagina


    public Pagina() {
        this("");
    }// end of constructor

    public Pagina(String alf) {
        super();
    }// end of constructor

    @Override
    public String toString() {
        return "";
    }// end of method


    /**
     * Recupera una istanza di Pagina usando la query specifica
     *
     * @return istanza di Pagina, null se non trovata
     */
    public static Pagina find(long id) {
        Pagina instance = null;
//        AEntity entity = AQuery.queryById(Pagina.class, id);
//
//        if (entity != null) {
//            if (entity instanceof Pagina) {
//                instance = (Pagina) entity;
//            }// end of if cycle
//        }// end of if cycle

        return instance;
    }// end of method

    /**
     * Recupera una istanza di Pagina usando la query specifica
     *
     * @return istanza di Pagina, null se non trovata
     */
    public static Pagina find(String alf) {
        Pagina instance = null;
//        BaseEntity entity = AQuery.queryOne(Pagina.class, Pagina_.pippo, alf);
//
//        if (entity != null) {
//            if (entity instanceof Pagina) {
//                instance = (Pagina) entity;
//            }// end of if cycle
//        }// end of if cycle

        return instance;
    }// end of method

    public synchronized static int count() {
        int totRec = 0;
//        long totTmp = AQuery.getCount(Pagina.class);
//
//        if (totTmp > 0) {
//            totRec = (int) totTmp;
//        }// fine del blocco if
//
        return totRec;
    }// end of method

//    public synchronized static ArrayList<Pagina> findAll() {
//        return (ArrayList<Pagina>) AQuery.getList(Pagina.class);
//    }// end of method

//    @Override
//    public Pagina clone() throws CloneNotSupportedException {
//        try {
//            return (Pagina) BeanUtils.cloneBean(this);
//        } catch (Exception ex) {
//            throw new CloneNotSupportedException();
//        }// fine del blocco try-catch
//    }// end of method





    /**
     * Estrae i parametri dalla revisione.
     * @mappa
     */
    private void estraeMappaRevisione(Map mappa) {
//        ArrayList obj = mappa.values();
//
//        contentformat = (String) mappa[CONTENT_FORMAT];
//        testo = (String) obj.get(0);
    } // fine del metodo

    public String getJSON() {
        return graffe(getJsonTitle() + VIR + getJsonPageid() + VIR + getJsonNs() + VIR + getJsonLast() + VIR + getJsonStart() + VIR + getJsonTesto());
    }// fine del metodo

    public String getJsonTitle() {
        return apici(TITLE) + PUNTI + apici(title);
    }// fine del metodo

    public String getJsonPageid() {
        return apici(PAGE_ID) + PUNTI + apici(pageid);
    }// fine del metodo

    public String getJsonNs() {
        return apici(NS) + PUNTI + apici(ns);
    }// fine del metodo

    public String getJsonLast() {
        return apici(LAST_REV_ID) + PUNTI + apici(lastrevid);
    }// fine del metodo

    public String getJsonStart() {
        return apici(START_TIME_STAMP) + PUNTI + apici(starttimestamp);
    }// fine del metodo

    public String getJsonTesto() {
        return apici(TEXT) + PUNTI + apici(testo);
    }// fine del metodo

    public String apici(Object entrata) {
        return APICI + entrata + APICI;
    }// fine del metodo

    public String graffe(String entrata) {
        return GRAFFA_INI + entrata + GRAFFA_END;
    }// fine del metodo

    String getTesto() {
        return testo;
    }// fine del metodo

}// end of entity class
