package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.liste.ListaAttivita;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.USA_FORCETOC_COGNOMI;
import static it.algos.vaadwiki.application.WikiCost.USA_SOLO_PRIMA_ATTIVITA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 15-dic-2019
 * Time: 22:36
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (standard, con frequenza settimanale) <br>
 * Può essere invocato dal bottone 'Upload all' della classe AttivitaList <br>
 * Può essere invocato dal bottone della colonna 'Upload' della classe ViewAttivita <br>
 * Necessita del login come bot <br>
 * Creata con appContext.getBean(UploadAttivita.class, attivita) <br>
 * Punto di inzio @PostConstruct inizia() nella sottoclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadAttivita extends UploadNomiCognomi {


    //--property
    protected Attivita attivita;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadAttivita() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadCognome.class, cognome) <br>
     *
     * @param attivita di cui costruire la pagina sul server wiki
     */
    public UploadAttivita(Attivita attivita) {
        this.attivita = attivita;
    }// end of constructor


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
        lista = appContext.getBean(ListaAttivita.class, attivita);
        super.soggetto = attivita.plurale;
        super.inizia();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.typeDidascalia = EADidascalia.listaCognomi;
        super.usaSuddivisioneParagrafi = true;
        super.usaRigheRaggruppate = false;
        super.titoloPagina = uploadService.getTitoloAttivita(attivita);
        super.usaHeadTocIndice = pref.isBool(USA_FORCETOC_COGNOMI);
        super.usaHeadIncipit = true;
        super.usaBodyDoppiaColonna = false;
        super.tagCategoria = LibWiki.setCat("Bio attività", text.primaMaiuscola(attivita.getPlurale()));
        super.usaNote = true;
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        String testo = VUOTA;
        String message1="Le didascalie delle voci sono quelle previste nel [[Progetto:Biografie/Didascalie|progetto biografie]]";
        String message2 = "La lista non è esaustiva e contiene solo le persone che sono citate nell'enciclopedia e per le quali è stato implementato correttamente il [[template:Bio|template Bio]]";
        String message3 = "Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        String message4 = VUOTA;
        String message5 = "Le nazionalità sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        String message6 = "Nel paragrafo ''Senza nazionalità utilizzabile'' (eventuale) vengono raggruppate quelle voci biografiche che '''non''' usano il parametro ''nazionalità'' oppure che usano una nazionalità di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''";
        String message77 = "Alcune nazionalità, ancorché correttamente inserite nel [[template:Bio|template Bio]], potrebbero risultare di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''. Vengono raggruppate nel paragrafo eventuale ''Senza nazionalità utilizzabile''.";

        if (pref.isBool(USA_SOLO_PRIMA_ATTIVITA)) {
            message4 = "Ogni persona è presente in una sola [[Progetto:Biografie/Attività|lista]], in base a quanto riportata nel parametro ''attività'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona";
        } else {
            message4 = "Ogni persona è presente in diverse [[Progetto:Biografie/Attività|liste]], in base a quanto riportato nei parametri ''attività'', ''attività2''  e ''attività3'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona. Le ''attivitàAltre'' non vengono considerate";
        }// end of if/else cycle

        testo += "Questa è una lista";
        testo += LibWiki.setRef(message1);
        testo += " di persone";
        testo += LibWiki.setRef(message2);
        testo += " presenti nell'enciclopedia che hanno come attività";
        testo += LibWiki.setRef(message3);
        testo += " principale";
        testo += LibWiki.setRef(message4);
        testo += " quella di ";
        testo += "'''''";
        testo += soggetto;
        testo += "'''''";
        testo += " e sono suddivise per ";
        testo += "nazionalità.";
        testo += LibWiki.setRef(message5);
        testo += LibWiki.setRef(message6);

        return testo;
    }// fine del metodo

}// end of class
