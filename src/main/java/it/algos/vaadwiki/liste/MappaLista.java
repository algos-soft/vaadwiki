package it.algos.vaadwiki.liste;

import com.google.common.collect.Lists;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.wiki.LibWiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.AST;
import static it.algos.vaadwiki.application.WikiCost.TAG_SEP;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 05-dic-2019
 * Time: 21:03
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MappaLista {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AArrayService array;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private SecoloService secoloService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private MeseService meseService;

    //--parametro in ingresso
    private List<WrapDidascalia> listaDidascalie;

    //--parametro in ingresso
    private EADidascalia typeDidascalia;

    //--parametro in ingresso
    private boolean usaSuddivisioneParagrafi;

    //--parametro in ingresso
    private boolean usaRigheRaggruppate;

    //--parametro in ingresso
    private String titoloParagrafoVuoto;

    //--parametro in ingresso
    private boolean paragrafoVuotoInCoda;

    //--parametro in ingresso
    private boolean usaParagrafoSize;

    //--parametro in ingresso
    private boolean usaLinkParagrafo;

    //--parametro in ingresso
    private boolean usaOrdineAlfabetico;

    //--property elaborata
    private List<String> titoloParagrafiDisordinato;

    //--property elaborata
    private List<String> titoloParagrafiOrdinato;

    //--property elaborata
    private List<String> titoloParagrafiDefinitivo;


    //--property elaborata
    private LinkedHashMap<String, Integer> numVociParagrafi;

    //--property elaborata
    private LinkedHashMap<String, Integer> numVociParagrafiDefinitivi;


    //--property elaborata
    private int numVociTotali;

    //--property elaborata
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> mappa;

    //--property elaborata
    private LinkedHashMap<String, List<WrapDidascalia>> mappaWrapUno;

    //--property elaborata
    private LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaWrapDue;

    //--property elaborata
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>>> mappaWrapTre;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public MappaLista() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(MappaLista.class, ..., ...) <br>
     */
    public MappaLista(
            List<WrapDidascalia> listaDidascalie,
            EADidascalia typeDidascalia,
            boolean usaSuddivisioneParagrafi,
            boolean usaRigheRaggruppate,
            String titoloParagrafoVuoto,
            boolean paragrafoVuotoInCoda,
            boolean usaParagrafoSize,
            boolean usaLinkParagrafo,
            boolean usaOrdineAlfabetico) {
        this.listaDidascalie = listaDidascalie;
        this.typeDidascalia = typeDidascalia;
        this.usaSuddivisioneParagrafi = usaSuddivisioneParagrafi;
        this.usaRigheRaggruppate = usaRigheRaggruppate;
        this.titoloParagrafoVuoto = titoloParagrafoVuoto;
        this.paragrafoVuotoInCoda = paragrafoVuotoInCoda;
        this.usaParagrafoSize = usaParagrafoSize;
        this.usaLinkParagrafo = usaLinkParagrafo;
        this.usaOrdineAlfabetico = usaOrdineAlfabetico;
    }// end of constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(MappaLista.class, ..., ...) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    private void postConstruct() {

        //--crea la mappa wrap di primo livello
        creaMappaWrapUno();

        //--crea la mappa wrap di secondo livello
        creaMappaWrapDue();

        //--crea la mappa wrap di terzo livello
        creaMappaWrapTre();

        //--dimensioni della mappa
        calcolaDimensioni();

        //--ordinamento dei titoli dei paragrafi
        //--ordinamento alfabetico delle sottopagine
        ordinaChiavi();

        //--ordinamento delle liste
        ordinaListe();

        //--creazione della mappa finale
        creaMappaFinale();
    }// end of method


    /**
     * Crea la mappa mappaWrapUno, primo livello <br>
     * I paragrafi hanno titolo='chiaveParagrafo' della WrapDidascalia <br>
     */
    private void creaMappaWrapUno() {
        String key;

        if (listaDidascalie != null) {
            mappaWrapUno = new LinkedHashMap<>();
            for (WrapDidascalia wrap : listaDidascalie) {
                key = wrap.chiaveParagrafo;
                add(mappaWrapUno, wrap, key);
            }// end of for cycle
        }// end of if cycle

    }// fine del metodo


    /**
     * Crea la mappa mappaWrapDue, secondo livello <br>
     * La sottopagina ha titolo='chiaveSottoPagina' della WrapDidascalia <br>
     */
    private void creaMappaWrapDue() {
        List<WrapDidascalia> listaDisordinata;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPagina;
        String key;

        if (mappaWrapUno != null) {
            mappaWrapDue = new LinkedHashMap<>();
            for (String chiaveParagrafo : mappaWrapUno.keySet()) {
                mappaSottoPagina = new LinkedHashMap<>();
                listaDisordinata = mappaWrapUno.get(chiaveParagrafo);
                if (listaDisordinata != null && listaDisordinata.size() > 0) {
                    for (WrapDidascalia wrap : listaDisordinata) {
                        key = wrap.chiaveSottoPagina;
                        add(mappaSottoPagina, wrap, key);
                    }// end of for cycle
                }// end of if cycle
                mappaWrapDue.put(chiaveParagrafo, mappaSottoPagina);
            }// end of for cycle
        }// end of if cycle

    }// fine del metodo


    /**
     * Crea la mappa mappaWrapTre, terzo livello <br>
     * La sottosottopagina ha titolo='chiaveSottoSottoPagina' della WrapDidascalia <br>
     */
    private void creaMappaWrapTre() {
        List<WrapDidascalia> listaDisordinata;
        LinkedHashMap<String, List<WrapDidascalia>> mappaInternaWrapDue;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaSottoPagina;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoSottoPagina;
        String key;

        if (mappaWrapDue != null) {
            mappaWrapTre = new LinkedHashMap<>();
            for (String chiaveParagrafo : mappaWrapDue.keySet()) {
                mappaInternaWrapDue = mappaWrapDue.get(chiaveParagrafo);
                mappaSottoPagina = new LinkedHashMap<>();
                if (mappaInternaWrapDue != null && mappaInternaWrapDue.size() > 0) {
                    for (String chiaveSottoPagina : mappaInternaWrapDue.keySet()) {
                        mappaSottoSottoPagina = new LinkedHashMap<>();
                        listaDisordinata = mappaInternaWrapDue.get(chiaveSottoPagina);
                        if (listaDisordinata != null && listaDisordinata.size() > 0) {
                            for (WrapDidascalia wrap : listaDisordinata) {
                                key = wrap.chiaveSottoSottoPagina;
                                add(mappaSottoSottoPagina, wrap, key);
                            }// end of for cycle
                        }// end of if cycle
                        mappaSottoPagina.put(chiaveSottoPagina, mappaSottoSottoPagina);
                    }// end of for cycle
                }// end of if cycle
                mappaWrapTre.put(chiaveParagrafo, mappaSottoPagina);
            }// end of for cycle
        }// end of if cycle

    }// fine del metodo


    /**
     * Ordinamento dei titoli dei paragrafi <br>
     * Costruisce la lista dei paragrafi esistenti dalla mappa <br>
     * Ordina i paragrafi in base al tipo di lista (alfabetico, per anno, per giorno) <br>
     * Mette in coda (eventualmente) il paragrafo dal titolo vuoto <br>
     * Sostituisce il titolo del paragrafo vuoto <br>
     * Costruisce i titoli definitivi dei paragrafi (link a wikipagine, quadre e dimensioni del paragrafo) <br>
     * Sostituisce i titoli definitivi nella mappa <br>
     */
    private void ordinaChiavi() {
        titoloParagrafiDisordinato = new ArrayList<>();
        titoloParagrafiOrdinato = new ArrayList<>();
        titoloParagrafiDefinitivo = new ArrayList<>();
        numVociParagrafiDefinitivi = new LinkedHashMap<>();

        ordinaPrimoLivello();
        ordinaSecondoLivello();
        ordinaTerzoLivello();
    }// fine del metodo


    /**
     * Ordinamento delle chiavi di primo livello <br>
     * Costruisce la lista dei paragrafi esistenti dalla mappa <br>
     * Ordina i paragrafi in base al tipo di lista (alfabetico, per anno, per giorno) <br>
     * Mette in coda (eventualmente) il paragrafo dal titolo vuoto <br>
     * Sostituisce il titolo del paragrafo vuoto <br>
     * Costruisce i titoli definitivi dei paragrafi (link a wikipagine, quadre e dimensioni del paragrafo) <br>
     * Sostituisce i titoli definitivi nella mappa <br>
     */
    private void ordinaPrimoLivello() {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>>> mappa = null;

        if (array.isValid(mappaWrapUno)) {
            titoloParagrafiDisordinato.addAll(mappaWrapUno.keySet());
        }// end of if cycle

        if (titoloParagrafiDisordinato != null && titoloParagrafiDisordinato.size() > 0) {
            switch (typeDidascalia) {
                case giornoNato:
                case giornoMorto:
                    titoloParagrafiOrdinato = secoloService.riordina(titoloParagrafiDisordinato);
                    break;
                case annoNato:
                case annoMorto:
                    titoloParagrafiOrdinato = meseService.riordina(titoloParagrafiDisordinato);
                    break;
                case listaNomi:
                case listaCognomi:
                    titoloParagrafiOrdinato = titoloParagrafiDisordinato;
                    titoloParagrafiOrdinato = array.sort(titoloParagrafiOrdinato);
                    break;
                default:
                    titoloParagrafiOrdinato = titoloParagrafiDisordinato;
                    break;
            } // end of switch statement
        }// end of if cycle

        if (paragrafoVuotoInCoda && titoloParagrafiOrdinato != null) {
            if (titoloParagrafiOrdinato.contains(VUOTA)) {
                titoloParagrafiOrdinato.remove(VUOTA);
                titoloParagrafiOrdinato.add(VUOTA);
            }// end of if cycle
        }// end of if cycle

        if (mappaWrapTre != null && titoloParagrafiOrdinato != null) {
            mappa = new LinkedHashMap<>();
            for (String titolo : titoloParagrafiOrdinato) {
                mappa.put(titoloDefinitivoParagrafo(titolo), mappaWrapTre.get(titolo));
            }// end of for cycle
        }// end of if cycle

        mappaWrapTre = mappa;
    }// fine del metodo


    /**
     * Ordinamento delle chiavi di secondo livello <br>
     * Ordinamento alfabetico <br>
     * Sostituisce le chiavi nella sotto-mappa <br>
     */
    private void ordinaSecondoLivello() {
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafo;

        if (mappaWrapTre != null && mappaWrapTre.size() > 0) {
            for (String chiaveParagrafo : mappaWrapTre.keySet()) {
                mappaParagrafo = mappaWrapTre.get(chiaveParagrafo);
                if (mappaParagrafo != null && mappaParagrafo.size() > 0) {

                    mappaWrapTre.put(chiaveParagrafo, ordinaMappaDue(mappaParagrafo));

                }// end of if cycle
            }// end of for cycle
        }// end of if cycle
    }// fine del metodo


    /**
     * Ordinamento delle chiavi di terzo livello <br>
     * Ordinamento alfabetico <br>
     * Sostituisce le chiavi nella sotto-mappa <br>
     */
    private void ordinaTerzoLivello() {
    }// fine del metodo


    /**
     * Ordinamento delle chiavi di secondo livello <br>
     * Ordinamento alfabetico <br>
     * Sostituisce le chiavi nella sotto-mappa <br>
     */
    private LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> ordinaMappaDue(LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaDisordinata) {
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaOut = null;
        List<String> listaChiavi = null;

        if (mappaDisordinata != null && mappaDisordinata.size() > 0) {
            mappaOut = new LinkedHashMap<>();
            listaChiavi = Lists.newArrayList(mappaDisordinata.keySet());
            listaChiavi = array.sort(listaChiavi);
            for (String titolo : listaChiavi) {
                mappaOut.put(titolo, mappaDisordinata.get(titolo));
            }// end of for cycle
        }// end of if cycle

        return mappaOut;
    }// fine del metodo


    /**
     * Ordinamento delle chiavi di terzo livello <br>
     * Ordinamento alfabetico <br>
     * Sostituisce le chiavi nella sotto-mappa <br>
     */
    private LinkedHashMap<String, List<WrapDidascalia>> ordinaMappaTre(LinkedHashMap<String, List<WrapDidascalia>> mappaDisordinata) {
        LinkedHashMap<String, List<WrapDidascalia>> mappaOut = null;
        List<String> listaChiavi = null;

        if (mappaDisordinata != null && mappaDisordinata.size() > 0) {
            mappaOut = new LinkedHashMap<>();
            listaChiavi = Lists.newArrayList(mappaDisordinata.keySet());
            listaChiavi = array.sort(listaChiavi);
            for (String titolo : listaChiavi) {
                mappaOut.put(titolo, mappaDisordinata.get(titolo));
            }// end of for cycle
        }// end of if cycle

        return mappaOut;
    }// fine del metodo


    /**
     * Costruzione del titolo definitivo dei paragrafi <br>
     * Eventuale link a wikipagine <br>
     * Doppie quadre <br>
     * Eventuali dimensioni del paragrafo <br>
     */
    private String titoloDefinitivoParagrafo(String titolo) {
        String titoloDefinitivo = VUOTA;

        if (text.isValid(titoloParagrafoVuoto) && titolo.equals(VUOTA)) {
            titoloDefinitivo = titoloParagrafoVuoto;
        } else {
            if (usaLinkParagrafo) {
                titoloDefinitivo = LibWiki.setQuadre(titolo);
            } else {
                titoloDefinitivo = titolo;
            }// end of if/else cycle
        }// end of if/else cycle

        if (usaParagrafoSize && numVociParagrafi != null && numVociParagrafi.size() > 0) {
            if (numVociParagrafi.get(titolo) > 0) {
                titoloDefinitivo += " <small><small>(" + numVociParagrafi.get(titolo) + ")</small></small>";
            }// end of if cycle
        }// end of if cycle

        titoloParagrafiDefinitivo.add(titoloDefinitivo);
        numVociParagrafiDefinitivi.put(titoloDefinitivo, numVociParagrafi.get(titolo));

        return titoloDefinitivo;
    }// fine del metodo


    /**
     * Dimensioni della mappa <br>
     */
    private void calcolaDimensioni() {
        List<WrapDidascalia> listaDisordinata;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafo;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPagina;

        if (mappaWrapTre != null) {
            numVociParagrafi = new LinkedHashMap<>();
            numVociTotali = 0;
            for (String chiaveParagrafo : mappaWrapTre.keySet()) {
                mappaParagrafo = mappaWrapTre.get(chiaveParagrafo);
                if (mappaParagrafo != null && mappaParagrafo.size() > 0) {
                    for (String chiaveSottoPagina : mappaParagrafo.keySet()) {
                        mappaSottoPagina = mappaParagrafo.get(chiaveSottoPagina);
                        if (mappaSottoPagina != null && mappaSottoPagina.size() > 0) {
                            for (String chiaveSottoSottoPagina : mappaSottoPagina.keySet()) {
                                listaDisordinata = mappaSottoPagina.get(chiaveSottoSottoPagina);
                                if (listaDisordinata != null && listaDisordinata.size() > 0) {
                                    if (numVociParagrafi.get(chiaveParagrafo) != null) {
                                        numVociParagrafi.put(chiaveParagrafo, numVociParagrafi.get(chiaveParagrafo) + listaDisordinata.size());
                                    } else {
                                        numVociParagrafi.put(chiaveParagrafo, listaDisordinata.size());
                                    }// end of if/else cycle
                                    numVociTotali += listaDisordinata.size();
                                }// end of if cycle
                            }// end of for cycle
                        }// end of if cycle
                    }// end of for cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

    }// fine del metodo


    /**
     * Costruzione del titolo definitivo dei paragrafi <br>
     */
    private void ordinaListe() {
        List<WrapDidascalia> listaDisordinata;
        List<WrapDidascalia> listaOrdinata;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafo;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPagina;

        if (mappaWrapTre != null) {
            for (String chiaveParagrafo : mappaWrapTre.keySet()) {
                mappaParagrafo = mappaWrapTre.get(chiaveParagrafo);
                if (mappaParagrafo != null && mappaParagrafo.size() > 0) {
                    for (String chiaveSottoPagina : mappaParagrafo.keySet()) {
                        mappaSottoPagina = mappaParagrafo.get(chiaveSottoPagina);
                        if (mappaSottoPagina != null && mappaSottoPagina.size() > 0) {
                            for (String chiaveSottoSottoPagina : mappaSottoPagina.keySet()) {
                                listaDisordinata = mappaSottoPagina.get(chiaveSottoSottoPagina);
                                listaOrdinata = ordinaListaWrap(listaDisordinata);
                                mappaSottoPagina.put(chiaveSottoSottoPagina, listaOrdinata);
                            }// end of for cycle
                        }// end of if cycle
                        mappaParagrafo.put(chiaveSottoPagina, mappaSottoPagina);
                    }// end of for cycle
                }// end of if cycle
                mappaWrapTre.put(chiaveParagrafo, mappaParagrafo);
            }// end of for cycle
        }// end of if cycle

    }// fine del metodo


    /**
     * Ordinamento alfabetico ?
     */
    private List<WrapDidascalia> ordinaListaWrap(List<WrapDidascalia> listaDisordinata) {
        List<WrapDidascalia> listaOrdinata = listaDisordinata;

        return listaOrdinata;
    }// fine del metodo


    /**
     * Costruzione della mappa definitiva <br>
     * Sostituisce il testo alla WrapDidascalia <br>
     */
    private void creaMappaFinale() {
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafoWrap;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPaginaWrap;
        List<WrapDidascalia> listaOrdinataWrap;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafoTxt;
        LinkedHashMap<String, List<String>> mappaSottoPaginaTxt;
        List<String> listaTxt;

        if (mappaWrapTre != null) {
            mappa = new LinkedHashMap<>();
            for (String chiaveParagrafo : mappaWrapTre.keySet()) {
                mappaParagrafoWrap = mappaWrapTre.get(chiaveParagrafo);
                mappaParagrafoTxt = new LinkedHashMap<>();
                if (mappaParagrafoWrap != null && mappaParagrafoWrap.size() > 0) {
                    for (String chiaveSottoPagina : mappaParagrafoWrap.keySet()) {
                        mappaSottoPaginaWrap = mappaParagrafoWrap.get(chiaveSottoPagina);
                        mappaSottoPaginaTxt = new LinkedHashMap<>();
                        if (mappaSottoPaginaWrap != null && mappaSottoPaginaWrap.size() > 0) {
                            for (String chiaveSottoSottoPagina : mappaSottoPaginaWrap.keySet()) {
                                listaOrdinataWrap = mappaSottoPaginaWrap.get(chiaveSottoSottoPagina);
                                listaTxt = creaRighe(listaOrdinataWrap);
                                mappaSottoPaginaTxt.put(chiaveSottoSottoPagina, listaTxt);
                            }// end of for cycle
                        }// end of if cycle
                        mappaParagrafoTxt.put(chiaveSottoPagina, mappaSottoPaginaTxt);
                    }// end of for cycle
                }// end of if cycle
                mappa.put(chiaveParagrafo, mappaParagrafoTxt);
            }// end of for cycle

        }// end of if cycle
    }// fine del metodo


    private List<String> creaRighe(List<WrapDidascalia> listaWrapDidascalie) {
        List<String> listaTxt = null;
        LinkedHashMap<String, Integer> mappa;

        if (array.isValid(listaWrapDidascalie)) {
            listaTxt = new ArrayList<>();
            if (listaWrapDidascalie.size() == 1) {
                creaRigaConChiave(listaTxt, listaWrapDidascalie.get(0));
            } else {
                if (usaRigheRaggruppate) {
                    mappa = creaMappaChiavi(listaWrapDidascalie);
                    for (String key : mappa.keySet()) {
                        if (mappa.get(key) == 1) {
                            creaRigaConChiave(listaTxt, listaWrapDidascalie, key);
                        } else {
                            creaRigheMultiple(listaTxt, listaWrapDidascalie, key);
                        }// end of if/else cycle
                    }// end of for cycle
                } else {
                    for (WrapDidascalia wrap : listaWrapDidascalie) {
                        creaRigaConChiave(listaTxt, wrap);
                    }// end of for cycle
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if cycle

        return listaTxt;
    }// fine del metodo


    /**
     * Conta quante righe ci sono per ogni chiave <br>
     */
    private LinkedHashMap<String, Integer> creaMappaChiavi(List<WrapDidascalia> listaWrapDidascalie) {
        LinkedHashMap<String, Integer> mappa = null;
        String key;

        if (listaWrapDidascalie != null) {
            mappa = new LinkedHashMap<>();
            for (WrapDidascalia wrap : listaWrapDidascalie) {
                key = wrap.chiaveRiga;
                if (mappa.containsKey(key)) {
                    mappa.put(key, mappa.get(key) + 1);
                } else {
                    mappa.put(key, 1);
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        return mappa;
    }// fine del metodo


    /**
     * Riga singola con la chiave iniziale <br>
     * Ne esiste solo una con questa chiave <br>
     */
    private void creaRigaConChiave(List<String> listaTxt, WrapDidascalia wrap) {
        String testoRiga = VUOTA;

        testoRiga += AST;
        if (text.isValid(wrap.chiaveRiga)) {
            testoRiga += LibWiki.setQuadre(wrap.chiaveRiga);
            testoRiga += TAG_SEP;
        }// end of if cycle
        testoRiga += wrap.getTestoSenza();

        listaTxt.add(testoRiga);
    }// fine del metodo


    /**
     * Riga singola senza la chiave iniziale <br>
     * Ne esistono più di una con la stessa chiave <br>
     * La chiave viene scritta (da sola) nella riga iniziale del gruppo con la stessa chiave <br>
     */
    private void creaRigaSenzaChiave(List<String> listaTxt, WrapDidascalia wrap) {
        String testoRiga = VUOTA;

        testoRiga += AST;
        testoRiga += AST;
        testoRiga += wrap.getTestoSenza();

        listaTxt.add(testoRiga);
    }// fine del metodo


    /**
     * Riga singola con la chiave iniziale <br>
     * Ne esiste solo una con questa chiave <br>
     */
    private void creaRigaConChiave(List<String> listaTxt, List<WrapDidascalia> listaWrapDidascalie, String key) {
        List<WrapDidascalia> listaWrap = selezionaListaWrap(listaWrapDidascalie, key);

        if (listaWrap != null && listaWrap.size() == 1) {
            creaRigaConChiave(listaTxt, listaWrap.get(0));
        }// end of if cycle
    }// fine del metodo


    /**
     * Gruppo di righe con la stessa chiave <br>
     * Nella prima riga la chiave, da sola <br>
     * Nelle righe successive, le singole didascalie senza la chiave <br>
     */
    private void creaRigheMultiple(List<String> listaTxt, List<WrapDidascalia> listaWrapDidascalie, String key) {
        String testoRiga = VUOTA;
        List<WrapDidascalia> listaWrap = selezionaListaWrap(listaWrapDidascalie, key);

        if (text.isValid(key)) {
            testoRiga += AST;
            testoRiga += LibWiki.setQuadre(key);
            listaTxt.add(testoRiga);

            if (listaWrap != null && listaWrap.size() > 1) {
                for (WrapDidascalia wrap : selezionaListaWrap(listaWrapDidascalie, key)) {
                    creaRigaSenzaChiave(listaTxt, wrap);
                }// end of for cycle
            }// end of if cycle
        } else {
            if (listaWrap != null && listaWrap.size() > 1) {
                for (WrapDidascalia wrap : selezionaListaWrap(listaWrapDidascalie, key)) {
                    testoRiga = AST;
                    testoRiga += wrap.getTestoSenza();
                    listaTxt.add(testoRiga);
                }// end of for cycle
            }// end of if cycle
        }// end of if/else cycle

    }// fine del metodo


    /**
     * Seleziona una lista di WrapDidascalia con la chiave richiesta <br>
     */
    private List<WrapDidascalia> selezionaListaWrap(List<WrapDidascalia> listaWrapDidascalie, String key) {
        List<WrapDidascalia> listaWrap = null;

        if (listaWrapDidascalie != null && listaWrapDidascalie.size() > 0) {
            listaWrap = new ArrayList<>();
            for (WrapDidascalia wrap : listaWrapDidascalie) {
                if (wrap.chiaveRiga.equals(key)) {
                    listaWrap.add(wrap);
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaWrap;
    }// fine del metodo


    private void add(HashMap<String, List<WrapDidascalia>> mappa, WrapDidascalia wrap, String key) {
        List<WrapDidascalia> listaDisordinata;

        if (mappa.get(key) == null) {
            listaDisordinata = new ArrayList<>();
        } else {
            listaDisordinata = mappa.get(key);
        }// end of if/else cycle
        listaDisordinata.add(wrap);
        mappa.put(key, listaDisordinata);
    }// fine del metodo


    public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> getMappa() {
        return mappa;
    }// fine del metodo


    public int getNumVoci() {
        return numVociTotali;
    }// fine del metodo


    public List<String> getTitoloParagrafiDisordinato() {
        return titoloParagrafiDisordinato != null ? titoloParagrafiDisordinato : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoloParagrafiOrdinato() {
        return titoloParagrafiOrdinato != null ? titoloParagrafiOrdinato : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoloParagrafiDefinitivo() {
        return titoloParagrafiDefinitivo != null ? titoloParagrafiDefinitivo : new ArrayList<>();
    }// fine del metodo


    public List<Integer> getDimParagrafi() {
        List<Integer> lista = new ArrayList<>();

        if (numVociParagrafiDefinitivi != null) {
            for (String titolo : numVociParagrafiDefinitivi.keySet()) {
                lista.add(numVociParagrafiDefinitivi.get(titolo));
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo


    /**
     * Lista di righe <br>
     */
    private List<String> getLista() {
        List<String> listaRighe = null;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafoTxt;
        LinkedHashMap<String, List<String>> mappaSottoPaginaTxt;
        List<String> listaTxt = null;

        if (mappa != null) {
            listaRighe = new ArrayList<>();
            for (String chiaveParagrafo : mappa.keySet()) {
                mappaParagrafoTxt = mappa.get(chiaveParagrafo);
                if (mappaParagrafoTxt != null && mappaParagrafoTxt.size() > 0) {
                    for (String chiaveSottoPagina : mappaParagrafoTxt.keySet()) {
                        mappaSottoPaginaTxt = mappaParagrafoTxt.get(chiaveSottoPagina);
                        if (mappaSottoPaginaTxt != null && mappaSottoPaginaTxt.size() > 0) {
                            for (String chiaveSottoSottoPagina : mappaSottoPaginaTxt.keySet()) {
                                listaTxt = mappaSottoPaginaTxt.get(chiaveSottoSottoPagina);
                                listaRighe.addAll(listaTxt);
                            }// end of for cycle
                        }// end of if cycle
                    }// end of for cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaRighe;
    }// fine del metodo


    /**
     * Testo finale della pagina <br>
     * Con o senza suddivisione per paragrafi <br>
     * Con o senza righe raggruppate <br>
     * Con o senza wikilink nel titolo dei paragrafi <br>
     * Con o senza dimensioni nel titolo dei paragrafi <br>
     * Col paragrafo senza titolo per primo o per ultimo <br>
     * Se si usano le sottopagine riporta solo il link alla sottopagina. La lista delle biografie è in altra mappa <br>
     */
    public String getTesto() {
        StringBuilder testoLista = new StringBuilder();
        List<String> listaRighe = getLista();

        if (listaRighe != null && listaRighe.size() > 0) {
            for (String riga : listaRighe) {
                testoLista.append(riga);
                testoLista.append(A_CAPO);
            }// end of for cycle
        }// end of if cycle

        return testoLista.toString().trim();
    }// fine del metodo

}// end of class
