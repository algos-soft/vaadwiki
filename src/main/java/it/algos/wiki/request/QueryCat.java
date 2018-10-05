package it.algos.wiki.request;


import it.algos.wiki.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Query per recuperare le pagine di una categoria
 * NON legge le sottocategorie
 * Non necessita di Login, ma se esiste lo usa
 * Può essere sovrascritta per leggere anche le sottocategorie
 */
public class QueryCat extends QueryWiki {

    protected static final int LIMITE = 5000;
    //--stringa per selezionare il namespace (0=principale - 14=sottocategorie) (per adesso solo il principale)
    protected static String NS_0 = "&cmnamespace=0";
    //--stringa per selezionare il namespace (0=principale - 14=sottocategorie) (per adesso solo il principale)
    protected static String NS_0_14 = "&cmnamespace=0|14";
    //--stringa per la lista di categoria
    private static String CAT = "&list=categorymembers";
    //--stringa per selezionare il tipo di categoria (page, subcat, file) (per adesso solo page)
    private static String TYPE_CAT = "&cmtype=page|subcat";
    private static String TYPE = "&cmtype=page";
    //--stringa per ottenere il codice di continuazione
    private static String CONT = "&rawcontinue";
    //--stringa per selezionare il numero di valori in risposta
    private static String LIMIT = "&cmlimit=";
    //--stringa per indicare il titolo della pagina
    private static String TITLE = "&cmtitle=Category:";
//    //--stringa iniziale (sempre valida) del DOMAIN a cui aggiungere le ulteriori specifiche
//    private static String API_BASE_CAT = API_BASE + CAT + NS + TYPE + CONT + LIMIT;

    //--stringa per il successivo inizio della lista
    private static String CONTINUE = "&cmcontinue=";
    protected int limite;
    protected String namespace;

    // liste di pagine della categoria (namespace=0)
    private ArrayList<Long> listaPageids;
    private ArrayList<String> listaTitles;

    // liste di sottocategorie della categoria (namespace=14)
    private ArrayList<Long> listaCatPageids;
    private ArrayList<String> listaCatTitles;

    private boolean limite5000;
    private boolean vociCategorieTitoli;

    /**
     * Costruttore completo
     */
    public QueryCat(String title) {
        this(title, true);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryCat(String title, boolean vociCategorieTitoli) {
        this(title, LIMITE, vociCategorieTitoli);
    }// fine del metodo costruttore

    /**
     * Costruttore completo
     */
    public QueryCat(String title, int limite, boolean vociCategorieTitoli) {
        this.title = title;
        this.limite = limite;
        this.vociCategorieTitoli = vociCategorieTitoli;
        super.tipoRicerca = TipoRicerca.title;
        super.tipoRequest = TipoRequest.read;
        this.doInit();
    }// fine del metodo costruttore


    @Override
    protected void doInit() {

        if (title != null) {
            pageid = "";
            if (namespace == null) {
                if (vociCategorieTitoli) {
                    namespace = NS_0_14;
                } else {
                    namespace = NS_0;
                }// end of if/else cycle
            }// end of if cycle
            domain = this.getDomain();
        }// fine del blocco if

        try { // prova ad eseguire il codice
            super.firstRequest();
            while (!continua.equals("")) {
                domain = this.getDomain();
                super.firstRequest();
            } // fine del blocco while
        } catch (Exception unErrore) { // intercetta l'errore
            errore = unErrore.getClass().getSimpleName();
        }// fine del blocco try-catch

    } // fine del metodo

    /**
     * Costruisce la stringa della request
     * Domain per l'URL dal titolo della pagina o dal pageid (a seconda del costruttore usato)
     *
     * @return domain
     */
    @Override
    protected String getDomain() {
        String domain = "";
        String titolo = "";
        String startDomain;

        if (vociCategorieTitoli) {
            startDomain = API_BASE + CAT + namespace + TYPE_CAT + LIMIT + limite + TITLE;
        } else {
            startDomain = API_BASE + CAT + namespace + TYPE + LIMIT + limite + TITLE;
        }// end of if/else cycle

        try { // prova ad eseguire il codice
            titolo = URLEncoder.encode(title, Cost.ENC);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        if (!titolo.equals("")) {
            domain = startDomain + titolo;
        }// fine del blocco if

        if (!continua.equals("")) {
            domain += CONTINUE + continua;
        }// fine del blocco if

        return domain;
    } // fine del metodo

    /**
     * Regola il risultato
     * <p>
     * Informazioni, contenuto e validita della risposta
     * Controllo del contenuto (testo) ricevuto
     * PUO essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected void regolaRisultato(String risultatoRequest) {
        testoPrimaRequest = risultatoRequest;
        String txtContinua;
        HashMap<String, ArrayList> mappa = null;
        ArrayList<Long> listaPageidsTmp;
        ArrayList<String> listaTitlesTmp;

        if (LibWiki.isWarnings(risultatoRequest)) {
            setLimite5000(false);
        } else {
            setLimite5000(true);
        }// end of if/else cycle

        if (vociCategorieTitoli) {
            mappa = LibWiki.getMappaWrap(risultatoRequest);

            if (mappa != null) {
//                if (mappa.get(LibWiki.KEY_VOCE_PAGEID) != null && mappa.get(LibWiki.KEY_VOCE_PAGEID).size() > 0) {
//                    this.setListaPageids(LibArray.somma(this.getListaPageids(), mappa.get(LibWiki.KEY_VOCE_PAGEID)));
//                }// end of if cycle
//
//                if (mappa.get(LibWiki.KEY_VOCE_TITLE) != null && mappa.get(LibWiki.KEY_VOCE_TITLE).size() > 0) {
//                    this.setListaTitles(LibArray.somma(this.getListaTitles(), mappa.get(LibWiki.KEY_VOCE_TITLE)));
//                }// end of if cycle
//
//                if (mappa.get(LibWiki.KEY_CAT_PAGEID) != null && mappa.get(LibWiki.KEY_CAT_PAGEID).size() > 0) {
//                    this.setListaCatPageids(LibArray.somma(this.getListaCatPageids(), mappa.get(LibWiki.KEY_CAT_PAGEID)));
//                }// end of if cycle
//
//                if (mappa.get(LibWiki.KEY_CAT_TITLE) != null && mappa.get(LibWiki.KEY_CAT_TITLE).size() > 0) {
//                    this.setListaCatTitles(LibArray.somma(this.getListaCatTitles(), mappa.get(LibWiki.KEY_CAT_TITLE)));
//                }// end of if cycle
            }// end of if cycle

            if (mappa == null) {
                if (Api.esiste("Category:" + title)) {
                    risultato = TipoRisultato.letta;
                } else {
                    risultato = TipoRisultato.nonTrovata;
                }// end of if/else cycle
                valida = false;
                return;
            }// fine del blocco if
        } else {
            listaPageidsTmp = LibWiki.creaListaCatJson(risultatoRequest);

            if (listaPageidsTmp == null) {
                if (Api.esiste("Category:" + title)) {
                    risultato = TipoRisultato.letta;
                } else {
                    risultato = TipoRisultato.nonTrovata;
                }// end of if/else cycle
                valida = false;
                return;
            }// fine del blocco if

            this.addListaPageids(listaPageidsTmp);
        }// end of if/else cycle

        risultato = TipoRisultato.letta;
        valida = true;
        txtContinua = LibWiki.creaCatContinue(risultatoRequest);
        this.continua = txtContinua;
    } // fine del metodo

    private void addListaPageids(ArrayList<Long> listaNew) {
        ArrayList<Long> lista;

        lista = this.getListaPageids();
        if (lista == null) {
            lista = new ArrayList<Long>();
        }// fine del blocco if

        for (Long pageid : listaNew) {
            lista.add(pageid);
        } // fine del ciclo for-each

        this.setListaPageids(lista);
    } // fine del metodo

    private void addListaTitles(ArrayList<String> listaNew) {
        ArrayList<String> lista;

        lista = this.getListaTitles();
        if (lista == null) {
            lista = new ArrayList<String>();
        }// fine del blocco if

        for (String title : listaNew) {
            lista.add(title);
        } // fine del ciclo for-each

        this.setListaTitles(lista);
    } // fine del metodo

    public ArrayList<Long> getListaPageids() {
        return listaPageids;
    } // fine del metodo

    void setListaPageids(ArrayList<Long> listaPageids) {
        this.listaPageids = listaPageids;
    } // fine del metodo

    public ArrayList<String> getListaTitles() {
        return listaTitles;
    }// end of getter method

    public void setListaTitles(ArrayList<String> listaTitles) {
        this.listaTitles = listaTitles;
    }//end of setter method

    public String getTxtPageids() {
        return LibWiki.creaListaPageids(getListaPageids());
    } // fine del metodo

    public ArrayList<Long> getListaCatPageids() {
        return listaCatPageids;
    }// end of getter method

    public void setListaCatPageids(ArrayList<Long> listaCatPageids) {
        this.listaCatPageids = listaCatPageids;
    }//end of setter method

    public ArrayList<String> getListaCatTitles() {
        return listaCatTitles;
    }// end of getter method

    public void setListaCatTitles(ArrayList<String> listaCatTitles) {
        this.listaCatTitles = listaCatTitles;
    }//end of setter method


//    public ArrayList<Long> getListaAllPageids() {
//        return (ArrayList<Long>) LibArray.somma(listaPageids, listaCatPageids);
//    }// end of getter method

//    public ArrayList<String> getListaAllTitles() {
//        return (ArrayList<String>) LibArray.somma(listaTitles, listaCatTitles);
//    }// end of getter method


    public boolean isLimite5000() {
        return limite5000;
    }// end of getter method

    public void setLimite5000(boolean limite5000) {
        this.limite5000 = limite5000;
    }//end of setter method

}// end of class

