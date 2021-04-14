package it.algos.vaadflow14.wizard.enumeration;

import java.util.ArrayList;
import java.util.List;
import it.algos.vaadflow14.backend.application.*;


/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 04-nov-2020
 * Time: 18:56
 */
public enum AECheck {
    security("Utilizza Spring Security", true, false, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    config("Directory CONFIG di risorse on-line esterne al JAR (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    documentation("Directory DOC di documentazione (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    frontend("Directory FRONTEND del Client (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    links("Directory LINKS a siti web utili (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    snippets("Directory SNIPPETS di codice suggerito (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    flow("Directory BASE di VaadFlow (Wizard compreso)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, true),

    projectNew("Directory modulo del nuovo progetto (...)", true, false, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    projectUpdate("Directory modulo del progetto selezionato (...)", false, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    resources("Directory RESOURCES (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    property("File application.PROPERTIES (sources)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    banner("File BANNER di SpringBoot (sources)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    git("File GIT di esclusione (sources)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    pom("File POM.xml di Maven (sources)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    read("File README con note di testo (sources)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    test("Directory Test (VaadFlow)", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    all("Abilita/disabilita tutti i checkBox", true, true, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    file("Sovrascrive il singolo FILE", false, false, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    directory("Sovrascrive la DIRECTORY", false, false, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    entity("Entity base del package", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, true),

    menu("Inserimento del package nel menu", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, true),

    service("Service specifico del package", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, true),

    logic("Business logic del package", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, true),

    form("Form specifico del package", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, false),

    list("List specifico del package", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, false),

    company("Entity subclass di Company", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, false),

    rowIndex("Entity usa rowIndex", false, false, true, true, FlowCost.VUOTA, FlowCost.VUOTA, true),

    ordine("Entity usa property (int)", false, false, true, true, "ordine", "PropertyOrdine", false, true, "ordine"),

    code("Entity usa property (String)", false, false, true, true, "code", "PropertyCode", true, true, "code"),

    descrizione("Entity usa property (String)", false, false, true, true, "descrizione", "PropertyDescrizione", true, true, "descrizione"),

    valido("Entity usa property (boolean)", false, false, true, true, "valido", "PropertyValido", false, true, "valido"),

    docFile("Update doc of all packages", false, false, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    sovrascrivePackage("Sovrascrive un package esistente", false, false, false, false, FlowCost.VUOTA, FlowCost.VUOTA, false),

    ;


    private String caption;

    private boolean newProject;

    private boolean updateProject;

    private boolean newPackage;

    private boolean updatePackage;

    private String field;

    private String sourcesTag;

    private boolean acceso;

    private boolean accesoInizialmente;

    private boolean fieldAssociato;

    private String fieldName;


    /**
     * Costruttore completo <br>
     */
    AECheck(String caption, boolean newProject, boolean updateProject, boolean newPackage, boolean updatePackage, String field, String sourcesTag, boolean accesoInizialmente) {
        this.caption = caption;
        this.newProject = newProject;
        this.updateProject = updateProject;
        this.newPackage = newPackage;
        this.updatePackage = updatePackage;
        this.field = field;
        this.sourcesTag = sourcesTag;
        this.accesoInizialmente = accesoInizialmente;
        this.acceso = accesoInizialmente;
    }

    /**
     * Costruttore completo <br>
     */
    AECheck(String caption, boolean newProject, boolean updateProject, boolean newPackage, boolean updatePackage, String field, String sourcesTag, boolean accesoInizialmente, boolean fieldAssociato, String fieldName) {
        this.caption = caption;
        this.newProject = newProject;
        this.updateProject = updateProject;
        this.newPackage = newPackage;
        this.updatePackage = updatePackage;
        this.field = field;
        this.sourcesTag = sourcesTag;
        this.accesoInizialmente = accesoInizialmente;
        this.acceso = accesoInizialmente;
        this.fieldAssociato = fieldAssociato;
        this.fieldName = fieldName;
    }


    /**
     * Ripristina il valore di default <br>
     */
    public static void reset() {
        for (AECheck aeCheck : AECheck.values()) {
            aeCheck.setAcceso(false);
        }
    }


    public static List<AECheck> getNewProject() {
        List<AECheck> listaChecks = new ArrayList<>();

        for (AECheck aeCheck : AECheck.values()) {
            if (aeCheck.isNewProject()) {
                listaChecks.add(aeCheck);
            }
        }

        return listaChecks;
    }


    public static List<AECheck> getUpdateProject() {
        List<AECheck> listaChecks = new ArrayList<>();

        for (AECheck aeCheck : AECheck.values()) {
            if (aeCheck.isUpdateProject()) {
                listaChecks.add(aeCheck);
            }
        }

        return listaChecks;
    }


    public static List<AECheck> getNewPackage() {
        List<AECheck> listaChecks = new ArrayList<>();

        for (AECheck aeWiz : AECheck.values()) {
            if (aeWiz.isNewPackage()) {
                listaChecks.add(aeWiz);
            }
        }

        return listaChecks;
    }


    public static List<AECheck> getUpdatePackage() {
        List<AECheck> listaChecks = new ArrayList<>();

        for (AECheck aeWiz : AECheck.values()) {
            if (aeWiz.isUpdatePackage()) {
                listaChecks.add(aeWiz);
            }
        }

        return listaChecks;
    }


    /**
     * Visualizzazione di controllo <br>
     */
    public static void printInfo(String posizione) {
//        if (FLAG_DEBUG_WIZ) {
//            System.out.println("********************");
//            System.out.println("AECheck  - " + posizione);
//            System.out.println("********************");
//            for (AECheck check : AECheck.values()) {
//                if (check.isFieldAssociato()) {
//                    System.out.println("AECheck." + check.name() + " \"" + check.caption + "\" = " + check.is() + " -> value=" + check.fieldName);
//                }
//                else {
//                    System.out.println("AECheck." + check.name() + " \"" + check.caption + "\" = " + check.is());
//                }
//            }
//            System.out.println("");
//        }
    }


    public boolean isNewProject() {
        return newProject;
    }


    public boolean isUpdateProject() {
        return updateProject;
    }


    public boolean isNewPackage() {
        return newPackage;
    }


    public boolean isUpdatePackage() {
        return updatePackage;
    }


    public boolean is() {
        return acceso;
    }


    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }


    public String getCaption() {
        return caption;
    }


    public String getField() {
        return field;
    }


    public String getSourcesTag() {
        return sourcesTag;
    }

    public boolean isAccesoInizialmente() {
        return accesoInizialmente;
    }

    public boolean isFieldAssociato() {
        return fieldAssociato;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

