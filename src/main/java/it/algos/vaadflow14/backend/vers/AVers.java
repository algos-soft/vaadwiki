package it.algos.vaadflow14.backend.vers;

import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.utility.versione.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 08-feb-2022
 * Time: 17:06
 * <p>
 * Log delle versioni, modifiche e patch installate <br>
 * <p>
 * Executed on container startup <br>
 * Setup non-UI logic here <br>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat o altri) <br>
 * Eseguita quindi a ogni avvio/riavvio del server e NON a ogni sessione <br>
 * Classe astratta con metodi generali. Viene implementata nelle sottoclassi specifiche di ogni progetto <br>
 */
public abstract class AVers implements AIVers {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected VersioneService versioneService;


    /**
     * Inserimento di una versione del progetto base VaadFlow14 <br>
     */
    protected void flow(final AETypeVers type, final String code, final String descrizione) {
        this.crea(type, code, descrizione, true, false);
    }


    /**
     * Inserimento di una versione del progetto specifico in esecuzione <br>
     */
    protected void specifico(final AETypeVers type, final String code, final String descrizione) {
        this.crea(type, code, descrizione, false, false);
    }

    /**
     * Inserimento di una versione del progetto base VaadFlow14 <br>
     * Controlla che la entity non esista già <br>
     */
    protected void crea(final AETypeVers type, final String titolo, final String descrizione, final boolean vaadFlow, final boolean usaCompany) {
        versioneService.creaIfNotExist(type, titolo, null,descrizione, vaadFlow, usaCompany);
    }

    //            if (versioneService.isMancaByKeyUnica(PATCH_1)) {
    //        if (addPreferenzaInvioMessaggi()) {
    //            versioneService.creaIfNotExistKey(PATCH_1, "1.68 del 29.1.22", "Aggiunta delle preferenze usaInvioMessaggi (una per ogni croce). Regolato il valore a false per tutte le croci");
    //        }
    //    }

}
