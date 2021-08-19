package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 17-ago-2021
 * Time: 19:49
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Gson service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GsonServiceTest extends ATest {

    private static final String DATA_BASE_NAME = "vaadflow14";

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    GsonService service;


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
        service.logger = logger;
        service.reflection = reflection;
        service.annotation = annotation;

        service.fixProperties(DATA_BASE_NAME);
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
    @DisplayName("1 - countGraffe")
    void countGraffe() {
        sorgente = "Forse domani ]] me ne vado [[ davvero [[ e forse";
        previstoIntero = 0;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse domani ]] me ne vado { davvero [[ e forse { no { mercoledì";
        previstoIntero = -1;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse domani ]] me ne [ vado { davvero [[ e [forse } no } mercoledì}}";
        previstoIntero = -1;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse {domani ]] me ne [ vado davvero [[ e [forse no } mercoledì";
        previstoIntero = 1;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse {domani ]] me ne [ vado { davvero [[ e [forse } no } mercoledì";
        previstoIntero = 2;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }

    @Test
    @Order(2)
    @DisplayName("2 - estraeGraffa")
    void estraeGraffa() {
        sorgente = VUOTA;
        ottenuto = service.estraeGraffa(sorgente);
        assertTrue(text.isEmpty(ottenuto));

        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}";
        ottenuto = service.estraeGraffa(sorgente);
        assertTrue(text.isEmpty(ottenuto));

        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"class\":\"giorno\"}";
        previsto = "\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},";
        ottenuto = service.estraeGraffa(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - eliminaGraffa")
    void eliminaGraffa() {
        sorgente = VUOTA;
        ottenuto = service.eliminaGraffa(sorgente);
        assertTrue(text.isEmpty(ottenuto));

        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}";
        previsto = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}";
        ottenuto = service.eliminaGraffa(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"class\":\"giorno\"}";
        previsto = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}";
        ottenuto = service.eliminaGraffa(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(4)
    @DisplayName("4 - estraeGraffe (nessuna)")
    void estraeGraffe() {
        System.out.println("4 - estraeGraffe (nessuna)");

        System.out.println(VUOTA);
        System.out.println("sorgente nullo, array nullo");
        sorgente = VUOTA;
        ottenutoArray = service.estraeGraffe(sorgente);
        assertNull(ottenutoArray);

        System.out.println(VUOTA);
        System.out.println("array di un solo elemento col testo originale completo");
        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}";
        previstoIntero = 1;
        previstoArray = array.creaArraySingolo(sorgente);
        ottenutoArray = service.estraeGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoArray.size());
        assertEquals(previstoArray, ottenutoArray);
        print(ottenutoArray);
    }


    @Test
    @Order(5)
    @DisplayName("5 - estraeGraffe (una)")
    void estraeGraffe1() {
        System.out.println("5 - estraeGraffe (una)");
        System.out.println(VUOTA);
        System.out.println("array di due elementi col testo senza graffe nel primo e il contenuto interno della graffa nel secondo");

        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"class\":\"giorno\"}";
        previstoIntero = 2;
        previstoArray = new ArrayList<>();
        previstoArray.add("{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}");
        previstoArray.add("\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"}");
        ottenutoArray = service.estraeGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoArray.size());
        assertEquals(previstoArray, ottenutoArray);
        print(ottenutoArray);
    }

    @Test
    @Order(6)
    @DisplayName("6 - estraeGraffe (due)")
    void estraeGraffe2() {
        System.out.println("6 - estraeGraffe (due)");
        System.out.println(VUOTA);
        System.out.println("array di tre elementi col testo senza graffe nel primo e i contenuti interni delle due graffe nel secondo e nel terzo");

        sorgente = "{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"anno\":{\"id\":\"1876\",\"collectionName\":\"anno\"},\"class\":\"giorno\"}";
        previstoIntero = 3;
        previstoArray = new ArrayList<>();
        previstoArray.add("{\"id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"class\":\"giorno\"}");
        previstoArray.add("\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"}");
        previstoArray.add("\"anno\":{\"id\":\"1876\",\"collectionName\":\"anno\"}");
        ottenutoArray = service.estraeGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoArray.size());
        assertEquals(previstoArray, ottenutoArray);
        print(ottenutoArray);
    }

    @Test
    @Order(7)
    @DisplayName("7 - creazione di un entityBean da un testo jSon")
    void crea() {
        System.out.println("7 - creazione di un entityBean da un testo jSon");

        sorgente = "piazza";
        clazz = Via.class;
        entityBean = service.crea(clazz, sorgente);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "5gennaio";
        clazz = Giorno.class;
        entityBean = service.crea(clazz, sorgente);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "1786";
        clazz = Anno.class;
        entityBean = service.crea(clazz, sorgente);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);
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