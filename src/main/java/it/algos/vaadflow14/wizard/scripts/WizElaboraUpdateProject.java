package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.wrapper.*;
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
public class WizElaboraUpdateProject extends WizElabora {

    @Override
    public void esegue() {
        super.esegue();
        AIResult result = AResult.errato();
        String srcPath = AEWizCost.pathVaadFlow14Root.get();
        String destPath = AEWizCost.pathTargetProjectRoot.get();
        String dir = AEWizCost.nameTargetProjectLower.get();
        String nameProject = AEWizCost.nameTargetProjectUpper.get();
        String value;
        String message;
        AECopyWiz copyWiz;
        String sourcesName;

        for (AEWizCost aeWizCost : wizService.getAll()) {
            if (aeWizCost.isAcceso()) {

                if (aeWizCost == AEWizCost.pathTargetProjectModulo) {
                    //                    super.creaModuloProgetto();@//@todo Controllare
                    continue;
                }

                copyWiz = aeWizCost.getCopyWiz();
                value = aeWizCost.get();

                if (text.isEmpty(value)) {
                    logger.error(String.format("In AEWizCost.%s manca il valore del path", aeWizCost.name()));
                    continue;
                }

                switch (copyWiz) {
                    case dirDeletingAll:
                    case dirAddingOnly:
                    case dirSoloSeNonEsiste:
                        result = wizService.copyDir(copyWiz, srcPath + value, destPath + value, dir);
                        break;
                    case fileSovrascriveSempreAncheSeEsiste:
                    case fileSoloSeNonEsiste:
                    case fileCheckFlagSeEsiste:
                        wizService.copyFile(copyWiz, srcPath + value, destPath + value, AEWizCost.nameProjectCurrentUpper.get().toLowerCase());
                        break;
                    case sourceSovrascriveSempreAncheSeEsiste:
                    case sourceSoloSeNonEsiste:
                    case sourceCheckFlagSeEsiste:
                        sourcesName = aeWizCost.getSourcesName();
                        if (text.isValid(sourcesName)) {
                            result = wizService.creaFile(copyWiz, sourcesName, destPath + value, dir);
                        }
                        else {
                            message = String.format("Manca il nome del file sorgente in AEWizCost.%s", aeWizCost.name());
                            logger.log(AETypeLog.wizard, message);
                        }
                        break;
                    default:
                        logger.warn("Switch - caso non definito", this.getClass(), "esegue");
                        break;
                }
                message = String.format("Update project (%s): ", nameProject);
                logger.log(AETypeLog.wizard, message + result.getMessage());
            }
        }

        //--cancella directory sources (se esiste)
        if (file.isEsisteDirectory(AEWizCost.pathTargetProjectSources.get())) {
            file.deleteDirectory(AEWizCost.pathTargetProjectSources.get());
            message = String.format("Cancellata la directory 'sources'");
            logger.log(AETypeLog.wizard, message);
        }
    }

}
