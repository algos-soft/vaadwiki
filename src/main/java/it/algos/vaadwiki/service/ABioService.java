package it.algos.vaadwiki.service;

import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.*;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.vaadwiki.modules.wiki.WikiService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static it.algos.vaadwiki.application.WikiCost.BIO_NEEDED_MINUMUM_SIZE;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: dom, 12-ago-2018
 * Time: 18:45
 */
@Slf4j
public abstract class ABioService {


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public BioService bioService;

//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected WikiLoginOld wikiLoginOld;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public PreferenzaService pref;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ElaboraService elaboraService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public Api api;



    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ATextService text;

    @Autowired
    protected ApplicationContext appContext;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected GenereService genereService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NewService newService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected DeleteService deleteService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected UpdateService updateService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PageService pageService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AArrayService array;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMongoService mongo;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMailService mail;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected LogService logger;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected LibBio libBio;


    /**
     * Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile, altrimenti non esegue <br>
     */
    protected boolean checkMongo() {
        boolean scarso = false;

        if (checkBioScarso()) {
            mail.send("Upload attivita", "Abortito l'upload delle attività perché il mongoDb delle biografie sembra vuoto o comunque carente di voci che invece dovrebbero esserci.");
            scarso = true;
        }// end of if cycle

        return scarso;
    }// end of method


    /**
     * Controlla che il mongoDb delle voci biografiche abbia una dimensione accettabile <br>
     * Per evitare di 'sparare' sul server pagine con biografie 'mancanti' <br>
     * Valore da aggiornare ogni tanto <br>
     */
    protected boolean checkBioScarso() {
        return bioService.count() < BIO_NEEDED_MINUMUM_SIZE;
    }// end of method

}// end of class
