package it.algos.vaadflow14.wizard.enumeration;

import static it.algos.vaadflow14.wizard.scripts.WizCost.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 08-mar-2018
 * Time: 08:23
 */
public enum AEFlag {

    isBaseFlow(),
    isProject(),
    isPackage(),
    isNewProject(),
    isUpdateProject(),
    isNewPackage(),
    isUpdatePackage(),
    isDocPackages(),
    ;

    private boolean status;


    AEFlag() {
        this.set(false);
    }


    public static void reset() {
        for (AEFlag flag : AEFlag.values()) {
            flag.set(false);
        }
    }

    /**
     * Visualizzazione di controllo <br>
     */
    public static void printInfo(String posizione) {
        if (FLAG_DEBUG_WIZ) {
            System.out.println("********************");
            System.out.println("AEFlag  - " + posizione);
            System.out.println("********************");
            for (AEFlag flag : AEFlag.values()) {
                System.out.println("AEFlag." + flag.name() + " = " + flag.is());
            }
            System.out.println("");
        }
    }

    public boolean is() {
        return status;
    }

    public void set(boolean status) {
        this.status = status;
    }

}// end of enumeration class
