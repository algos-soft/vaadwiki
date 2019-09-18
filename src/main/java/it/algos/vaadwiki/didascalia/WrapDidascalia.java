package it.algos.vaadwiki.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.wiki.LibWiki;
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
     * Chiave principale <br>
     * Di solito il paragrafo (che potrebbe anche non esserci) <br>
     */
    public String chiaveUno;

    /**
     * Ulteriore chiave per la lista di discalie per ogni chiave <br>
     */
    public String chiaveDue;

    /**
     * Composta dal cognome concatenato al titolo della pagina wikipedia (per essere sicuri che qualcosa esisista) <br>
     */
    public String chiaveTre;


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
        String paginaAttivita = "";
        boolean sessoMaschile = true;

        try { // prova ad eseguire il codice
            sessoMaschile = bio.getSesso().equals("F") ? false : true;
        } catch (Exception unErrore) { // intercetta l'errore
            log.error("Manca il sesso alla bio: " + bio.wikiTitle);
        }// fine del blocco try-catch

        switch (type) {
            case giornoNato:
                didascalia = didascaliaService.getDidascaliaGiornoNato(bio);
                this.chiave = bio.getAnnoNascita() != null ? bio.getAnnoNascita().titolo : "";
                this.ordine = text.isValid(chiave) ? annoService.findByKeyUnica(chiave).ordine : 0;

                chiaveUno = bio.getAnnoNascita() != null ? bio.getAnnoNascita().secolo.titolo : "";
                chiaveDue = chiave;
                chiaveTre = text.isValid(bio.getCognome()) ? bio.getCognome() : bio.getWikiTitle();
                break;
            case giornoMorto:
                didascalia = didascaliaService.getDidascaliaGiornoMorto(bio);
                this.chiave = bio.getAnnoMorte() != null ? bio.getAnnoMorte().titolo : "";
                this.ordine = text.isValid(chiave) ? annoService.findByKeyUnica(chiave).ordine : 0;

                chiaveUno = bio.getAnnoMorte() != null ? bio.getAnnoMorte().secolo.titolo : "";
                chiaveDue = chiave;
                chiaveTre = text.isValid(bio.getCognome()) ? bio.getCognome() : bio.getWikiTitle();
                break;
            case annoNato:
                didascalia = didascaliaService.getDidascaliaAnnoNato(bio);
                this.chiave = bio.getGiornoNascita() != null ? bio.getGiornoNascita().titolo : "";
                this.ordine = text.isValid(chiave) ? giornoService.findByKeyUnica(chiave).ordine : 0;

                chiaveUno = bio.getGiornoNascita() != null ? bio.getGiornoNascita().mese.titoloLungo : "";
                chiaveUno = text.primaMaiuscola(chiaveUno);
                chiaveDue = chiave;
                chiaveTre = text.isValid(bio.getCognome()) ? bio.getCognome() : bio.getWikiTitle();
                break;
            case annoMorto:
                didascalia = didascaliaService.getDidascaliaAnnoMorto(bio);
                this.chiave = bio.getGiornoMorte() != null ? bio.getGiornoMorte().titolo : "";
                this.ordine = text.isValid(chiave) ? giornoService.findByKeyUnica(chiave).ordine : 0;

                chiaveUno = bio.getGiornoMorte() != null ? bio.getGiornoMorte().mese.titoloLungo : "";
                chiaveUno = text.primaMaiuscola(chiaveUno);
                chiaveDue = chiave;
                chiaveTre = text.isValid(bio.getCognome()) ? bio.getCognome() : bio.getWikiTitle();
                break;
            case listaNomi:
            case listaCognomi:
                didascalia = didascaliaService.getDidascaliaListe(bio);
                this.chiave = bio.getAttivita() != null ? bio.getAttivita().singolare : "";

                chiaveUno = fixChiaveUno(chiave, sessoMaschile);
                chiaveDue = "";
                chiaveTre = text.isValid(bio.getCognome()) ? bio.getCognome() : bio.getWikiTitle();
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
