package it.algos.vaadflow14.wizard.scripts;

import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;

/**
 * Project wikibio
 * Created by Algos
 * User: gac
 * Date: gio, 11-mar-2021
 * Time: 21:19
 */
public abstract class WizDialogPackage extends WizDialog {


    /**
     * Apertura del dialogo <br>
     */
    public void open(WizRecipient wizRecipient) {
        super.wizRecipient = wizRecipient;
        super.isNuovoProgetto = false;

        AEFlag.isProject.set(false);
        AEFlag.isPackage.set(true);

        super.inizia();
    }

    /**
     * Apertura del dialogo <br>
     */
    public void open(WizRecipient wizRecipient, String nomeModulo) {
        super.wizRecipient = wizRecipient;
        super.isNuovoProgetto = false;
        this.nomeModulo = nomeModulo;

        AEFlag.isProject.set(false);
        AEFlag.isPackage.set(true);

        super.inizia();
    }


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola i valori regolabili della Enumeration AEWizCost <br>
     * Verranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject,WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Deprecated
    protected boolean regolaPackages(final String pathProject, final String packageName) {
        String message;

        if (text.isEmpty(pathProject)) {
            message = String.format("Non è stato selezionato il progetto di riferimento.");
            logger.log(AETypeLog.wizard, message);
            return false;
        }

        if (text.isEmpty(packageName)) {
            message = String.format("Manca il nome del package da creare/modificare.");
            logger.log(AETypeLog.wizard, message);
            return false;
        }

        //--inserisce il path completo del progetto in esecuzione
        AEWizCost.pathTargetProjectRoot.setValue(pathProject);

        //--inserisce il nome del package da creare/modificare
        AEWizCost.nameTargetPackagePunto.setValue(text.fixSlashToPunto(packageName));

        //--regola tutti i valori automatici, dopo aver inserito quelli fondamentali
        AEWizCost.fixValoriDerivati();

        return false;
    }

}
