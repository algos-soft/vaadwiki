package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

import java.io.*;
import java.util.stream.*;

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


    //--titolo
    //--esiste come keyId nel mongoDB
    //--esiste come keyProperty nel mongoDB
    private static Stream<Arguments> GIORNI() {
        return Stream.of(
                Arguments.of(null, false, false),
                Arguments.of(VUOTA, false, false),
                Arguments.of("43 novembre", false, false),
                Arguments.of("29gennaio", true, false),
                Arguments.of("29 gennaio", true, true)
        );
    }

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


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(1)
    @DisplayName("1 - findById (gson)")
        //--titolo
        //--esiste come keyId nel mongoDB
        //--esiste come keyProperty nel mongoDB
    void findById(final String titolo, final boolean esisteKeyId, final boolean esisteKeyProperty) {
        System.out.println("1 - findById (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = titolo;
        giorno = null;
        try {
            giorno = service.findById(sorgente);
            printGiornoID(sorgente, giorno);
        } catch (AlgosException unErrore) {
            assertNull(giorno);
            printError(unErrore);
        }
        assertEquals(esisteKeyId, giorno != null);
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(2)
    @DisplayName("2 - findByKey (gson)")
        //--titolo
        //--esiste come keyId nel mongoDB
        //--esiste come keyProperty nel mongoDB
    void findByKey(final String titolo, final boolean esisteKeyId, final boolean esisteKeyProperty) {
        System.out.println("2 - findByKey (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = titolo;
        giorno = null;
        try {
            giorno = service.findByKey(sorgente);
            printGiornoKey(sorgente, giorno);
        } catch (AlgosException unErrore) {
            assertNull(giorno);
            printError(unErrore);
        }
        assertEquals(esisteKeyProperty, giorno != null);
    }


    @ParameterizedTest
    @MethodSource(value = "GIORNI")
    @Order(3)
    @DisplayName("3 - findByProperty (gson)")
    void findByProperty(final String titolo, final boolean esisteKeyId, final boolean esisteKeyProperty) {
        System.out.println("3 - findByProperty (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        String propertyName = "titolo";

        String propertyValue = titolo;
        giorno = null;
        try {
            giorno = service.findByProperty(propertyName, propertyValue);
            printGiornoProperty(propertyName, propertyValue, giorno);
        } catch (AlgosException unErrore) {
            assertNull(giorno);
            printError(unErrore);
        }
        assertEquals(esisteKeyProperty, giorno != null);
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
    @AfterAll
    void tearDownAll() {
    }

}