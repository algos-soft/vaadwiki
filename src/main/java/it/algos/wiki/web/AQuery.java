package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:35
 * Legge (scrive) una pagina internet di tipo HTTP oppure di tipo HTTPS.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class AQuery {

    protected static String ENCODE = "UTF-8";

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring in automatico <br>
//     * Metodo più semplice. Non si possono passare parametri <br>
//     * Viene iniettata nel ciclo di 'init()' di questa classe ed è quindi disponibile solo DOPO il ciclo <br>
//     * <p>
//     * Se serve prima, sempre senza possibilità di passare parametri, occorre: <br>
//     * 1) dichiararla nel costruttore  <br>
//     * 2) spostare il suo uso in un metodo @PostConstruct  <br>
//     * 3) dichiararla con Xxx.getInstance(), ma la classe Xxx DEVE essere un istanza esplicita di Singleton Class <br>
//     * 4) dichiararla (sconsigliato per l'uso dei Test) con StaticContextAccessor.getBean(Xxx.class) <br>
//     * <p>
//     * Property pubblic per poterla usare nei Test <br>
//     */
//    @Autowired
//    public UrlGetConnection urlGetConnection;
//
//    /**
//     * Istanza (@Scope = 'singleton') inietta da Spring in automatico <br>
//     * Metodo più semplice. Non si possono passare parametri <br>
//     * Viene iniettata nel ciclo di 'init()' di questa classe ed è quindi disponibile solo DOPO il ciclo <br>
//     * <p>
//     * Se serve prima, sempre senza possibilità di passare parametri, occorre: <br>
//     * 1) dichiararla nel costruttore  <br>
//     * 2) spostare il suo uso in un metodo @PostConstruct  <br>
//     * 3) dichiararla con Xxx.getInstance(), ma la classe Xxx DEVE essere un istanza esplicita di Singleton Class <br>
//     * 4) dichiararla (sconsigliato per l'uso dei Test) con StaticContextAccessor.getBean(Xxx.class) <br>
//     * <p>
//     * Property pubblic per poterla usare nei Test <br>
//     */
//    @Autowired
//    public UrlRequest urlRequest;


    protected boolean isUsaPost = false;

    protected boolean isDownloadCookies = false;

    protected boolean isUploadCookies = false;


    /**
     * Request principale
     * Quella base usa solo il GET
     * In alcune request (non tutte) si aggiunge anche il POST
     *
     * @param urlDomain stringa della request
     */
    public String urlRequest(String urlDomain) {
        String risposta = "";
        URLConnection urlConn;

        //--Controlla la stringa della request
        urlDomain = fixUrlDomain(urlDomain);

        try { // prova ad eseguire il codice
            //--crea la connessione base (GET)
            urlConn = this.creaGetConnection(urlDomain);


            if (isUploadCookies) {
                this.uploadCookies(urlConn);
            }// end of if cycle

//            //--POST
//            this.creaPostPreliminary(urlConn);
//
//            //--GET
//            input = urlConn.getInputStream();
//            inputReader = new InputStreamReader(input, INPUT);
//
//            //--cookies
//            //--recupera i cookies ritornati e li memorizza nei parametri
//            //--in modo da poterli rinviare nella seconda richiesta
//            if (needCookies) {
////            this.downlodCookies(urlConn);
//            }// end of if cycle


            //--crea una connessione di tipo POST, se richiesta
            this.creaPostConnection(urlConn);

            //--Invia la request (GET oppure anche POST)
            risposta = sendRequest(urlConn);

//            urlConn = urlGetConnection.esegue(urlDomain);
//            risposta = urlRequest.esegue(urlConn);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        // controlla il valore di ritorno della request e regola il/i risultato/i
        elaboraRisposta(risposta);

        return risposta;
    } // fine del metodo


    /**
     * Controlla la stringa della request
     * Inserisce un tag specifico
     * Codifica i caratteri
     *
     * @param urlDomain stringa della request originale
     *
     * @return stringa della request modificata0
     */
    public String fixUrlDomain(String urlDomain) {
        return urlDomain;
    } // fine del metodo


    /**
     * Crea la connessione base (GET)
     * <p>
     * Regola i parametri della connessione
     *
     * @param urlDomain stringa della request
     *
     * @return connessione con la request
     */
    private URLConnection creaGetConnection(String urlDomain) throws Exception {
        URLConnection urlConn = null;

        if (text.isValid(urlDomain)) {
            urlConn = new URL(urlDomain).openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        }// end of if cycle

        return urlConn;
    } // fine del metodo


    /**
     * Aggiunge il POST della request
     * In alcune request (non tutte) è obbligatorio anche il POST
     *
     * @param urlConn connessione con la request
     */
    private void creaPostConnection(URLConnection urlConn) throws Exception {
        if (isUsaPost) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost());
            out.close();
        }// end of if cycle
    } // fine del metodo


    /**
     * Crea il testo del POST della request
     * <p>
     * In alcune request (non tutte) è obbligatorio anche il POST
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost() {
        return VUOTA;
    } // fine del metodo


    /**
     * Invia la request (GET oppure POST)
     * Testo della risposta
     *
     * @param urlConn connessione con la request
     *
     * @return valore di ritorno della request
     */
    private String sendRequest(URLConnection urlConn) throws Exception {
        InputStream input;
        InputStreamReader inputReader;
        BufferedReader readBuffer;
        StringBuilder textBuffer = new StringBuilder();
        String stringa;

        input = urlConn.getInputStream();
        inputReader = new InputStreamReader(input, "UTF8");

        //--cookies
        //--recupera i cookies ritornati e li memorizza nei parametri
        //--in modo da poterli rinviare nella seconda richiesta
        if (isDownloadCookies) {
            this.downlodCookies(urlConn);
        }// end of if cycle

        // read the response
        readBuffer = new BufferedReader(inputReader);
        while ((stringa = readBuffer.readLine()) != null) {
            textBuffer.append(stringa);
        }// fine del blocco while

        //--close all
        readBuffer.close();
        inputReader.close();
        input.close();

        return textBuffer.toString();
    } // fine del metodo


    /**
     * Allega i cookies alla request (upload)
     * Serve solo la sessione
     *
     * @param urlConn connessione
     */
    protected void uploadCookies(URLConnection urlConn) {
    } // fine del metodo


    /**
     * Grabs cookies from the URL connection provided.
     * Cattura i cookies ritornati e li memorizza nei parametri
     * Sovrascritto nelle sottoclassi specifiche
     *
     * @param urlConn connessione
     */
    protected void downlodCookies(URLConnection urlConn) {
    } // fine del metodo


    /**
     * Elabora la risposta
     * <p>
     * Informazioni, contenuto e validità della risposta
     * Controllo del contenuto (testo) ricevuto
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected void elaboraRisposta(String rispostaRequest) {
    } // fine del metodo

}// end of class
