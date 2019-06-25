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
            creaPrefBool(USA_DAEMON_BIO, "Crono per ciclo bio completo", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_ATTIVITA, "Crono per download attività, extra-ciclo", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_NAZIONALITA, "Crono per download nazionalità, extra-ciclo", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_PROFESSIONE, "Crono per download professione, extra-ciclo", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_CATEGORIA, "Crono per download categoria, extra-ciclo", false);
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
            creaPrefDate(LAST_DOWNLOAD_CATEGORIA, "Ultimo controllo di tutte le pagine esistenti nella categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_BIO, "Ultimo update delle pagine della categoria BioBot");
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
            creaPrefInt(DURATA_DOWNLOAD_CATEGORIA, "Durata in secondi dell'ultimo download delle pagine della categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_BIO, "Durata in secondi dell'ultimo update delle pagine della categoria BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(CAT_BIO, "Categoria attiva", "BioBot");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(WIKI_PAGE_LIMIT, "Numero di pagine wiki da controllare nel blocco", 250);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_CHECK_LISTE_PAGEID, "Controllo di sicurezza delle liste di pageid tra Categoria server e mongoDB", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(SEND_MAIL_CICLO, "Mail di conferma e controllo del ciclo giornaliero", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_UPLOAD_DURANTE_DOWNLOAD, "Corregge sul wiki la voce durante il download", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(SOGLIA_NOMI_MONGO, "Soglia minima per creare una entity nella collezione Nomi sul mongoDB", 10);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(SOGLIA_NOMI_PAGINA_WIKI, "Soglia minima per creare una pagina di un nome sul server wiki", 50);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_ELABORA_NOME, "Ultima elaborazione dei nomi");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(SOGLIA_COGNOMI_MONGO, "Soglia minima per creare una entity nella collezione Cognomi sul mongoDB", 10);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(SOGLIA_COGNOMI_PAGINA_WIKI, "Soglia minima per creare una pagina di un nome sul server wiki", 50);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_ELABORA_COGNOME, "Ultima elaborazione dei cognomi");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_DAEMON_GENERE, "Crono per download genere, extra-ciclo", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate(LAST_DOWNLOAD_GENERE, "Ultimo download del modulo genere (plurali)");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefInt(DURATA_DOWNLOAD_GENERE, "Durata in secondi dell'ultimo download del modulo genere (plurali)");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_REGISTRA_SEMPRE_CRONO, "Registra sempre le pagine di giorni ed anni", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_PARAGRAFI_GIORNI, "Paragrafi dei secoli nelle liste dei giorni", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_PARAGRAFI_ANNI, "Paragrafi dei mesi nelle liste degli anni", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA, "Titolo del paragrafo per le biografie senza anno di nascita specificato", "Senza anno di nascita");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA, "Posiziona come ultimo il paragrafo per le biografie senza anno specificato", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_FORCETOC_GIORNI, "Usa l'indice nelle liste dei giorni", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(TAG_PARAGRAFO_VUOTO_GIORNI_MORTE, "Titolo del paragrafo per le biografie senza anno di morte specificato", "Senza anno di morte");
        }// fine del blocco if


        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(TAG_PARAGRAFO_VUOTO_ANNI_NASCITA, "Titolo del paragrafo per le biografie senza giorno di nascita specificato", "Senza giorno di nascita");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(IS_PARAGRAFO_VUOTO_ANNI_IN_CODA, "Posiziona come ultimo il paragrafo per le biografie senza giorno specificato", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_FORCETOC_ANNI, "Usa l'indice nelle liste degli anni", false);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(TAG_PARAGRAFO_VUOTO_ANNI_MORTE, "Titolo del paragrafo per le biografie senza giorno di morte specificato", "Senza giorno di morte");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_RIGHE_RAGGRUPPATE_GIORNI, "Usa righe raggruppate per anno nella liste dei giorni", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefBool(USA_RIGHE_RAGGRUPPATE_ANNI, "Usa righe raggruppate per giorno nella liste degli anni", true);
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(TAG_PARAGRAFO_VUOTO_NOMI, "Titolo del paragrafo per le biografie senza attività specificata", "Senza attività specificata");
        }// fine del blocco if

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefTxt(TAG_PARAGRAFO_VUOTO_COGNOMI, "Titolo del paragrafo per le biografie senza attività specificata", "Senza attività specificata");
        }// fine del blocco if

        return k;
    }// end of method


}// end of bootstrap class