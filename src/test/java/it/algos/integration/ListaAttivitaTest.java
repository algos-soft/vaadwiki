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
import org.junit.*;
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
@DisplayName("Test Lista")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAttivitaTest extends WTest {

    /**
     * Inietta da Spring
     */
    @Autowired
    public MongoTemplate mongoOp;

    /**
     * The Service.
     */
    @Autowired
    public MongoService mongoService;

    private Attivita attivita = null;

    private List<Bio> listaBio = null;

    private List<String> listaDidascalie = null;

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
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
        //        attivita = null;
        //        listaAttivita.bioService = bioService;

        FlowVar.typeSerializing = AETypeSerializing.spring;
    }

    //    @Test
    //    @Order(0)
    //    void prova() {
    //        clazz = Giorno.class;
    //        String propertyName = "mese";
    //        String propertyValue = "ottobre";
    //
    //        try {
    //            listaBean = mongoService.fetch(clazz, propertyName, propertyValue);
    //            Assertions.assertNotNull(listaBean);
    //        } catch (AlgosException unErrore) {
    //            printError(unErrore);
    //        }
    //    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(1)
    @DisplayName("1 - Recupera le attività e controlla l'esistenza")
        //--nome attivita
        //--esiste nel mongoDB
    void checkAttivita(final String nomeAttivita, final boolean esisteAttivitaSingolare) {
        attivita = null;
        try {
            attivita = attivitaService.findByKey(nomeAttivita);
            System.out.println(String.format("Trovata l'attività '%s'", attivita.singolare));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertEquals(esisteAttivitaSingolare, attivita != null);
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(2)
    @DisplayName("2 - Crea lista biografie")
        //--nome attivita
        //--esiste nel mongoDB
    void creaListaBiografie(final String nomeAttivita, final boolean esisteAttivitaSingolare) {
        ListaAttivita listaAttivita = getListaAttivita(nomeAttivita);
        assertEquals(esisteAttivitaSingolare, listaAttivita != null);

        listaBio = null;
        if (listaAttivita != null) {
            listaBio = listaAttivita.getListaBio();
        }
        printListaBio(nomeAttivita, listaBio);
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(3)
    @DisplayName("3 - Crea didascalie")
        //--nome attivita
        //--esiste nel mongoDB
    void creaDidascalie(final String nomeAttivita, final boolean esisteAttivitaSingolare) {
        String didascalia = VUOTA;
        ListaAttivita listaAttivita = getListaAttivita(nomeAttivita);
        assertEquals(esisteAttivitaSingolare, listaAttivita != null);

        listaBio = null;
        listaDidascalie = new ArrayList<>();
        if (listaAttivita != null) {
            listaBio = listaAttivita.getListaBio();
        }

        if (listaBio != null) {
            for (Bio bio : listaBio) {
                didascalia = didascaliaService.getAttivitaNazionalita(bio);
                if (textService.isValid(didascalia)) {
                    listaDidascalie.add(didascalia);
                }
            }
        }

        if (listaDidascalie!=null) {
            for (String singola : listaDidascalie) {
                System.out.println(String.format("%s", singola));
            }
        }

    }

    private ListaAttivita getListaAttivita(String nomeAttivita) {
        ListaAttivita listaAttivita = null;
        Attivita attivita = null;

        try {
            attivita = attivitaService.findByKey(nomeAttivita);
            Assert.assertNotNull(attivita);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        if (attivita != null) {
            listaAttivita = new ListaAttivita(attivita);

            //--forza la injection (solo in questo UnitTest perché normalmente viene lanciato da SpringBoot)
            listaAttivita.bioService = bioService;
            listaAttivita.attivitaService = attivitaService;
            listaAttivita.mongoService = mongoServiceImpl;

            //--forza il metodo @PostConstruct (solo in questo UnitTest perché normalmente viene lanciato da SpringBoot)
            listaAttivita.inizia();
        }

        return listaAttivita;
    }


    private void printListaBio(final String attivita, final List<Bio> listaBio) {
        if (listaBio != null) {
            if (listaBio.size() > 0) {
                System.out.println(String.format("Ci sono %d biografie per l'attività '%s'", listaBio.size(), attivita));
                System.out.println(VUOTA);
                for (Bio bio : listaBio) {
                    System.out.println(String.format("'%s'", bio.getWikiTitle()));
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