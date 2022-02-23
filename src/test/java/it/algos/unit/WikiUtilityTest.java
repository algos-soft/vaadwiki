package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadwiki.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 20-feb-2022
 * Time: 10:34
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test di controllo per alcune utility specifiche di VaadWiki")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiUtilityTest extends ATest {

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    WikiUtility service;


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
    @DisplayName("1 - Small numero")
    void smallNumero() {
        sorgenteIntero = 0;
        ottenuto = service.smallNumero(sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%d%s%s", sorgenteIntero, FlowCost.FORWARD, ottenuto));

        sorgenteIntero = 4;
        ottenuto = service.smallNumero(sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%d%s%s", sorgenteIntero, FlowCost.FORWARD, ottenuto));

        sorgenteIntero = 70;
        ottenuto = service.smallNumero(sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%d%s%s", sorgenteIntero, FlowCost.FORWARD, ottenuto));

        sorgenteIntero = 120;
        ottenuto = service.smallNumero(sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%d%s%s", sorgenteIntero, FlowCost.FORWARD, ottenuto));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Set paragrafo base")
    void setParagrafo() {
        sorgente = "Britannici";
        ottenuto = service.setParagrafo(sorgente);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s%s%s", sorgente, FlowCost.FORWARD, ottenuto));

        sorgente = "[[Francesi]]";
        ottenuto = service.setParagrafo(sorgente);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s%s%s", sorgente, FlowCost.FORWARD, ottenuto));

        sorgente = "[[Progetto:Biografie/Nazionalità/Tedeschi|Tedeschi]]";
        ottenuto = service.setParagrafo(sorgente);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s%s%s", sorgente, FlowCost.FORWARD, ottenuto));
    }

    @Test
    @Order(3)
    @DisplayName("3 - Set paragrafo con numero")
    void setParagrafoNumero() {
        sorgente = "Britannici";
        sorgenteIntero = 0;
        ottenuto = service.setParagrafo(sorgente,sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s,%s%s%s", sorgente, sorgenteIntero,FlowCost.FORWARD, ottenuto));

        sorgente = "[[Francesi]]";
        sorgenteIntero = 7;
        ottenuto = service.setParagrafo(sorgente,sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s,%s%s%s", sorgente, sorgenteIntero,FlowCost.FORWARD, ottenuto));

        sorgente = "[[Progetto:Biografie/Nazionalità/Tedeschi|Tedeschi]]";
        sorgenteIntero = 70;
        ottenuto = service.setParagrafo(sorgente,sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s,%s%s%s", sorgente, sorgenteIntero,FlowCost.FORWARD, ottenuto));

        sorgente = "[[Progetto:Biografie/Nazionalità/Tedeschi|Tedeschi]]";
        sorgenteIntero = 120;
        ottenuto = service.setParagrafo(sorgente,sorgenteIntero);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("%s,%s%s%s", sorgente, sorgenteIntero,FlowCost.FORWARD, ottenuto));

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
    @AfterAll
    void tearDownAll() {
    }

}