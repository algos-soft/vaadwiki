package it.algos.vaadwiki.liste;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 10:34
 * <p>
 * Classe specializzata la creazione di liste. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
@Slf4j
public abstract class Lista {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public GiornoService giornoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public AnnoService annoService;


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
    public ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    public BioService bioService;

    /**
     * Typo di didascalia da preparare per questa lista <br>
     * Property della classe perché regolata nelle sottoclassi concrete <br>
     */
    public EADidascalia typeDidascalia;

    //--property
    public String testo;

    //--property
    public int size;

    /**
     * Lista grezza delle voci biografiche da inserire in questa lista <br>
     * Property della classe perché regolata nelle sottoclassi concrete <br>
     */
    public ArrayList<Bio> listaGrezzaBio;

    public ArrayList<WrapDidascalia> listaDidascalie;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiaveUno (ordinata) che corrisponde al titolo del paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveDue e da un ArrayList di didascalie (testo) <br>
     * La chiaveUno è un secolo, un mese, un'attività (a seconda del tipo di didascalia) <br>
     * La chiaveUno è un link a pagina di wikipedia (escluso titoloParagrafoVuoto) con doppie quadre <br>
     * La chiaveDue è un anno, un giorno (a seconda del tipo di didascalia) <br>
     * Le didascalie sono ordinate per cognome <br>
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa;

    //--property
    public boolean usaSuddivisioneParagrafi;

    //--property
    public boolean usaRigheRaggruppate;

    //--property
    public String titoloParagrafoVuoto;

    //--property
    public boolean paragrafoVuotoInCoda;

    //--property
    public boolean usaParagrafoSize;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ListaService listaService;

    private LinkedHashMap<String, ArrayList<String>> mappaSemplice;


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
    protected void inizia() {
        this.fixPreferenze();
        this.creaDidascalie();
        this.creaMappa(listaDidascalie);
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.usaSuddivisioneParagrafi = true;
        this.titoloParagrafoVuoto = "";
        this.paragrafoVuotoInCoda = true;
        this.usaParagrafoSize = false;
    }// end of method


    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno/nome/cognome (key) e poi per cognome (value)
     */
    protected void creaDidascalie() {
        //--Crea la lista grezza delle voci biografiche
        this.listaGrezzaBio = listaBio();

        //--Crea una lista di didascalie specifiche
        listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, typeDidascalia);

        //--Ordina la lista di didascalie specifiche
        listaDidascalie = this.ordinaListaDidascalie(listaDidascalie);

        //@todo ASSOLUTAMENTE PROVVISORIO
        listaDidascalie = new ArrayList<>(listaDidascalie.subList(0, 50));
        //@todo ASSOLUTAMENTE PROVVISORIO

        this.size = listaDidascalie.size();
    }// end of method


    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiaveUno (ordinata) che corrisponde al titolo del paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveDue e da un ArrayList di didascalie (testo) <br>
     * La chiaveUno è un secolo, un mese, un'attività (a seconda del tipo di didascalia) <br>
     * La chiaveUno è un link a pagina di wikipedia (escluso titoloParagrafoVuoto) con doppie quadre <br>
     * La chiaveDue è un anno, un giorno (a seconda del tipo di didascalia) <br>
     * Le didascalie sono ordinate per cognome <br>
     *
     * @param listaDidascalie da raggruppare
     */
    protected void creaMappa(ArrayList<WrapDidascalia> listaDidascalie) {
        mappa = listaService.creaMappa(listaDidascalie, titoloParagrafoVuoto, paragrafoVuotoInCoda);
    }// fine del metodo


    /**
     * Recupera una lista (array) di records Bio che usano un'istanza della property appropriata <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return lista delle istanze di Bio che usano questa istanza della property appropriata
     */
    @SuppressWarnings("all")
    public ArrayList<Bio> listaBio() {
        return null;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie specifiche <br>
     */
    @SuppressWarnings("all")
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        return listaService.ordinaListaDidascalie(listaDisordinata);
    }// fine del metodo

}// end of class
