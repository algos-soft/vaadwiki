package it.algos.vaadflow14.wizard.enumeration;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;

import java.util.*;

/**
 * Project vaadwiki14
 * Created by Algos
 * User: gac
 * Date: mar, 09-feb-2021
 * Time: 13:49
 */
public enum AEModulo {

    dirModulo("Modulo", "Directory principale del modulo target", true, false, VUOTA),
    dirBackend("Backend", "Directory backend del modulo target", true, false, AEWizCost.dirBackend.get()),
    dirApplication("Application", "Directory application del modulo target", true, false, AEWizCost.dirApplication.get()),
    dirBoot("Boot", "Directory boot del modulo target", true, false, AEWizCost.dirBoot.get()),
    dirBackEnum("Enumeration", "Directory backend/enumeration del modulo target", true, false, AEWizCost.dirBackEnum.get()),
    dirUiEnum("Enumeration", "Directory ui/enumeration del modulo target", true, false, AEWizCost.dirUiEnum.get()),
    dirData("Data", "Directory data del modulo target", true, false, AEWizCost.dirData.get()),
    dirPackages("Packages", "Directory packages del modulo target", true, false, AEWizCost.dirPackages.get()),
    dirUI("UI", "Directory UI del modulo target", true, false, AEWizCost.dirUI.get()),
    fileMain("Main", "File main del modulo target", false, true, VUOTA, APP_NAME, VUOTA, APP_NAME),
    fileCost("Cost", "File Cost del modulo target", false, true, AEWizCost.dirApplication.get(), FILE_COST, VUOTA, FILE_COST),
    fileBoot("Boot", "File Boot del modulo target", false, true, AEWizCost.dirBoot.get(), FILE_BOOT, VUOTA, FILE_BOOT),
    fileData("Data", "File Data del modulo target", false, true, AEWizCost.dirData.get(), FILE_DATA, VUOTA, FILE_DATA),
    filePreferenza("Preferenza", "File AExxxPreferenza del modulo target", false, true, AEWizCost.dirBackEnum.get(), FILE_PREFERENZA, FILE_PREFIX_ENUMERATION, FILE_PREFERENZA),
    fileButton("Button", "File AExxxButton del modulo target", false, true, AEWizCost.dirUiEnum.get(), FILE_BUTTON, FILE_PREFIX_ENUMERATION, FILE_BUTTON),

    //            creaDirectorySecurity();
    ;


    private String tag;

    private String descrizione;

    private boolean isDirectory;

    private boolean isFile;

    private String directory;

    private String sourcesName;

    private String prefix;

    private String fileName;

    private AECopyWiz copyWiz;

    private String absolutePath;

    private boolean acceso;

    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isFile, String directory) {
        this(tag, descrizione, isDirectory, isFile, directory, VUOTA, null);
    }

    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isFile, String directory, String sourcesName) {
        this(tag, descrizione, isDirectory, isFile, directory, sourcesName, VUOTA, sourcesName, null);
    }

    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isFile, String directory, String sourcesName, String fileName) {
        this(tag, descrizione, isDirectory, isFile, directory, sourcesName, VUOTA, fileName, null);
    }

    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isFile, String directory, String sourcesName, String prefix, String fileName) {
        this(tag, descrizione, isDirectory, isFile, directory, sourcesName, prefix, fileName, null);
    }

    /**
     * Costruttore completo <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isFile, String directory, String sourcesName, String prefix, String fileName, AECopyWiz copyWiz) {
        this.tag = tag;
        this.descrizione = descrizione;
        this.isDirectory = isDirectory;
        this.isFile = isFile;
        this.directory = directory;
        this.sourcesName = sourcesName;
        this.prefix = prefix;
        this.fileName = fileName;
        this.copyWiz = copyWiz != null ? copyWiz : isDirectory ? AECopyWiz.dirAddingOnly : AECopyWiz.sourceCheckFlagSeEsiste;
        this.absolutePath = VALORE_MANCANTE;
    }


    public static List<AEModulo> getDirectories() {
        List<AEModulo> lista = new ArrayList<>();

        for (AEModulo mod : AEModulo.values()) {
            if (mod.isDirectory) {
                lista.add(mod);
            }
        }

        return lista;
    }


    public static List<AEModulo> getFiles() {
        List<AEModulo> lista = new ArrayList<>();

        for (AEModulo mod : AEModulo.values()) {
            if (mod.isFile) {
                lista.add(mod);
            }
        }

        return lista;
    }

    public static List<AEModulo> getFilesValidi() {
        List<AEModulo> lista = new ArrayList<>();

        for (AEModulo mod : AEModulo.values()) {
            if (mod.isFile && mod.is()) {
                lista.add(mod);
            }
        }

        return lista;
    }

    /**
     * Regolazioni iniziali indipendenti dal dialogo di input <br>
     * Chiamato da Wizard.initView() <br>
     */
    public static void fixValues(String pathModulo, String project) {
        for (AEModulo mod : AEModulo.values()) {
            mod.absolutePath = pathModulo + mod.directory;
            if (mod.isFile) {
                mod.absolutePath += mod.prefix + project + mod.fileName + FlowCost.JAVA_SUFFIX;
            }
        }
    }


    //--metodo statico invocato da Wizard.initView()
    public static void printInfo() {
        System.out.println(FlowCost.VUOTA);
        System.out.println("********************");
        System.out.println("Directory e files del modulo da creare/modificare");
        System.out.println("********************");
        for (AEModulo sub : AEModulo.values()) {
            System.out.print("AEModulo." + sub.name() + ": \"" + sub.descrizione + "\" " + FlowCost.UGUALE_SPAZIATO + sub.getAbsolutePath());
            System.out.println(FlowCost.VUOTA);
        }
        System.out.println(FlowCost.VUOTA);
    }


    public String getTag() {
        return tag;
    }

    //    public String getDescrizione() {
    //        return descrizione;
    //    }


    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isFile() {
        return isFile;
    }

    public String getDirectory() {
        return directory;
    }

    public String getSourcesName() {
        return sourcesName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getFileName() {
        return fileName;
    }

    public AECopyWiz getCopyWiz() {
        return copyWiz;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public boolean is() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }
}
