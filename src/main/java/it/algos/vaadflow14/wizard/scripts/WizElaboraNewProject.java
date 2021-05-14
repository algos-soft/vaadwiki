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
 * Date: lun, 13-apr-2020
 * Time: 05:31
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WizElaboraNewProject extends WizElabora {


    @Override
    public void esegue() {
        super.esegue();
        AIResult result = AResult.errato();
        String srcPath = AEWizCost.pathVaadFlow14Root.get();
        String destPath = AEWizCost.pathTargetProjectRoot.get();
        String nameProject = AEWizCost.nameTargetProjectUpper.get();
        String value;
        String message;
        AECopyWiz copyWiz;
        String sourcesName;
        String directory = AEWizCost.nameTargetProjectUpper.get();

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
                    case dirSoloSeNonEsiste:
                    case dirDeletingAll:
                    case dirAddingOnly:
                        result = wizService.copyDir(copyWiz, srcPath + value, destPath + value, directory);
                        break;
                    case fileSoloSeNonEsiste:
                    case fileSovrascriveSempreAncheSeEsiste:
                    case fileCheckFlagSeEsiste:
                        wizService.copyFile(copyWiz, value, destPath + value, AEWizCost.nameCurrentProjectUpper.get().toLowerCase());
                        break;
                    case sourceSoloSeNonEsiste:
                    case sourceSovrascriveSempreAncheSeEsiste:
                    case sourceCheckFlagSeEsiste:
                        sourcesName = aeWizCost.getSourcesName();
                        if (text.isValid(sourcesName)) {
                            result = wizService.creaFile(copyWiz, sourcesName, destPath + value, directory);
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
                message = String.format("New project (%s): ", nameProject) + result.getMessage();
                logger.log(AETypeLog.wizard, message);
            }
        }
    }


}
