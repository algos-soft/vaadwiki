package it.algos.vaadwiki.backend.upload;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.professione.*;
import it.algos.vaadwiki.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

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

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ProfessioneService professioneService;

    protected String notaCreazione;

    protected String notaAttivitaMultiple;

    protected AETypePagina typePagina;

    protected List<String> lista;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param entityBean di cui costruire la pagina sul server wiki
     */
    public UploadAttivita(final Attivita attivita) {
        this.typePagina = AETypePagina.paginaPrincipale;
        this.attivita = attivita;
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
    public UploadAttivita(final Attivita attivita, final AETypePagina typePagina) {
        this.typePagina = typePagina;
        this.attivita = attivita;
    }// end of constructor


    /**
     * Costruttore alternativo con parametri per le sottopagine <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Uso: appContext.getBean(UploadAttivita.class, attivita) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param entityBean di cui costruire la pagina sul server wiki
     */
    public UploadAttivita(final Attivita attivita, final LinkedHashMap<String, List<String>> mappa) {
        this.typePagina = AETypePagina.sottoPagina;
        this.attivita = attivita;
        this.mappa = mappa;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        int maxNumBio = 50; //@todo preferenze
        int maxNumParagrafo = 50; //@todo preferenze
        super.fixPreferenze();
        Professione professione = null;

        String attivitaTxt = html.bold("attività");
        String unitaBio = html.bold(maxNumBio + " unità");
        String unitaParagrafo = html.bold(maxNumParagrafo + " unità");
        String lista = html.bold("[[Progetto:Biografie/Attività|lista]]");
        String liste = html.bold("[[Progetto:Biografie/Attività|liste]]");
        String paragrafi = html.bold("paragrafi");
        String singola = html.bold("''attività''");
        String multipla = html.bold("''attività'', ''attività2'' e ''attività3''");
        String linkAttivita = html.bold(attivita.plurale);

        try {
            professione = professioneService.findByProperty("attivita", attivita.singolare);
        } catch (AlgosException unErrore) {
            logger.warn(unErrore, this.getClass(), "fixPreferenze");
        }
        if (professione != null) {
            notaLinkAttivita = html.bold(String.format("%s%s%s%s%s", DOPPIE_QUADRE_INI, professione.getPagina(), PIPE, attivita.plurale, DOPPIE_QUADRE_END));
        }
        else {
            notaLinkAttivita = String.format("%s", linkAttivita);
        }

        notaCreazione = String.format("La pagina di una singola %s viene creata se le relative voci biografiche superano le %s.", attivitaTxt, unitaBio);
        if (pref.isBool(USA_TRE_ATTIVITA)) {
            notaAttivitaMultiple = String.format("Ogni persona è presente in diverse %s, in base a quanto riportato nei parametri %s del %s presente nella voce biografica specifica della persona. Le ''attivitàAltre'' non vengono considerate", liste, multipla, notaTemplate);
        }
        else {
            notaAttivitaMultiple = String.format("Ogni persona è presente in una sola %s, in base a quanto riportato nel parametro %s del %s presente nella voce biografica specifica della persona", lista, singola, notaTemplate);
        }
        notaSottoPagina = "Questa sottopagina specifica viene creata se il numero di voci biografiche nel paragrafo della pagina principale supera le " + taglioSottoPagina + " unità.";
        notaSuddivisione = String.format("La lista è suddivisa in %s per ogni nazionalità individuata. Se il numero di voci biografiche nel paragrafo supera le %s viene creata una sottopagina", paragrafi, unitaParagrafo);

        super.tagCategoria = String.format("[[Categoria:Bio attività%s%s%s", PIPE, text.primaMaiuscola(attivita.plurale), DOPPIE_QUADRE_END);
    }

    /**
     * Costruisce l'istanza associata di ListaXxx <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    public void fixMappa() {
        super.fixMappa();
        Lista lista;

        if (attivita != null) {
            switch (typePagina) {
                case paginaPrincipale -> {
                    lista = appContext.getBean(ListaAttivita.class, attivita);
                    if (lista != null) {
                        mappa = lista.getMappa();
                        numVoci = lista.getNumDidascalie();
                    }
                }
                case sottoPagina -> numVoci = arrayService.dimLista(mappa);
                default -> {}
            }
        }
    }


    /**
     * Costruisce la frase di incipit iniziale <br>
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    @Override
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
        if (!pref.isBool(USA_TRE_ATTIVITA)) {
            buffer.append(" principale");
        }
        buffer.append(text.setRef(notaAttivitaMultiple));
        buffer.append(" quella di ");
        buffer.append(notaLinkAttivita);
        buffer.append(". Le persone sono suddivise");
        buffer.append(text.setRef(notaSuddivisione));
        buffer.append(" per nazionalità.");
        buffer.append(text.setRef(notaNazionalita));
        buffer.append(text.setRef(notaVuotoAttivita));

        return buffer.toString();
    }

    /**
     * Inserisce nel testo i rinvii alle sottoPagine <br>
     * Elabora gli upload delle sottoPagine <br>
     * <p>
     * Sovrascritto <br>
     */
    protected String fixSottoPagine(final String testoIn) {
        String testoOut = testoIn;

        try {
            attivita = attivitaService.findByKey("aforista");
        } catch (AlgosException unErrore) {
            int a = 87;
        }
        if (typePagina == AETypePagina.paginaPrincipale) {
            //            appContext.getBean(UploadAttivita.class, attivita, AETypePagina.sottoPagina);
        }

        return testoOut;
    }

    /**
     * Lista delle voci correlate (eventuale) <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected List<String> listaVociCorrelate() {
        List<String> lista = new ArrayList<>();

        lista.add(":Categoria:" + text.primaMaiuscola(attivita.plurale));
        lista.add("Progetto:Biografie/Attività");
        return lista;
    }

    /**
     * Titolo della pagina 'madre' <br>
     * <p>
     * DEVE essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     */
    @Override
    protected String getTitoloPaginaMadre() {
        return "Pippox";
    }

    @Override
    protected String getSummary() {
        return VUOTA;
    }

}
