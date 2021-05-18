package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.wizard.scripts.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 17-mag-2021
 * Time: 11:37
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("WizServiceTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WizServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    WizService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        service.text = text;
        service.array = array;
        service.file = file;
        service.date = date;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("1 - fixOldDate - vuote")
    void fixOldDate() {
        sorgente = "Prova di squadra";
        sorgente2 = "Prova di squadra modificata";
        previsto = sorgente2;

        ottenuto = service.fixOldDate(VUOTA, VUOTA);
        assertTrue(text.isEmpty(ottenuto));

        ottenuto = service.fixOldDate(sorgente, VUOTA);
        assertTrue(text.isEmpty(ottenuto));

        ottenuto = service.fixOldDate(VUOTA, sorgente2);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(2)
    @DisplayName("2 - fixOldDate due")
    void fixOldDate2() {
        sorgente = file.leggeFile("/Users/gac/Documents/IdeaProjects/operativi/vaadwiki/src/main/java/it/algos/vaadflow14/backend/packages/anagrafica/via/Via.java");
        sorgente2 = service.leggeFile("entity");
        sorgente3 = service.elaboraFileCreatoDaSource(sorgente2);

        ottenuto = service.fixOldDate(sorgente, sorgente3);
        assertTrue(text.isValid(ottenuto));
        System.out.println(ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - fixOldDate tre")
    void fixOldDate3() {
        sorgente = service.leggeFile("entity");
        sorgente2 = service.elaboraFileCreatoDaSource(sorgente);

        ottenuto = service.fixOldDate(sorgente, sorgente2);
        assertTrue(text.isValid(ottenuto));
        System.out.println(ottenuto);
    }


    @Test
    @Order(4)
    @DisplayName("4 - fixOldDate quattro")
    void fixOldDate4() {
        sorgente = "/**\n" +
                " * Project: vaadwiki <br>\n" +
                " * Created by Algos <br>\n" +
                " * User: gac <br>\n" +
                " * First time: lun, 17-mag-2021 alle 10:12 <br>\n" +
                " * Last doc revision: lun, 17-mag-2021 alle 21:42 <br>\n" +
                " * <p>\n" +
                " * Classe (obbligatoria) di un package <br>\n" +
                " * Estende la entity astratta AEntity che contiene la key property ObjectId <br>\n" +
                " * Le properties sono PUBLIC per poter usare la Reflection <br>\n" +
                " * Unica classe obbligatoria per un package. <br>\n" +
                " * Le altre servono solo se si vuole qualcosa in più dello standard minimo. <br>\n" +
                " * <p>";

        previsto = "/**\n" +
                " * Project: vaadwiki <br>\n" +
                " * Created by Algos <br>\n" +
                " * User: gac <br>\n" +
                " * First time: lun, 17-mag-2021 alle 10:12 <br>\n" +
                " * Last doc revision: mar, 18-mag-2021 alle 21:42 <br>\n" +
                " * <p>\n" +
                " * Classe (obbligatoria) di un package <br>\n" +
                " * Estende la entity astratta AEntity che contiene la key property ObjectId <br>\n" +
                " * Le properties sono PUBLIC per poter usare la Reflection <br>\n" +
                " * Unica classe obbligatoria per un package. <br>\n" +
                " * Le altre servono solo se si vuole qualcosa in più dello standard minimo. <br>\n" +
                " * <p>";

        ottenuto= service.fixOldDate(sorgente,previsto);
        assertEquals(previsto,ottenuto);
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterEach
    void tearDownAll() {
    }

}