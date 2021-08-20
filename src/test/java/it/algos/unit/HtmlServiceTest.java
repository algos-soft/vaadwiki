package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 07-mag-2021
 * Time: 18:57
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("HtmlService - Gestione dei testi html.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HtmlServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private HtmlService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = htmlService;
    }

    @Test
    @Order(1)
    @DisplayName("1 - getNumTag")
    void getNumTag() {
        sorgente = "Forse domani ]] me ne vado {{ davvero [[ e forse { no { mercoledì";
        previstoIntero = 0;

        ottenutoIntero = service.getNumTag(VUOTA, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = "]";
        previstoIntero = 2;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = "]]";
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = GRAFFA_INI;
        previstoIntero = 4;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);

        sorgente2 = DOPPIE_GRAFFE_INI;
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(sorgente, sorgente2);
        assertEquals(ottenutoIntero, previstoIntero);
    }

    @Test
    @Order(2)
    @DisplayName("2 - isPariTag")
    void isPariTag() {
        sorgente = "Forse domani ]] me ne [ vado {{ davvero [[ e [forse } no } mercoledì}}";
        previstoBooleano = false;

        ottenutoBooleano = service.isPariTag(VUOTA, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = DOPPIE_GRAFFE_END;
        sorgente3 = DOPPIE_GRAFFE_INI;
        previstoBooleano = true;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = GRAFFA_END;
        sorgente3 = GRAFFA_INI;
        previstoBooleano = false;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = DOPPIE_QUADRE_END;
        sorgente3 = DOPPIE_QUADRE_INI;
        previstoBooleano = true;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);

        sorgente2 = QUADRA_END;
        sorgente3 = QUADRA_INI;
        previstoBooleano = false;
        ottenutoBooleano = service.isPariTag(sorgente, sorgente2, sorgente3);
        assertEquals(ottenutoBooleano, previstoBooleano);
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