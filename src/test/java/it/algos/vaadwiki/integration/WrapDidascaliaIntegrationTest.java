package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.didascalia.Didascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 25-gen-2020
 * Time: 10:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WrapDidascaliaIntegrationTest extends ATest {


    private static String NOME_BIO_UNO = "Angelo Fumagalli";

    private Bio bioUno;

    private Didascalia didascalia;

    private WrapDidascalia wrap;

    private String chiaveParagrafo;

    private String chiaveAttivita;

    private String chiaveLista;

    private String chiavePagina;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private BioService bioService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ApplicationContext appContext;


    @Before
    public void setUp() {
        Assert.assertNotNull(bioService);
        Assert.assertNotNull(appContext);

        bioUno = bioService.findByKeyUnica(NOME_BIO_UNO);
        Assert.assertNotNull(bioUno);
    }// end of method


    @Test
    public void listaNomi() {
        wrap = appContext.getBean(WrapDidascalia.class, bioUno, EADidascalia.listaNomi);

        previsto = "abate";
        chiaveAttivita = wrap.getChiaveAttivita();
        assertEquals(previsto, chiaveAttivita);

        previsto = "Abati";
        chiaveParagrafo = wrap.getChiaveParagrafo();
        assertEquals(previsto, chiaveParagrafo);

//        previsto = "abati e badesse";
//        chiaveLista = wrap.getChiaveLista();
//        assertEquals(previsto, chiaveLista);

        previsto = "abate";
        chiavePagina = wrap.getChiavePagina();
        assertEquals(previsto, chiavePagina);
    }// end of method

}// end of class
