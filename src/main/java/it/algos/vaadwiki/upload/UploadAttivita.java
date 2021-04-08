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
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

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
public class UploadAttivita extends Upload {


    //--property
    protected Attivita attivita;

    private String notaSuddivisione;

    private String notaParagrafoVuoto;

    private String notaCreazione;
    protected String notaAttivitaSingola;
    protected String notaAttivitaMultiple;


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
     * Usa: appContext.getBean(UploadAttivita.class, cognome) <br>
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

        super.typeDidascalia = EADidascalia.listaAttivita;
        super.usaSuddivisioneParagrafi = true;
        super.usaRigheRaggruppate = false;
        super.titoloPagina = uploadService.getTitoloAttivita(attivita);
        super.usaHeadTocIndice = pref.isBool(USA_FORCETOC_COGNOMI);
        super.usaHeadIncipit = true;
        super.usaBodyDoppiaColonna = false;
        super.tagCategoria = LibWiki.setCat("Bio attività", text.primaMaiuscola(attivita.plurale));
        super.usaNote = true;
        super.usaVociCorrelate = true;
        super.usaBodySottopagine = true;

        // note <ref>
        notaCreazione = "La pagina di una singola attività viene creata se le relative voci biografiche superano le " + pref.getInt(SOGLIA_ATT_NAZ_PAGINA_WIKI) + " unità.";
        notaSuddivisione = "La lista è suddivisa in paragrafi per ogni nazionalità individuata. Se il numero di voci biografiche nel paragrafo supera le " + taglioSottoPagina + " unità, viene creata una sottopagina.";
        notaParagrafoVuoto = "Nel paragrafo " + titoloParagrafoVuoto + " (eventuale) vengono raggruppate quelle voci biografiche che '''non''' usano il parametro ''nazionalità'' oppure che usano una nazionalità di difficile elaborazione da parte del '''[[Utente:Biobot|<span style=\"color:green;\">bot</span>]]'''";
        notaAttivitaSingola = "Ogni persona è presente in una sola [[Progetto:Biografie/Attività|lista]], in base a quanto riportato nel parametro ''attività'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona";
        notaAttivitaMultiple = "Ogni persona è presente in diverse [[Progetto:Biografie/Attività|liste]], in base a quanto riportato nei parametri ''attività'', ''attività2''  e ''attività3'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona. Le ''attivitàAltre'' non vengono considerate";
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        String testo = VUOTA;

        testo += "Questa è una lista";
        testo += LibWiki.setRef(notaDidascalie);
        testo += LibWiki.setRef(notaOrdinamento);
        testo += " di persone";
        testo += LibWiki.setRef(notaEsaustiva);
        testo += " presenti";
        testo += LibWiki.setRef(notaCreazione);
        testo += " nell'enciclopedia che hanno come attività";
        testo += LibWiki.setRef(notaAttivita);
        testo += " principale";
        testo += LibWiki.setRef(pref.isBool(USA_SOLO_PRIMA_ATTIVITA) ? notaAttivitaSingola : notaAttivitaMultiple);
        testo += " quella di ";
        testo += LibWiki.setBold(soggetto);
        testo += ". Le persone sono suddivise";
        testo += LibWiki.setRef(notaSuddivisione);
        testo += " per nazionalità.";
        testo += LibWiki.setRef(notaNazionalita);
        testo += LibWiki.setRef(notaParagrafoVuoto);

        return testo;
    }// fine del metodo


    /**
     * Costruisce la frase di incipit iniziale per la sottopagina
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecificoSottopagina(String soggettoSottopagina) {
        String testo = VUOTA;

        testo += "Questa è una lista";
        testo += LibWiki.setRef(notaDidascalie);
        testo += LibWiki.setRef(notaOrdinamento);
        testo += " di persone";
        testo += LibWiki.setRef(notaEsaustiva);
        testo += " presenti";
        testo += LibWiki.setRef(notaSottoPagina);
        testo += " nell'enciclopedia che hanno come attività";
        testo += LibWiki.setRef(notaAttivita);
        testo += " principale";
        testo += LibWiki.setRef(pref.isBool(USA_SOLO_PRIMA_ATTIVITA) ? notaAttivitaSingola : notaAttivitaMultiple);
        testo += " quella di ";
        testo += LibWiki.setBold(soggetto);
        testo += " e sono ";
        testo += LibWiki.setBold(soggettoSottopagina);
        testo += LibWiki.setRef(notaNazionalita);
        testo += ".";

        return testo;
    }// fine del metodo


    /**
     * Lista delle voci correlate (eventuale)
     * Sovrascritto
     */
    protected List<String> listaVociCorrelate() {
        List<String> lista = new ArrayList<>();

        lista.add(":Categoria:" + text.primaMaiuscola(attivita.plurale));
        lista.add("Progetto:Biografie/Attività");
        return lista;
    }// fine del metodo

}// end of class
