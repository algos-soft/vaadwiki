package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 17-ago-2021
 * Time: 19:49
 * <p>
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
     * Gia 'costruita' nella superclasse <br>
     */
    private GsonService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = gSonService;
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
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
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.estraeGraffa(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        previsto = "\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"}";
        ottenuto = service.estraeGraffa(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - eliminaGraffa")
    void eliminaGraffa() {
        sorgente = VUOTA;
        ottenuto = service.eliminaGraffa(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        previsto = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.eliminaGraffa(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        previsto = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
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
        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        previstoIntero = 1;
        previstoArray = arrayService.creaArraySingolo(sorgente);
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

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        previstoIntero = 2;
        previstoArray = new ArrayList<>();
        previstoArray.add("{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}");
        previstoArray.add("\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"}");
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

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"anno\":{\"_id\":\"1876\",\"collectionName\":\"anno\"},\"_class\":\"giorno\"}";
        previstoIntero = 3;
        previstoArray = new ArrayList<>();
        previstoArray.add("{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}");
        previstoArray.add("\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"}");
        previstoArray.add("\"anno\":{\"_id\":\"1876\",\"collectionName\":\"anno\"}");
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
        String mongoToString ;
        String entityToString ;
        AEntity entityFromMongoString;
        AEntity entityFromEntityString;

        sorgente = "piazza";
        clazz = Via.class;
        entityBean = mongoService.findById(clazz,sorgente);
        assertNotNull(entityBean);

        mongoToString = service.mongoToString(clazz, sorgente);
        entityToString = service.entityToString(entityBean);
        System.out.println(String.format("mongoToString: %s", mongoToString));
        System.out.println(String.format("entityToString: %s", entityToString));
        entityFromMongoString= service.stringToEntity(clazz,mongoToString);
        entityFromEntityString= service.stringToEntity(clazz,entityToString);
        assertNotNull(entityFromMongoString);
        assertNotNull(entityFromEntityString);

        //        System.out.println(VUOTA);
//        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
//        System.out.println(entityBean);

        sorgente = "5gennaio";
        clazz = Giorno.class;
        ottenuto = service.mongoToString(clazz, sorgente);
        entityBean = service.stringToEntity(clazz, ottenuto);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "1786";
        clazz = Anno.class;
        ottenuto = service.mongoToString(clazz, sorgente);
        entityBean = service.stringToEntity(clazz, ottenuto);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "5gennaio";
        clazz = Giorno.class;
        entityBean = mongoService.findById(clazz, sorgente);
        ottenuto = service.entityToString(entityBean);

        entityBean = service.stringToEntity(clazz, ottenuto);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);
    }


    @Test
    @Order(8)
    @DisplayName("8 - creazione di un entityBean da keyId")
    void creaId() {
        System.out.println("8 - creazione di un entityBean da keyId");

        sorgente = "piazza";
        clazz = Via.class;
        entityBean = service.creaId(clazz, sorgente);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "5gennaio";
        clazz = Giorno.class;
        entityBean = service.creaId(clazz, sorgente);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "1786";
        clazz = Anno.class;
        entityBean = service.creaId(clazz, sorgente);
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);
    }


    @Test
    @Order(9)
    @DisplayName("9 - creazione di un testo jSon da mongoDb")
    void legge() {
        System.out.println("9 - creazione di un testo jSon da mongoDb");
        String jsonInString;

        sorgente = "campiello";
        clazz = Via.class;
        previsto = "{\"_id\":\"campiello\",\"ordine\":20,\"nome\":\"campiello\",\"reset\":true,\"_class\":\"via\"}";
        ottenuto = service.mongoToString(clazz, sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "8marzo";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"8marzo\",\"ordine\":68,\"titolo\":\"8 marzo\",\"mese\":{\"id\":\"marzo\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.mongoToString(clazz, sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "23 ottobre";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"23ottobre\",\"ordine\":297,\"titolo\":\"23 ottobre\",\"mese\":{\"id\":\"ottobre\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.mongoToString(clazz, sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }


    @Test
    @Order(10)
    @DisplayName("10 - Java object to JSON string")
    void writeValueAsString() {
        System.out.println("10 - Java object to JSON string");

        sorgente = "piazzale";
        clazz = Via.class;
        previsto = "{\"_id\":\"piazzale\",\"ordine\":6,\"nome\":\"piazzale\",\"reset\":true,\"_class\":\"via\"}";
        entityBean = mongoService.findById(clazz, sorgente);
        ottenuto = service.entityToString(entityBean);
        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "8marzo";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"8marzo\",\"ordine\":68,\"titolo\":\"8 marzo\",\"mese\":{\"id\":\"marzo\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        entityBean = mongoService.findById(clazz, sorgente);
        ottenuto = service.entityToString(entityBean);
        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "23 ottobre";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"23ottobre\",\"ordine\":297,\"titolo\":\"23 ottobre\",\"mese\":{\"id\":\"ottobre\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        entityBean = mongoService.findByKey(clazz, sorgente);
        ottenuto = service.entityToString(entityBean);
        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(11)
    @DisplayName("11 - prove")
    void finale() {
        sorgente = "quartiere";
        clazz = Via.class;

        System.out.println("11 - From mongoDB to string passando da Doc");
        ottenuto = service.mongoToString(clazz, sorgente);
        System.out.println(ottenuto);
        System.out.println("11 - Crea una entity col jsonString appena ottenuto");
        entityBean = service.stringToEntity(clazz, ottenuto);
        System.out.println(entityBean);

        System.out.println(VUOTA);
        System.out.println("11 - From entityBean to string ");
        entityBean = mongoService.findById(clazz, sorgente);
        ottenuto = service.entityToString(entityBean);
        System.out.println(ottenuto);
        System.out.println("11 - Crea una entity col jsonString appena ottenuto");
        entityBean = service.stringToEntity(clazz, ottenuto);
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