package it.algos.wiki;

/**
 * Legge una pagina wiki
 * Non serve il login
 * Legge solamente e NON scrive
 * Accetta sia il titolo che il pageid
 */
public class ReadWiki extends Read {

//    // codifica dei caratteri
//    private static String ENCODE = "UTF-8";
//
//    // tag per la costruzione della stringa della request
//    private static String TAG_INI = "https://it.wikipedia.org/w/api.php?format=json&action=query";
//    private static String TAG_PROP = "&prop=info|revisions&rvprop=content";
//    private static String TAG_QUERY = TAG_INI + TAG_PROP;
//    private static String TAG_TITOLO = "&titles=";
//    private static String TAG_PAGEID = "&pageids=";
//
//    // controllo del contenuto
//    private static String TAG_MISSING = "{\"batchcomplete\":\"\",\"query\":{\"pages\":{\"-1\"";
//    private static String TAG_MISSING_2 = "{\"batchcomplete\":\"\",\"query\":{\"pages\":[{\"pageid\":0,\"missing\":\"\"}]}}";
//
//    // titolo o pageid della pagina
//    private String titlePageid;
//    private TipoRicerca tipoRicerca;
//
//    /**
//     * Costruttore di default per il sistema (a volte serve)
//     */
//    public ReadWiki() {
//    }// fine del metodo costruttore
//
//    /**
//     * Costruttore con parametri ridotti
//     * <p>
//     * Costruisce un'istanza della superclasse con un costruttore senza parametri
//     * Regola alcune property
//     * Regola il domain
//     * Invoca il metodo inizializza() della superclasse
//     */
//    public ReadWiki(String title) {
//        super();
//        this.titlePageid = title;
//        this.tipoRicerca = TipoRicerca.title;
//        this.setDomain();
//        super.inizializza();
//    }// fine del metodo costruttore
//
//    /**
//     * Costruttore con parametri ridotti
//     * <p>
//     * Costruisce un'istanza della superclasse con un costruttore senza parametri
//     * Regola alcune property
//     * Regola il domain
//     * Invoca il metodo inizializza() della superclasse
//     */
//    public ReadWiki(int pageId) {
//        super();
//        this.titlePageid = "" + pageId;
//        this.tipoRicerca = TipoRicerca.pageid;
//        this.setDomain();
//        super.inizializza();
//    }// fine del metodo costruttore
//
//    /**
//     * Costruttore completo
//     */
//    public ReadWiki(String titlePageid, TipoRicerca tipoRicerca) {
//        super();
//        this.titlePageid = titlePageid;
//        this.tipoRicerca = tipoRicerca;
//        this.setDomain();
//        super.inizializza();
//    }// fine del metodo costruttore
//
//
//    /**
//     * Costruisce la stringa della request
//     */
//    private void setDomain() {
//        String domain = "";
//        String tipoQueryTxt = "";
//        String titolo = "";
//
//        if (tipoRicerca != null) {
//            tipoQueryTxt = tipoRicerca.toString();
//        }// fine del blocco if
//
//        try { // prova ad eseguire il codice
//            titolo = URLEncoder.encode(titlePageid, ENCODE);
//        } catch (Exception unErrore) { // intercetta l'errore
//        }// fine del blocco try-catch
//
//        if (!tipoQueryTxt.equals("")) {
//            if (tipoQueryTxt.equals(TipoRicerca.title.toString())) {
//                domain = TAG_QUERY + TAG_TITOLO + titolo;
//            }// fine del blocco if
//            if (tipoQueryTxt.equals(TipoRicerca.pageid.toString())) {
//                domain = TAG_QUERY + TAG_PAGEID + titolo;
//            }// fine del blocco if
//        }// fine del blocco if
//
//        this.domain = domain;
//    } // fine del metodo
//
//    @Override
//    protected boolean isValida() {
//        boolean valida = true;
//
//        if (contenuto == null) {
//            return false;
//        }// fine del blocco if
//
//        if (contenuto.equals("")) {
//            valida = false;
//        }// fine del blocco if
//
//        if (contenuto.length() < 200) {
//            if (contenuto.startsWith(TAG_MISSING) || contenuto.contains("missing")) {
//                valida = false;
//            }// fine del blocco if
//        }// fine del blocco if
//
//        return valida;
//    } // end of getter method
//
//    public String getContenuto() {
//        return contenuto;
//    }
//
//    public boolean isTrovata() {
//        return trovata;
//    }
} // fine della classe
