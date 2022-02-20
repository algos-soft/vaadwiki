package it.algos.vaadwiki.backend.liste;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import org.springframework.beans.factory.annotation.*;

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
 * La (List<Bio>) listaBio, la (List<String>) listaDidascalie, la (Map<String, List>) mappa e (String) testoConParagrafi
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
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ALogService logger;

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
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una List <br>
     * La mappa viene creata nel @PostConstruct dell'istanza <br>
     * La mappa è ordinata per titoli dei paragrafi <br>
     */
    protected Map<String, List> mappa;

    /**
     * Lista delle listaDidascalie che hanno una valore valido per la pagina specifica <br>
     * La lista viene creata nel @PostConstruct dell'istanza <br>
     * Il testo è ordinato per titoli dei paragrafi <br>
     */
    protected String testoConParagrafi;

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
        this.fixMappaDidascalie();
        this.fixTestoParagrafi();
    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }


    /**
     * Regolazioni iniziali per gestire i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
    }

    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixListaBio() {
        //--Crea la lista vuota delle voci biografiche
        listaBio = new ArrayList<>();
    }


    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixListaDidascalie() {
    }

    /**
     * Costruisce una mappa di didascalie con titoli dei paragrafi che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixMappaDidascalie() {
        //--Crea la mappa vuota delle didascalie
        this.mappa = new LinkedHashMap<>();
    }

    /**
     * Costruisce un testo con titoli dei paragrafi <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixTestoParagrafi() {
        StringBuilder buffer = new StringBuilder();
        List<String> lista;

        if (mappa != null && mappa.size() > 0) {
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);

                if (lista != null && lista.size() > 0) {
                    buffer.append(fixTitoloParagrafo(key, lista.size()));

                    for (Object stringa : lista) {
                        buffer.append(ASTERISCO);
                        buffer.append(SPAZIO);
                        buffer.append(stringa);
                        buffer.append(A_CAPO);
                    }
                    buffer.append(A_CAPO);
                }
            }
        }
        this.testoConParagrafi = buffer.toString();
    }

    public List<Bio> getListaBio() {
        return listaBio;
    }

    public Map<String, List> getMappa() {
        return mappa;
    }

    public int getNumDidascalie() {
        return listaDidascalie != null ? listaDidascalie.size() : 0;
    }

    public String getTestoConParagrafi() {
        return testoConParagrafi;
    }

    public String fixTitoloParagrafo(final String keyParagrafo, int numero) {
        String titolo = VUOTA;
        String keyMaiuscola = text.primaMaiuscola(keyParagrafo);
        String tag = "Progetto:Biografie/Nazionalità/";

        tag += keyMaiuscola;
        tag += PIPE;
        tag += keyMaiuscola;
        tag = text.setDoppieQuadre(tag);

        titolo += UGUALE_DOPPIO;
        titolo += tag;
        if (true) { //@todo mettere eventualmente un flag in preferenze
            titolo += SPAZIO;
            titolo += "<span style=\"font-size:70%\">(";
            titolo += numero;
            titolo += ")</span>";
        }
        titolo += UGUALE_DOPPIO;
        titolo += A_CAPO;

        return titolo;
    }

}
