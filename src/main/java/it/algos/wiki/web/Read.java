package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URLConnection;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 18:09
 * <p>
 * Legge una pagina internet di tipo HTTP oppure di tipo HTTPS.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class Read {

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    public final static String TAG_WIKI = "https://it.wikipedia.org/wiki/";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    public final static String TAG_API = "https://it.wikipedia.org/w/api.php?";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    public final static String TAG_BASE = TAG_API + "format=json&formatversion=2&";

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    public final static String TAG_QUERY = TAG_BASE + "action=query&";


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring in automatico <br>
     * Metodo più semplice. Non si possono passare parametri <br>
     * Viene iniettata nel ciclo di 'init()' di questa classe ed è quindi disponibile solo DOPO il ciclo <br>
     * <p>
     * Se serve prima, sempre senza possibilità di passare parametri, occorre: <br>
     * 1) dichiararla nel costruttore  <br>
     * 2) spostare il suo uso in un metodo @PostConstruct  <br>
     * 3) dichiararla con Xxx.getInstance(), ma la classe Xxx DEVE essere un istanza esplicita di Singleton Class <br>
     * 4) dichiararla (sconsigliato per l'uso dei Test) con StaticContextAccessor.getBean(Xxx.class) <br>
     * <p>
     * Property pubblic per poterla usare nei Test <br>
     */
    @Autowired
    public UrlGetConnection urlGetConnection;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring in automatico <br>
     * Metodo più semplice. Non si possono passare parametri <br>
     * Viene iniettata nel ciclo di 'init()' di questa classe ed è quindi disponibile solo DOPO il ciclo <br>
     * <p>
     * Se serve prima, sempre senza possibilità di passare parametri, occorre: <br>
     * 1) dichiararla nel costruttore  <br>
     * 2) spostare il suo uso in un metodo @PostConstruct  <br>
     * 3) dichiararla con Xxx.getInstance(), ma la classe Xxx DEVE essere un istanza esplicita di Singleton Class <br>
     * 4) dichiararla (sconsigliato per l'uso dei Test) con StaticContextAccessor.getBean(Xxx.class) <br>
     * <p>
     * Property pubblic per poterla usare nei Test <br>
     */
    @Autowired
    public UrlRequest urlRequest;

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe e usato come libreria <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    protected ATextService text = ATextService.getInstance();


    /**
     * Request di tipo GET
     * Accetta SOLO un domain (indirizzo) completo
     *
     * @return risposta grezza
     */
    public String legge() {
        return legge("");
    } // fine del metodo


    /**
     * Request di tipo GET
     * Accetta SOLO un domain (indirizzo) completo
     *
     * @param indirizzoWeb completo
     *
     * @return risposta grezza
     */
    public String legge(String indirizzoWeb) {
        String risposta = "";
        URLConnection urlConn;
        String tag = getTagIniziale();

        try { // prova ad eseguire il codice
            String indirizzoWebCompleto = indirizzoWeb.startsWith(tag) ? indirizzoWeb : tag + indirizzoWeb;
            urlConn = urlGetConnection.esegue(indirizzoWebCompleto);
            risposta = urlRequest.esegue(urlConn);
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return risposta;
    } // fine del metodo


    /**
     * Inserisce un tag specifico prima del titoloWiki della pagina per costruire il 'domain' completo
     */
    public String getTagIniziale() {
        return "";
    } // fine del metodo

}// end of class
