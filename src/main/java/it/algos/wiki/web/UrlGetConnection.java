package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.service.ATextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.net.URL;
import java.net.URLConnection;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 17:31
 * <p>
 * Connessione base
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UrlGetConnection {

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
    public ATextService text;


    /**
     * Crea la connessione di tipo GET
     */
    public URLConnection esegue(String domain) throws Exception {
        URLConnection urlConn = null;

        if (text.isValid(domain)) {
            urlConn = new URL(domain).openConnection();
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; it-it) AppleWebKit/418.9 (KHTML, like Gecko) Safari/419.3");
        }// end of if cycle

        return urlConn;
    } // fine del metodo


}// end of class
