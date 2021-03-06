package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.didascalia.DidascaliaService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Random;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.didascalia.DidascaliaService.TITOLO_PAGINA_WIKI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 09-Jun-2019
 * Time: 18:31
 * <p>
 * Statistica specializzata per costruire sul server wiki una pagina di esempio di didascalie. <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheDidascalie extends Statistiche {


    private static String TAG_HEAD_TEMPLATE_AVVISO = "StatBio";

    private static String[] NOMI = {"Sergio Ferrero", "Gene Clark", "Bill Miller (astista)", "Harry Fox"};

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    private BioService bioService;


    private Bio bio;

    private String testoPagina;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheDidascalie.class) <br>
     */
    public StatisticheDidascalie() {
    }// end of Spring constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheDidascalie.class, bio) <br>
     *
     * @param bio di cui costruire la didascalia
     */
    public StatisticheDidascalie(Bio bio) {
        this.bio = bio;
    }// end of constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheDidascalie.class, bio) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void initIstanzaDopoInitDiSpringBoot() {
        inizia();
    }// end of method


    /**
     * Recupera la biografia da usare come esempio <br>
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void inizia() {
        fixBiografia();
        elaboraPagina();
        registraPagina();
    }// end of method


    /**
     * Recupera la biografia da usare come esempio <br>
     * Se era stata passata come parametro alla creazione dell'istanza, usa quella <br>
     * Altrimenti recupera una biografia di default <br>
     */
    protected void fixBiografia() {
        if (bio == null) {
            bio = bioService.findByKeyUnica(getNome());
        }// end of if cycle
    }// end of method


    /**
     * Elaborazione principale della pagina <br>
     * <p>
     * Costruisce head <br>
     * Costruisce body <br>
     * Costruisce footer <br>
     * Ogni blocco esce trimmato (inizio e fine) <br>
     * Gli spazi (righe) di separazione vanno aggiunti qui <br>
     * Registra la pagina <br>
     */
    protected void elaboraPagina() {
        testoPagina = VUOTA;

        //--header
        this.elaboraHead();

        //--body
        //--a capo, ma senza senza righe di separazione
        testoPagina += A_CAPO;
        this.elaboraBody();

        //--footer
        //--di fila nella stessa riga, senza ritorno a capo (se inizia con <include>)
        testoPagina += A_CAPO;
        this.elaboraFooter();
    }// fine del metodo


    /**
     * Costruisce il testo iniziale della pagina (header)
     * <p>
     * Non sovrascrivibile <br>
     * Ogni blocco esce trimmato (per l'inizio) e con un solo ritorno a capo per fine riga. <br>
     * Eventuali spazi gestiti da chi usa il metodo <br>
     */
    private void elaboraHead() {
        String testo = VUOTA;

        //--Posizione il template di avviso
        testo = elaboraTemplateAvviso();

        //--Ritorno ed avviso vanno (eventualmente) protetti con 'include'
        testo = elaboraInclude(testo);

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Costruisce il template di avviso
     */
    private String elaboraTemplateAvviso() {
        String testo = VUOTA;
        String dataCorrente = date.get();

        testo += TAG_HEAD_TEMPLATE_AVVISO;
        testo += "|data=";
        testo += dataCorrente.trim();
        testo = LibWiki.setGraffe(testo);

        return testo;
    }// fine del metodo


    /**
     * Incorpora il testo iniziale nel tag 'include'
     */
    private String elaboraInclude(String testoIn) {
        String testoOut = testoIn;
        testoOut = LibBio.setNoIncludeRiga(testoIn);
        return testoOut;
    }// fine del metodo


    /**
     * Corpo della pagina <br>
     */
    protected void elaboraBody() {
        String testo = VUOTA;

        testo += "==Didascalie==";
        testo += A_CAPO;

        //--inizio tabella
        testo += A_CAPO;
        testo += "{| class=\"wikitable\"\n|colspan=\"3\" |";

        testo += "Pagina di servizio per il '''controllo'''";
        testo += " delle didascalie utilizzate nelle pagine delle liste di giorni, anni, nomi e cognomi.";
        testo += " Le didascalie sono di diversi tipi:";

        testo += rigaBiografie();
        testo += rigaGiornoNato();
        testo += rigaGiornoMorto();
        testo += rigaAnnoNato();
        testo += rigaAnnoMorto();
        testo += rigaListeNome();
        testo += rigaListeCognome();
        testo += rigaListeAttivita();
        testo += rigaListeNazionalita();

        //--fine tabella
        testo += "\n|}";

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia Biografie <br>
     */
    protected String rigaBiografie() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        String title = "";
        String linkPagina;

        if (bio != null) {
            title = bio.wikiTitle;
            if (text.isValid(title)) {
                didascalia = didascaliaService.getBiografie(bio);
                linkPagina = LibWiki.setQuadre(title);
                testo += "Nell'incipit della pagina biografica " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia GiornoNato <br>
     */
    protected String rigaGiornoNato() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        Giorno giorno = null;
        String linkPagina = "";

        if (bio != null) {
            giorno = bio.giornoNascita;
            if (giorno != null) {
                didascalia = didascaliaService.getGiornoNatoCon(bio);
                linkPagina = uploadService.getTitoloGiornoNato(giorno);
                linkPagina = LibWiki.setQuadre(linkPagina.toLowerCase());
                testo += "Nella pagina con la lista dei " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia GiornoMorto <br>
     */
    protected String rigaGiornoMorto() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        Giorno giorno = null;
        String linkPagina;

        if (bio != null) {
            giorno = bio.giornoMorte;
            if (giorno != null) {
                didascalia = didascaliaService.getGiornoMortoCon(bio);
                linkPagina = uploadService.getTitoloGiornoMorto(giorno);
                linkPagina = LibWiki.setQuadre(linkPagina.toLowerCase());
                testo += "Nella pagina con la lista dei " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia AnnoNato <br>
     */
    protected String rigaAnnoNato() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        Anno anno = null;
        String linkPagina;

        if (bio != null) {
            anno = bio.annoNascita;
            if (anno != null) {
                didascalia = didascaliaService.getAnnoNatoCon(bio);
                linkPagina = uploadService.getTitoloAnnoNato(anno);
                linkPagina = LibWiki.setQuadre(linkPagina.toLowerCase());
                testo += "Nella pagina con la lista dei " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia AnnoMorto <br>
     */
    protected String rigaAnnoMorto() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        Anno anno = null;
        String linkPagina;

        if (bio != null) {
            anno = bio.annoMorte;
            if (anno != null) {
                didascalia = didascaliaService.getAnnoMortoCon(bio);
                linkPagina = uploadService.getTitoloAnnoMorto(anno);
                linkPagina = LibWiki.setQuadre(linkPagina.toLowerCase());
                testo += "Nella pagina con la lista dei " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia Liste <br>
     */
    protected String rigaListeNome() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        String nome = "";
        String linkPagina;

        if (bio != null) {
            nome = bio.nome;
            if (text.isValid(nome)) {
                didascalia = didascaliaService.getListeSenza(bio);
                linkPagina = LibWiki.setQuadre("persone di nome " + nome);
                testo += "Nella pagina con la lista delle " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia Liste <br>
     */
    protected String rigaListeCognome() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        String cognome = "";
        String linkPagina;

        if (bio != null) {
            cognome = bio.cognome;
            if (text.isValid(cognome)) {
                didascalia = didascaliaService.getListeSenza(bio);
                linkPagina = LibWiki.setQuadre("persone di cognome " + cognome);
                testo += "Nella pagina con la lista delle " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia Liste <br>
     */
    protected String rigaListeAttivita() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        String attivita = "";
        String linkPagina;

        if (bio != null) {
            attivita = bio.attivita != null ? bio.attivita.plurale : VUOTA;
            if (text.isValid(attivita)) {
                didascalia = didascaliaService.getListeSenza(bio);
                linkPagina = LibWiki.setQuadre("Progetto:Biografie/Attività/" + text.primaMaiuscola(attivita) + "|" + attivita);
                testo += "Nella pagina con la lista di " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Riga di esempio per la didascalia Liste <br>
     */
    protected String rigaListeNazionalita() {
        String testo = INIZIO_RIGA;
        String didascalia = "";
        String nazionalita = "";
        String linkPagina;

        if (bio != null) {
            nazionalita = bio.nazionalita != null ? bio.nazionalita.plurale : VUOTA;
            if (text.isValid(nazionalita)) {
                didascalia = didascaliaService.getListeSenza(bio);
                linkPagina = LibWiki.setQuadre("Progetto:Biografie/Nazionalità/" + text.primaMaiuscola(nazionalita) + "|" + nazionalita);
                testo += "Nella pagina con la lista di " + SEP_DOPPIO + linkPagina + SEP_DOPPIO + LibBio.setBold(didascalia);
            }// end of if cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    /**
     * Costruisce il testo finale della pagina
     */
    protected void elaboraFooter() {
        String testo = VUOTA;

        testo += "{{BioCorrelate}}";
        testo += A_CAPO;
        testo += LibBio.setNoIncludeMultiRiga("[[Categoria:Progetto Biografie|{{PAGENAME}}]]");

        testoPagina += testo.trim();
    }// fine del metodo


    /**
     * Registra la pagina sul server wiki <br>
     */
    protected void registraPagina() {
        String titolo;

        if (text.isValid(testoPagina)) {
            testoPagina = testoPagina.trim();

            if (pref.isBool(USA_DEBUG)) {
                titolo = PAGINA_PROVA;
                testoPagina = TITOLO_PAGINA_WIKI + A_CAPO + testoPagina;
            } else {
                titolo = TITOLO_PAGINA_WIKI;
            }// fine del blocco if-else

            appContext.getBean(AQueryWrite.class, titolo, testoPagina);
        }// fine del blocco if
    }// end of method


    /**
     * Contenuto random della lista di nomi <br>
     */
    private String getNome() {
        String nome = VUOTA;
        Random random = null;
        int dim = 0;
        int pos = 0;

        if (NOMI != null && NOMI.length > 0) {
            dim = NOMI.length;
            random = new Random();
            pos = random.nextInt(dim);
            nome = NOMI[pos];
        }// end of if cycle

        return nome;
    }// end of method

}// end of class
