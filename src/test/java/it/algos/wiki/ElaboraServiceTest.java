package it.algos.wiki;

import it.algos.test.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.mongodb.core.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 31-lug-2021
 * Time: 17:09
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("ElaboraServiceTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElaboraServiceTest extends ATest {


    protected Giorno previstoGiorno;

    protected Giorno ottenutoGiorno;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    ElaboraService service;

    @Autowired
    public MongoTemplate mongoOp;

    @InjectMocks
    GiornoService giornoService;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

//        MockitoAnnotations.initMocks(this);
//        MockitoAnnotations.initMocks(service);
//        Assertions.assertNotNull(service);
//        service.text = text;
//        service.giornoService = giornoService;
//
//        MockitoAnnotations.initMocks(giornoService);
//        Assertions.assertNotNull(giornoService);
//        giornoService.text = text;
//        giornoService.array = array;
//        giornoService.reflection = reflection;
//        giornoService.annotation = annotation;
//        giornoService.wikiApi = wikiApi;
//        giornoService.date = date;
//        giornoService.logger = logger;
//        giornoService.mongo = mongo;
//
////        MockitoAnnotations.initMocks(mongoOp);
//        Assertions.assertNotNull(mongoOp);
//        mongo.mongoOp = mongoOp;
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
    @DisplayName("fixGiornoLink")
    void fixGiornoLink() {
        previstoGiorno = null;

        sorgente = "testo errato";
        ottenutoGiorno = service.fixGiornoLink(sorgente);
        assertNull(ottenutoGiorno);

        sorgente = "31 febbraio";
        ottenutoGiorno = service.fixGiornoLink(sorgente);
        assertNull(ottenutoGiorno);

        sorgente = "4 termidoro";
        ottenutoGiorno = service.fixGiornoLink(sorgente);
        assertNull(ottenutoGiorno);

        sorgente = "17 marzo";
        previstoGiorno=giornoService.findById("17marzo");
        ottenutoGiorno = service.fixGiornoLink(sorgente);
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno,ottenutoGiorno);
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