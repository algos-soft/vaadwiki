package it.algos.vaadwiki.backend.boot;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.vers.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 11-feb-2022
 * Time: 17:23
 */
@SpringComponent
@Qualifier(TAG_WIKI_VERSION)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@AIScript()
public class WikiVers extends FlowVers {


    /**
     * This method is called prior to the servlet context being initialized (when the Web application is deployed). <br>
     * You can initialize servlet context related data here. <br>
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE <br>
     */
    @Override
    public void inizia() {
        super.inizia();

        //--prima installazione del progetto specifico Simple
        //--non fa nulla, solo informativo
        super.specifico(AETypeVers.setup, "Setup", "Installazione iniziale del progetto specifico Wiki");
    }

}


