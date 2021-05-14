package it.algos.vaadflow14.wizard.scripts;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.checkbox.*;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.dialog.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import org.springframework.beans.factory.annotation.*;

import java.io.*;
import java.util.*;


/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 13-apr-2020
 * Time: 05:17
 * <p>
 * Classe astratta per alcuni dialoghi di regolazione dei parametri per il Wizard <br>
 */
public abstract class WizDialog extends Dialog {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WizService wizService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ALogService logger;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired

    public ADateService date;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected AFileService file;

    protected WizRecipient wizRecipient;

    protected LinkedHashMap<String, Checkbox> mappaCheckboxOld;

    protected LinkedHashMap<String, WizBox> mappaWizBox;

    protected boolean isNuovoProgetto;

    protected boolean isNuovoPackage;

    protected boolean isStartThisProgetto;

    protected Button confirmButton;

    protected Button cancelButton;

    protected Button buttonForzaDirectory;

    protected TextField fieldPackageName;

    protected TextField fieldProjectNameUpper;

    /**
     * Sezione superiore,coi titoli e le info <br>
     */
    protected VerticalLayout topLayout;

    /**
     * Sezione centrale con la scelta del progetto <br>
     */
    protected VerticalLayout selezioneLayout;

    /**
     * Sezione centrale con la selezione dei flags <br>
     */
    protected VerticalLayout checkBoxLayout;

    /**
     * Sezione inferiore coi bottoni di uscita e conferma <br>
     */
    protected VerticalLayout bottomLayout;

    protected H3 titoloCorrente;

    protected ComboBox<File> fieldComboProgettiNuovi;

    protected ComboBox<AEProgetto> fieldComboProgetti;

    protected ComboBox<String> fieldComboPackages;


    /**
     * Regolazioni grafiche
     */
    protected void inizia() {
        this.setCloseOnEsc(true);
        this.setCloseOnOutsideClick(true);
        this.removeAll();

        //--creazione iniziale dei bottoni (chiamati anche da selezioneLayout)
        this.creaBottoni();

        //--info di avvisi iniziali
        this.creaTopLayout();

        //--solo per newProject
        this.creaSelezioneLayout();

        //--checkbox di spunta
        this.creaCheckBoxLayout();

        //--creazione bottoni di comando
        this.creaBottomLayout();

        //--superClasse
        super.open();
    }// end of method


    /**
     * Controlla che il dialogo possa usare alcuni flag compatibili (tra di loro) <br>
     */
    protected boolean check() {
        boolean valido = true;

        //--Deve essere o un progetto o un package
        valido = valido && (AEFlag.isProject.is() || AEFlag.isPackage.is());

        //--Se è un progetto, deve essere nuovo o update
        if (AEFlag.isProject.is()) {
            valido = valido && (AEFlag.isNewProject.is() != AEFlag.isUpdateProject.is());
        }

        //--Se è un package, deve essere nuovo o update
        if (AEFlag.isPackage.is()) {
            valido = valido && (AEFlag.isNewPackage.is() != AEFlag.isUpdatePackage.is());
        }

        if (!valido) {
            wizService.printInfo("Blocco entrata dialogo");
            logger.warn("Il dialogo non è stato aperto perché alcuni flags non sono validi per operare correttamente");
        }

        return valido;
    }


    /**
     * Sezione superiore,coi titoli e le info <br>
     * Legenda iniziale <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaTopLayout() {
    }// end of method


    /**
     * Sezione centrale con la scelta del progetto <br>
     * Spazzola la directory 'ideaProjects' <br>
     * Recupera i possibili progetti 'vuoti' <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superClasse <br>
     */
    protected void creaSelezioneLayout() {
    }


    /**
     * Sezione centrale con la selezione dei flags <br>
     * Crea i checkbox di controllo <br>
     * Spazzola (nella sottoclasse) la Enumeration per aggiungere solo i checkbox adeguati: <br>
     * newProject
     * updateProject
     * newPackage
     * updatePackage
     * Spazzola la Enumeration e regola a 'true' i chekBox secondo il flag 'isAcceso' <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaCheckBoxLayout() {
        checkBoxLayout = fixSezione("Flags di regolazione");
        this.add(checkBoxLayout);
        mappaWizBox = new LinkedHashMap<>();
    }


    protected void creaBottoni() {
        cancelButton = new Button("Annulla", event -> {
            esceDalDialogo(false);
        });//end of lambda expressions
        cancelButton.setIcon(VaadinIcon.ARROW_LEFT.create());
        cancelButton.addClickShortcut(Key.ARROW_LEFT);
        cancelButton.setWidth(NORMAL_WIDTH);
        cancelButton.setHeight(NORMAL_HEIGHT);
        cancelButton.setVisible(true);

        confirmButton = new Button("Conferma", event -> {
            esceDalDialogo(true);
        });//end of lambda expressions
        confirmButton.setIcon(VaadinIcon.EDIT.create());
        confirmButton.setWidth(NORMAL_WIDTH);
        confirmButton.setHeight(NORMAL_HEIGHT);
        confirmButton.setVisible(true);
        confirmButton.setEnabled(false);
    }


    protected void creaBottomLayout() {
        bottomLayout = new VerticalLayout();
        HorizontalLayout layoutFooter = new HorizontalLayout();
        layoutFooter.setSpacing(true);
        layoutFooter.setMargin(true);

        layoutFooter.add(cancelButton, confirmButton);
        bottomLayout.add(layoutFooter);
        this.add(bottomLayout);
    }


    /**
     * Aggiunge al layout i checkbox di controllo <br>
     */
    protected void addCheckBoxMap() {
        checkBoxLayout.removeAll();
        for (String key : mappaWizBox.keySet()) {
            checkBoxLayout.add(mappaWizBox.get(key));
        }
    }


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola tutti i valori delle enumeration AEDir, AECheck e EAToken che saranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject,WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     */
    protected boolean regolazioniFinali() {
        boolean status = true;

        status = status && this.regolaAEWizCost();
        AEWizCost.print(AEWizValue.inserito);
        AEWizCost.print(AEWizValue.derivato);

        //        status = status && this.regolaAEDir();
        status = status && this.regolaAECheck();
        status = status && this.regolaAEPackage();
        status = status && this.regolaAEToken();
        AEModulo.fixValues(AEWizCost.pathTargetProjectModulo.get(), AEWizCost.nameTargetProjectUpper.get());

//        AEWizCost.print(AEWizValue.inserito);
//        AEWizCost.print(AEWizValue.derivato);
//        wizService.printInfoStart();
        return status;
    }

    /**
     * Chiamato alla dismissione del dialogo <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     */
    protected boolean regolaAEWizCost() {
        return true;
    }


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Resetta i valori regolabili della Enumeration AEDir <br>
     * Elabora tutti i valori della Enumeration AEDir dipendenti dal nome del progetto <br>
     * Verranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject,WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected boolean regolaAEDir() {
        return true;
    }


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Elabora tutti i valori della Enumeration AECheck dipendenti dal nome del progetto <br>
     * Verranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject, WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     */
    protected boolean regolaAECheck() {
        for (AECheck check : AECheck.values()) {
            if (mappaWizBox != null && mappaWizBox.get(check.name()) != null) {
                check.setAcceso(mappaWizBox.get(check.name()).is());
                if (check.isFieldAssociato()) {
                    WizBox alfa = mappaWizBox.get(check.name());
                    String beta = alfa.getValue();
                    check.setFieldName(beta.toLowerCase());
                }
            }
        }

        return true;
    }


    /**
     * Chiamato alla dismissione del dialogo <br>
     * Regola alcuni valori della Enumeration EAToken che saranno usati da WizElaboraNewProject e WizElaboraUpdateProject <br>
     */
    protected boolean regolaAEToken() {
        String projectNameUpper;
        String projectModuloLower;
        String packageName;
        String fileName;
        AEToken.reset();

        projectNameUpper = AEWizCost.nameTargetProjectUpper.isValida() ? AEWizCost.nameTargetProjectUpper.get() : VUOTA;
        projectModuloLower = AEWizCost.nameTargetProjectModulo.isValida() ? AEWizCost.nameTargetProjectModulo.get() : VUOTA;
        packageName = AEWizCost.nameTargetPackage.isValida() ? AEWizCost.nameTargetPackage.get() : VUOTA;
        fileName = AEWizCost.nameTargetFileUpper.get();
        return wizService.regolaAEToken(projectNameUpper, projectModuloLower, packageName, fileName);
    }

    /**
     * Chiamato alla dismissione del dialogo <br>
     * Elabora tutti i valori della Enumeration AEPackage dipendenti dalla classe del package <br>
     * Verranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject, WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     */
    protected boolean regolaAEPackage() {
        WizBox wizBox;
        String fieldName;

        if (mappaWizBox == null) {
            return false;
        }

        for (AEPackage pack : AEPackage.values()) {
            wizBox = mappaWizBox.get(pack.name());
            if (wizBox != null) {
                pack.setAcceso(wizBox.is());
                if (pack.isProperty()) {
                    fieldName = wizBox.getValue();
                    if (text.isValid(fieldName)) {
                        pack.setFieldName(fieldName);
                    }
                }
            }
        }
        AEPackage.printInfo("test");
        return true;
    }

    protected VerticalLayout fixSezione(String titolo) {
        return fixSezione(titolo, "black");
    }


    protected VerticalLayout fixSezione(String titolo, String color) {
        VerticalLayout layoutTitolo = new VerticalLayout();
        H3 titoloH3 = new H3(text.primaMaiuscola(titolo));

        layoutTitolo.setMargin(false);
        layoutTitolo.setSpacing(false);
        layoutTitolo.setPadding(false);
        titoloH3.getElement().getStyle().set("color", "blue");
        layoutTitolo.add(titoloH3);

        return layoutTitolo;
    }

    /**
     * Chiamato alla dismissione del dialogo <br>
     * Recupera i valori inseriti dall'utente <br>
     * Regola i valori regolabili della Enumeration AEWizCost <br>
     * Verranno usati da: <br>
     * WizElaboraNewProject, WizElaboraUpdateProject,WizElaboraNewPackage, WizElaboraUpdatePackage <br>
     * Metodo che NON può essere sovrascritto <br>
     *
     * @param pathProject            (obbligatorio) path completo del progetto target. Da cui si ricava nameTargetProjectModulo (file.estraeClasseFinale).
     * @param nameTargetProjectUpper (obbligatorio) nome maiuscolo del progetto. Può essere diverso da nameTargetProjectModulo (Es. vaadwiki e Wiki)
     * @param packageName            (facoltativo) nome del package da creare/modificare. Eventualmente con sub-directory (separatore slash)
     *
     * @return false se manca uno dei due parametri obbligatori
     */
    protected boolean fixValoriInseriti(final String pathProject, final String nameTargetProjectUpper, final String packageName) {

        if (text.isEmpty(pathProject) || text.isEmpty(nameTargetProjectUpper)) {
            return false;
        }

        //--inserisce il path completo del progetto selezionato nella Enumeration
        //--dal path completo deriva il valore di directory/modulo -> nameTargetProjectModulo
        //--mentre il nome (maiuscolo) del progetto deve essere inserito -> nameTargetProjectUpper
        //--perché potrebbe essere diverso (Es. vaadwiki -> Wiki)
        AEWizCost.pathTargetProjectRoot.setValue(pathProject);

        //--inserisce  il nome (maiuscolo) del progetto
        //--perché potrebbe essere diverso dal valore di directory/modulo (Es. vaadwiki -> Wiki)
        AEWizCost.nameTargetProjectUpper.setValue(text.primaMaiuscola(nameTargetProjectUpper));

        //--inserisce il nome (eventuale) del package da creare/modificare
        if (text.isValid(packageName)) {
            AEWizCost.nameTargetPackagePunto.setValue(text.fixSlashToPunto(packageName));
        }

        //--regola tutti i valori automatici, dopo aver inserito quelli fondamentali
        AEWizCost.fixValoriDerivati();

//        wizService.printProgetto();
        return true;
    }


    /**
     * Esce dal dialogo con due possibilità (a seconda del flag) <br>
     * 1) annulla <br>
     * 2) esegue <br>
     */
    protected void esceDalDialogo(boolean esegue) {
        if (esegue) {
            if (!regolazioniFinali()) {
                if (AEFlag.isNewPackage.is()) {
                    logger.info("Manca il nome del nuovo package che non può quindi essere creato ", this.getClass(), "esceDalDialogo");
                    //                }
                    //                else {
                    //                    logger.info("Mancano alcuni dati essenziali per l'elaborazione richiesta, che è stata quindi abortita", this.getClass(), "esceDalDialogo");
                }

                this.close();
                return;
            }
            //            wizService.printInfoCompleto("Uscita del dialogo");

            this.close();
            wizRecipient.esegue();
        }
        else {
            this.close();
            Notification.show("Dialogo annullato", 2000, Notification.Position.MIDDLE);
        }
    }

}
