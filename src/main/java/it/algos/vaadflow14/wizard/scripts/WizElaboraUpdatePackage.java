package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.wizard.enumeration.AECheck;
import it.algos.vaadflow14.wizard.enumeration.AEFlag;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 04-nov-2020
 * Time: 18:07
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WizElaboraUpdatePackage extends WizElabora {

    /**
     * Evento lanciato alla chiusura del dialogo
     */
    @Override
    public void esegue() {
        super.esegue();
        if (AEFlag.isUpdatePackage.is()) {
            super.fixPackage();
        }
    }

}
