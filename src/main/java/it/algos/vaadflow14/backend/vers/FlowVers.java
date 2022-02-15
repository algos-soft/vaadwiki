package it.algos.vaadflow14.backend.vers;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 08-feb-2022
 * Time: 16:45
 * Log delle versioni, modifiche e patch installate <br>
 * <p>
 * Executed on container startup <br>
 * Setup non-UI logic here <br>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat o altri) <br>
 * Eseguita quindi a ogni avvio/riavvio del server e NON a ogni sessione <br>
 * Classe astratta. Viene implementata nella sottoclasse specifica di ogni progetto <br>
 */
@SpringComponent
@Qualifier(TAG_FLOW_VERSION)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@AIScript(sovraScrivibile = false)
public class FlowVers extends AVers {


    /**
     * This method is called prior to the servlet context being initialized (when the Web application is deployed). <br>
     * You can initialize servlet context related data here. <br>
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE <br>
     */
    public void inizia() {
        //--prima installazione del progetto base VaadFlow14
        //--non fa nulla, solo informativo
        super.flow(AETypeVers.setup, "Setup", "Installazione iniziale di VaadFlow");
    }

}
