package it.algos.vaadwiki.backend.upload;

import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 21:25
 * <p>
 * Classe specializzata per caricare (upload) le liste biografiche sul server wiki. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Necessita del login come bot <br>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 * Punto d'inizio @PostConstruct inizia() nella superclasse <br>
 */
public abstract class Upload {

    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;
    /**
     * Istanza di una interfaccia SpringBoot <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    protected boolean usaWikiPaginaTest = true;

    protected AEntity entityBean;

    protected String wikiPaginaTitle = "Utente:Gac/Prova";

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
        if (usaWikiPaginaTest) {
            uploadPaginaTest();
        }
        else {
            uploadPaginaFinale();
        }
    }

    protected void uploadPaginaTest() {
    }

    protected void uploadPaginaFinale() {
    }

}
