package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.utility.UtilityView;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 02-apr-2020
 * Time: 06:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageServiceIntegrationTest extends ATest {

    private final static String TITOLO = UtilityView.WIKI_TITLE_DEBUG;

    private static String BIO = "{{Bio\n" +
            "|Nome = Rudolf\n" +
            "|Cognome = Kolisch\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Klamm am Semmering\n" +
            "|GiornoMeseNascita = 20 luglio\n" +
            "|AnnoNascita = 1896\n" +
            "|LuogoMorte = Watertown\n" +
            "|LuogoMorteLink = Watertown (Massachusetts)\n" +
            "|GiornoMeseMorte = 1º agosto\n" +
            "|AnnoMorte = 1978\n" +
            "|Epoca = 1900\n" +
            "|Epoca2 = 2000\n" +
            "|Attività = violinista\n" +
            "|Nazionalità = austriaco\n" +
            "|NazionalitàNaturalizzato = statunitense\n" +
            "}}";

    @Autowired
    private Api api;

    @Autowired
    private PageService service;

    @Autowired
    private BioService bioService;

    @Autowired
    private ElaboraService elaboraService;

    private Page page;

    private Bio bio;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(api);
        Assert.assertNotNull(service);
    }// end of method


    /**
     * Crea una entity Bio partendo da una Page <br>
     * La entity NON viene salvata <br>
     *
     * @param page scaricata dal server wiki
     *
     * @return entity Bio
     */
    @Test
    public void creaBio() {
        page = api.leggePage(TITOLO);
        Assert.assertNotNull(page);

        bio = service.creaBio(page);
        Assert.assertNotNull(page);

        previsto = BIO;
        ottenuto = bio.tmplBioServer;
        Assert.assertEquals(previsto, ottenuto);
    }// end of single test


//    /**
//     * Elabora la singola voce biografica<br>
//     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
//     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
//     * Quello che resta è affidabile ed utilizzabile per le liste <br>
//     */
//    @Test
//    public void esegueNoSave() {
//        if (bio != null) {
//        }// end of if cycle
//    }// end of method

}// end of test class
