package it.algos.vaadwiki.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.boot.AVers;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.modules.versione.VersioneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Log delle versioni, modifiche e patch installate
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiVers extends AVers {


    private final static String CODE_PROJECT = "K";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private VersioneService vers;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private PreferenzaService pref;


    /**
     * Executed on container startup
     * Setup non-UI logic here
     * <p>
     * This method is called prior to the servlet context being initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE
     */
    public int inizia() {
        int k = super.inizia();
        codeProject = CODE_PROJECT;

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_BIO, "Crono per ciclo bio completo",true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_ATTIVITA, "Crono per download attività, extra-ciclo",true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_NAZIONALITA, "Crono per download nazionalità, extra-ciclo",true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_PROFESSIONE, "Crono per download professione, extra-ciclo",true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_CATEGORIA, "Crono per download categoria, extra-ciclo",true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_ATTIVITA, "Ultimo download del modulo attività");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_NAZIONALITA, "Ultimo download del modulo nazionalità");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_PROFESSIONE, "Ultimo download del modulo professione");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_CATEGORIA, "Ultimo controllo di tutte le voci esistenti nella categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_BIO, "Ultimo update delle voci della categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_ATTIVITA, "Durata in secondi dell'ultimo download del modulo attività");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_NAZIONALITA, "Durata in secondi dell'ultimo download del modulo nazionalità");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_PROFESSIONE, "Durata in secondi dell'ultimo download del modulo professione");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_CATEGORIA, "Durata in secondi dell'ultimo download delle voci della categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_BIO, "Durata in secondi dell'ultimo update delle voci della categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(CAT_BIO, "Categoria attiva", "BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(PAGE_LIMIT, "Numero di pagine da controllare nel blocco", 500);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_CHECK_LISTE_PAGEID, "Controllo di sicurezza delle liste di pageid tra Categoria server e mongoDB", true);
        }// fine del blocco if

        return k;
    }// end of method


}// end of bootstrap class