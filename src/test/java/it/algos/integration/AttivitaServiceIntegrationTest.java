package it.algos.integration;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 09-gen-2022
 * Time: 15:02
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Attivita service integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AttivitaServiceIntegrationTest extends WTest {


    /**
     * The Service.
     */
    @Autowired
    public MongoService mongoService;

    /**
     * Inietta da Spring
     */
    @Autowired
    public MongoTemplate mongoOp;

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private AttivitaService service;

    private List<Attivita> listaAttivita;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = attivitaService;

        MockitoAnnotations.initMocks(mongoService);
        Assertions.assertNotNull(mongoService);

        MockitoAnnotations.initMocks(mongoOp);
        Assertions.assertNotNull(mongoOp);
    }


    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void wFixRiferimentiIncrociati() {
        super.wFixRiferimentiIncrociati();

        attivitaService.mongo = mongoService;
        mongoService.mongoOp = mongoOp;
        bioService.mongo = mongoService;
        attivitaService.array = arrayService;
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

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(1)
    @DisplayName("1 - Controlla l'esistenza delle attività singolari")
        //--nome attivita singolare
        //--esiste nel mongoDB
    void checkSingolare(final String nomeAttivitaSingolare, final boolean esisteAttivitaSingolare) {
        System.out.println(String.format("1 - Controlla l'esistenza dell'attività singolare '%s'", nomeAttivitaSingolare));
        System.out.println(VUOTA);
        attivita = null;

        try {
            attivita = service.findSingolare(nomeAttivitaSingolare);
            System.out.println(String.format("Trovata l'attività singolare '%s'", attivita.singolare));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertEquals(esisteAttivitaSingolare, attivita != null);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(2)
    @DisplayName("2 - Controlla l'esistenza delle attività singolari con un metodo specifico di AttivitaService")
        //--nome attivita singolare
        //--esiste nel mongoDB
    void isEsisteSingolare(final String nomeAttivitaSingolare, final boolean esisteAttivitaSingolare) {
        System.out.println(String.format("2 - Controlla l'esistenza dell'attività singolare '%s' con AttivitaService.isEsisteSingolare() ", nomeAttivitaSingolare));
        System.out.println(VUOTA);

        try {
            ottenutoBooleano = service.isEsisteSingolare(nomeAttivitaSingolare);
            System.out.println(String.format("Trovata l'attività singolare '%s' nel mongoDB", nomeAttivitaSingolare));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertEquals(esisteAttivitaSingolare, ottenutoBooleano);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALE")
    @Order(3)
    @DisplayName("3 - Controlla l'esistenza delle attività plurali")
        //--nome attivita plurale
        //--esiste nel mongoDB
    void isEsistePlurale(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        System.out.println(String.format("3 - Controlla l'esistenza dell'attività plurale '%s'", nomeAttivitaPlurale));
        System.out.println(VUOTA);

        try {
            ottenutoBooleano = service.isEsistePlurale(nomeAttivitaPlurale);
            if (ottenutoBooleano) {
                System.out.println(String.format("Trovata l'attività plurale '%s'", nomeAttivitaPlurale));
            }
            else {
                System.out.println(String.format("L'attività plurale '%s' non esiste nel mongoDB", nomeAttivitaPlurale));
            }
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertEquals(esisteAttivitaPlurale, ottenutoBooleano);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALE")
    @Order(4)
    @DisplayName("4 - Lista delle attività (stringa) singolari con lo stesso plurale")
        //--nome attivita plurale
        //--esiste nel mongoDB
    void listaSingolariDaPlurale(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        System.out.println(String.format("4 - Lista delle attività (stringa) singolari con lo stesso plurale '%s'", nomeAttivitaPlurale));
        System.out.println(VUOTA);

        try {
            ottenutoBooleano = service.isEsistePlurale(nomeAttivitaPlurale);
            assertEquals(esisteAttivitaPlurale, ottenutoBooleano);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            return;
        }

        try {
            listaStr = attivitaService.fetchSingolariDaPlurale(nomeAttivitaPlurale);
            if (arrayService.isEmpty(listaStr)) {
                System.out.println(String.format("Non esiste l'attività plurale '%s'", nomeAttivitaPlurale));
                return;
            }
            System.out.println(String.format("Trovata l'attività plurale '%s' che corrisponde a %d attività singolari", nomeAttivitaPlurale, listaStr.size()));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printSingolari(listaStr);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALE")
    @Order(5)
    @DisplayName("5 - Lista delle attività (Attivita) singolari con lo stesso plurale")
        //--nome attivita plurale
        //--esiste nel mongoDB
    void listaAttivitaDaPlurale(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        System.out.println(String.format("5 - Lista delle attività (Attivita) singolari con lo stesso plurale '%s'", nomeAttivitaPlurale));
        System.out.println(VUOTA);

        try {
            ottenutoBooleano = service.isEsistePlurale(nomeAttivitaPlurale);
            assertEquals(esisteAttivitaPlurale, ottenutoBooleano);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            return;
        }

        try {
            listaAttivita = attivitaService.fetchAttivitaDaPlurale(nomeAttivitaPlurale);
            if (listaAttivita.size() == 0) {
                System.out.println(String.format("Non esiste l'attività plurale '%s'", nomeAttivitaPlurale));
                return;
            }
            System.out.println(String.format("Trovata l'attività plurale '%s' che corrisponde a %d attività singolari", nomeAttivitaPlurale, listaAttivita.size()));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printAttivita(listaAttivita);
    }

    private void printSingolari(final List<String> listaAttivita) {
        String sep = "'";

        for (String attivita : listaAttivita) {
            System.out.println(String.format("%s%s%s", sep, attivita, sep));
        }
    }


    private void printAttivita(final List<Attivita> listaAttivita) {
        String ini = "[";
        String end = "]";

        for (Attivita attivita : listaAttivita) {
            System.out.println(String.format("%s%s%s", ini, attivita.singolare, end));
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