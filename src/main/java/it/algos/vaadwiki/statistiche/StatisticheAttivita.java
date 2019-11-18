package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryWrite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.LAST_UPLOAD_STATISTICHE_ATTIVITA;

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

    private static String TAG_HEAD_TEMPLATE_AVVISO = "StatBio";

    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Attività";

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AttivitaService service;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private BioService bioService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AArrayService array;

    @Autowired
    private AMongoService mongo;

    private String testoPagina;

    private List<String> listaAttivitaPlurali;


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
    protected void postConstruct() {
        long inizio = System.currentTimeMillis();
        inizia();
        setLastUpload(inizio);
    }// end of method


    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void inizia() {
        creaLista();
        elaboraPagina();
        registraPagina();
    }// end of method


    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */
    protected void creaLista() {
        listaAttivitaPlurali = service.findAllPlurali();
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
        int numBio = bioService.count();

        testo += A_CAPO;
        testo += "==Attività usate==";
        testo += A_CAPO;
        testo += "'''";
        testo += listaAttivitaPlurali.size();
        testo += "'''";
        testo += " attività";
        testo += "<ref>Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]</ref>";
        testo += " '''effettivamente utilizzate''' nelle  '''[[:Categoria:BioBot|" + text.format(numBio) + "]]'''";
        testo += "<ref>La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO</ref>";
        testo += " voci biografiche che usano il [[template:Bio|template Bio]] ed i parametri '''''attività'''''.";

        //--tabella
        testo += A_CAPO;
        testo += creaTabella();
        testo += A_CAPO;

        testo += "==Note==";
        testo += A_CAPO;
        testo += "<references/>";

        testoPagina += testo.trim();
    }// fine del metodo


    private String creaTabella() {
        String testo = "";

        testo += inizioTabella();
        testo += colonneTabella();
        testo += corpoTabella();
        testo += fineTabella();

        return testo;
    }// fine del metodo


    private String inizioTabella() {
        String testo = "";

        testo += A_CAPO;
        testo += "{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF;\"";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    private String colonneTabella() {
        String testo = "";
        String color = "! style=\"background-color:#CCC;\" |";

        testo += color;
        testo += "'''#'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''lista";
        testo += "<ref>Nelle liste le biografie sono suddivise per attività rilevanti della persona. Se il numero di voci di un paragrafo diventa rilevante, vengono create delle sottopagine specifiche di quella attività. Le sottopagine sono suddivise a loro volta in paragrafi alfabetici secondo l'iniziale del cognome.</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''categoria";
        testo += "<ref>Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]].</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''1° att'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''2° att'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''3° att'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''totale'''";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    private String corpoTabella() {
        StringBuilder testo = new StringBuilder();
        int cont = 0;
        int k = 0;

        for (String attivitaPlurale : listaAttivitaPlurali) {
            k=k+1;
            cont=k;
            testo.append(rigaTabella(attivitaPlurale, cont));
        }// end of for cycle

        return testo.toString();
    }// fine del metodo


    private String rigaTabella(String attivitaPlurale, int cont) {
        String testo = "";
        String tagDx = "style=\"text-align: right;\" |";
        String nome = attivitaPlurale.toLowerCase();
        String listaTag = "[[Progetto:Biografie/Attività/";
        String categoriaTag = "[[:Categoria:";
        String sepTag = "|";
        String endTag = "]]";
        String lista;
        String categoria;
        List<Attivita> listaAttivita = service.findAllByPlurale(attivitaPlurale);
        int numAttivitaUno = 0;
        int numAttivitaDue = 0;
        int numAttivitaTre = 0;
        int numAttivitaTotali = 0;

        lista = listaTag + text.primaMaiuscola(nome) + sepTag + nome + endTag;
        categoria = categoriaTag + nome + sepTag + nome + endTag;

        for (Attivita attivita : listaAttivita) {
            numAttivitaUno += bioService.countByAttivitaPrincipale(attivita);
            numAttivitaDue += bioService.countByAttivitaDue(attivita);
            numAttivitaTre += bioService.countByAttivitaTre(attivita);
            numAttivitaTotali += bioService.countByAttivitaTotali(attivita);
        }// end of for cycle

        testo += "|-";
        testo += A_CAPO;
        testo += "|";

        testo += tagDx;
        testo += cont;

        testo += " || ";
        testo += lista;

        testo += " || ";
        testo += categoria;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaUno;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaDue;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaTre;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaTotali;

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    private String fineTabella() {
        String testo = "";

        testo += "|}";
        testo += A_CAPO;

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
     * Registra nelle preferenze la data dell'ultimo upload effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo upload effettuato, in minuti <br>
     */
    protected void setLastUpload(long inizio) {
        int delta = 1000 * 60;
        LocalDateTime lastDownload = LocalDateTime.now();
        pref.saveValue(LAST_UPLOAD_STATISTICHE_ATTIVITA, lastDownload);

        long fine = System.currentTimeMillis();
        long durata = fine - inizio;
        int minuti = 0;
        if (durata > delta) {
            minuti = (int) durata / delta;
        } else {
            minuti = 0;
        }// end of if/else cycle
        pref.saveValue(WikiCost.DURATA_UPLOAD_STATISTICHE_ATTIVITA, minuti);
    }// end of method

}// end of class
