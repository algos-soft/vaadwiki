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

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ATextService text;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected DidascaliaGiornoNato didascaliaGiornoNato;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected DidascaliaGiornoMorto didascaliaGiornoMorto;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected DidascaliaAnnoNato didascaliaAnnoNato;
    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected DidascaliaAnnoMorto didascaliaAnnoMorto;


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
     * Testo della didascalia completo <br>
     */
    private String testo;

    /**
     * Testo della didascalia SENZA la chiave che viene aggiunta nella composizione della pagina <br>
     * La chiave viene aggiunta in maniera differente tra 'righeSemplici' o 'righeRaggruppate' <br>
     */
    private String testoSenza;

    private Bio bio;

    private EADidascalia type;


    public WrapDidascalia() {
    }// end of constructor


    public WrapDidascalia(String riferimento, int ordine, String chiave, String sottoChiave, String testo) {
        this.riferimento = riferimento;
        this.ordine = ordine;
        this.chiave = chiave;
        this.sottoChiave = sottoChiave;
        this.testo = testo;
    }// end of constructor


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
        switch (type) {
            case completa:
                break;
            case giornoNato:
                giornoNato();
                break;
            case annoNato:
                annoNato();
                break;
            case giornoMorto:
                giornoMorto();
                break;
            case annoMorto:
                annoMorto();
                break;
            case standard:
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement
    }// end of method


    public void giornoNato() {
        this.riferimento = bio.getGiornoNato();
        this.chiave = bio.getAnnoNato();
        this.ordine = text.isValid(chiave) ? annoService.findByKeyUnica(chiave).ordine : 0;
        String wikiTitle = bio.getWikiTitle();
        String cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
        this.sottoChiave = cognome + wikiTitle;
        this.testo = didascaliaGiornoNato.esegue(bio);
        this.testoSenza = didascaliaGiornoNato.esegueSenza(bio);
    }// end of method


    public void annoNato() {
        this.riferimento = bio.getAnnoNato();
        this.chiave = bio.getGiornoNato();
        this.ordine = text.isValid(chiave) ? giornoService.findByKeyUnica(chiave).ordine : 0;
        String wikiTitle = bio.getWikiTitle();
        String cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
        this.sottoChiave = cognome + wikiTitle;
        this.testo = didascaliaAnnoNato.esegue(bio);
        this.testoSenza = didascaliaAnnoNato.esegueSenza(bio);
    }// end of method


    public void giornoMorto() {
        this.riferimento = bio.getGiornoMorto();
        this.chiave = bio.getAnnoMorto();
        this.ordine = text.isValid(chiave) ? annoService.findByKeyUnica(chiave).ordine : 0;
        String wikiTitle = bio.getWikiTitle();
        String cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
        this.sottoChiave = cognome + wikiTitle;
        this.testo = didascaliaGiornoMorto.esegue(bio);
        this.testoSenza = didascaliaGiornoMorto.esegueSenza(bio);
    }// end of method


    public void annoMorto() {
        this.riferimento = bio.getAnnoMorto();
        this.chiave = bio.getGiornoMorto();
        this.ordine = text.isValid(chiave) ? giornoService.findByKeyUnica(chiave).ordine : 0;
        String wikiTitle = bio.getWikiTitle();
        String cognome = text.isValid(bio.getCognome()) ? bio.getCognome() : wikiTitle;
        this.sottoChiave = cognome + wikiTitle;
        this.testo = didascaliaAnnoMorto.esegue(bio);
        this.testoSenza = didascaliaAnnoMorto.esegueSenza(bio);
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


    public String getTesto() {
        return testo;
    }// end of method


    public String getTestoSenza() {
        return testoSenza;
    }// end of method

}// end of class
