package it.algos.vaadwiki.wiki;


import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.time.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 11-giu-2021
 * Time: 06:47
 * Wrapper per i dati base essenziali di una biografia <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BioWrap {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiBotService wikiBot;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BioUtility bioUtility;


    private long pageid;

    private String title;

    //--contenuto completo della pagina
    private String testo;

    //--template Bio
    private String tmplBio;

    private LocalDateTime lastModifica;

    private Map<String, Object> mappaApi;

    private Map<String, String> mappaBio;


    public BioWrap(Map<String, Object> mappaApi) {
        this.mappaApi = mappaApi;
    }

    /**
     * Performing the initialization in a constructor is not suggested <br>
     * as the state of the UI is not properly set up when the constructor is invoked. <br>
     * <p>
     * La injection viene fatta da SpringBoot solo alla fine del metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * L'istanza può essere creata con  appContext.getBean(xxxClass.class);  oppure con @Autowired <br>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        if (mappaApi != null) {
            this.pageid = (Long) mappaApi.get(FlowCost.KEY_MAPPA_PAGEID);
            this.title = (String) mappaApi.get(FlowCost.KEY_MAPPA_TITLE);
            this.testo = (String) mappaApi.get(FlowCost.KEY_MAPPA_TEXT);
            this.lastModifica = (LocalDateTime) mappaApi.get(KEY_MAPPA_LAST_MODIFICA);
        }

        if (wikiBot != null && text.isValid(testo)) {
            this.tmplBio = wikiBot.estraeTmpl(this.testo);
        }

//        if (bioUtility != null && text.isValid(tmplBioServer)) {
//            mappaBio = bioUtility.estraeMappa(tmplBioServer);
//            this.tmplBioClient = this.tmplBioServer + "pippoz";
//        }
    }

    public long getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

    public String getTesto() {
        return testo;
    }

    public String getTmplBio() {
        return tmplBio;
    }

    public String getNome() {
        return mappaBio != null ? mappaBio.get("Nome") : VUOTA;
    }

    public String getCognome() {
        return mappaBio != null ? mappaBio.get("Cognome") : VUOTA;
    }

    public LocalDateTime getLastModifica() {
        return lastModifica;
    }

}
