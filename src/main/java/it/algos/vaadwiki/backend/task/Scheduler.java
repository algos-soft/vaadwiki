package it.algos.vaadwiki.backend.task;

import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.genere.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.packages.nomeDoppio.*;
import it.algos.vaadwiki.backend.packages.professione.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 06-giu-2021
 * Time: 15:02
 * //    @Scheduled(cron = "${ciclo.download.bio}")
 * // second, minute, hour, day of month, month, day(s) of week
 */
@Component
@EnableAsync
public class Scheduler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AttivitaService attivita;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NomeDoppioService prenome;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ProfessioneService professione;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public GenereService genere;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaService nazionalita;

    private boolean flag = false;

    @Async
    @Scheduled(cron = "0 41 * * * *")
    public void downloadPrenome() {
        if (flag && prenome != null) {
            prenome.download();
        }
    }


    @Async
    @Scheduled(cron = "0 2 0 * * *")
    public void downloadGenere() {
        if (flag && genere != null) {
            genere.download();
        }
    }

    @Async
    @Scheduled(cron = "0 3 0 * * *")
    public void downloadProfessione() {
        if (flag && professione != null) {
            professione.download();
        }
    }

    @Async
    @Scheduled(cron = "0 4 0 * * *")
    public void downloadAttivita() {
        if (flag && attivita != null) {
            attivita.download();
        }
    }

    @Async
    @Scheduled(cron = "0 5 0 * * *")
    public void downloadNazionalita() {
        if (flag && nazionalita != null) {
            nazionalita.download();
        }
    }

}
