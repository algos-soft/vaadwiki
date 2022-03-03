package it.algos.vaadwiki.backend.liste;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.support.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 18:43
 * <p>
 * Classe specializzata nella creazione di liste. <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale) <br>
 * Liste di attività e nazionalità (in Progetto:Biografie) <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 * <p>
 * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List<String>>) mappa e (String) testoConParagrafi
 * vengono tutte regolate alla creazione dell'istanza in @PostConstruct e sono disponibili da subito <br>
 * Si può quindi usare la chiamata appContext.getBean(ListaXxx.class, yyy).getTestoConParagrafi() senza esplicitare l'istanza <br>
 */
public abstract class Lista {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ArrayService array;

    /**
     * The Service.
     */
    @Autowired
    public MongoService mongoService;

    /**
     * The App context.
     */
    @Autowired
    protected GenericApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ALogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected WikiUtility wikiUtility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;

    /**
     * Lista delle biografie che hanno una valore valido per la pagina specifica <br>
     * La lista viene creata nel @PostConstruct dell'istanza <br>
     * La lista è ordinata per cognome <br>
     */
    protected List<Bio> listaBio;


    /**
     * Lista delle listaDidascalie che hanno una valore valido per la pagina specifica <br>
     * La lista viene creata nel @PostConstruct dell'istanza <br>
     * La lista è ordinata per cognome <br>
     */
    protected List<String> listaDidascalie;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) che corrisponde al titolo del paragrafo <br>
     * Ogni valore della mappa è costituito da una lista di didascalie per ogni paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     */
    protected LinkedHashMap<String, List<String>> mappaUno;
    protected Map<String, Map<String, List<String>>> mappaDue;
    protected LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> mappaTre;


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    public void inizia() {
        this.fixPreferenze();
        this.regolazioniIniziali();
        this.fixListaBio();
        this.fixListaDidascalie();
        this.fixMappaParagrafi();
    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }


    /**
     * Regolazioni iniziali per gestire (nella sottoclasse Attivita) i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
    }

    /**
     * Costruisce una lista di biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixListaBio() {
    }


    /**
     * Costruisce una lista di didascalie (testo) che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixListaDidascalie() {
    }


    /**
     * Costruisce una mappa ordinata di didascalie suddivise per paragrafi <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected void fixMappaParagrafi() {
    }


    /**
     * Estrae dalla mappa un testo completo <br>
     * Comprende titoli dei paragrafi e la lista delle didascalie <br>
     * Può essere sovrascritto, senza invocare il metodo della superclasse <br>
     */
    public String getTestoConParagrafi() {
        StringBuilder buffer = new StringBuilder();
        List<String> lista;

        if (mappaUno != null && mappaUno.size() > 0) {
            for (String key : mappaUno.keySet()) {
                lista = mappaUno.get(key);

                if (lista != null && lista.size() > 0) {
                    buffer.append(wikiUtility.setParagrafo(key));
                    for (String stringa : lista) {
                        buffer.append(ASTERISCO);
                        buffer.append(SPAZIO);
                        buffer.append(stringa);
                        buffer.append(A_CAPO);
                    }
                    buffer.append(A_CAPO);
                }
            }
        }

        return buffer.toString().trim();
    }

    public String getTitoloParagrafo(final String keyParagrafo, int numero) {
        return VUOTA;
    }

    public List<Bio> getListaBio() {
        return listaBio;
    }

    public List<String> getListaDidascalie() {
        return listaDidascalie;
    }

    public Map<String, List<String>> getMappaUno() {
        return mappaUno;
    }

    public Map<String, Map<String, List<String>>> getMappaDue() {
        return mappaDue;
    }

    public Map<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> getMappaTre() {
        return mappaTre;
    }

    public int getNumDidascalie() {
        return listaDidascalie.size();
    }


    public int getBioSize() {
        return listaBio.size();
    }

}
