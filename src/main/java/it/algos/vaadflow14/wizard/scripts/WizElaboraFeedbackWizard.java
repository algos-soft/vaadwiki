package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 19-apr-2020
 * Time: 09:55
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WizElaboraFeedbackWizard extends WizElabora {

    /**
     * I termini 'srcPath' e 'destPath', sono invertiti <br>
     */
    @Override
    public void esegue() {
        super.esegue();
        super.isNuovoProgetto = false;
        String srcPath;
        String destPath;

        this.copiaFileWizard();

        //--enumeration
        srcPath = AEWizCost.pathTargetProjectRoot.get() + AEWizCost.dirRootWizardEnumeration.get();
        destPath = AEWizCost.pathVaadFlow14Root.get() + AEWizCost.dirRootWizardEnumeration.get();
        this.copiaDirectories(srcPath, destPath, "enum");

        //--scripts
        srcPath = AEWizCost.pathTargetProjectRoot.get() + AEWizCost.dirRootWizardScripts.get();
        destPath = AEWizCost.pathVaadFlow14Root.get() + AEWizCost.dirRootWizardScripts.get();
        this.copiaDirectories(srcPath, destPath, "scripts");
    }


    /**
     * File Wizard <br>
     * Ricopia il file Wizard da questo progetto a VaadFlow <br>
     * Sovrascrive completamente il file Wizard esistente su VaadFlow che viene perso <br>
     * I termini 'srcPath' e 'destPath', sono invertiti <br>
     */
    protected void copiaFileWizard() {
        String message;
        AECopyWiz copyWiz = AECopyWiz.fileSovrascriveSempreAncheSeEsiste;
        String type = text.setTonde(copyWiz.name());
        String dirWizard = AEWizCost.dirRootWizard.get();
        String pathFile = dirWizard + AEWizCost.fileWizard.get();
        String srcPath = AEWizCost.pathTargetProjectRoot.get() + pathFile;
        String destPath = AEWizCost.pathVaadFlow14Root.get() + pathFile;
        String pathBreve = file.findPathBreveDa(destPath, DIR_ALGOS);

        if (!file.isEsisteFile(srcPath)) {
            logger.warn("Errato il path per il file Wizard locale da ricopiare", this.getClass(), "copiaFileWizard");
        }

        if (!file.isEsisteFile(destPath)) {
            logger.warn("Errato il path per il file Wizard da sostituire su VaadFlow14", this.getClass(), "copiaFileWizard");
        }
        String x = "123";
        String[] pippoz = new String[x.length()];
List ak;
        wizService.copyFile(copyWiz, srcPath, destPath, DIR_VAADFLOW);
        message = String.format("Feedback del file %s %s che è stato completamente sostituito.", pathBreve, type);
        logger.log(AETypeLog.wizard, message);
    }


    /**
     * Directory enumeration e scripts <br>
     * Ricopia le directory Wizard/Enumeration da questo progetto a VaadFlow <br>
     * Sovrascrive completamente la directory wizard/Enumeration esistente su VaadFlow che viene persa <br>
     * Sovrascrive completamente la directory wizard/Scripts esistente su VaadFlow che viene persa <br>
     * I termini 'srcPath' e 'destPath', sono invertiti <br>
     */
    protected void copiaDirectories(String srcPath, String destPath, String tag) {
        String message;
        AECopyWiz copyWiz = AECopyWiz.dirDeletingAll;
        String type = text.setTonde(copyWiz.name());
        String pathBreve = file.findPathBreveDa(destPath, DIR_ALGOS);

        if (!file.isEsisteDirectory(srcPath)) {
            message = String.format("Errato il path per la directory %s locale da ricopiare.", tag);
            logger.warn(message, this.getClass(), "copiaDirectories");
        }

        if (!file.isEsisteDirectory(destPath)) {
            message = String.format("Errato il path per la directory %s da sostituire su VaadFlow14.", tag);
            logger.warn(message, this.getClass(), "copiaDirectories");
        }

        wizService.copyDir(copyWiz, srcPath, destPath, DIR_VAADFLOW);
        message = String.format("Feedback della directory %s %s che è stata completamente sostituita.", pathBreve, type);
        logger.log(AETypeLog.wizard, message);
    }

}
