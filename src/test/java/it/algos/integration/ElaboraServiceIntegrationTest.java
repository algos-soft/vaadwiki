package it.algos.integration;

import it.algos.test.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.test.context.junit.jupiter.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 31-lug-2021
 * Time: 18:32
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Elabora Service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ElaboraServiceIntegrationTest extends ATest {

    protected Giorno previstoGiorno;

    protected Giorno ottenutoGiorno;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    ElaboraService service;

    @Autowired
    public MongoOperations mongoOp;

    @InjectMocks
    GiornoService giornoService;


    /**
     * Qui passa una volta sola <br>
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
//        giornoService.keyPropertyName="giorno";
//
//        MockitoAnnotations.initMocks(mongoOp);
//        Assertions.assertNotNull(mongoOp);
//        mongo.mongoOp = mongoOp;
    }


    @BeforeEach
    void setUpEach() {
    }


    @Test
    @Order(1)
    @DisplayName("fixGiornoLink")
    void fixGiornoLink() {
        previstoGiorno = null;

        sorgente = "testo errato";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }

        assertNull(ottenutoGiorno);

        sorgente = "31 febbraio";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(ottenutoGiorno);

        sorgente = "4 termidoro";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(ottenutoGiorno);

        sorgente = "17 marzo";
        try {
            previstoGiorno = giornoService.findById("17 marzo");
        } catch (AlgosException unErrore) {
        }
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);

        sorgente = "5 Agosto";
        try {
            previstoGiorno = giornoService.findByKey("5 agosto");
        } catch (AlgosException unErrore) {
        }
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);

        sorgente = "3ottobre";
        try {
            previstoGiorno = giornoService.findByKey("3 ottobre");
        } catch (AlgosException unErrore) {
        }
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);

        sorgente = "24  maggio";
        try {
            previstoGiorno = giornoService.findByKey("24 maggio");
        } catch (AlgosException unErrore) {
        }
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);

        sorgente = " 8   gennaio ";
        try {
            previstoGiorno = giornoService.findByKey("8 gennaio");
        } catch (AlgosException unErrore) {
        }
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);

        sorgente = "?";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(ottenutoGiorno);

        sorgente = "11 luglio <ref>Marcello";
        try {
            previstoGiorno = giornoService.findByKey("11 luglio");
        } catch (AlgosException unErrore) {
        }
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(ottenutoGiorno);
        assertEquals(previstoGiorno, ottenutoGiorno);

        sorgente = "21 dicembre circa";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(ottenutoGiorno);

        sorgente = "5 maggio?";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(ottenutoGiorno);

        sorgente = "settembre";
        try {
            ottenutoGiorno = service.fixGiornoLink(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(ottenutoGiorno);
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
