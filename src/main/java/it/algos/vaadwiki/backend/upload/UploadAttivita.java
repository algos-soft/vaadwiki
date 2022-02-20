package it.algos.vaadwiki.backend.upload;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
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
