package it.algos.vaadwiki;

import it.algos.vaadwiki.service.AWikiService;
import it.algos.wiki.web.AQueryWrite;
import it.algos.wiki.web.WLogin;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 28-Apr-2019
 * Time: 08:42
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("querywrite")
@DisplayName("Test per le request di scrittura")
public class AQueryWriteTest extends ATest {


    protected final static String TITOLO_PAGINA_DEBUG = "Utente:Biobot/2";

    protected final static String TESTO_DEBUG = "Prova di scrittura";

    @InjectMocks
    protected AQueryWrite query;

    @InjectMocks
    protected AWikiService wikiService;

    @InjectMocks
    public WLogin wLogin;

    @BeforeAll
    public void setUp() {
        super.setUpTest();
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(query);
        MockitoAnnotations.initMocks(wikiService);
        MockitoAnnotations.initMocks(wLogin);
        query.setUrlDomain(TITOLO_PAGINA_DEBUG);
        query.setNewText(TESTO_DEBUG);
        query.wikiService = wikiService;
        query.wLogin = wLogin;
    }// end of method


    @Test
    /**
     * Request principale <br>
     * <p>
     * La stringa del urlDomain per la request viene elaborata <br>
     * Si crea la connessione <br>
     * La request base usa solo il GET <br>
     * In alcune request (non tutte) si aggiunge anche il POST <br>
     * Alcune request (non tutte) scaricano e memorizzano i cookies ricevuti nella connessione <br>
     * Alcune request (non tutte) hanno bisogno di inviare i cookies nella request <br>
     * Si invia la connessione <br>
     * La response viene sempre elaborata per estrarre le informazioni richieste <br>
     */
    public void urlRequest() {
        query.urlRequest();
    }// end of single test

}// end of class
