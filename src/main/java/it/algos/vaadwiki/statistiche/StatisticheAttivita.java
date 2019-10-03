package it.algos.vaadwiki.statistiche;

import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.didascalia.DidascaliaService.TITOLO_PAGINA_WIKI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 01-ott-2019
 * Time: 07:57
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheAttivita extends Statistiche {

    private String testoPagina;
    private static String TAG_HEAD_TEMPLATE_AVVISO = "StatBio";

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheAttivita.class) <br>
     */
    public StatisticheAttivita() {
    }// end of Spring constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheAttivita.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void initIstanzaDopoInitDiSpringBoot() {
        inizia();
    }// end of method


    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void inizia() {
        elaboraPagina();
        registraPagina();
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

//        testo += "Pagina di servizio per il '''controllo'''";
//        testo += " delle didascalie utilizzate nelle pagine delle liste di giorni, anni, nomi e cognomi.";
//        testo += " Le didascalie sono di diversi tipi:";


        testo +="Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' elenco]] utilizzato dal [[template:Bio|template Bio]]</ref> '''effettivamente utilizzate''' nelle  '''[[:Categoria:BioBot|351.787]]'''<ref>La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO</ref> voci biografiche che usano il [[template:Bio|template Bio]].";


        //--fine tabella
        testo += "\n|}";

        testoPagina += testo.trim();
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

}// end of class
