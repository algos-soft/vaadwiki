package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 04-nov-2020
 * Time: 18:04
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WizDialogUpdatePackage extends WizDialogPackage {


    /**
     * Sezione superiore,coi titoli e le info <br>
     */
    protected HorizontalLayout propertyLayout;


    /**
     * Apertura del dialogo <br>
     */
    public void open(WizRecipient wizRecipient) {
        super.wizRecipient = wizRecipient;
        AEFlag.isPackage.set(true);
        AEFlag.isUpdatePackage.set(true);

        AEFlag.isProject.set(false);
        AEFlag.isPackage.set(true);
        AEFlag.isNewPackage.set(false);
        AEFlag.isUpdatePackage.set(true);

        this.regolazioniIniziali();
        super.inizia();
    }

    protected void regolazioniIniziali() {
        String pathProject ;
        String projectNameUpper ;

        //--recupera il path completo del progetto in esecuzione
        //--sempre AEWizCost.pathCurrent sia in AEFlag.isBaseFlow che in un progetto specifico
        pathProject = AEWizCost.pathCurrentProjectRoot.get();

        //--recupera il nome (maiuscolo) del progetto in esecuzione
        //--sempre AEWizCost.nameProjectCurrentUpper sia in AEFlag.isBaseFlow che in un progetto specifico
        projectNameUpper = AEWizCost.nameCurrentProjectUpper.get();

        //--inserisce i valori fondamentali (3) e poi regola tutti i valori automatici derivati
         super.fixValoriInseriti(pathProject, projectNameUpper, VUOTA);
    }

    /**
     * Legenda iniziale <br>
     * Viene sovrascritta nella sottoclasse che deve invocare PRIMA questo metodo <br>
     */
    @Override
    protected void creaTopLayout() {
        topLayout = fixSezione("Modifica di un package", "green");
        this.add(topLayout);

        topLayout.add(text.getLabelGreenBold("Update di un package esistente in questo progetto"));
        topLayout.add(text.getLabelRedBold("Seleziona nel comboBox il package da aggiornare"));
    }


    /**
     * Sezione centrale con la scelta del progetto <br>
     * Spazzola la la directory packages <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaSelezioneLayout() {
        selezioneLayout = fixSezione("Selezione...");
        this.add(selezioneLayout);

        List<String> packages = wizService.getPackages();
        Collections.sort(packages);
        String label = "Packages esistenti nella directory di questo progetto";

        if (array.isAllValid(packages)) {

            fieldComboPackages = new ComboBox<>();
            // Choose which property from Department is the presentation value
            //        fieldComboProgetti.setItemLabelGenerator(AEProgetto::nameProject);
            fieldComboPackages.setWidth("22em");
            fieldComboPackages.setAllowCustomValue(false);
            fieldComboPackages.setLabel(label);

            fieldComboPackages.setItems(packages);
            confirmButton.setEnabled(false);
            if (packages.size() == 1) {
                fieldComboPackages.setValue(packages.get(0));
                confirmButton.setEnabled(true);
            }
            addListener();
            selezioneLayout.add(fieldComboPackages);
        }
    }


    /**
     * Crea i checkbox di controllo <br>
     * Spazzola (nella sottoclasse) la Enumeration per aggiungere solo i checkbox adeguati: <br>
     * newProject
     * updateProject
     * newPackage
     * updatePackage
     * Spazzola la Enumeration e regola a 'true' i chekBox secondo il flag 'isAcceso' <br>
     * DEVE essere sovrascritto nella sottoclasse <br>
     */
    @Override
    protected void creaCheckBoxLayout() {
        super.creaCheckBoxLayout();
        Checkbox unCheckbox;

        for (AEPackage pack : AEPackage.values()) {
            mappaWizBox.put(pack.name(), new WizBox(pack));
        }
        super.addCheckBoxMap();

        //        //--regola tutti i checkbox
        //        AECheck.reset();

        //        for (AECheck check : AECheck.getUpdatePackage()) {
        //            mappaWizBox.put(check.name(), new WizBox(check));
        //        }

        unCheckbox = mappaWizBox.get(AECheck.rowIndex.name()).getBox();
        unCheckbox.addValueChangeListener(e -> { sincroRow(); });
        unCheckbox = mappaWizBox.get(AECheck.ordine.name()).getBox();
        unCheckbox.addValueChangeListener(e -> { sincroOrdine(); });

        //        unCheckbox = mappaWizBox.get(AECheck.docFile.name()).getBox();
        //        unCheckbox.addValueChangeListener(e -> { sincroDocPackage(); });

        super.addCheckBoxMap();
        propertyLayout = new HorizontalLayout();
        this.add(propertyLayout);
    }

    /**
     * legge i nomi dei 4 fields base (int, String, String, boolean) (se ci sono)
     */
    private int leggeFieldsBaseEsistenti(String packageName) {
        int tot = 0;
        String nameSourceText = AEWizCost.pathTargetPackageSlash.get() + AEWizCost.nameTargetFileUpper.get() + JAVA_SUFFIX;
        String sourceText = file.leggeFile(nameSourceText);

        tot = pack(sourceText, AETypeField.integer, "int", 1, AEPackage.ordine) ? tot + 1 : tot;
        tot = pack(sourceText, AETypeField.text, "String", 1, AEPackage.code) ? tot + 1 : tot;
        tot = pack(sourceText, AETypeField.text, "String", 2, AEPackage.description) ? tot + 1 : tot;
        tot = pack(sourceText, AETypeField.booleano, "boolean", 1, AEPackage.valido) ? tot + 1 : tot;

        return tot;
    }

    /**
     * Controlla se nel file AEntity esiste una property del tipo indicato (nella posizione) <br>
     */
    private boolean pack(String sourceText, AETypeField type, String tag, int pos, AEPackage pack) {
        boolean esiste = false;
        String tagType = "type = AETypeField." + type.name();
        String tagIni = "public " + tag;
        String tagEnd = FlowCost.PUNTO_VIRGOLA;
        int posIni = 0;
        int posEnd = 0;
        String fieldName;

        pack.setAcceso(false);
        pack.setFieldName(VUOTA);

        if (sourceText.contains(tagType)) {
            posIni = sourceText.indexOf(tagType);
            if (pos == 2) {
                posIni = sourceText.indexOf(tagType, posIni + tagType.length());
            }
            posIni = sourceText.indexOf(tagIni, posIni);
            posIni += tagIni.length();
            posEnd = sourceText.indexOf(tagEnd, posIni);

            fieldName = sourceText.substring(posIni, posEnd).trim();

            if (text.isValid(fieldName)) {
                pack.setAcceso(true);
                pack.setFieldName(fieldName);
                esiste = true;
            }
        }

        return esiste;
    }

    /**
     * conto le properties esistenti
     */
    private int contaFieldsTotaliEsistenti(String packageName) {
        int tot = 0;
        String tag = "@AIField\\(type";

        String nameSourceText = AEWizCost.pathTargetPackageSlash.get() + AEWizCost.nameTargetFileUpper.get() + JAVA_SUFFIX;
        String sourceText = file.leggeFile(nameSourceText);

        tot = sourceText.split(tag).length - 1;
        return tot;
    }

    protected void sincroRow() {
        Checkbox checkRow = mappaWizBox.get(AECheck.rowIndex.name()).getBox();
        Checkbox checkOrdine = mappaWizBox.get(AECheck.ordine.name()).getBox();

        if (checkRow.getValue()) {
            checkOrdine.setValue(false);
        }
    }

    protected void sincroOrdine() {
        Checkbox checkRow = mappaWizBox.get(AECheck.rowIndex.name()).getBox();
        Checkbox checkOrdine = mappaWizBox.get(AECheck.ordine.name()).getBox();

        if (checkOrdine.getValue()) {
            checkRow.setValue(false);
        }
    }

    protected void sincroDocPackage() {
        Checkbox checkDocPackage = mappaWizBox.get(AECheck.docFile.name()).getBox();

        if (checkDocPackage.getValue()) {
            for (String key : mappaWizBox.keySet()) {
                if (!key.equals(AECheck.docFile.name())) {
                    mappaWizBox.get(key).setValue(false);
                }
            }
            confirmButton.setEnabled(true);
        }
        else {
            confirmButton.setEnabled(false);
        }
    }


    protected void creaBottoni() {
        super.creaBottoni();

        cancelButton.getElement().setAttribute("theme", "primary");
        confirmButton.getElement().setAttribute("theme", "error");
    }


    private void addListener() {
        fieldComboPackages.addValueChangeListener(event -> sincroProject(event.getValue()));
    }


    private void sincroProject(String packageName) {
        int fields = 0;
        if (text.isValid(packageName)) {
            packageName = text.fixPuntoToSlash(packageName);
            AEWizCost.nameTargetPackagePunto.setValue(text.fixSlashToPunto(packageName));
            AEWizCost.nameTargetPackage.setValue(text.fixPuntoToSlash(packageName));
            packageName = text.levaTestoPrimaDi(packageName, SLASH);
            AEWizCost.nameTargetFileUpper.setValue(text.primaMaiuscola(packageName));
            AEWizCost.pathTargetPackageSlash.setValue(AEWizCost.pathTargetProjectPackages.get() + AEWizCost.nameTargetPackage.get() + SLASH);
        }
        fields = leggeFieldsBaseEsistenti(packageName);

        for (AEPackage pack : AEPackage.values()) {
            mappaWizBox.put(pack.name(), new WizBox(pack));
        }
        super.addCheckBoxMap();

        propertyLayout.removeAll();
        if (contaFieldsTotaliEsistenti(packageName) > fields) {
            propertyLayout.add(text.getLabelRedBold("Attenzione ci sono altri fields che verrebbero cancellati"));
        }

        confirmButton.setEnabled(text.isValid(packageName));
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
        String projectNameUpper = VUOTA;
        String packageName = VUOTA;

        //--recupera il path completo del progetto in esecuzione
        //--sempre AEWizCost.pathCurrent sia in AEFlag.isBaseFlow che in un progetto specifico
        pathProject = AEWizCost.pathCurrentProjectRoot.get();

        //-recupera il nome (maiuscolo) del progetto in esecuzione, usando il valore del file xxxApplication
        //--estraendo la parte del nome precedente il tag 'Application'
        //--sempre AEWizCost.nameProjectCurrentUpper sia in AEFlag.isBaseFlow che in un progetto specifico
        projectNameUpper = wizService.estraeProjectFromApplication();

        if (fieldComboPackages != null && text.isValid(fieldComboPackages.getValue())) {
            packageName = fieldComboPackages.getValue();
        }

        //--inserisce i valori fondamentali (3) e poi regola tutti i valori automatici derivati
        return super.fixValoriInseriti(pathProject, projectNameUpper, packageName);
    }

    /**
     * Chiamato alla dismissione del dialogo <br>
     * Resetta i valori regolabili della Enumeration AEDir <br>
     * Elabora tutti i valori della Enumeration AEDir dipendenti dal nome del progetto <br>
     * Verranno usati da WizElaboraNewProject e WizElaboraUpdateProject <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected boolean regolaAEDir() {
        boolean status = true;
        String packageName = VUOTA;
        super.regolaAEDir();

        if (fieldComboPackages != null && fieldComboPackages.getValue() != null) {
            packageName = fieldComboPackages.getValue();
        }

        if (mappaWizBox != null && mappaWizBox.get(AECheck.docFile.name()) != null && mappaWizBox.get(AECheck.docFile.name()).is()) {
            AEDir.modificaPackageAll(VUOTA);
        }
        else {
            status = status && AEDir.modificaPackageAll(packageName);
        }

        return status;
    }

}
