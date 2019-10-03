package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.login.ALogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 09-feb-2019
 * Time: 08:14
 * Wrapper coi dati del collegamento al server wiki.
 * <p>
 * Questa classe: <ul>
 * <li> Mantiene il lguserid </li>
 * <li> Mantiene il lgusername </li>
 * <li> Mantiene il lgtoken </li>
 * <li> Mantiene il sessionid </li>
 * </ul>
 * <p>
 * Tipicamente esiste un solo oggetto di questo tipo per il bot
 * L'istanza viene creata all'avvio del programma e mantenuta disponibile in appContext
 * <p>
 */
@SpringComponent
@Qualifier("wikilogin")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class WLogin  {

    private boolean isValido;

    private boolean isAdmin;

    private boolean isBot;

    private long lguserid;

    private String lgusername;

    private String itwiki_BPsession;

    // ci metto tutti i cookies restituiti da URLConnection.responses
    private HashMap cookies;


    /**
     * Regolazione dei valori dell'ultimo collegamento di login effettuato
     */
    public void regola(long lguserid, String lgusername, String itwiki_BPsession) {
        this.reset();
        this.isValido = true;
        this.isAdmin = true;
        this.isBot = true;
        this.lguserid = lguserid;
        this.lgusername = lgusername;
        this.itwiki_BPsession = itwiki_BPsession;
    } // fine del metodo


    /**
     * Azzeramento di ogni valore
     */
    public void reset() {
        isValido = false;
        isAdmin = false;
        isBot = false;
        lguserid = 0L;
        lgusername = VUOTA;
        itwiki_BPsession = VUOTA;
    } // fine del metodo


    public String getLgusername() {
        return lgusername;
    }


    public HashMap getCookies() {
        return cookies;
    } // fine del metodo


    public void setCookies(HashMap cookies) {
        this.cookies = cookies;
    } // fine del metodo


    public boolean isBot() {
        return isBot;
    } // fine del metodo

}// end of class
