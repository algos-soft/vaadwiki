package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 20-ago-2021
 * Time: 07:02
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("AnnoService - Entity cronologica")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnoServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    private AnnoService service;

    private Anno anno;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        service.mongo = mongoService;
        service.text = textService;
        service.annotation = annotationService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        anno = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - findById (gson)")
    void findById() {
        System.out.println("1 - findById (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "946" + AESecolo.TAG_AC;
        try {
            anno = service.findById(sorgente);
            assertNotNull(anno);
            printAnnoID(sorgente, anno);
        } catch (Exception unErrore) {
            assertNull(anno);
            printAnnoID(sorgente, anno);
            System.out.println(unErrore);
        }

        sorgente = "946";
        anno = null;
        try {
            anno = service.findById(sorgente);
            assertNotNull(anno);
            printAnnoID(sorgente, anno);
        } catch (Exception unErrore) {
            assertNull(anno);
            printAnnoID(sorgente, anno);
            System.out.println(unErrore);
        }

        sorgente = "3946";
        anno = null;
        try {
            anno = service.findById(sorgente);
            assertNull(anno);
            printAnnoID(sorgente, anno);
        } catch (Exception unErrore) {
            assertNull(anno);
            printAnnoID(sorgente, anno);
            System.out.println(unErrore);
        }
    }


    @Test
    @Order(2)
    @DisplayName("2 - findByKey (gson)")
    void findByKey() {
        System.out.println("2 - findByKey (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "1946dc";
        try {
            anno = service.findByKey(sorgente);
            assertNotNull(anno);
            printAnnoKey(sorgente, anno);
        } catch (Exception unErrore) {
            assertNull(anno);
            printAnnoKey(sorgente, anno);
            System.out.println(unErrore);
        }

        sorgente = "1946";
        anno = null;
        try {
            anno = service.findByKey(sorgente);
            assertNotNull(anno);
            printAnnoKey(sorgente, anno);
        } catch (Exception unErrore) {
            assertNull(anno);
            printAnnoKey(sorgente, anno);
            System.out.println(unErrore);
        }

        sorgente = "1948";
        anno = null;
        try {
            anno = service.findByKey(sorgente);
            assertNotNull(anno);
            printAnnoKey(sorgente, anno);
        } catch (Exception unErrore) {
            assertNull(anno);
            printAnnoKey(sorgente, anno);
            System.out.println(unErrore);
        }
    }


    @Test
    @Order(3)
    @DisplayName("3 - findByProperty (gson)")
    void findByProperty() {
        System.out.println("3 - findByProperty (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        sorgente = "titolo";

        sorgente2 = "847a.c.";
        try {
            anno = service.findByProperty(sorgente, sorgente2);
            assertNotNull(anno);
            printAnnoProperty(sorgente, sorgente2, anno);
        } catch (AlgosException unErrore) {
            assertNull(anno);
            printAnnoProperty(sorgente, sorgente2, anno);
            System.out.println(unErrore);
        }

        sorgente2 = "847 a.C.";
        anno = null;
        try {
            anno = service.findByProperty(sorgente, sorgente2);
            assertNotNull(anno);
            printAnnoProperty(sorgente, sorgente2, anno);
        } catch (AlgosException unErrore) {
            assertNull(anno);
            printAnnoProperty(sorgente, sorgente2, anno);
            System.out.println(unErrore);
        }
    }


    void printAnnoID(final String keyId, final Anno anno) {
        printAnno("keyID", keyId, anno);
    }

    void printAnnoKey(final String keyPropertyValue, final Anno anno) {
        printAnno("keyValue", keyPropertyValue, anno);
    }

    void printAnnoProperty(final String propertyName, final Serializable propertyValue, final Anno anno) {
        printAnno(propertyName, (String) propertyValue, anno);
    }

    void printAnno(final String tag, final String value, final Anno anno) {
        System.out.println(VUOTA);
        if (anno == null) {
            System.out.println(String.format("Non esiste l'anno con %s=%s", tag, value));
        }
        else {
            System.out.println(String.format("Ho trovato l'anno con %s=%s", tag, value));
            System.out.print(String.format("%s", value));
            System.out.print(String.format("%s", FORWARD));
            System.out.print(String.format("[%s]", anno.ordine));
            System.out.print(String.format("%s", VIRGOLA_SPAZIO));
            System.out.print(String.format("[%s]", anno.titolo));
            System.out.print(String.format("%s", VIRGOLA_SPAZIO));
            System.out.print(String.format("[%s]", anno.bisestile));
            System.out.print(String.format("%s", VIRGOLA_SPAZIO));
            System.out.println(String.format("[%s]", anno.secolo));
        }
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