package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.professione.Professione;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.modules.attivita.AttivitaService.EX;
import static it.algos.vaadwiki.modules.attivita.AttivitaService.EX2;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 21:15
 * <p>
 * Wrapper per gestire le didascalie
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class WrapDidascalia implements Comparable<WrapDidascalia> {

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public GiornoService giornoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    public AnnoService annoService;

    public Bio bio;

    /**
     * Chiave principale del paragrafo (potrebbe anche non esserci) <br>
     * Secolo per i giorni (opzionale, potrebbe anche non esserci) <br>
     * Mesi per gli anni (opzionale, potrebbe anche non esserci) <br>
     * Attività plurale maschile o femminile per i nomi, cognomi, nazionalità (obbligatoria) <br>
     * Nazionalità per le attività (obbligatoria) <br>
     * Esempio: Calciatori/Brasiliani -> chiaveParagrafo=Brasiliani
     */
    public String chiaveParagrafo;

    /**
     * Chiave secondaria per la sottopagina (opzionale, potrebbe anche non esserci) <br>
     * Esempio: Calciatori/Brasiliani/C -> chiaveSottoParagrafo=C
     */
    public String chiaveSottoPagina;

    /**
     * Chiave terziaria per la ulteriore suddivisione delle sottopagine (opzionale, potrebbe anche non esserci) <br>
     * Esempio: Calciatori/Brasiliani/C/Ca -> chiaveSottoSottoParagrafo=Ca
     */
    public String chiaveSottoSottoPagina;

    /**
     * Composta dal cognome concatenato al titolo della pagina wikipedia (per essere sicuri che qualcosa esisista) <br>
     */
    public String chiaveCognome;

    /**
     * La pagina della professione linkata dal titolo del paragrafo (se esiste) <br>
     */
    public String chiaveProfessione;

    /**
     * La chiave della singola riga (opzionale, potrebbe anche non esserci) <br>
     * Usata da Giorni ed Anni <br>
     */
    public String chiaveRiga;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GenereService genereService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Riferimento (giorno o anno) a cui si riferisce la didascalia <br>
     * Simile al titolo della pagina in cui andrà inserita la lista che usa la didascalia <br>
     * Es.: '21 marzo' per tutte le didascalie che vanno nella pagina 'Nati il 21 marzo' o 'Morti il 21 marzo' <br>
     * Es.: '1937' per tutte le didascalie che vanno nella pagina 'Nati nel 1937' o 'Morti nel 1937' <br>
     */
    private String riferimento;

    /**
     * Ordine di presentazione delle didascalie nella pagina <br>
     * È uguale alla property 'ordine' della entity Giorno od Anno corrispondente alla chiave della mappa <br>
     * Es.: '81' per una didascalia che va nelle pagine 'Nati...' o 'Morti...' di una persona nata il '21 marzo' <br>
     * Es.: '3937' per una didascalia che va nelle pagine 'Nati...' o 'Morti...' di una persona nata nel '1937' <br>
     */
    private int chiaveOrdinamentoNumerico;

    /**
     * Chiave per le righe multiple <br>
     * Es.: '1937' per una didascalia che va nelle pagine 'Nati...' o 'Morti...' di una persona nata nel '1937' <br>
     * Es.: '21 marzo' per una didascalia che va nelle pagine 'Nati...' o 'Morti...' di una persona nata il '21 marzo' <br>
     */
    private String chiave;

    /**
     * Ulteriore chiave per la lista di discalie per ogni chiave <br>
     * Composta dal cognome concatenato al titolo della pagina wikipedia (per essere sicuri che qualcosa esisista) <br>
     */
    private String sottoChiave;

    /**
     * Testo della didascalia CON la chiave che viene usata nella composizione con 'righeParagrafo' <br>
     */
    private String testoCon;

    /**
     * Testo della didascalia SENZA la chiave che viene aggiunta nella composizione della pagina <br>
     * La chiave viene aggiunta in maniera differente tra 'righeSemplici' o 'righeRaggruppate' <br>
     */
    private String testoSenza;

    private EADidascalia type;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public WrapDidascalia() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(WrapDidascalia.class, bio, type) <br>
     *
     * @param bio  di cui costruire la didascalia
     * @param type con cui costruire la didascalia
     */
    public WrapDidascalia(Bio bio, EADidascalia type) {
        this.bio = bio;
        this.type = type;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     */
    @PostConstruct
    public void inizia() {
        Didascalia didascalia = null;
//        String paginaAttivita = "";
        boolean sessoMaschile = true;
        String key = VUOTA;

        try { // prova ad eseguire il codice
            sessoMaschile = bio.sesso.equals("F") ? false : true;
        } catch (Exception unErrore) { // intercetta l'errore
//            logger.error("Manca il sesso alla bio: " + bio.wikiTitle);
        }// fine del blocco try-catch

        switch (type) {
            //--i nomi dei secoli ed i numeri degli anni sono in formato testo e non possono essere ordinati
            //--uso il numero d'ordine di ogni anno
            case giornoNato:
                didascalia = didascaliaService.getDidascaliaGiornoNato(bio);
                key = bio.annoNascita != null ? bio.annoNascita.titolo : "";

                chiaveParagrafo = bio.annoNascita != null ? bio.annoNascita.secolo.titolo : "";
                chiaveSottoPagina = VUOTA;
                chiaveSottoSottoPagina = VUOTA;
                chiaveRiga = key;
                chiaveOrdinamentoNumerico = text.isValid(key) ? annoService.findByKeyUnica(key).ordine : 0;
                chiaveCognome = text.isValid(bio.cognome) ? bio.cognome : bio.wikiTitle;
                break;
            //--i nomi dei secoli ed i numeri degli anni sono in formato testo e non possono essere ordinati
            //--uso il numero d'ordine di ogni anno
            case giornoMorto:
                didascalia = didascaliaService.getDidascaliaGiornoMorto(bio);
                key = bio.annoMorte != null ? bio.annoMorte.titolo : "";

                chiaveParagrafo = bio.annoMorte != null ? bio.annoMorte.secolo.titolo : "";
                chiaveSottoPagina = VUOTA;
                chiaveSottoSottoPagina = VUOTA;
                chiaveRiga = key;
                chiaveOrdinamentoNumerico = text.isValid(key) ? annoService.findByKeyUnica(key).ordine : 0;
                chiaveCognome = text.isValid(bio.cognome) ? bio.cognome : bio.wikiTitle;
                break;
            //--i nomi dei mesi e dei giorni sono in formato testo e non possono essere ordinati
            //--uso il numero d'ordine di ogni giorno
            case annoNato:
                didascalia = didascaliaService.getDidascaliaAnnoNato(bio);
                key = bio.giornoNascita != null ? bio.giornoNascita.titolo : "";

                chiaveParagrafo = bio.giornoNascita != null ? bio.giornoNascita.mese.titoloLungo : "";
                chiaveParagrafo = text.primaMaiuscola(chiaveParagrafo);
                chiaveSottoPagina = VUOTA;
                chiaveSottoSottoPagina = VUOTA;
                chiaveRiga = key;
                chiaveOrdinamentoNumerico = text.isValid(key) ? giornoService.findByKeyUnica(key).ordine : 0;
                chiaveCognome = text.isValid(bio.cognome) ? bio.cognome : bio.wikiTitle;
                break;
            //--i nomi dei mesi e dei giorni sono in formato testo e non possono essere ordinati
            //--uso il numero d'ordine di ogni giorno
            case annoMorto:
                didascalia = didascaliaService.getDidascaliaAnnoMorto(bio);
                key = bio.giornoMorte != null ? bio.giornoMorte.titolo : "";

                chiaveParagrafo = bio.giornoMorte != null ? bio.giornoMorte.mese.titoloLungo : "";
                chiaveParagrafo = text.primaMaiuscola(chiaveParagrafo);
                chiaveSottoPagina = VUOTA;
                chiaveSottoSottoPagina = VUOTA;
                chiaveRiga = key;
                chiaveOrdinamentoNumerico = text.isValid(key) ? giornoService.findByKeyUnica(key).ordine : 0;
                chiaveCognome = text.isValid(bio.cognome) ? bio.cognome : bio.wikiTitle;
                break;
            case listaNomi:
                if (text.isValid(bio.wikiTitle)) {

                    if (bio.wikiTitle.equals("Angelo Corbo")) {
                        int a = 87;
                    }// end of if cycle
                    if (bio.wikiTitle.equals("Angelo Joppi")) {
                        int a = 87;
                    }// end of if cycle
                    if (bio.wikiTitle.equals("Ciro De Vita")) {
                        int a = 87;
                    }// end of if cycle
                    if (bio.wikiTitle.equals("Ciro Sarno")) {
                        int a = 87;
                    }// end of if cycle
                    if (bio.wikiTitle.equals("Ciro Siciliano")) {
                        int a = 87;
                    }// end of if cycle
                    if (bio.wikiTitle.equals("Giuseppe Vinci")) {
                        int a = 87;
                    }// end of if cycle
                    if (bio.wikiTitle.equals("Giuseppe Biancani")) {
                        int a = 87;
                    }// end of if cycle
                }// end of if cycle

                didascalia = didascaliaService.getDidascaliaListe(bio);
                chiave = bio.attivita != null ? bio.attivita.singolare : "";

                chiaveParagrafo = bio.attivita != null ? bio.attivita.plurale : "";
                chiaveCognome = text.isValid(bio.cognome) ? bio.cognome : bio.wikiTitle;
                chiaveSottoPagina = text.isValid(chiaveCognome) ? chiaveCognome.substring(0, 1).toUpperCase() : "";
                chiave = chiaveParagrafo.toLowerCase();
                chiaveProfessione = getGenere(bio);
                break;
            case listaCognomi:
                didascalia = didascaliaService.getDidascaliaListe(bio);
                chiave = bio.attivita != null ? bio.attivita.singolare : "";

                if (bio.wikiTitle.equals("Fiammetta Rossi")) {
                    int a =87;
                }

                chiaveParagrafo = fixChiaveUno(chiave, sessoMaschile);
                chiaveCognome = text.isValid(bio.nome) ? bio.nome : bio.wikiTitle;
                chiaveSottoPagina = text.isValid(chiaveCognome) ? chiaveCognome.substring(0, 1).toUpperCase() : "";
                chiave = chiaveParagrafo.toLowerCase();
                chiaveProfessione = getGenere(bio);
                break;
            case listaAttivita:
                didascalia = didascaliaService.getDidascaliaListe(bio);
                chiave = bio.nazionalita != null ? bio.nazionalita.plurale : "";

                chiaveParagrafo = chiave;
                chiaveCognome = bio.nazionalita != null ? bio.nazionalita.plurale : bio.wikiTitle;
                chiaveSottoPagina = text.isValid(bio.cognome) ? bio.cognome.substring(0, 1) : bio.wikiTitle.substring(0, 1);
                chiaveProfessione = getGenere(bio);
                break;
            case listaNazionalita:
                didascalia = didascaliaService.getDidascaliaListe(bio);
                chiave = bio.attivita != null ? bio.attivita.plurale : "";

                chiaveParagrafo = chiave;
                chiaveCognome = bio.attivita != null ? bio.attivita.plurale : bio.wikiTitle;
                chiaveSottoPagina = text.isValid(bio.cognome) ? bio.cognome.substring(0, 1) : bio.wikiTitle.substring(0, 1);
                chiaveProfessione = getGenere(bio);
                break;
            case biografie:
                didascalia = didascaliaService.getDidascaliaBiografie(bio);
                this.chiave = "";
                break;
            default:
//                logger.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        String wikiTitle = bio.wikiTitle;
        String cognome = text.isValid(bio.cognome) ? bio.cognome : wikiTitle;
        this.sottoChiave = cognome + wikiTitle;

        if (didascalia != null) {
            this.testoCon = didascalia.testoCon;
            this.testoSenza = didascalia.testoSenza;
        }// end of if cycle
    }// end of method


    /**
     * Chiave per nomi e cognomi. Titolo del paragrafo.
     * La prima parte è la pagina della professione (a cui va il wikilink)
     * Se non esiste, rimane l'attività singolare
     * La seconda parte è il plurale dell'attività distinto per genere (che è quanto viene visualizzato)
     */
    public String fixChiaveUno(String chiaveSingolare, boolean sessoMaschile) {
        String chiaveUno = "";
        String linkPaginaProfessione = professioneService.getPagina(chiaveSingolare);
        String attivitaPlurale = "";
        String sep = "|";

        if (text.isEmpty(chiaveSingolare)) {
            return "";
        }// end of if cycle

        linkPaginaProfessione = text.isValid(linkPaginaProfessione) ? linkPaginaProfessione : chiaveSingolare;
        if (sessoMaschile) {
            attivitaPlurale = genereService.getPluraleMaschile(chiaveSingolare);
        } else {
            attivitaPlurale = genereService.getPluraleFemminile(chiaveSingolare);
        }// end of if/else cycle

        chiaveUno = text.primaMaiuscola(linkPaginaProfessione) + sep + text.primaMaiuscola(attivitaPlurale);
        chiaveUno = LibWiki.setQuadre(chiaveUno);

        chiaveUno = text.primaMaiuscola(attivitaPlurale); //@todo PROVVISORIO  PROVA
        return chiaveUno;
    }// end of method


    /**
     * La pagina della professione linkata dal titolo del paragrafo (se esiste) <br>
     */
    public String getGenere(Bio bio) {
        String chiave = "";
        Attivita attivita = bio.attivita;
        Professione professione = null;

        if (attivita != null) {
            chiave = attivita.singolare;
        }// end of if cycle

        if (chiave.startsWith(EX)) {
            chiave = chiave.substring(EX.length());
        }// end of if cycle

        if (chiave.startsWith(EX2)) {
            chiave = chiave.substring(EX2.length());
        }// end of if cycle

        if (text.isValid(chiave)) {
            professione = professioneService.findByKeyUnica(chiave);
        }// end of if cycle

        if (professione != null) {
            chiave = professione.pagina;
        }// end of if cycle

        return chiave;
    }// end of method


    public String getRiferimento() {
        return riferimento;
    }// end of method


    public int getChiaveOrdinamentoNumerico() {
        return chiaveOrdinamentoNumerico;
    }// end of method


    public String getChiave() {
        return chiave;
    }// end of method


    public String getSottoChiave() {
        return sottoChiave;
    }// end of method


    public String getTestoCon() {
        return testoCon;
    }// end of method


    public String getTestoSenza() {
        return testoSenza;
    }// end of method


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(WrapDidascalia wrap) {
        return this.getChiave().compareTo(wrap.getChiave());
    }// end of method


}// end of class
