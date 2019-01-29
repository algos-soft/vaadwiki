package it.algos.wiki.mediawiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.web.UrlPostConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 08:48
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UrlLoginConnection extends UrlPostConnection {

    private String lgname;

    private String lgpassword;

    private String lgtoken;


    /**
     * Costruttore @Autowired
     * In the newest Spring release, it’s constructor does not need to be annotated with @Autowired annotation.
     * L' @Autowired (esplicito o implicito) funziona SOLO per UN costruttore
     * Se ci sono DUE o più costruttori, va in errore
     * Se ci sono DUE costruttori, di cui uno senza parametri, inietta quello senza parametri
     */
    public UrlLoginConnection() {
    }// end of @Autowired constructor


    /**
     * Costruttore
     *
     * @param lgname     del Bot
     * @param lgpassword del Bot
     * @param lgtoken    recuperato dalla precedente request
     */
    public UrlLoginConnection(String lgname, String lgpassword, String lgtoken) {
        this.lgname = lgname;
        this.lgpassword = lgpassword;
        this.lgtoken = lgtoken;
    }// end of  constructor


    /**
     * Crea il testo del POST della request
     * DEVE essere sovrascritto nelle sottoclassi specifiche
     */
    @Override
    protected String elaboraPost() {
        String testoPost = "";

        testoPost += "&lgname=";
        testoPost += lgname;
        testoPost += "&lgpassword=";
        testoPost += lgpassword;
        testoPost += "&lgtoken=";
        testoPost += lgtoken;

        return testoPost;
    }// end of method

}// end of class
