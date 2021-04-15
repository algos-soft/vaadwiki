package it.algos.vaadflow14.wizard.enumeration;

import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 08-mar-2018
 * Time: 08:23
 * <p>
 * Enumeration dei progetti gestiti da Wizard <br>
 * Di default stanno nel path -> /Users/gac/Documents/IdeaProjects/operativi/ <br>
 * Altrimenti usare la property pathCompleto <br>
 */
public enum AEProgetto {

    bio("vaadwiki", "Wiki", VUOTA),
    wam("vaadwam", "Wam", VUOTA),
    beta("beta", "Beta", "/Users/gac/Documents/IdeaProjects/tutorial/beta/"),
    untitled("untitled", "Torino", "/Users/gac/Documents/IdeaProjects/untitled/"),
    moneglia("moneglia", "Moneglia", VUOTA),
    gestione("gestione", "Gestione", VUOTA),

    ;


    private String directoryAndProjectModuloLower;

    private String projectNameUpper;

    //--path completo se diverso da /Users/gac/Documents/IdeaProjects/operativi/...
    private String pathCompleto;


    AEProgetto(String directoryAndProjectModuloLower, String projectNameUpper, String pathCompleto) {
        this.directoryAndProjectModuloLower = directoryAndProjectModuloLower;
        this.projectNameUpper = projectNameUpper;
        this.pathCompleto = pathCompleto;
    }

    public static List<AEProgetto> get() {
        List<AEProgetto> lista = new ArrayList<>();

        for (AEProgetto project : AEProgetto.values()) {
            lista.add(project);
        }

        return lista;
    }

    public static List<String> getNames() {
        List<String> nomi = new ArrayList<>();

        for (AEProgetto progetto : AEProgetto.values()) {
            nomi.add(progetto.getProjectNameUpper());
        }

        return nomi;
    }


    public static AEProgetto getProgettoByName(String nameProject) {
        for (AEProgetto progetto : AEProgetto.values()) {
            if (progetto.getProjectNameUpper().equalsIgnoreCase(nameProject)) {
                return progetto;
            }
        }

        return null;
    }

    public static AEProgetto getProgettoByDirectory(String directory) {
        for (AEProgetto progetto : AEProgetto.values()) {
            if (progetto.getDirectoryAndProjectModuloLower().equalsIgnoreCase(directory)) {
                return progetto;
            }
        }

        return null;
    }


    /**
     * Visualizzazione di controllo <br>
     */
    public static void printInfo() {
        if (FLAG_DEBUG_WIZ) {
            System.out.println("********************");
            System.out.println("Progetti della enumeration AEProgetto");
            System.out.println("********************");
            for (AEProgetto progetto : AEProgetto.values()) {
                System.out.println("AEProgetto." + progetto.name() + " -> " + progetto.getProjectNameUpper()
                        + " - " + progetto.getDirectoryAndProjectModuloLower());
            }
            System.out.println("");
        }
    }

    public String getProjectNameUpper() {
        return projectNameUpper;
    }

    public String getDirectoryAndProjectModuloLower() {
        return directoryAndProjectModuloLower;
    }

    public String getPathCompleto() {
        return pathCompleto.endsWith(FlowCost.SLASH) ? pathCompleto : pathCompleto + FlowCost.SLASH;
    }
}// end of enumeration class
