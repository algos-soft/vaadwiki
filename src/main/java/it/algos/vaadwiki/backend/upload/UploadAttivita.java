package it.algos.vaadwiki.backend.upload;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.exceptions.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 12-feb-2022
 * Time: 17:19
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadAttivita extends Upload {

    private final static String WIKI_TITLE_DEBUG = QueryWrite.WIKI_TITLE_DEBUG;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaService attivitaService;

    protected Attivita attivita;

    protected String notaCreazione;

    protected String notaAttivitaSingola;

    protected String notaAttivitaMultiple;

    protected WrapLista.AETypeMappa typeMappa;

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param entityBean di cui costruire la pagina sul server wiki
     */
    public UploadAttivita(final AEntity entityBean) {
        this(entityBean, WrapLista.AETypeMappa.paginaPrincipale, true);
    }// end of constructor

    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param entityBean di cui costruire la pagina sul server wiki
     */
    public UploadAttivita(final AEntity entityBean, final WrapLista.AETypeMappa typeMappa) {
        this(entityBean, typeMappa, true);
    }// end of constructor


    /**
     * Costruttore base con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param entityBean        di cui costruire la pagina sul server wiki
     * @param usaWikiPaginaTest una sottoPagina di Utente:Gac/
     */
    public UploadAttivita(final AEntity entityBean, final WrapLista.AETypeMappa typeMappa, final boolean usaWikiPaginaTest) {
        //        super.entityBean = entityBean;
        //        super.usaWikiPaginaTest = usaWikiPaginaTest;
        this.typeMappa = typeMappa;
        this.attivita = (Attivita) entityBean;
    }// end of constructor

    public UploadAttivita inizia() {
//        summary = text.setDoppieQuadre("Utente:" + botLogin.getUsername() + "|" + botLogin.getUsername());
//        this.fixPreferenze();
//        this.regolazioniIniziali();
//        this.fixLista();
//        this.fixVoci();
//        this.fixPagina();

        return (UploadAttivita)super.inizia();
    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        int maxNum = 50;
        super.fixPreferenze();

        notaCreazione = String.format("La pagina di una singola attività viene creata se le relative voci biografiche superano le %d unità.", maxNum);
        notaAttivitaSingola = "Ogni persona è presente in una sola [[Progetto:Biografie/Attività|lista]], in base a quanto riportato nel parametro ''attività'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona";
        notaAttivitaMultiple = "Ogni persona è presente in diverse [[Progetto:Biografie/Attività|liste]], in base a quanto riportato nei parametri ''attività'', ''attività2''  e ''attività3'' del [[template:Bio|template Bio]] presente nella voce biografica specifica della persona. Le ''attivitàAltre'' non vengono considerate";

        notaDidascalie = "Le didascalie delle voci sono quelle previste nel [[Progetto:Biografie/Didascalie|progetto biografie]]";
        //        notaOrdinamento = "Le voci, all'interno di ogni paragrafo, sono in ordine alfabetico per " + LibWiki.setBold("cognome") + "; se questo manca si utilizza il " + LibWiki.setBold("titolo") + " della pagina.";
        notaEsaustiva = "La lista non è esaustiva e contiene solo le persone che sono citate nell'enciclopedia e per le quali è stato implementato correttamente il [[template:Bio|template Bio]]";
        notaSottoPagina = "Questa sottopagina specifica viene creata se il numero di voci biografiche nel paragrafo della pagina principale supera le " + taglioSottoPagina + " unità.";
        notaAttivita = "Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
        notaNazionalita = "Le nazionalità sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]";
    }

    /**
     * Costruisce l'istanza associata di ListaXxx <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    public void fixLista() {
        super.fixLista();
        lista = appContext.getBean(ListaAttivita.class, attivita);
    }

    //    protected String getTestoParagrafi() {
    //        String nomeAttivitaPlurale = VUOTA;
    //
    //        try {
    //            nomeAttivitaPlurale = attivita.getPlurale();
    //        } catch (Exception unErrore) {
    //            AlgosException.stack(unErrore, this.getClass(), "getTestoParagrafi");
    //        }
    //        return appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale).getTestoParagrafi();
    //    }

    //    /**
    //     * Upload basato su attivitàPlurale <br>
    //     */
    //    protected void uploadPaginaTest() {
    //        ListaAttivita lista = null;
    //        String newText = VUOTA;
    //        String summary = "attività di prova";
    //        String nomeAttivitaPlurale = VUOTA;
    //
    //        try {
    //            nomeAttivitaPlurale = attivita.getPlurale();
    //            lista = appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale);
    //        } catch (Exception unErrore) {
    //            AlgosException.stack(unErrore, this.getClass(), "uploadPaginaTest");
    //        }
    //
    //        if (lista != null) {
    //            newText = lista.getTestoParagrafi();
    //        }
    //
    //        if (text.isValid(newText)) {
    //            appContext.getBean(QueryWrite.class).urlRequest(WIKI_TITLE_DEBUG, newText, summary);
    //        }
    //
    //    }


    /**
     * Costruisce la frase di incipit iniziale <br>
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("Questa è una lista");
        buffer.append(text.setRef(notaDidascalie));
        buffer.append(text.setRef(notaOrdinamento));
        buffer.append(" di persone");
        buffer.append(text.setRef(notaEsaustiva));
        buffer.append(" presenti");
        buffer.append(text.setRef(notaCreazione));
        buffer.append(" nell'enciclopedia che hanno come attività");
        buffer.append(text.setRef(notaAttivita));
        buffer.append(" principale");
        buffer.append(text.setRef(pref.isBool(USA_TRE_ATTIVITA) ? notaAttivitaSingola : notaAttivitaMultiple));
        buffer.append(" quella di ");
        //        testo += text.setBold(soggetto);
        buffer.append(attivita.plurale);
        buffer.append(". Le persone sono suddivise");
        //        testo += text.setRef(notaSuddivisione);
        buffer.append(" per nazionalità.");
        buffer.append(text.setRef(notaNazionalita));
        //        testo += text.setRef(notaParagrafoVuoto);

        return buffer.toString();
    }

    /**
     * Inserisce nel testo i rinvii alle sottoPagine <br>
     * Elabora gli upload delle sottoPagine <br>
     * <p>
     * Sovrascritto <br>
     */
    protected String fixSottopagine(final String testoIn) {
        String testoOut = testoIn;

        try {
            attivita = attivitaService.findByKey("aforista");
        } catch (AlgosException unErrore) {
            int a = 87;
        }
        if (typeMappa == WrapLista.AETypeMappa.paginaPrincipale) {
            appContext.getBean(UploadAttivita.class, attivita, WrapLista.AETypeMappa.sottoPagina).inizia();
        }

        return testoOut;
    }

    /**
     * Titolo della pagina 'madre' <br>
     * <p>
     * DEVE essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     */
    protected String getTitoloPaginaMadre() {
        return "Pippox";
    }

    protected String getSummary() {
        return VUOTA;
    }

}
