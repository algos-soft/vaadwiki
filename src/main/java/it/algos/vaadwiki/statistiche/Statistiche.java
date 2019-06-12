package it.algos.vaadwiki.statistiche;

import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.service.LibBio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 09-Jun-2019
 * Time: 18:29
 * <p>
 * Classe specializzata per costruire statistiche e pagine di servizio del Progetto Bio. <br>
 * Didascalie <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
public abstract class Statistiche {


    protected final static String INIZIO_RIGA = "\n|-\n|";

    protected final static String SEP = "||";

    protected static String PAGINA_PROVA = "Utente:Biobot/2";

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile SOLO DOPO @PostConstruct <br>
     */
    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    protected LibBio libBio;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo un eventuale metodo @PostConstruct <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine di init() <br>
     */
    @Autowired
    protected GiornoService giornoService;

}// end of class
