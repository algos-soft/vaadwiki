package it.algos.vaadwiki.liste;

import com.google.common.collect.Lists;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.LibWiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
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
    public ApplicationContext appContext;

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
    private PreferenzaService pref;

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
    private ListaService listaService;

//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring <br>
//     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
//     */
//    @Autowired
//    private MeseService meseService;

    //--parametro in ingresso
    private String soggetto;

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
    private boolean usaLinkParagrafo;

    //--parametro in ingresso
    private boolean usaParagrafoSize;

    //--parametro in ingresso
    private boolean usaSottopagine;

    //--property elaborata
    @Deprecated
    private List<String> titoloParagrafiDisordinato;

    //--property elaborata
    @Deprecated
    private List<String> titoloParagrafiOrdinato;

    //--property elaborata
    @Deprecated
    private List<String> titoloParagrafiDefinitivo;

    //--property elaborata
    private LinkedHashMap<String, Integer> numVociParagrafi;

    //--property elaborata
    @Deprecated
    private LinkedHashMap<String, Integer> numVociParagrafiDefinitivi;

    //--property elaborata
    private TitoliLista titoliLista;

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

    //--property elaborata
    private int taglioSottoPagina;


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
     * Senza paragrafi <br>
     */
    public MappaLista(List<WrapDidascalia> listaDidascalie, EADidascalia typeDidascalia, boolean usaRigheRaggruppate, boolean paragrafoVuotoInCoda) {
        this(VUOTA, listaDidascalie, typeDidascalia, usaRigheRaggruppate, paragrafoVuotoInCoda, false, VUOTA, false, false, false, 0);
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(MappaLista.class, ..., ...) <br>
     * Con paragrafi <br>
     */
    public MappaLista(
            String soggetto,
            List<WrapDidascalia> listaDidascalie,
            EADidascalia typeDidascalia,
            boolean usaRigheRaggruppate,
            boolean paragrafoVuotoInCoda,
            boolean usaSuddivisioneParagrafi,
            String titoloParagrafoVuoto,
            boolean usaLinkParagrafo,
            boolean usaParagrafoSize,
            boolean usaSottopagine,
            int taglioSottoPagina) {
        this.soggetto = soggetto;
        this.listaDidascalie = listaDidascalie;
        this.typeDidascalia = typeDidascalia;
        this.usaRigheRaggruppate = usaRigheRaggruppate;
        this.paragrafoVuotoInCoda = paragrafoVuotoInCoda;
        this.usaSuddivisioneParagrafi = usaSuddivisioneParagrafi;
        this.titoloParagrafoVuoto = titoloParagrafoVuoto;
        this.usaLinkParagrafo = usaLinkParagrafo;
        this.usaParagrafoSize = usaParagrafoSize;
        this.usaSottopagine = usaSottopagine;
        this.taglioSottoPagina = taglioSottoPagina > 0 ? taglioSottoPagina : Lista.TAGLIO_SOTTOPAGINA_DEFAULT;
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

        //--crea i titoli
        creaTitoli();

        //--crea la mappa wrap di secondo livello
        creaMappaWrapDue();

        //--crea la mappa wrap di terzo livello
        creaMappaWrapTre();

        //--ordinamento dei titoli dei paragrafi
        titoliLista.ordina();

        //--ordinamento alfabetico delle sottopagine
        ordinaChiavi();

        //--dimensioni della mappa
        calcolaDimensioni();

        //--ordinamento delle liste
        ordinaListe();

        titoliLista.definitivi(numVociParagrafi);

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
                key = wrap.chiaveParagrafo.toLowerCase();
                add(mappaWrapUno, wrap, key);
            }// end of for cycle
        }// end of if cycle
    }// fine del metodo


    /**
     * Costruzione dei titoli dei paragrafi <br>
     * aggiunge il link alla pagina di wikipedia per la professione di riferimento <br>
     * se le didascalie non sono omogenee (non puntano alla stessa pagina di link), non mette il link <br>
     * aggiunge le parentesi quadre <br>
     * se il titolo del paragrafo è vuoto, sostituisce col titolo previsto standard <br>
     */
    private void creaTitoli() {
        LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafiTitolo = new LinkedHashMap<>();
        String chiave = "";
        List<String> listaProfessioni;
        List<String> listaVisibili;
//        List<WrapDidascalia> listaWrap;
        List<Titolo> listaTitoli;
        Titolo titolo;
        String professione;
        String pagina;
        String visibile;

        if (mappaWrapUno != null) {
            listaTitoli = new ArrayList();
            for (String chiaveParagrafo : mappaWrapUno.keySet()) {

                listaProfessioni = new ArrayList();
                listaVisibili = new ArrayList();
                pagina = VUOTA;
                professione = VUOTA;
                visibile = VUOTA;

                for (WrapDidascalia wrap : mappaWrapUno.get(chiaveParagrafo)) {
                    chiave = listaService.getTitoloParagrafo(wrap.bio);
                    professione = listaService.getProfessioneDaBio(wrap.bio);
                    if (!listaProfessioni.contains(professione)) {
                        listaProfessioni.add(professione);
                    }// end of if cycle
                    visibile = listaService.getGenereDaBio(wrap.bio);
                    if (!listaVisibili.contains(visibile)) {
                        listaVisibili.add(visibile);
                    }// end of if cycle
                }// end of for cycle

                if (listaProfessioni.size() == 1) {
                    pagina = listaProfessioni.get(0);
                } else {
                    pagina = VUOTA;
                }// end of if/else cycle
                visibile = listaVisibili.get(0);
                chiave = text.isValid(chiave) ? chiave : titoloParagrafoVuoto;
                titolo = new Titolo(chiaveParagrafo, pagina, visibile);
                listaTitoli.add(titolo);
                mappaParagrafiTitolo.put(chiave, mappaWrapUno.get(chiaveParagrafo));
            }// end of for cycle
            titoliLista = appContext.getBean(TitoliLista.class, listaTitoli, typeDidascalia, titoloParagrafoVuoto, paragrafoVuotoInCoda, usaLinkParagrafo, usaParagrafoSize);
        }// end of if cycle
    }// fine del metodo


//    /**
//     * Crea i titoli in una classe dedicata <br>
//     */
//    private void creaTitoli2() {
//        List<String> lista;
//
//        if (mappaWrapUno != null) {
//            lista = new ArrayList<>(mappaWrapUno.keySet());
//            titoliLista = appContext.getBean(TitoliLista.class, lista, typeDidascalia, titoloParagrafoVuoto, paragrafoVuotoInCoda, usaLinkParagrafo, usaParagrafoSize);
//        }// end of if cycle
//    }// fine del metodo


    /**
     * Crea la mappa mappaWrapDue, secondo livello <br>
     * La sottopagina ha titolo='chiaveSottoPagina' della WrapDidascalia <br>
     */
    private void creaMappaWrapDue() {
        List<WrapDidascalia> listaDisordinata;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPagina;
        String key;

        if (mappaWrapUno != null && titoliLista != null) {
            mappaWrapDue = new LinkedHashMap<>();
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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

        if (mappaWrapDue != null && titoliLista != null) {
            mappaWrapTre = new LinkedHashMap<>();
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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
//        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>>> mappa = null;
//
//        if (array.isValid(mappaWrapUno)) {
//            titoloParagrafiDisordinato.addAll(mappaWrapUno.keySet());
//        }// end of if cycle
//
//        if (titoloParagrafiDisordinato != null && titoloParagrafiDisordinato.size() > 0) {
//            switch (typeDidascalia) {
//                case giornoNato:
//                case giornoMorto:
//                    titoloParagrafiOrdinato = secoloService.riordina(titoloParagrafiDisordinato);
//                    break;
//                case annoNato:
//                case annoMorto:
//                    titoloParagrafiOrdinato = meseService.riordina(titoloParagrafiDisordinato);
//                    break;
//                case listaNomi:
//                case listaCognomi:
//                    titoloParagrafiOrdinato = titoloParagrafiDisordinato;
//                    titoloParagrafiOrdinato = array.sort(titoloParagrafiOrdinato);
//                    break;
//                default:
//                    titoloParagrafiOrdinato = titoloParagrafiDisordinato;
//                    break;
//            } // end of switch statement
//        }// end of if cycle
//
//        if (paragrafoVuotoInCoda && titoloParagrafiOrdinato != null) {
//            if (titoloParagrafiOrdinato.contains(VUOTA)) {
//                titoloParagrafiOrdinato.remove(VUOTA);
//                titoloParagrafiOrdinato.add(VUOTA);
//            }// end of if cycle
//        }// end of if cycle
//
//        if (mappaWrapTre != null && titoloParagrafiOrdinato != null) {
//            mappa = new LinkedHashMap<>();
//            for (String titolo : titoloParagrafiOrdinato) {
//                mappa.put(titoloDefinitivoParagrafo(titolo), mappaWrapTre.get(titolo));
//            }// end of for cycle
//        }// end of if cycle
//
//        mappaWrapTre = mappa;
    }// fine del metodo


    /**
     * Ordinamento delle chiavi di secondo livello <br>
     * Ordinamento alfabetico <br>
     * Sostituisce le chiavi nella sotto-mappa <br>
     */
    private void ordinaSecondoLivello() {
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafo;

        if (mappaWrapTre != null && mappaWrapTre.size() > 0) {
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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


//    /**
//     * Costruzione del titolo definitivo dei paragrafi <br>
//     * Eventuale link a wikipagine <br>
//     * Doppie quadre <br>
//     * Eventuali dimensioni del paragrafo <br>
//     */
//    private String titoloDefinitivoParagrafo(String titolo) {
//        String titoloDefinitivo = VUOTA;
//
//        if (text.isValid(titoloParagrafoVuoto) && titolo.equals(VUOTA)) {
//            titoloDefinitivo = titoloParagrafoVuoto;
//        } else {
//            if (usaLinkParagrafo) {
//                titoloDefinitivo = LibWiki.setQuadre(titolo);
//            } else {
//                titoloDefinitivo = titolo;
//            }// end of if/else cycle
//        }// end of if/else cycle
//
//        if (usaParagrafoSize && numVociParagrafi != null && numVociParagrafi.size() > 0) {
//            if (numVociParagrafi.get(titolo) > 0) {
//                titoloDefinitivo += " <span style=\"font-size:70%\">(" + numVociParagrafi.get(titolo) + ")</span>";
//            }// end of if cycle
//        }// end of if cycle
//
//        titoloParagrafiDefinitivo.add(titoloDefinitivo);
//        numVociParagrafiDefinitivi.put(titoloDefinitivo, numVociParagrafi.get(titolo));
//
//        return titoloDefinitivo;
//    }// fine del metodo


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
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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


//    /**
//     * Dimensioni della sottoPagina <br>
//     */
//    private int calcolaDimensioniSottoPagina(LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafo) {
//        int numVociSottoPagina = 0;
//        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPagina;
//        List<WrapDidascalia> listaDisordinata;
//
//        if (mappaParagrafo != null && mappaParagrafo.size() > 0) {
//            for (String chiaveSottoPagina : mappaParagrafo.keySet()) {
//                mappaSottoPagina = mappaParagrafo.get(chiaveSottoPagina);
//                if (mappaSottoPagina != null && mappaSottoPagina.size() > 0) {
//                    for (String chiaveSottoSottoPagina : mappaSottoPagina.keySet()) {
//                        listaDisordinata = mappaSottoPagina.get(chiaveSottoSottoPagina);
//                        if (listaDisordinata != null && listaDisordinata.size() > 0) {
//                            numVociSottoPagina += listaDisordinata.size();
//                        }// end of if cycle
//                    }// end of for cycle
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return numVociSottoPagina;
//    }// fine del metodo


    /**
     * Costruzione del titolo definitivo dei paragrafi <br>
     */
    private void ordinaListe() {
        List<WrapDidascalia> listaDisordinata;
        List<WrapDidascalia> listaOrdinata;
        LinkedHashMap<String, LinkedHashMap<String, List<WrapDidascalia>>> mappaParagrafo;
        LinkedHashMap<String, List<WrapDidascalia>> mappaSottoPagina;

        if (mappaWrapTre != null) {
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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

                //controllo titolo paragrafo definitivo per attività, genere, professione

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


    public int getNumParagrafi() {
        return titoliLista.getChiavi().size();
    }// fine del metodo


    public List<String> getTitoliParagrafiDisordinati() {
        return titoliLista != null ? titoliLista.getChiavi() : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoliParagrafiOrdinati() {
        return titoliLista != null ? titoliLista.getOrdinati() : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoliParagrafiPagine() {
        return titoliLista != null ? titoliLista.getPagine() : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoliParagrafiVisibili() {
        return titoliLista != null ? titoliLista.getVisibili() : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoliParagrafiLinkati() {
        return titoliLista != null ? titoliLista.getLinkati() : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoliParagrafiConSize() {
        return titoliLista != null ? titoliLista.getConSize() : new ArrayList<>();
    }// fine del metodo


    public List<String> getTitoliParagrafiDefinitivi() {
        return titoliLista != null ? titoliLista.getDefinitivi() : new ArrayList<>();
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
     * Lista di righe con il paragrafo <br>
     * Sottopagine (elaborate a parte) sono opzionali <br>
     */
    private List<String> getListaCon() {
        List<String> listaRighe = null;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaSottopagina;
        LinkedHashMap<String, List<String>> mappaTre;
        String titoloDefinitivo = VUOTA;

        if (mappa != null) {
            listaRighe = new ArrayList<>();
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
                listaRighe.add(VUOTA);
                titoloDefinitivo = titoliLista.getDefinitivo(chiaveParagrafo);
                listaRighe.add(LibBio.setParagrafo(titoloDefinitivo));
                mappaSottopagina = mappa.get(chiaveParagrafo);
                if (mappaSottopagina != null && mappaSottopagina.size() > 0) {
                    if (sogliaSuperata(chiaveParagrafo)) {
                        listaRighe.add(sottopagina(chiaveParagrafo, mappaSottopagina));
                    } else {
                        for (String chiaveDue : mappaSottopagina.keySet()) {
                            mappaTre = mappaSottopagina.get(chiaveDue);
                            if (mappaTre != null && mappaTre.size() > 0) {
                                for (String chiaveTre : mappaTre.keySet()) {
                                    listaRighe.addAll(mappaTre.get(chiaveTre));
                                }// end of for cycle
                            }// end of if cycle
                        }// end of for cycle
                    }// end of if/else cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return listaRighe;
    }// end of method


    private boolean sogliaSuperata(String chiaveParagrafo) {
        boolean superata = false;

        if (titoliLista.getSize(chiaveParagrafo) > taglioSottoPagina) {
            superata = true;
        }// end of if cycle

        return superata;
    }// end of method


    /**
     * Lista di righe senza paragrafo <br>
     * Mette da parte la mappa della sottopagina <br>
     * Restituisce la riga di 'rinvio' alla sottopagina <br>
     */
    private String sottopagina(String chiaveParagrafo, LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaSottopagina) {
        //Mette da parte la mappa della sottopagina
        //@todo da fare

        String tagFisso = "Vedi anche|";
        String tagTypo = typeDidascalia.pagina;
        String tag = "/";
        return LibWiki.setGraffe(tagFisso + tagTypo + soggetto + tag + chiaveParagrafo);
    }// end of method


    /**
     * Lista di righe senza paragrafo <br>
     */
    private List<String> getListaSenza() {
        List<String> listaRighe = null;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaParagrafoTxt;
        LinkedHashMap<String, List<String>> mappaSottoPaginaTxt;
        List<String> listaTxt = null;

        if (mappa != null) {
            listaRighe = new ArrayList<>();
            for (String chiaveParagrafo : titoliLista.getChiavi()) {
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
        List<String> listaRighe;

        if (usaSuddivisioneParagrafi) {
            listaRighe = getListaCon();
        } else {
            listaRighe = getListaSenza();
        }// end of if/else cycle

        if (listaRighe != null && listaRighe.size() > 0) {
            for (String riga : listaRighe) {
                testoLista.append(riga);
                testoLista.append(A_CAPO);
            }// end of for cycle
        }// end of if cycle

        return testoLista.toString().trim();
    }// fine del metodo

}// end of class
