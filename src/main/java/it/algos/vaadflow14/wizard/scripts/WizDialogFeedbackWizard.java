package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project provider
 * Created by Algos
 * User: gac
 * Date: dom, 25-ott-2020
 * Time: 17:57
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WizDialogFeedbackWizard extends WizDialog {

    /**
     * Apertura del dialogo <br>
     */
    public void open(WizRecipient wizRecipient) {
        super.wizRecipient = wizRecipient;
        super.isNuovoProgetto = false;
        super.titoloCorrente = new H3();
        AEFlag.isProject.set(true);

        super.inizia();
    }


    /**
     * Legenda iniziale <br>
     * Viene sovrascritta nella sottoclasse che deve invocare PRIMA questo metodo <br>
     */
    @Override
    protected void creaTopLayout() {
        String pathWizard;
        String pathProject;
        String pathModuloBase;
        String pathSources;
        topLayout = fixSezione(TITOLO_FEEDBACK_PROGETTO, "green");
        this.add(topLayout);

        if (!AEFlag.isBaseFlow.is()) {
            pathWizard = file.findPathBreve(AEWizCost.pathVaadFlow14Wizard.get(), "algos");
            pathProject = file.findPathBreve(AEWizCost.nameProjectCurrentUpper.get(), "algos");
            pathModuloBase = file.findPathBreve(AEWizCost.pathVaadFlow14Root.get(), "operativi");
            topLayout.add(text.getLabelGreenBold(String.format("Ricopia la directory %s di %s su %s", pathWizard, pathProject, pathModuloBase)));

            pathSources = file.findPathBreve(AEWizCost.pathVaadFlow14WizSources.get(), AEWizCost.dirVaadFlow14.get());
            topLayout.add(text.getLabelGreenBold(String.format("Non modifica la sub-directory %s esistente su %s", pathSources, pathModuloBase)));

            topLayout.add(text.getLabelRedBold("Le modifiche sono irreversibili"));
        }

    }

    protected void creaCheckBoxLayout() {
    }


    protected void creaBottoni() {
        super.creaBottoni();

        cancelButton.getElement().setAttribute("theme", "primary");
        confirmButton.getElement().setAttribute("theme", "error");
        confirmButton.setEnabled(true);
    }

    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola tutti i valori delle enumeration AEDir, AECheck e EAToken che saranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject,WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     */
    @Override
    protected boolean regolazioniFinali() {
        return this.regolaAEWizCost();
    }

    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola i valori regolabili della Enumeration AEWizCost <br>
     * Verranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject,WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected boolean regolaAEWizCost() {
        String pathProject = VUOTA;

        //--inserisce il path completo del progetto in esecuzione
        pathProject = AEWizCost.pathCurrent.get();
        AEWizCost.pathTargetProjectRoot.setValue(pathProject);

        //--regola tutti i valori automatici, dopo aver inserito quelli fondamentali
        AEWizCost.fixValoriDerivati();

        AEWizCost.print("Test uscita feedback");
        return true;
    }

}
