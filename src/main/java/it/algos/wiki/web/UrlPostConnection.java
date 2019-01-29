package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.PrintWriter;
import java.net.URLConnection;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 08:42
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UrlPostConnection extends UrlGetConnection {


    /**
     * Crea la connessione di tipo POST
     * In alcune request (non tutte) è obbligatorio anche il POST
     */
    public URLConnection esegue(String domain) throws Exception {
        URLConnection urlConn = esegue(domain);

        if (urlConn != null) {
            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.print(elaboraPost());
            out.close();
        }// end of if cycle

        return urlConn;
    } // fine del metodo


    /**
     * Crea il testo del POST della request
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    protected String elaboraPost() {
        return VUOTA;
    } // fine del metodo

}// end of class
