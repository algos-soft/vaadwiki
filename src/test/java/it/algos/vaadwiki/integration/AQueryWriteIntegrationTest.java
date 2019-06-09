package it.algos.vaadwiki.integration;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.views.UtilityView;
import it.algos.wiki.web.AQueryWrite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Mon, 06-May-2019
 * Time: 20:22
 */


/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 04-May-2019
 * Time: 08:26
 * <p>
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
public class AQueryWriteIntegrationTest extends ATest {

    private final static String TITOLO = UtilityView.WIKI_TITLE_DEBUG;

    private final static String TESTO = "Prova 999";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected PreferenzaService pref;

    private AQueryWrite query;

    @Autowired
    private ApplicationContext appContext;


    @Before
    public void setUp() {
        Assert.assertNotNull(appContext);
        pref.saveValue(FlowCost.USA_DEBUG, true);
    }// end of method


    @Test
    public void urlRequest() {
        query = appContext.getBean(AQueryWrite.class, TITOLO, TESTO);
    }// end of method

}// end of class
