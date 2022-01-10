package it.algos.integration;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.support.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 05-gen-2022
 * Time: 07:54
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test per le liste di attività")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAttivitaIntegrationTest extends WTest {

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
     * The App context.
     */
    @Autowired
    protected GenericApplicationContext appContext;

    private List<Bio> listaBio = null;

    private List<String> listaDidascalie = null;

    private Map<String, List> mappaUno = null;

    private ListaAttivita istanza;

    private Attivita attivita;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        MockitoAnnotations.initMocks(mongoService);
        Assertions.assertNotNull(mongoService);

        MockitoAnnotations.initMocks(mongoOp);
        Assertions.assertNotNull(mongoOp);

        FlowVar.typeSerializing = AETypeSerializing.spring;
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void wFixRiferimentiIncrociati() {
        super.wFixRiferimentiIncrociati();

        mongoService.mongoOp = mongoOp;
        bioService.mongo = mongoService;

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

        istanza = null;
        attivita = null;
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(1)
    @DisplayName("1 - Crea una istanza listaAttivita che usa una singola attività")
        //--nome attivita singolare
        //--esiste nel mongoDB
    void creaIstanzaSingolare(final String nomeAttivitaSingolare, final boolean esisteAttivitaSingolare) {
        System.out.println(String.format("1 - Crea una istanza listaAttivita che usa una singola attività '%s'", nomeAttivitaSingolare));
        System.out.println("Per usare il costruttore adeguato della classe ListaAttivita, devo passargli una entity Attivita e non una stringa");

        if (errorSingolare(nomeAttivitaSingolare, esisteAttivitaSingolare)) {
            return;
        }

        try {
            attivita = attivitaService.findByKey(nomeAttivitaSingolare);
            assertNotNull(attivita);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            return;
        }

        istanza = appContext.getBean(ListaAttivita.class, attivita);
        printIstanza(istanza);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALE")
    @Order(2)
    @DisplayName("2 - Crea una istanza listaAttivita che usa una attività plurale")
        //--nome attivita plurale
        //--esiste nel mongoDB
    void creaIstanzaPlurale(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        System.out.println(String.format("2 - Crea una istanza listaAttivita che usa una attività plurale '%s'", nomeAttivitaPlurale));
        System.out.println("Per usare il costruttore adeguato della classe ListaAttivita, devo passargli una stringa nomeAttivitaPlurale e non una Attivita");

        if (errorPlurale(nomeAttivitaPlurale, esisteAttivitaPlurale)) {
            return;
        }

        istanza = appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale);
        printIstanza(istanza);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_SINGOLARE")
    @Order(3)
    @DisplayName("3 - Crea una lista di didascalie complete per l'attività singolare")
        //--nome attivita singolare
        //--esiste nel mongoDB
    void creaListaDidascalieComplete(final String nomeAttivitaSingolare, final boolean esisteAttivitaSingolare) {
        System.out.println(String.format("3 - Crea una lista di didascalie complete per l'attività singolare '%s'", nomeAttivitaSingolare));

        if (errorSingolare(nomeAttivitaSingolare, esisteAttivitaSingolare)) {
            return;
        }

        try {
            attivita = attivitaService.findByKey(nomeAttivitaSingolare);
            assertNotNull(attivita);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            return;
        }

        listaDidascalie = appContext.getBean(ListaAttivita.class, attivita).getListaDidascalie();
        printListaDidascalie(nomeAttivitaSingolare, listaDidascalie);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALE")
    @Order(4)
    @DisplayName("4 - Crea una lista di didascalie complete per l'attività plurale")
        //--nome attivita plurale
        //--esiste nel mongoDB
    void creaListaPluraleDidascalieComplete(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        System.out.println(String.format("4 - Crea una lista di didascalie complete per l'attività plurale '%s'", nomeAttivitaPlurale));

        if (errorPlurale(nomeAttivitaPlurale, esisteAttivitaPlurale)) {
            return;
        }

        listaDidascalie = appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale).getListaDidascalie();
        printListaDidascalie(nomeAttivitaPlurale, listaDidascalie);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA_PLURALE")
    @Order(5)
    @DisplayName("5 - Crea una mappa con paragrafi per l'attività plurale")
        //--nome attivita plurale
    void creaParagrafiNazionalita(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        System.out.println(String.format("5 - Crea una mappa con paragrafi per l'attività plurale '%s' divisa per paragrafi di nazionalità", nomeAttivitaPlurale));

        if (errorPlurale(nomeAttivitaPlurale, esisteAttivitaPlurale)) {
            return;
        }

        Map<String, List> mappa = appContext.getBean(ListaAttivita.class, nomeAttivitaPlurale).getMappa();
        printParagrafi(mappa);
    }


    private boolean errorSingolare(final String nomeAttivitaSingolare, final boolean esisteAttivitaSingolare) {
        boolean errato = false;

        try {
            ottenutoBooleano = attivitaService.isEsisteSingolare(nomeAttivitaSingolare);
            assertEquals(ottenutoBooleano, esisteAttivitaSingolare);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            errato = true;
        }

        if (!ottenutoBooleano) {
            System.out.println(VUOTA);
            System.out.println(String.format("Non esiste l'attività singolare '%s'", nomeAttivitaSingolare));
            errato = true;
        }

        return errato;
    }


    private boolean errorPlurale(final String nomeAttivitaPlurale, final boolean esisteAttivitaPlurale) {
        boolean errato = false;

        try {
            ottenutoBooleano = attivitaService.isEsistePlurale(nomeAttivitaPlurale);
            assertEquals(ottenutoBooleano, esisteAttivitaPlurale);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            errato = true;
        }

        if (!ottenutoBooleano) {
            System.out.println(VUOTA);
            System.out.println(String.format("Non esiste l'attività plurale '%s'", nomeAttivitaPlurale));
            errato = true;
        }

        return errato;
    }


    private void printIstanza(final ListaAttivita istanza) {
        List<String> listaNomiAttivitaSingole;
        String nomeAttivitaRiferimento;
        List<Bio> listaBio;
        boolean singola;
        if (istanza == null) {
            return;
        }
        assertNotNull(istanza);
        assertNotNull(istanza.getListaNomiAttivitaSingole());
        assertTrue(istanza.getListaNomiAttivitaSingole().size() > 0);
        listaBio = istanza.getListaBio();

        listaNomiAttivitaSingole = istanza.getListaNomiAttivitaSingole();
        singola = istanza.getAttivitaSingola() != null;
        nomeAttivitaRiferimento = singola ? istanza.getAttivitaSingola().singolare : istanza.getNomeAttivitaPlurale();
        System.out.println(VUOTA);
        System.out.println(String.format("Nell'istanza di ListaAttivita costruita per '%s' ci sono %d attività singole", nomeAttivitaRiferimento, listaNomiAttivitaSingole.size()));
        for (String singolaAttivita : listaNomiAttivitaSingole) {
            System.out.println(String.format("'%s'", singolaAttivita));
        }
        System.out.println(VUOTA);
        if (singola) {
            printListaBio(nomeAttivitaRiferimento, listaBio);
        }
        else {
            printListaBioPlurale(nomeAttivitaRiferimento, listaBio);
        }
    }

    private void printListaBio(final String nomeAttivitaSingolare, final List<Bio> listaBio) {
        if (listaBio != null) {
            if (listaBio.size() > 0) {
                System.out.println(String.format("Ci sono %d biografie che usano l'attività singolare '%s'", listaBio.size(), nomeAttivitaSingolare));
                for (Bio bio : listaBio) {
                    System.out.println(String.format("'%s'", bio.getWikiTitle()));
                }
            }
            else {
                System.out.println(String.format("Non esiste nessuna biografia che usi l'attività singolare '%s'", nomeAttivitaSingolare));
            }
        }
        else {
            System.out.println(String.format("Non esiste nessuna biografia che usi l'attività singolare '%s'", nomeAttivitaSingolare));
        }
    }


    private void printListaBioPlurale(final String nomeAttivitaPlurale, final List<Bio> listaBio) {
        if (listaBio != null) {
            if (listaBio.size() > 0) {
                System.out.println(String.format("Ci sono %d biografie che usano l'attività plurale '%s'", listaBio.size(), nomeAttivitaPlurale));
                for (Bio bio : listaBio) {
                    System.out.println(String.format("'%s'", bio.getWikiTitle()));
                }
            }
            else {
                System.out.println(String.format("L'array listaBio esiste ma è vuota; non ci sono biografie per l'attività plurale '%s'", nomeAttivitaPlurale));
            }
        }
        else {
            System.out.println(String.format("Non esiste l'array listaBio; l'attività plurale '%s' non sembra corretta", nomeAttivitaPlurale));
        }
    }

    private void printListaDidascalie(final String attivita, final List<String> listaDidascalie) {
        if (listaDidascalie != null) {
            if (listaDidascalie.size() > 0) {
                System.out.println(String.format("Ci sono %d biografie per l'attività '%s'", listaDidascalie.size(), attivita));
                System.out.println(VUOTA);
                for (String singola : listaDidascalie) {
                    System.out.println(String.format("* %s", singola));
                }
            }
            else {
                System.out.println(String.format("L'array listaBio esiste ma è vuota; non ci sono biografie per l'attività '%s'", attivita));
            }
        }
        else {
            System.out.println(String.format("Non esiste l'array listaBio; l'attività '%s' non sembra corretta", attivita));
        }
    }


    private void printParagrafi(final Map<String, List> mappa) {
        List lista;
        String asterisco = "*";

        System.out.println(VUOTA);
        if (arrayService.isAllValid(mappa)) {
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);

                if (arrayService.isAllValid(lista)) {
                    System.out.print("==");
                    System.out.print(key);
                    System.out.print("==");
                    System.out.print(A_CAPO);

                    for (Object stringa : lista) {
                        System.out.print(asterisco);
                        System.out.print(SPAZIO);
                        System.out.println(stringa);
                    }
                    System.out.print(A_CAPO);
                }
            }
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