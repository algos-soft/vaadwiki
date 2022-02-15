package it.algos.vaadwiki.backend.liste;

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
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
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

    //--property
    protected List<Bio> listaBio;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiaveUno (ordinata) che corrisponde al titolo del paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveDue e da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveTre e da un ArrayList di didascalie (testo) <br>
     * La chiaveUno è un secolo, un mese, un'attività (a seconda del tipo di didascalia) <br>
     * La chiaveUno è un link a pagina di wikipedia (escluso titoloParagrafoVuoto) con doppie quadre <br>
     * La chiaveDue è una lettera alfabetica <br>
     * La chiaveTre è una doppia lettera alfabetica <br>
     */
    protected Map<String, List> mappa;


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
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata <br>
     */
    public void fixListaBio() {
        listaBio = new ArrayList<>();
    }


    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixListaDidascalie() {
    }

    /**
     * Costruisce una mappa di didascalie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixMappaDidascalie() {
        //--Crea la lista grezza delle voci biografiche
        this.mappa = new LinkedHashMap<>();
    }

    public List<Bio> getListaBio() {
        return listaBio;
    }

    public Map<String, List> getMappa() {
        return mappa;
    }

    public String getTestoPagina() {
        StringBuffer testoPagina = new StringBuffer();
        List lista;

        if (mappa != null && mappa.size() > 0) {
            for (String keyParagrafo : mappa.keySet()) {
                lista = mappa.get(keyParagrafo);

                if (lista != null && lista.size() > 0) {
                    testoPagina.append(keyParagrafo);
                    for (Object didascalia : lista) {
                        testoPagina.append(didascalia);
                    }
                }
            }
        }

        return testoPagina.toString();
    }

}
