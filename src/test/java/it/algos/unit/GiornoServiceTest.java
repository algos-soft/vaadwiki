package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 19-ago-2021
 * Time: 18:56
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("GiornoService - Entity cronologica")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GiornoServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    private GiornoService service;

    private Giorno giorno;

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

        giorno = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - findById (gson)")
    void findById() {
        System.out.println("1 - findById (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "43 novembre";
        try {
            giorno = service.findById(sorgente);
            assertNotNull(giorno);
            printGiornoID(sorgente, giorno);
        } catch (Exception unErrore) {
            assertNull(giorno);
            printGiornoID(sorgente, giorno);
            System.out.println(unErrore);
        }

        sorgente = "29gennaio";
        giorno = null;
        try {
            giorno = service.findById(sorgente);
            assertNotNull(giorno);
            printGiornoID(sorgente, giorno);
        } catch (Exception unErrore) {
            assertNull(giorno);
            printGiornoID(sorgente, giorno);
            System.out.println(unErrore);
        }

        sorgente = "29 gennaio";
        giorno = null;
        try {
            giorno = service.findById(sorgente);
            assertNotNull(giorno);
            printGiornoID(sorgente, giorno);
        } catch (Exception unErrore) {
            assertNull(giorno);
            printGiornoID(sorgente, giorno);
            System.out.println(unErrore);
        }
    }


    @Test
    @Order(2)
    @DisplayName("2 - findByKey (gson)")
    void findByKey() {
        System.out.println("2 - findByKey (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "43 novembre";
        try {
            giorno = service.findByKey(sorgente);
            assertNotNull(giorno);
            printGiornoKey(sorgente, giorno);
        } catch (Exception unErrore) {
            assertNull(giorno);
            printGiornoKey(sorgente, giorno);
            System.out.println(unErrore);
        }

        sorgente = "29gennaio";
        giorno = null;
        try {
            giorno = service.findByKey(sorgente);
            assertNotNull(giorno);
            printGiornoKey(sorgente, giorno);
        } catch (Exception unErrore) {
            assertNull(giorno);
            printGiornoKey(sorgente, giorno);
            System.out.println(unErrore);
        }

        sorgente = "29 gennaio";
        giorno = null;
        try {
            giorno = service.findByKey(sorgente);
            assertNotNull(giorno);
            printGiornoKey(sorgente, giorno);
        } catch (Exception unErrore) {
            assertNull(giorno);
            printGiornoKey(sorgente, giorno);
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

        sorgente2 = "29gennaio";
        try {
            giorno = service.findByProperty(sorgente, sorgente2);
            assertNotNull(giorno);
            printGiornoProperty(sorgente, sorgente2, giorno);
        } catch (AMongoException unErrore) {
            assertNull(giorno);
            printGiornoProperty(sorgente, sorgente2, giorno);
            System.out.println(unErrore);
        }

        sorgente2 = "29 gennaio";
        try {
            giorno = service.findByProperty(sorgente, sorgente2);
            assertNotNull(giorno);
            printGiornoProperty(sorgente, sorgente2, giorno);
        } catch (AMongoException unErrore) {
            assertNull(giorno);
            printGiornoProperty(sorgente, sorgente2, giorno);
            System.out.println(unErrore);
        }
    }

    void printGiornoID(final String keyId, final Giorno giorno) {
        printGiorno("keyID", keyId, giorno);
    }

    void printGiornoKey(final String keyPropertyValue, final Giorno giorno) {
        printGiorno("keyValue", keyPropertyValue, giorno);
    }

    void printGiornoProperty(final String propertyName, final Serializable propertyValue, final Giorno giorno) {
        printGiorno(propertyName, (String) propertyValue, giorno);
    }

    void printGiorno(final String tag, final String value, final Giorno giorno) {
        System.out.println(VUOTA);
        if (giorno == null) {
            System.out.println(String.format("Non esiste il giorno con %s=%s", tag, value));
        }
        else {
            System.out.println(String.format("Ho trovato il giorno con %s=%s", tag, value));
            System.out.print(String.format("%s", value));
            System.out.print(String.format("%s", FORWARD));
            System.out.print(String.format("[%s]", giorno.ordine));
            System.out.print(String.format("%s", VIRGOLA_SPAZIO));
            System.out.print(String.format("[%s]", giorno.titolo));
            System.out.print(String.format("%s", VIRGOLA_SPAZIO));
            System.out.println(String.format("[%s]", giorno.mese));
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
    @AfterEach
    void tearDownAll() {
    }

}