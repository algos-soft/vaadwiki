package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 19-apr-2020
 * Time: 09:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WizElaboraNewPackage extends WizElabora {


    private String packageName;

    private String pathPackage;

    private String fileName;

    private String suffisso;

    private String pathSource;

    private String pathDest;

    private String testoFile;

    private boolean sovrascrive = false;

    /**
     * Per ogni file serve:
     * - nome del package
     * - nome del file: suffisso
     * - testo file recuperato dalle sources
     * - path di destinazione
     */


    /**
     * Creazione completa del package
     * Crea una directory
     * Crea i files previsti nella enumeration
     */
    @Override
    public void esegue() {
        super.esegue();
        this.creaDirectory();
        super.fixPackage();
    }


    protected void creaDirectory() {
        String message;

        if (file.isNotEsisteDirectory(AEWizCost.pathTargetPackageSlash.get())) {
            if (file.creaDirectory(AEWizCost.pathTargetPackageSlash.get())) {
                message = String.format("Creata in %s la directory ../packages/%s per un nuovo package che non esisteva.", AEWizCost.nameProjectCurrentUpper.get(), AEWizCost.nameTargetPackage.get());
                logger.log(AETypeLog.wizard, message);
            }
        }
    }

}
