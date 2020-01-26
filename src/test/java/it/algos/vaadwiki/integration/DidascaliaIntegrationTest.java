package it.algos.vaadwiki.integration;

import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.didascalia.Didascalia;
import it.algos.vaadwiki.didascalia.DidascaliaService;
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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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


    private static String NOME_BIO_UNO = "Ron Clarke";

    private static String NOME_BIO_DUE = "Giovanni di Pacheco";

    private static String NOME_BIO_TRE = "Sonia Todd";


    @Autowired
    public MongoOperations mongoOp;

    @Autowired
    public MongoTemplate mongoTemplate;

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

    private Bio bioUno;

    private Bio bioDue;

    private Bio bioTre;

    private Didascalia didascalia;


    @Before
    public void setUp() {
        Assert.assertNotNull(appContext);
        Assert.assertNotNull(didascaliaService);
        Assert.assertNotNull(text);
        Assert.assertNotNull(bioService);

        bioUno = bioService.findByKeyUnica(NOME_BIO_UNO);
        Assert.assertNotNull(bioUno);
        bioDue = bioService.findByKeyUnica(NOME_BIO_DUE);
        Assert.assertNotNull(bioDue);
        bioTre = bioService.findByKeyUnica(NOME_BIO_TRE);
        Assert.assertNotNull(bioTre);
    }// end of method


    public void metodoPerOrdinareTuttiTest() {
    }// end of single test


    @Test
    public void tipiPossibiliDidascalieClarke() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + NOME_BIO_UNO);
        System.out.println("*************");
        System.out.println("Senza chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseSenza(bioUno, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            } else {
                System.out.println(type.name() + ": Manca");
            }// end of if/else cycle
        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseCon(bioUno, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            } else {
                System.out.println(type.name() + ": Manca");
            }// end of if/else cycle
        }// end of for cycle
        System.out.println("*************");
    }// end of single test


    @Test
    public void tipiPossibiliDidascaliePacheco() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + NOME_BIO_DUE);
        System.out.println("*************");
        System.out.println("Senza chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseSenza(bioDue, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            } else {
                System.out.println(type.name() + ": Manca");
            }// end of if/else cycle
        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseCon(bioDue, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            } else {
                System.out.println(type.name() + ": Manca");
            }// end of if/else cycle
        }// end of for cycle
        System.out.println("*************");
    }// end of single test


    @Test
    public void tipiPossibiliDidascalieTodd() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + NOME_BIO_TRE);
        System.out.println("*************");
        System.out.println("Senza chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseSenza(bioTre, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            } else {
                System.out.println(type.name() + ": Manca");
            }// end of if/else cycle
        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        for (EADidascalia type : EADidascalia.values()) {
            ottenuto = didascaliaService.getBaseCon(bioTre, type);
            if (text.isValid(ottenuto)) {
                System.out.println(type.name() + ": " + ottenuto);
            } else {
                System.out.println(type.name() + ": Manca");
            }// end of if/else cycle
        }// end of for cycle
        System.out.println("*************");
    }// end of single test


    @Test
    public void giornoNato() {
        System.out.println("*************");
        System.out.println("Didascalia per giornoNato (21 febbraio) di " + NOME_BIO_UNO + " : " + didascaliaService.getBiografie(bioUno));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaGiornoNato(bioUno);
        Assert.assertNotNull(didascalia);

        previsto = "[[Ron Clarke]], mezzofondista, maratoneta e politico australiano († [[2015]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getGiornoNatoSenza(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[1937]] - [[Ron Clarke]], mezzofondista, maratoneta e politico australiano († [[2015]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getGiornoNatoCon(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave annoNato");
        System.out.println(ottenuto);
        System.out.println("*************");

        System.out.println("");

        System.out.println("*************");
        System.out.println("Didascalia per giornoNato (manca) di " + NOME_BIO_DUE + " : " + didascaliaService.getBiografie(bioDue));
        didascalia = didascaliaService.getDidascaliaGiornoNato(bioDue);
        Assert.assertNotNull(didascalia);

        previsto = "";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getGiornoNatoSenza(bioDue);
        assertTrue(text.isEmpty(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("*************");
        System.out.println("Manca il giorno di nascita");
        System.out.println("*************");
    }// end of method


    @Test
    public void giornoMorto() {
        System.out.println("*************");
        System.out.println("Didascalia per giornoMorto (17 giugno) di " + NOME_BIO_UNO + " : " + didascaliaService.getBiografie(bioUno));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaGiornoMorto(bioUno);
        Assert.assertNotNull(didascalia);

        previsto = "[[Ron Clarke]], mezzofondista, maratoneta e politico australiano (n. [[1937]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getGiornoMortoSenza(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[2015]] - [[Ron Clarke]], mezzofondista, maratoneta e politico australiano (n. [[1937]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getGiornoMortoCon(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave annoMorto");
        System.out.println(ottenuto);
        System.out.println("*************");

        System.out.println("");

        System.out.println("*************");
        System.out.println("Didascalia per giornoMorto (14 ottobre) di " + NOME_BIO_DUE + " : " + didascaliaService.getBiografie(bioDue));
        didascalia = didascaliaService.getDidascaliaGiornoMorto(bioDue);
        Assert.assertNotNull(didascalia);

        previsto = "[[Giovanni di Pacheco]], nobile e politico spagnolo (n. [[1419]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getGiornoMortoSenza(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[1474]] - [[Giovanni di Pacheco]], nobile e politico spagnolo (n. [[1419]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getGiornoMortoCon(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave annoMorto");
        System.out.println(ottenuto);
        System.out.println("*************");
    }// end of method


    @Test
    public void annoNato() {
        System.out.println("*************");
        System.out.println("Didascalia per annoNato (1937) di " + NOME_BIO_UNO + " : " + didascaliaService.getBiografie(bioUno));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaAnnoNato(bioUno);
        Assert.assertNotNull(didascalia);

        previsto = "[[Ron Clarke]], mezzofondista, maratoneta e politico australiano († [[2015]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getAnnoNatoSenza(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[21 febbraio]] - [[Ron Clarke]], mezzofondista, maratoneta e politico australiano († [[2015]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getAnnoNatoCon(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave giornoNato");
        System.out.println(ottenuto);
        System.out.println("*************");

        System.out.println("");

        System.out.println("*************");
        System.out.println("Didascalia per annoNato (1419) di " + NOME_BIO_DUE + " : " + didascaliaService.getBiografie(bioDue));
        didascalia = didascaliaService.getDidascaliaAnnoNato(bioDue);
        Assert.assertNotNull(didascalia);

        previsto = "[[Giovanni di Pacheco]], nobile e politico spagnolo († [[1474]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getAnnoNatoSenza(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[Giovanni di Pacheco]], nobile e politico spagnolo († [[1474]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getAnnoNatoCon(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave giornoNato");
        System.out.println(ottenuto);
        System.out.println("*************");
    }// end of method


    @Test
    public void annoMorto() {
        System.out.println("*************");
        System.out.println("Didascalia per annoMorto (2015) di " + NOME_BIO_UNO + " : " + didascaliaService.getBiografie(bioUno));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaAnnoMorto(bioUno);
        Assert.assertNotNull(didascalia);

        previsto = "[[Ron Clarke]], mezzofondista, maratoneta e politico australiano (n. [[1937]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getAnnoMortoSenza(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[17 giugno]] - [[Ron Clarke]], mezzofondista, maratoneta e politico australiano (n. [[1937]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getAnnoMortoCon(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave giornoMorto");
        System.out.println(ottenuto);
        System.out.println("*************");

        System.out.println("");

        System.out.println("*************");
        System.out.println("Didascalia per annoMorto (1474) di " + NOME_BIO_DUE + " : " + didascaliaService.getBiografie(bioDue));
        didascalia = didascaliaService.getDidascaliaAnnoMorto(bioDue);
        Assert.assertNotNull(didascalia);

        previsto = "[[Giovanni di Pacheco]], nobile e politico spagnolo (n. [[1419]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getAnnoMortoSenza(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "[[4 ottobre]] - [[Giovanni di Pacheco]], nobile e politico spagnolo (n. [[1419]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getAnnoMortoCon(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave giornoMorto");
        System.out.println(ottenuto);
        System.out.println("*************");
    }// end of method


    @Test
    public void listeNomiCognomi() {
        System.out.println("*************");
        System.out.println("Didascalia per liste nomi/cognomi di " + NOME_BIO_UNO + " : " + didascaliaService.getBiografie(bioUno));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaListe(bioUno);
        Assert.assertNotNull(didascalia);

        previsto = "[[Ron Clarke]], mezzofondista, maratoneta e politico australiano ([[Melbourne]], n.[[1937]] - [[Gold Coast]], †[[2015]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getListeSenza(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "mezzofondista - [[Ron Clarke]], mezzofondista, maratoneta e politico australiano ([[Melbourne]], n.[[1937]] - [[Gold Coast]], †[[2015]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getListeCon(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave attività");
        System.out.println(ottenuto);

        System.out.println("");

        System.out.println("*************");
        System.out.println("Didascalia per liste nomi/cognomi di " + NOME_BIO_DUE + " : " + didascaliaService.getBiografie(bioDue));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaListe(bioDue);
        Assert.assertNotNull(didascalia);

        previsto = "[[Giovanni di Pacheco]], nobile e politico spagnolo ([[Belmonte (Spagna)|Belmonte]], n.[[1419]] - [[Santa Cruz de la Sierra (Spagna)|Santa Cruz de la Sierra]], †[[1474]])";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getListeSenza(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Senza chiave");
        System.out.println(ottenuto);

        previsto = "nobile - [[Giovanni di Pacheco]], nobile e politico spagnolo ([[Belmonte (Spagna)|Belmonte]], n.[[1419]] - [[Santa Cruz de la Sierra (Spagna)|Santa Cruz de la Sierra]], †[[1474]])";
        assertEquals(previsto, didascalia.testoCon);
        ottenuto = didascaliaService.getListeCon(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println("Con chiave attività");
        System.out.println(ottenuto);
    }// end of method


    @Test
    public void biografieComplete() {
        System.out.println("*************");
        System.out.println("Didascalia per biografia completa di " + NOME_BIO_UNO + " : " + didascaliaService.getBiografie(bioUno));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaBiografie(bioUno);
        Assert.assertNotNull(didascalia);

        previsto = "[[Ron Clarke]] ([[Melbourne]], [[21 febbraio]][[1937]] - [[Gold Coast]], [[17 giugno]][[2015]]), mezzofondista, maratoneta e politico australiano";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getBiografie(bioUno);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        System.out.println("");

        System.out.println("*************");
        System.out.println("Didascalia per biografia completa di " + NOME_BIO_DUE + " : " + didascaliaService.getBiografie(bioDue));
        System.out.println("*************");
        didascalia = didascaliaService.getDidascaliaBiografie(bioDue);
        Assert.assertNotNull(didascalia);

        previsto = "[[Giovanni di Pacheco]] ([[Belmonte (Spagna)|Belmonte]], n.[[1419]] - [[Santa Cruz de la Sierra (Spagna)|Santa Cruz de la Sierra]], [[4 ottobre]][[1474]]), nobile e politico spagnolo";
        assertEquals(previsto, didascalia.testoSenza);
        ottenuto = didascaliaService.getBiografie(bioDue);
        assertTrue(text.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

    }// end of method



}// end of class
