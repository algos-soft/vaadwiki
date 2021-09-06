package it.algos.wiki;

import com.mongodb.client.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 30-ago-2021
 * Time: 12:23
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Mongo service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoBioServiceTest extends WTest {

    protected static final String COLLEZIONE_INESISTENTE = "pomeriggio";

    protected static final String COLLEZIONE_VUOTA = "alfa";

    protected static final String COLLEZIONE_VALIDA = "via";

    private static final String DATA_BASE_NAME = "vaadflow14";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    protected MongoService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = mongoService;
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
    @DisplayName("1 - Stato del database")
    void status() {
        System.out.println("1- Stato del database");
        MongoDatabase dataBase;

        ottenuto = service.getDatabaseName();
        assertTrue(textService.isValid(ottenuto));
        System.out.println(VUOTA);
        System.out.println(String.format("Nome del dataBase corrente: [%s]", ottenuto));

        dataBase = service.getDataBase();
        assertNotNull(dataBase);
        System.out.println(VUOTA);
        System.out.println(String.format("DataBase corrente: [%s]", dataBase));

        listaStr = service.getCollezioni();
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("Collezioni esistenti: %s", listaStr));
    }

    @Test
    @Order(2)
    @DisplayName("2 - Stato delle collezioni")
    void isExists() {
        System.out.println("2 - Esistenza delle collezioni");

        sorgente = COLLEZIONE_INESISTENTE;
        ottenutoBooleano = service.isExistsCollection(sorgente);
        assertFalse(ottenutoBooleano);
        printCollection(sorgente, "non esiste");

        sorgente = COLLEZIONE_VALIDA;
        ottenutoBooleano = service.isExistsCollection(sorgente);
        assertTrue(ottenutoBooleano);
        printCollection(sorgente, "esiste");
        ottenutoBooleano = service.isExistsCollection(Via.class);
        assertTrue(ottenutoBooleano);
        printCollection(sorgente, " (letta dalla classe) esiste");

        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("2 - Validità delle collezioni");

        sorgente = COLLEZIONE_INESISTENTE;
        ottenutoBooleano = service.isValidCollection(sorgente);
        assertFalse(ottenutoBooleano);
        printCollection(sorgente, "non è valida");

        sorgente = COLLEZIONE_VALIDA;
        ottenutoBooleano = service.isValidCollection(sorgente);
        assertTrue(ottenutoBooleano);
        printCollection(sorgente, "è valida");
        ottenutoBooleano = service.isValidCollection(Via.class);
        assertTrue(ottenutoBooleano);
        printCollection(sorgente, " (letta dalla classe) è valida");
    }

    @Test
    @Order(3)
    @DisplayName("3 - Modifica (provvisoria) e save (provvisorio) di una entity (via)")
    void saveVia() {
        System.out.println("3 - Modifica (provvisoria) e save (provvisorio) di una entity (via)");
        int originario;
        int daModificare;
        int modificato;
        int finale;

        //--leggo una entityBean e memorizzo una property
        clazz = Via.class;
        sorgente = "corte";
        try {
            entityBean = service.findByKey(clazz, sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
        originario = ((Via) entityBean).getOrdine();
        System.out.println(VUOTA);
        System.out.println(String.format("Nella entity originale [%s] il valore di 'ordine' è [%s]", sorgente, originario));

        //--modifico la entityBean
        daModificare = mathService.random();
        ((Via) entityBean).setOrdine(daModificare);

        //--registro la entityBean modificata
        try {
            service.save(entityBean);
        } catch (AMongoException unErrore) {
            System.out.println(unErrore);
        }

        //--ri-leggo la entityBean (dal vecchio id) controllo la property per vedere se è stata modificata e registrata
        try {
            entityBean = service.findById(clazz, entityBean.getId());
        } catch (AMongoException unErrore) {
        }
        modificato = ((Via) entityBean).getOrdine();
        assertEquals(daModificare, modificato);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella entity modificata [%s] il valore di 'ordine' è [%s]", sorgente, modificato));

        //--ri-modifico la entityBean
        ((Via) entityBean).setOrdine(originario);

        //--ri-registro la entityBean come in origine
        try {
            service.save(entityBean);
        } catch (AMongoException unErrore) {
            System.out.println(unErrore);
        }

        //--ri-leggo la entityBean e ri-controllo la property
        try {
            entityBean = service.findByKey(clazz, sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
        finale = ((Via) entityBean).getOrdine();
        assertEquals(originario, finale);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella entity ricostruita [%s] il valore di 'ordine' è [%s], uguale al valore originario [%s]", sorgente, finale, originario));
    }

    @Test
    @Order(4)
    @DisplayName("4 - Save di una entity (bio)")
    void saveBio() {
        System.out.println("4 - Save di una entity (bio)");
        int originario;
        int daModificare;
        int modificato;
        int finale;

        //--leggo una entityBean e memorizzo una property
        clazz = Bio.class;
        sorgente = PAGINA_CINQUE;
        //        entityBean = bioService.downloadBio(sorgente);
        wrap = queryBio.urlRequest(sorgente).getWrap();
        assertNotNull(wrap);
        entityBean = bioService.fixBio(wrap);
        assertNotNull(entityBean);

        //--registro la entityBean
        try {
            service.save(entityBean);
        } catch (AMongoException unErrore) {
            System.out.println(unErrore);
        }

        //--cancello la entityBean (eventuale)
        ottenutoBooleano = service.delete(entityBean);
        assertTrue(ottenutoBooleano);
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

    protected void printCollection(final String collectionName, final String status) {
        System.out.println(VUOTA);
        System.out.println(String.format("La collezione '%s' %s", collectionName, status));
    }

}