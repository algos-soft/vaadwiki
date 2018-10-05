package it.algos.wiki;


import javax.servlet.ServletContext;

/**
 * Log delle versioni, modifiche e patch installate
 */
public class VersioneBootStrap {

    /**
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * l'ordine di inserimento è FONDAMENTALE
     * <p>
     * Se le versioni aumentano, conviene spostare in una classe esterna
     */
    public static void init(ServletContext svltCtx) {

        //--prima installazione del programma
        //--non fa nulla, solo informativo
//        if (LibVers.installa(1)) {
//            LibVers.nuova("Setup", "Installazione iniziale");
//        }// fine del blocco if

    }// end of method

}// end of boot class
