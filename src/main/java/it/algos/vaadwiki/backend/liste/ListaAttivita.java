package it.algos.vaadwiki.backend.liste;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.service.*;
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
 * Creata con appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale) in (@PostConstruct)UploadAttivita.inizia() <br>
 * In alternativa appContext.getBean(ListaAttivita.class, attivita) per usare solo una singola attività <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaAttivita extends Lista {

    protected static String BIO_PROPERTY = "attivita";

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

    /**
     * The Service.
     */
    @Autowired
    public MongoService mongoService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;

    //--property
    private String nomeAttivitaPlurale;

    //--property
    private Attivita attivitaSingola;

    //--property
    private List<String> listaNomiAttivitaSingole;

    //--property
    private List<String> listaDidascalie;


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param nomeAttivitaPlurale con cui recuperare le singole attività e costruire la pagina sul server wiki
     */
    public ListaAttivita(String nomeAttivitaPlurale) {
        this.nomeAttivitaPlurale = nomeAttivitaPlurale;
    }// end of constructor

    /**
     * Costruttore alternativo con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(ListaAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param attivitaSingola di cui recuperare la lista di biografie
     */
    public ListaAttivita(Attivita attivitaSingola) {
        this.attivitaSingola = attivitaSingola;
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
     * Regolazioni iniziali per gestire i due costruttori:attività plurali o attività singola <br>
     * DEVE essere sovrascritto, senza invocare PRIMA il metodo della superclasse <br>
     */
    protected void regolazioniIniziali() {
        super.regolazioniIniziali();

        if (text.isValid(nomeAttivitaPlurale) && attivitaSingola == null) {
            try {
                listaNomiAttivitaSingole = attivitaService.fetchSingolariDaPlurale(nomeAttivitaPlurale);
            } catch (AlgosException unErrore) {
                logger.warn(unErrore, getClass(), "regolazioniIniziali");
            }
        }
        if (text.isEmpty(nomeAttivitaPlurale) && attivitaSingola != null) {
            listaNomiAttivitaSingole = array.creaArraySingolo(attivitaSingola.singolare);
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

        for (String attivitaSingola : listaNomiAttivitaSingole) {
            listaBio.addAll(bioService.fetch(BIO_PROPERTY, attivitaSingola));
        }
    }

    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     * DEVE essere sovrascritto, senza invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixListaDidascalie() {
        super.fixListaDidascalie();

        listaDidascalie = didascaliaService.getLista(listaBio);
    }


    /**
     * Costruisce una mappa di didascalie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixMappaDidascalie() {
        super.fixListaDidascalie();

        mappa = getMappaNazionalita(listaBio);
    }


    private Map<String, List> getMappaNazionalita(final List<Bio> listaBio) {
        Map<String, List> mappa = new LinkedHashMap<String, List>();
        listaDidascalie = null;
        Set<String> setNazionalita;
        String titoloParagrafoNazionalita = VUOTA;

        if (listaBio == null) {
            return null;
        }

        setNazionalita = new LinkedHashSet<>();
        for (Bio bio : listaBio) {
            titoloParagrafoNazionalita = bio.nazionalita;
            setNazionalita.add(titoloParagrafoNazionalita);
        }

        for (String titoloParagrafo : setNazionalita) {
            listaDidascalie = new ArrayList<>();
            for (Bio bio : listaBio) {
                if (bio.nazionalita.equals(titoloParagrafo)) {
                    listaDidascalie.add(didascaliaService.getLista(bio));
                }
            }
            try {
                titoloParagrafo = nazionalitaService.findById(titoloParagrafo).plurale;
            } catch (AlgosException unErrore) {
            }
            titoloParagrafo = text.primaMaiuscola(titoloParagrafo);
            mappa.put(titoloParagrafo, listaDidascalie);
        }

        return mappa;
    }


    public List<String> getListaNomiAttivitaSingole() {
        return listaNomiAttivitaSingole;
    }

    public String getNomeAttivitaPlurale() {
        return nomeAttivitaPlurale;
    }

    public Attivita getAttivitaSingola() {
        return attivitaSingola;
    }


    public List<String> getListaDidascalie() {
        return listaDidascalie;
    }


}
