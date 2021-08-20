package it.algos.wiki;

import it.algos.test.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 11:06
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test sul service base ABioService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BioServiceTest extends ATest {

    public static final String PAGINA_PIOZZANO = "Piozzano";

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    BioUtility service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        assertNotNull(service);
//        service.text = text;
//        service.array = array;
//        service.wikiApi = wikiApi;
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

//    @Test
//    @Order(1)
//    @DisplayName("1 - leggeTmpl")
//    void leggeTmpl() {
//
//        sorgente = "Guido Rossi";
//        ottenuto = service.leggeTmpl(sorgente);
//        assertTrue(text.isValid(ottenuto));
//        assertTrue(ottenuto.startsWith(previsto));
//        System.out.println(VUOTA);
//        System.out.println(VUOTA);
//        System.out.println("1 - Legge un template bio");
//        System.out.println(VUOTA);
//        System.out.println(ottenuto);
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("2 - leggePagina")
//    void leggePagina() {
//        Pagina pagina = null;
//        sorgente = PAGINA_PIOZZANO;
//        sorgente2 = "xyz";
//
//        pagina = service.leggePagina(VUOTA);
//        assertNull(pagina);
//
//        pagina = service.leggePagina(sorgente2);
//        assertNull(pagina);
//
//        pagina = service.leggePagina(sorgente);
//        assertNotNull(pagina);
//    }


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