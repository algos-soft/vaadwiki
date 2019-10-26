package it.algos.vaadwiki;

import it.algos.wiki.web.*;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.GenericApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 12:07
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("webwiki")
@DisplayName("Test per le request al server wiki")
public class WebWikiTest extends ATest {


    @InjectMocks
    protected AQueryHTTP aQueryHTTP;

    @InjectMocks
    protected AQueryHTTPS aQueryHTTPS;

    @InjectMocks
    protected AQueryRaw aQueryRaw;

    @InjectMocks
    protected AQueryPage aQueryPage;

    @InjectMocks
    protected AQueryVoce aQueryVoce;

    @InjectMocks
    protected AQueryBio aQueryBio;

    @InjectMocks
    protected AQueryLogin aQueryLogin;


    @BeforeAll
    public void setUp() {
        super.setUpTest();
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(aQueryHTTP);
        MockitoAnnotations.initMocks(aQueryHTTPS);
        MockitoAnnotations.initMocks(aQueryRaw);
        MockitoAnnotations.initMocks(aQueryPage);
        MockitoAnnotations.initMocks(aQueryVoce);
        MockitoAnnotations.initMocks(aQueryBio);
        MockitoAnnotations.initMocks(aQueryLogin);
    }// end of method


//    @Test
//    public void prova() {
//        ArrayList<Long> lista = new ArrayList<>();
//        lista.add(77500987L);
//        lista.add(77330987L);
//
//        requestMultiPages.esegue(lista);
//    }// end of single test


    @Test
    public void provaWebVuota() {
        String indirizzoWeb = "";

        ottenuto = aQueryHTTP.urlRequest(indirizzoWeb);
        assertTrue(ottenuto.length() == 0);
    }// end of single test


    @Test
    public void provaWebBase() {
        String indirizzoWeb = "quattroprovince.it/";

        ottenuto = aQueryHTTP.urlRequest(indirizzoWeb);
        assertTrue(ottenuto.length() > 0);
    }// end of single test


    @Test
    public void provaWebSecurity() {
        String indirizzoWeb = "eff.org/https-everywhere";

        ottenuto = aQueryHTTPS.urlRequest(indirizzoWeb);
        assertTrue(ottenuto.length() > 0);
    }// end of single test


    @Test
    public void provaWiki() {
        String titoloWiki = "Sarmato";

        ottenuto = aQueryRaw.urlRequest(titoloWiki);
        assertTrue(ottenuto.length() > 0);
    }// end of single test


    @Test
    public void provaWiki2() {
        String titoloWiki = "Neal Ascherson";

        ottenuto = aQueryRaw.urlRequest(titoloWiki);
        assertTrue(ottenuto.length() > 0);
    }// end of single test


//    @Test
//    public void provaWikiPage() {
//        String titoloWiki = "Sarmato";
//
//        Page page = aQueryPage.crea(titoloWiki);
//        assertTrue(page.isValida());
//    }// end of single test
//
//
//    @Test
//    public void provaWikiVoce() {
//        String titoloWiki = "Billund";
//        String previsto = "{{Divisione amministrativa\n|Nome = Billund";
//
//        contenuto = aQueryVoce.urlRequest(titoloWiki);
//        assertTrue(contenuto.length() > 0);
//        assertTrue(contenuto.startsWith(previsto));
//    }// end of single test
//
//
//    @Test
//    public void provaWikiTemplateBio() {
//        String titoloWiki = "Neal Ascherson";
//        String previsto = "{{Bio\n|Nome = Neal\n";
//
//        String contenuto = aQueryBio.urlRequest(titoloWiki);
//        assertTrue(contenuto.length() > 0);
//        assertTrue(contenuto.startsWith(previsto));
//
//
//        String contenuto2 = new AQueryBio().urlRequest(titoloWiki);
//        assertTrue(contenuto2.length() > 0);
//        assertTrue(contenuto2.startsWith(previsto));
//
//
//        String contenuto3 = appContext.getBean(AQueryBio.class).urlRequest(titoloWiki);
//        assertTrue(contenuto3.length() > 0);
//    }// end of single test
//
//
//    @Test
//    public void provaLogin() {
//        aQueryLogin.urlRequest();
//    }// end of single test

}// end of class
