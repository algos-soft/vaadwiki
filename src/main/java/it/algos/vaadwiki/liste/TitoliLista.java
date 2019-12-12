package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.wiki.LibWiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 12-dic-2019
 * Time: 07:02
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TitoliLista {

    //--parametro in ingresso
    private List<String> disordinati;

    //--parametro in ingresso
    private EADidascalia typeDidascalia;

    //--parametro in ingresso
    private String titoloParagrafoVuoto;

    //--parametro in ingresso
    private boolean paragrafoVuotoInCoda;

    //--parametro in ingresso
    private boolean usaLinkParagrafo;

    //--parametro in ingresso
    private boolean usaParagrafoSize;

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

    //--property elaborata
    private List<String> ordinati;

    //--property elaborata
    private LinkedHashMap<String, String> mappaDefinitivi;

    //--property elaborata
    private LinkedHashMap<String, Integer> mappaVoci;

    //--property elaborata
    private List<Titolo> listaTitoli;

    //--property elaborata
    private LinkedHashMap<String, Titolo> mappaTitoli;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public TitoliLista() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(TitoliLista.class, ..., ...) <br>
     */
    public TitoliLista(List<Titolo> listaTitoli, EADidascalia typeDidascalia, String titoloParagrafoVuoto, boolean paragrafoVuotoInCoda, boolean usaLinkParagrafo, boolean usaParagrafoSize) {
        this.listaTitoli = listaTitoli;
        this.typeDidascalia = typeDidascalia;
        this.titoloParagrafoVuoto = titoloParagrafoVuoto;
        this.paragrafoVuotoInCoda = paragrafoVuotoInCoda;
        this.usaLinkParagrafo = usaLinkParagrafo;
        this.usaParagrafoSize = usaParagrafoSize;
    }// end of constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheAttivita.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void postConstruct() {
        mappaTitoli = new LinkedHashMap<>();

        if (listaTitoli != null && listaTitoli.size() > 0) {
            for (Titolo titolo : listaTitoli) {
                mappaTitoli.put(titolo.chiave, titolo);
            }// end of for cycle
        }// end of if cycle
    }// end of method


    /**
     * Ordinamento dei titoli <br>
     * Ordina i titoli in base al tipo di lista (alfabetico, per anno, per giorno) <br>
     * Mette in coda (eventualmente) il titolo vuoto <br>
     * Sostituisce il titolo vuoto <br>
     * Costruisce i titoli definitivi dei paragrafi (link a wikipagine, quadre e dimensioni del paragrafo) <br>
     */
    public void ordina() {
        if (typeDidascalia != null && disordinati != null && disordinati.size() > 0) {
            ordinati = new ArrayList<>();

            switch (typeDidascalia) {
                case giornoNato:
                case giornoMorto:
                    ordinati = secoloService.riordina(disordinati);
                    break;
                case annoNato:
                case annoMorto:
                    ordinati = meseService.riordina(disordinati);
                    break;
                case listaNomi:
                case listaCognomi:
                    ordinati = disordinati;
                    ordinati = array.sort(disordinati);
                    break;
                default:
                    ordinati = disordinati;
                    break;
            } // end of switch statement
        }// end of if cycle

        if (paragrafoVuotoInCoda && ordinati != null) {
            if (ordinati.contains(VUOTA)) {
                ordinati.remove(VUOTA);
                ordinati.add(VUOTA);
            }// end of if cycle
        }// end of if cycle
    }// fine del metodo


    /**
     * Costruisce i titoli definitivi dei paragrafi (link a wikipagine, quadre e dimensioni del paragrafo) <br>
     */
    public void definitivi(LinkedHashMap<String, Integer> numVociParagrafi) {
        if (ordinati != null) {
            mappaDefinitivi = new LinkedHashMap<>();
            mappaVoci = new LinkedHashMap<>();
            for (String titolo : ordinati) {
                mappaDefinitivi.put(titolo, titoloDefinitivo(titolo, numVociParagrafi.get(titolo)));
            }// end of for cycle
        }// end of if cycle
    }// fine del metodo


    /**
     * Costruzione del titolo definitivo dei paragrafi <br>
     * Eventuale link a wikipagine <br>
     * Doppie quadre <br>
     * Eventuali dimensioni del paragrafo <br>
     */
    private String titoloDefinitivo(String titolo, int size) {
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

        if (usaParagrafoSize && size > 0) {
            titoloDefinitivo += " <span style=\"font-size:70%\">(" + size + ")</span>";
        }// end of if cycle

        mappaVoci.put(titolo, size);

        return titoloDefinitivo;
    }// fine del metodo


    public List<String> getChiavi() {
        List<String> lista = null;

        if (mappaTitoli != null && mappaTitoli.size() > 0) {
            lista = new ArrayList<>();
            for (String key : mappaTitoli.keySet()) {
                lista.add(mappaTitoli.get(key).chiave);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo


    public List<String> getOrdinati() {
        return new ArrayList<>();
    }// fine del metodo


    public List<String> getPagine() {
        List<String> lista = null;

        if (mappaTitoli != null && mappaTitoli.size() > 0) {
            lista = new ArrayList<>();
            for (String key : mappaTitoli.keySet()) {
                lista.add(mappaTitoli.get(key).pagina);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo

    public List<String> getVisibili() {
        List<String> lista = null;

        if (mappaTitoli != null && mappaTitoli.size() > 0) {
            lista = new ArrayList<>();
            for (String key : mappaTitoli.keySet()) {
                lista.add(mappaTitoli.get(key).visibile);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo

    public List<String> getLinkati() {
        List<String> lista = null;

        if (mappaTitoli != null && mappaTitoli.size() > 0) {
            lista = new ArrayList<>();
            for (String key : mappaTitoli.keySet()) {
                lista.add(mappaTitoli.get(key).getLinkato());
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo

    public List<String> getConSize() {
        List<String> lista = null;

        if (mappaTitoli != null && mappaTitoli.size() > 0) {
            lista = new ArrayList<>();
            for (String key : mappaTitoli.keySet()) {
                lista.add(mappaTitoli.get(key).getConSize());
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo


    public List<String> getDefinitivi() {
        List<String> lista = null;

        if (mappaTitoli != null && mappaTitoli.size() > 0) {
            lista = new ArrayList<>();
            for (String key : mappaTitoli.keySet()) {
                lista.add(mappaTitoli.get(key).getDefinitivo());
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// fine del metodo


    public String getDefinitivo(String titolo) {
        return mappaTitoli.get(titolo).getDefinitivo();
    }// fine del metodo


    public int getSize(String titolo) {
        int size = 0;

        if (mappaVoci != null && mappaVoci.size() > 0 && mappaVoci.containsKey(titolo)) {
            size = mappaVoci.get(titolo);
        }// end of if cycle

        return size;
    }// fine del metodo


}// end of class
