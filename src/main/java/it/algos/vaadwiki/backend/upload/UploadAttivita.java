package it.algos.vaadwiki.backend.upload;

import com.querydsl.core.support.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
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
     * @param usaWikiPaginaTest una sottopagina di Utente:Gac/
     */
    public UploadAttivita(final AEntity entityBean, final boolean usaWikiPaginaTest) {
        super.entityBean = entityBean;
        super.usaWikiPaginaTest = usaWikiPaginaTest;
        this.attivita = (Attivita) entityBean;
    }// end of constructor


    protected void uploadPaginaTest() {
        ListaAttivita lista = null;
        String testoPagina = VUOTA;

        try {
            lista = appContext.getBean(ListaAttivita.class, attivita);
        } catch (Exception unErrore) {
            AlgosException.stack(unErrore, this.getClass(), "uploadPaginaTest");
        }

        if (lista != null) {
            testoPagina = lista.getTestoPagina();
        }

        if (text.isValid(testoPagina)) {
            appContext.getBean(QueryBase.class);
        }

    }


}
