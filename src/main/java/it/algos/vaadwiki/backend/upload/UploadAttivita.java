package it.algos.vaadwiki.backend.upload;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.wiki.query.*;
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

    protected Attivita attivita;

    protected String notaCreazione;

    protected String notaAttivitaSingola;

    protected String notaAttivitaMultiple;

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
        this(entityBean, true);
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
    public UploadAttivita(final AEntity entityBean, final boolean usaWikiPaginaTest) {
        //        super.entityBean = entityBean;
        //        super.usaWikiPaginaTest = usaWikiPaginaTest;
        this.attivita = (Attivita) entityBean;
    }// end of constructor

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
