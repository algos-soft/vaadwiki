package it.algos.vaadwiki.backend.liste;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 18:44
 * <p>
 * Lista delle biografie per attività <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadAttivita con appContext.getBean(ListaAttivita.class, attivita) <br>
 * <p>
 * Creata con appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
 * Creata con appContext.getBean(ListaAttivita.class, attivita, AETypeAttivita.singolare) per usare solo la singola attività <br>
 * Creata con appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale, AETypeAttivita.plurale) per usare tutte le singole attività <br>
 * Creata con appContext.getBean(ListaAttivita.class, nomeAttivitaSingolare, AETypeAttivita.singolare) per usare solo una singola attività <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaService attivitaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaService nazionalitaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioService bioService;


    //--property
    private String nomeAttivita;

    //--property
    private Attivita attivita;

    //--property
    private List<String> listaNomiAttivitaSingole;

    //--property
    private AETypeAttivita typeAttivita;

    private String titoloGrezzo;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) per usare tutte le attività che hanno la stessa attivita.plurale <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivita di cui recuperare le liste di tutte le singole attività di biografie
     */
    public ListaAttivita(final Attivita attivita) {
        this(attivita, AETypeAttivita.plurale);
    }// end of constructor


    /**
     * Costruttore alternativo con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita, AETypeAttivita.singolare) per usare solo una singola attività <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivita     di cui recuperare la lista di biografie
     * @param typeAttivita singolare o plurale
     */
    public ListaAttivita(final Attivita attivita, final AETypeAttivita typeAttivita) {
        this.attivita = attivita;
        this.typeAttivita = typeAttivita;
    }// end of constructor


    /**
     * Costruttore alternativo con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale, AETypeAttivita.plurale) per usare tutte le singole attività <br>
     * Uso: appContext.getBean(ListaAttivita.class, nomeAttivitaSingolare, AETypeAttivita.singolare) per usare solo una singola attività <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune properties. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param nomeAttivita singolare o plurale in funzione del flag
     * @param typeAttivita singola o plurale
     */
    public ListaAttivita(final String nomeAttivita, final AETypeAttivita typeAttivita) {
        this.nomeAttivita = nomeAttivita;
        this.typeAttivita = typeAttivita;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();
    }

    /**
     * Regolazioni iniziali per gestire (nella sottoclasse Attivita) i due costruttori:attività plurali o attività singola <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void regolazioniIniziali() {
        super.regolazioniIniziali();

        if (attivita != null) {
            switch (typeAttivita) {
                case singolare -> {
                    this.titoloGrezzo = attivita.singolare;
                    listaNomiAttivitaSingole = array.creaArraySingolo(attivita.singolare);
                }
                case plurale -> {
                    try {
                        this.titoloGrezzo = attivita.plurale;
                        listaNomiAttivitaSingole = attivitaService.fetchSingolariDaPlurale(attivita.plurale);
                    } catch (AlgosException unErrore) {
                        logger.warn(unErrore, getClass(), "regolazioniIniziali");
                    }
                }
                default -> {
                }
            }
        }
        else {
            switch (typeAttivita) {
                case singolare -> {
                    try {
                        attivita = attivitaService.findByProperty("singolare", nomeAttivita);
                        this.titoloGrezzo = attivita.singolare;
                    } catch (AlgosException unErrore) {
                        logger.warn(unErrore, this.getClass(), "regolazioniIniziali");
                    }
                    listaNomiAttivitaSingole = array.creaArraySingolo(nomeAttivita);
                }
                case plurale -> {
                    try {
                        attivita = attivitaService.findFirstByPlurale("plurale", nomeAttivita);
                        this.titoloGrezzo = attivita.plurale;
                        listaNomiAttivitaSingole = attivitaService.fetchSingolariDaPlurale(nomeAttivita);
                    } catch (AlgosException unErrore) {
                        logger.warn(unErrore, getClass(), "regolazioniIniziali");
                    }
                }
                default -> {
                }
            }
        }
    }


    /**
     * Costruisce una lista di biografie (Bio) che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixListaBio() {
        super.fixListaBio();

        try {
            listaBio = bioService.fetchAttivita(listaNomiAttivitaSingole);
        } catch (Exception unErrore) {
            logger.warn(unErrore, getClass(), "fixListaBio");
        }
    }


    public List<String> getListaNomiAttivitaSingole() {
        return listaNomiAttivitaSingole;
    }


    /**
     * Costruisce una mappa ordinata di didascalie suddivise per paragrafi <br>
     * DEVE essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    @Override
    protected void fixMappaParagrafi() {
        mappa = new LinkedHashMap<>();
        LinkedHashMap<String, List> mappaBio = new LinkedHashMap<>();
        String message;
        List<Bio> lista;
        Set<String> setNazionalita;
        List<String> listaNazionalita;
        List<String> didascalie;
        String nazionalitaPlurale;
        String didascalia;
        LinkedHashMap<String, String> mappaNazionalita = nazionalitaService.getMappa();

        if (listaBio == null) {
            return;
        }

        setNazionalita = new LinkedHashSet<>();
        listaDidascalie = new ArrayList<>();
        for (Bio bio : listaBio) {
            if (text.isValid(bio.nazionalita)) {
                nazionalitaPlurale = mappaNazionalita.get(bio.nazionalita);
                setNazionalita.add(nazionalitaPlurale);
            }
        }
        listaNazionalita = new ArrayList<>(setNazionalita);
        Collections.sort(listaNazionalita);

        for (String nazPlurale : listaNazionalita) {
            mappaBio.put(nazPlurale, new ArrayList());
        }
        mappaBio.put(ALTRE, new ArrayList());

        for (Bio bio : listaBio) {
            if (text.isValid(bio.nazionalita)) {
                nazionalitaPlurale = mappaNazionalita.get(bio.nazionalita);
                if (mappaBio.containsKey(nazionalitaPlurale)) {
                    mappaBio.get(nazionalitaPlurale).add(bio);
                }
                else {
                    message = String.format("La mappa non prevede la nazionalità %s", nazionalitaPlurale);
                    logger.warn(message, this.getClass(), "fixMappaNazionalita");
                }
            }
            else {
                mappaBio.get(ALTRE).add(bio);
            }
        }

        if (mappaBio.get(ALTRE) != null && mappaBio.get(ALTRE).size() == 0) {
            mappaBio.remove(ALTRE);
        }

        if (mappaBio != null && mappaBio.size() > 0) {
            for (String paragrafo : mappaBio.keySet()) {
                lista = mappaBio.get(paragrafo);

                didascalie = new ArrayList<>();
                for (Bio bio : lista) {
                    didascalia = didascaliaService.getLista(bio);
                    didascalie.add(didascalia);
                    listaDidascalie.add(didascalia);
                }
                paragrafo = text.primaMaiuscola(paragrafo);
                mappa.put(paragrafo, didascalie);
            }
        }
    }

    public int getNumeroAttivitaSingoleConLoStessoPlurale() {
        return switch (typeAttivita) {
            case singolare:
                yield 1;
            case plurale:
                yield listaNomiAttivitaSingole != null ? listaNomiAttivitaSingole.size() : 0;
        };
    }


    public enum AETypeAttivita {
        singolare, plurale
    }

}
