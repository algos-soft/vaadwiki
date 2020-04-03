package it.algos.vaadwiki.integration;

import it.algos.vaadflow.application.FlowCost;
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

import java.time.LocalDateTime;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 02-apr-2020
 * Time: 07:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BioServiceIntegrationTest extends ATest {

    private final static String TITOLO = UtilityView.WIKI_TITLE_DEBUG;

    @Autowired
    private Api api;

    @Autowired
    private BioService service;


    @Autowired
    private PageService pageService;


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
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties <br>
     *
     * La entity viene creata con le SOLE seguenti property valide:
     * id
     * pageid
     * wikiTitle
     * tmplBioServer
     * lastLettura
     *
     * @param page scaricata dal server wiki
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Test
    public void newEntity() {
        page = api.leggePage(TITOLO);
        Assert.assertNotNull(page);

        bio = service.newEntity(page);
        Assert.assertNotNull(bio);
        Assert.assertNotNull(bio.tmplBioServer);

        Assert.assertEquals(TITOLO, bio.wikiTitle);
        Assert.assertNull(bio.nome);
        Assert.assertNull(bio.cognome);
        Assert.assertEquals(FlowCost.START_DATE_TIME, bio.lastModifica);

    }// end of single test


}// end of test class
