package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki14
 * Created by Algos
 * User: gac
 * Date: ven, 15-gen-2021
 * Time: 09:16
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WizElaboraDocPackages extends WizElabora {

    /**
     * Evento lanciato alla chiusura del dialogo
     */
    @Override
    public void esegue() {
        super.fixDocPackage(false);
    }

}
