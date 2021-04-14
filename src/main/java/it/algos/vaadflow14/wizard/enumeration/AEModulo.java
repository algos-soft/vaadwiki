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
    dirData("Data", "Directory data del modulo target", true, false, AEWizCost.dirData.get()),
    dirPackages("Packages", "Directory packages del modulo target", true, false, AEWizCost.dirPackages.get()),
    dirUI("UI", "Directory UI del modulo target", true, false, AEWizCost.dirUI.get()),
    fileMain("Main", "File main del modulo target", false, true, VUOTA, APP_NAME),
    fileCost("Cost", "File Cost del modulo target", false, true, AEWizCost.dirApplication.get(), FILE_COST),
    fileBoot("Boot", "File Boot del modulo target", false, true, AEWizCost.dirBoot.get(), FILE_BOOT),
    fileData("Data", "File Data del modulo target", false, true, AEWizCost.dirData.get(), FILE_DATA),
    //            creaDirectorySecurity();
    ;


    private String tag;

    private String descrizione;

    private boolean isDirectory;

    private boolean isSourceFile;

    private String directory;

    private String sourcesName;

    private String fileName;

    private AECopyWiz copyWiz;

    private String absolutePath;


    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isSourceFile, String directory) {
        this(tag, descrizione, isDirectory, isSourceFile, directory, VUOTA, null);
    }

    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isSourceFile, String directory, String sourcesName) {
        this(tag, descrizione, isDirectory, isSourceFile, directory, sourcesName, sourcesName, null);
    }

    /**
     * Costruttore parziale <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isSourceFile, String directory, String sourcesName, String fileName) {
        this(tag, descrizione, isDirectory, isSourceFile, directory, sourcesName, fileName, null);
    }

    /**
     * Costruttore completo <br>
     */
    AEModulo(String tag, String descrizione, boolean isDirectory, boolean isSourceFile, String directory, String sourcesName, String fileName, AECopyWiz copyWiz) {
        this.tag = tag;
        this.descrizione = descrizione;
        this.isDirectory = isDirectory;
        this.isSourceFile = isSourceFile;
        this.directory = directory;
        this.sourcesName = sourcesName;
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


    public static List<AEModulo> getSourceFiles() {
        List<AEModulo> lista = new ArrayList<>();

        for (AEModulo mod : AEModulo.values()) {
            if (mod.isSourceFile) {
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
            if (mod.isSourceFile) {
                mod.absolutePath += project + mod.fileName + FlowCost.JAVA_SUFFIX;
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

    public boolean isSourceFile() {
        return isSourceFile;
    }

        public String getDirectory() {
            return directory;
        }

    public String getSourcesName() {
        return sourcesName;
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

}
