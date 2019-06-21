package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

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
public class WrapDidascalia {

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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    protected DidascaliaGiornoNato didascaliaGiornoNato;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    protected DidascaliaGiornoMorto didascaliaGiornoMorto;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    protected DidascaliaAnnoNato didascaliaAnnoNato;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    protected DidascaliaAnnoMorto didascaliaAnnoMorto;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    protected DidascaliaBiografie didascaliaBiografie;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
//    @Autowired
//    protected DidascaliaListe didascaliaListe;

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
    private int ordine;

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

        switch (type) {
            case giornoNato:
                didascalia = didascaliaService.getDidascaliaGiornoNato(bio);
                this.chiave = bio.getAnnoNascita() != null ? bio.getAnnoNascita().titolo : "";
//                this.riferimento = bio.getAnnoNascita() != null ? bio.getAnnoNascita().titolo : "";
                this.ordine = text.isValid(chiave) ? annoService.findByKeyUnica(chiave).ordine : 0;
                break;
            case giornoMorto:
                didascalia = didascaliaService.getDidascaliaGiornoMorto(bio);
                this.chiave = bio.getAnnoMorte() != null ? bio.getAnnoMorte().titolo : "";
                this.ordine = text.isValid(chiave) ? annoService.findByKeyUnica(chiave).ordine : 0;
                break;
            case annoNato:
                didascalia = didascaliaService.getDidascaliaAnnoNato(bio);
                this.chiave = bio.getGiornoNascita() != null ? bio.getGiornoNascita().titolo : "";
                this.ordine = text.isValid(chiave) ? giornoService.findByKeyUnica(chiave).ordine : 0;
                break;
            case annoMorto:
                didascalia = didascaliaService.getDidascaliaAnnoMorto(bio);
                this.chiave = bio.getGiornoMorte() != null ? bio.getGiornoMorte().titolo : "";
                this.ordine = text.isValid(chiave) ? giornoService.findByKeyUnica(chiave).ordine : 0;
                break;
            case liste:
                didascalia = didascaliaService.getDidascaliaListe(bio);
                this.chiave = bio.getAttivita() != null ? bio.getAttivita().singolare : "";
                break;
            case biografie:
                didascalia = didascaliaService.getDidascaliaBiografie(bio);
                this.chiave = "";
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        String wikiTitle = bio.getWikiTitle();
        String cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
        this.sottoChiave = cognome + wikiTitle;

        if (didascalia != null) {
            this.testoCon = didascalia.testoCon;
            this.testoSenza = didascalia.testoSenza;
        }// end of if cycle
    }// end of method


    public String getRiferimento() {
        return riferimento;
    }// end of method


    public int getOrdine() {
        return ordine;
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

}// end of class
