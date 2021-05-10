package it.algos.vaadwiki.wiki.query;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;

import java.net.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 15:53
 * <p>
 * Legge (scrive) una pagina internet di tipo HTTP oppure di tipo HTTPS. <br>
 */
public abstract class AQuery {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;

    /**
     * Request principale <br>
     * <p>
     * La stringa urlDomain per la request viene controllata ed elaborata <br>
     * Crea la connessione base di tipo GET
     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
     * In alcune request (non tutte) si aggiunge anche il POST <br>
     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
     * Invia la request con (eventuale) testo POST e con i cookies <br>
     * <p>
     * Risposta in formato testo JSON <br>
     * Recupera i cookies allegati alla risposta e li memorizza in WikiLogin per poterli usare in query successive <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     *
     * @param urlDomain indirizzo web usato nella urlRequest
     */
    public String urlRequest(String urlDomainGrezzo) {
        String urlResponse = VUOTA;
        URLConnection urlConn;
        String urlDomain;

//        try { // prova ad eseguire il codice
//            urlDomain = fixUrlDomain(urlDomainGrezzo);
//            urlConn = creaGetConnection(urlDomain);
//            uploadCookies(urlConn);
//            addPostConnection(urlConn);
//            urlResponse = sendRequest(urlConn);
//            downlodSecondaryCookies(urlConn);
//        } catch (Exception unErrore) { // intercetta l'errore
//            //            logger.error(unErrore.toString()+". Probabili problemi di connessione");
//            if (unErrore instanceof IOException) {
//                try { // prova ad eseguire il codice
//                    //                    logger.info("Riprovo. Se non esce subito un altro errore, vuol dire che questa volta il collegamento ha funzionato");
//                    urlDomain = fixUrlDomain(urlDomainGrezzo);
//                    urlConn = creaGetConnection(urlDomain);
//                    uploadCookies(urlConn);
//                    addPostConnection(urlConn);
//                    urlResponse = sendRequest(urlConn);
//                    downlodSecondaryCookies(urlConn);
//                } catch (Exception unErrore2) { // intercetta l'errore
//                    //                    logger.error("Questo url non ha funzionato: " + urlDomainGrezzo);
//                }// fine del blocco try-catch
//            }// end of if cycle
//        }// fine del blocco try-catch

//        return elaboraResponse(urlResponse);
        return "";
    }

}
