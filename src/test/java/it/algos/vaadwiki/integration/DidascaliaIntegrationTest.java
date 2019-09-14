package it.algos.vaadwiki.integration;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.didascalia.Didascalia;
import it.algos.vaadwiki.didascalia.DidascaliaService;
import it.algos.vaadwiki.didascalia.EADidascalia;
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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sun, 09-Jun-2019
 * Time: 13:23
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
public class DidascaliaIntegrationTest extends ATest {


    //    private static String NOME_BIO = "Ron Clarke";
    private static String NOME_BIO = "Giovanni di Pacheco";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ApplicationContext appContext;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ATextService text;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private BioService bioService;

    private Bio bio;

    private Didascalia didascalia;

    @Autowired
    public MongoOperations mongoOp;

    @Autowired
    public MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
        Assert.assertNotNull(appContext);
        Assert.assertNotNull(didascaliaService);
        Assert.assertNotNull(text);
        Assert.assertNotNull(bioService);

        bio = bioService.findByKeyUnica(NOME_BIO);
        Assert.assertNotNull(bio);
    }// end of method


    @Test
    public void metodoPerOrdinareTuttiTest() {
        type();
        giornoNato();
//        giornoMorto();
//        annoNato();
//        annoMorto();
        liste();
//        biografie();
    }// end of single test




    public void type() {
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + NOME_BIO);
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseCon(bio, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            }// end of if cycle
        }// end of for cycle
        System.out.println("*************");
        System.out.println("Senza chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseSenza(bio, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            }// end of if cycle
        }// end of for cycle
        System.out.println("*************");
    }// end of single test


    public void giornoNato() {
        previsto = "[[1937]] - [[Ron Clarke]], mezzofondista e politico australiano († [[2015]])";
        didascalia = didascaliaService.getDidascaliaGiornoNato(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getGiornoNatoCon(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        previsto = "[[Ron Clarke]], mezzofondista e politico australiano († [[2015]])";
        didascalia = didascaliaService.getDidascaliaGiornoNato(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getGiornoNatoSenza(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }// end of method


    public void giornoMorto() {
        previsto = "[[2015]] - [[Ron Clarke]], mezzofondista e politico australiano (n. [[1937]])";
        didascalia = didascaliaService.getDidascaliaGiornoMorto(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getGiornoMortoCon(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        previsto = "[[Ron Clarke]], mezzofondista e politico australiano (n. [[1937]])";
        didascalia = didascaliaService.getDidascaliaGiornoMorto(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getGiornoMortoSenza(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }// end of method


    public void annoNato() {
        previsto = "[[21 febbraio]] - [[Ron Clarke]], mezzofondista e politico australiano († [[2015]])";
        didascalia = didascaliaService.getDidascaliaAnnoNato(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getAnnoNatoCon(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        previsto = "[[Ron Clarke]], mezzofondista e politico australiano († [[2015]])";
        didascalia = didascaliaService.getDidascaliaAnnoNato(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getAnnoNatoSenza(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }// end of method


    public void annoMorto() {
        previsto = "[[17 giugno]] - [[Ron Clarke]], mezzofondista e politico australiano (n. [[1937]])";
        didascalia = didascaliaService.getDidascaliaAnnoMorto(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getAnnoMortoCon(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        previsto = "[[Ron Clarke]], mezzofondista e politico australiano (n. [[1937]])";
        didascalia = didascaliaService.getDidascaliaAnnoMorto(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getAnnoMortoSenza(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }// end of method


    public void liste() {
//        previsto = "[[Ron Clarke]], mezzofondista e politico australiano ([[Melbourne]], n.[[1937]] - [[Gold Coast]], †[[2015]])";
        previsto = "[[Giovanni di Pacheco]], nobile e politico spagnolo ([[Belmonte (Spagna)|Belmonte]], n.[[1419]] - [[Santa Cruz de la Sierra (Spagna)|Santa Cruz de la Sierra]], †[[1474]])";
        didascalia = didascaliaService.getDidascaliaListe(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoCon);

        ottenuto = didascaliaService.getListeCon(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        ottenuto = didascaliaService.getListeSenza(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

    }// end of method


    public void biografie() {
        previsto = "[[Ron Clarke]] ([[Melbourne]], [[21 febbraio]][[1937]] - [[Gold Coast]], [[17 giugno]][[2015]]), mezzofondista e politico australiano";
        didascalia = didascaliaService.getDidascaliaBiografie(bio);
        Assert.assertNotNull(didascalia);
        assertEquals(previsto, didascalia.testoCon);

        ottenuto = didascaliaService.getBiografieCon(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        ottenuto = didascaliaService.getBiografieSenza(bio);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }// end of method

}// end of class
