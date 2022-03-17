package it.algos.vaadwiki;

import it.algos.vaadwiki.service.AWikiService;
import it.algos.wiki.web.AQueryBot;
import it.algos.wiki.web.AQueryLogin;
import it.algos.wiki.web.WLogin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Tue, 30-Apr-2019
 * Time: 07:49
 */
//@ExtendWith(MockitoExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Tag("querylogin")
//@DisplayName("Test per login iniziale")
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = { WikiApplication.class })
//@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AQueryLoginTest {


    @Autowired
    public WLogin wLogin;

//    @InjectMocks
    protected AQueryLogin query;

    @Autowired
    protected AWikiService wikiService;

//    @InjectMocks
//    protected ApplicationContext appContext;

    @Autowired
    private ApplicationContext appContext;

//    @Autowired
//    private WebApplicationContext wac;

//    @BeforeAll
//    public void setUp() {
////        super.setUpTest();
//        MockitoAnnotations.initMocks(this);
//        MockitoAnnotations.initMocks(query);
//        MockitoAnnotations.initMocks(wikiService);
//        MockitoAnnotations.initMocks(wLogin);
////        MockitoAnnotations.initMocks(wac);
//        query.wikiService = wikiService;
//        query.wLogin = wLogin;
//
////        GenericApplicationContext ctx = new GenericApplicationContext();
////        query.setAppContext(ctx);
//    }// end of method


    @Test
    /**
     * Request al software mediawiki composta di due request <br>
     * <p>
     * La prima request preliminare è di tipo GET con action=login e senza ulteriori parametri
     * Crea la connessione base di tipo GET <br>
     * Invia la request senza testo POST e senza invio di cookies <br>
     * Nella preliminay scarica i cookies passati nella risposta, tra cui session
     * Nella preliminay recupera il logintoken necessario per la urlRequest successiva
     * <p>
     * La seconda request è di tipo POST
     * Nella seconda rinvia i cookies ricevuti (forse solo la session) <br>
     * Nella seconda invia un post con lgname, lgpassword e lgtoken <br>
     * <p>
     * La response viene elaborata per confermare il login andato a buon fine <br>
     */
    public void urlRequest() {
        query = appContext.getBean(AQueryLogin.class);
        Assert.assertNotNull(query);
        int a = 87;
        query.urlRequest();
    }// end of single test

}// end of class
