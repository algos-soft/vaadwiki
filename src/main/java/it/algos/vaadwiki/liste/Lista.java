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

    public LinkedHashMap<String, ArrayList<String>> mappaSemplice;

    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaComplessa;

    //--property
    public boolean usaSuddivisioneParagrafi;

    //--property
    public boolean usaRigheRaggruppate;

    //--property
    public String titoloParagrafoVuoto;

    //--property
    public boolean paragrafoVuotoInCoda;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ListaService listaService;


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
        ArrayList<WrapDidascalia> listaDidascalie = null;

        this.fixPreferenze();
        listaDidascalie = this.creaDidascalie();
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
    }// end of method


    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno/nome/cognome (key) e poi per cognome (value)
     */
    protected ArrayList<WrapDidascalia> creaDidascalie() {
        ArrayList<WrapDidascalia> listaDidascalie = null;

        //--Crea la lista grezza delle voci biografiche
        this.listaGrezzaBio = listaBio();

        //--Crea una lista di didascalie specifiche
        listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, typeDidascalia);

        //--Ordina la lista di didascalie specifiche
        listaDidascalie = this.ordinaListaDidascalie(listaDidascalie);//@todo la query è già ordinata  MA FORSE NO

        return listaDidascalie;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è uno dei giorni/anni/nomi/cognomi in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param listaDidascalie
     */
    protected void creaMappa(ArrayList<WrapDidascalia> listaDidascalie) {
        if (usaSuddivisioneParagrafi) {
            creaMappaConParagrafi(listaDidascalie);
            creaTestoConParagrafi();
        } else {
            creaTestoSenzaParagrafi(listaDidascalie);
        }// end of if/else cycle
    }// fine del metodo


    /**
     * Costruisce la mappa <br>
     */
    protected void creaMappaConParagrafi(ArrayList<WrapDidascalia> listaDidascalie) {

        //--Costruisce la mappa completa
        mappaComplessa = listaService.creaMappaChiaveUno(listaDidascalie, titoloParagrafoVuoto);

        if (paragrafoVuotoInCoda) {
            listaService.fixPosizioneParagrafoVuoto(mappaComplessa, titoloParagrafoVuoto);
        }// end of if cycle
    }// end of method


    /**
     * Costruisce il testo dalla mappa <br>
     */
    protected void creaTestoConParagrafi() {
        this.testo = listaService.righeParagrafo(mappaComplessa);
        this.size = listaService.getMappaDueSize(mappaComplessa);
    }// end of method


    /**
     * Costruisce il testo dalla mappa <br>
     */
    protected void creaTestoSenzaParagrafi(ArrayList<WrapDidascalia> listaDidascalie) {
        //--Costruisce la mappa completa
        mappaSemplice = listaService.creaMappa(listaDidascalie);

        if (usaRigheRaggruppate) {
            this.testo = listaService.righeRaggruppate(mappaSemplice);
        } else {
            this.testo = listaService.righeSemplici(mappaSemplice);
        }// end of if/else cycle
        this.size = listaService.getMappaSize(mappaSemplice);
    }// end of method


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
