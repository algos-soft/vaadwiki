package it.algos.vaadwiki.service;

import it.algos.vaadflow.modules.log.LogService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.*;
import it.algos.vaadwiki.download.*;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.wiki.Api;
import it.algos.wiki.WikiLoginOld;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: dom, 12-ago-2018
 * Time: 18:45
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class ABioService {


    @Autowired
    protected ApplicationContext appContext;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected WikiLoginOld wikiLoginOld;


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


//    /**
//     * La injection viene fatta da SpringBoot in automatico <br>
//     */
//    @Autowired
//    protected CategoriaService categoriaService;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public BioService bioService;


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
    protected AMongoService mongo;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ATextService text;


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

}// end of class
