package it.algos.wiki.request;

import it.algos.wiki.TipoRisultato;

/**
 * Classe concreta per le Request sul Web
 * Legge una pagina internet (qualsiasi)
 * Accetta SOLO un domain (indirizzo) completo
 */
public class QueryWeb extends Query {

    private static String WEB_MISSING = "UnknownHostException";

    /**
     * Costruttore utilizzato da una sottoclasse
     * <p>
     * L'istanza della sottoclasse usa un costruttore senza parametri
     * Regola alcune property
     * Regola il domain
     * Invoca il metodo inizializza() della superclasse (questa)
     */
    public QueryWeb() {
    }// fine del metodo costruttore senza parametri

    /**
     * Costruttore completo
     * <p>
     * L'istanza di questa classe viene chiamata con il domain già regolato
     * Parte subito il metodo inizializza() che esegue la Request
     */
    public QueryWeb(String domain) {
        this.domain = domain;
        this.doInit();
    }// fine del metodo costruttore completo

    /**
     * Metodo iniziale
     */
    protected void doInit() {
        try { // prova ad eseguire il codice
            testoPrimaRequest = this.firstRequest();
            risultato = TipoRisultato.letta;
            valida = true;
        } catch (Exception unErrore) { // intercetta l'errore
            errore = unErrore.getClass().getSimpleName();
            if (errore.equals(WEB_MISSING)) {
                risultato = TipoRisultato.nonTrovata;
            }// end of if cycle
        }// fine del blocco try-catch
    } // fine del metodo


    /**
     * Controlla di aver scritto la pagina
     * DEVE essere implementato nelle sottoclassi specifiche
     */
    @Override
    public boolean isScritta() {
        return false;
    } // fine del metodo

} // fine della classe
