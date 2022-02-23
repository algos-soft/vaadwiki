package it.algos.vaadwiki.backend.liste;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadwiki.backend.enumeration.*;
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
 * Lista delle persone per attività <br>
 * <p>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Usata fondamentalmente da UploadAttivita con appContext.getBean(ListaAttivita.class, attivita) <br>
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
     * @param typeAttivita singola o plurale
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
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
    }

    /**
     * Regolazioni iniziali per gestire i diversi costruttori <br>
     * DEVE essere sovrascritto, senza invocare PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
        if (attivita != null) {
            switch (typeAttivita) {
                case singolare -> listaNomiAttivitaSingole = array.creaArraySingolo(attivita.singolare);
                case plurale -> {
                    try {
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
                case singolare -> listaNomiAttivitaSingole = array.creaArraySingolo(nomeAttivita);
                case plurale -> {
                    try {
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
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata <br>
     */
    public void fixListaBio() {
        super.fixListaBio();
        listaBio = bioService.fetchAttivita(listaNomiAttivitaSingole);
        try {
            wrapLista = appContext.getBean(WrapLista.class,  AETypeLista.attivita,attivita.plurale, listaBio);
        } catch (Exception unErrore) {
            logger.warn(unErrore, getClass(), "fixListaBio");
        }
    }


    public List<String> getListaNomiAttivitaSingole() {
        return listaNomiAttivitaSingole;
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
