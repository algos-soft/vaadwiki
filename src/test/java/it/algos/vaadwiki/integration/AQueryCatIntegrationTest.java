package it.algos.vaadwiki.integration;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadwiki.ATest;
import it.algos.wiki.web.AQueryCatAll;
import it.algos.wiki.web.AQueryCatCategorie;
import it.algos.wiki.web.AQueryCatInfo;
import it.algos.wiki.web.AQueryCatPagine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 04-May-2019
 * Time: 08:26
 * Istanze di classi @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
 * - vanno dichiarate @Autowired
 * - sono disponibili da subito nel metodo @BeforeAll
 * - controllare sempre con Assert.assertNotNull(xyz);
 * Istanze di classi @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
 * - non vanno dichiarate @Autowired
 * - vanno costruite nel metodo @BeforeAll (oppure nel singolo test) con appContext.getBean(Xyz.class);
 * - controllare subito dopo con Assert.assertNotNull(xyz);
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AQueryCatIntegrationTest extends ATest {

    private final static String TITOLO_CAT_MINIMA = "Altopiani dell'Europa";

    private final static String TITOLO_CAT_PICCOLA = "Nati nel 1800";

    private final static String TITOLO_CAT_MEDIA = "Nati nel 1899";

    private final static String TITOLO_CAT_GRANDE = "BioBot";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;


    @Autowired
    private ApplicationContext appContext;


    @Before
    public void setUp() {
        Assert.assertNotNull(appContext);
        pref.saveValue(FlowCost.USA_DEBUG, true);
    }// end of method


    @Test
    public void queryCatInfo() {
        previstoIntero = 13;
        ottenutoIntero = appContext.getBean(AQueryCatInfo.class, TITOLO_CAT_MINIMA).numVoci;
        Assert.assertEquals(ottenutoIntero, previstoIntero);

        previstoIntero = 272;
        ottenutoIntero = appContext.getBean(AQueryCatInfo.class, TITOLO_CAT_PICCOLA).numVoci;
        Assert.assertEquals(ottenutoIntero, previstoIntero);

        previstoIntero = 1178;
        ottenutoIntero = appContext.getBean(AQueryCatInfo.class, TITOLO_CAT_MEDIA).numVoci;
        Assert.assertEquals(ottenutoIntero, previstoIntero);

        previstoIntero = 371000;
        ottenutoIntero = appContext.getBean(AQueryCatInfo.class, TITOLO_CAT_GRANDE).numVoci;
        Assert.assertEquals(Math.min(previstoIntero, ottenutoIntero), previstoIntero);

    }// end of method


    @Test
    public void queryCat() {
        previstoIntero = 13;
        ottenutoList = appContext.getBean(AQueryCatPagine.class, TITOLO_CAT_MINIMA).lista;
        Assert.assertNotNull(ottenutoList);
        Assert.assertEquals(ottenutoList.size(), previstoIntero);

        previstoIntero = 2;
        ottenutoList = appContext.getBean(AQueryCatCategorie.class, TITOLO_CAT_MINIMA).lista;
        Assert.assertNotNull(ottenutoList);
        Assert.assertEquals(ottenutoList.size(), previstoIntero);

        previstoIntero = 15;
        ottenutoList = appContext.getBean(AQueryCatAll.class, TITOLO_CAT_MINIMA).lista;
        Assert.assertNotNull(ottenutoList);
        Assert.assertEquals(ottenutoList.size(), previstoIntero);

        previstoIntero = 272;
        ottenutoList = appContext.getBean(AQueryCatPagine.class, TITOLO_CAT_PICCOLA).lista;
        Assert.assertNotNull(ottenutoList);
        Assert.assertEquals(ottenutoList.size(), previstoIntero);

        previstoIntero = 1177;
        ottenutoList = appContext.getBean(AQueryCatPagine.class, TITOLO_CAT_MEDIA).lista;
        Assert.assertNotNull(ottenutoList);
        Assert.assertEquals(ottenutoList.size(), previstoIntero);

//        previstoIntero = 371000;
//        ottenutoList = appContext.getBean(AQueryCatPagine.class, TITOLO_CAT_GRANDE).lista;
//        Assert.assertNotNull(ottenutoList);
//        Assert.assertEquals(Math.min(previstoIntero, ottenutoList.size()), previstoIntero);
    }// end of method

}// end of class
