package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.packages.security.utente.*;
import it.algos.vaadflow14.backend.service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 05-set-2020
 * Time: 16:22
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("AReflectionServiceTest")
@DisplayName("Unit test sulla riflessione")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AReflectionServiceTest extends ATest {

    protected static Class<? extends AEntity> UTENTE_CLASS = Utente.class;

    protected static Class<? extends AEntity> COMPANY_CLASS = Company.class;

    protected static Class<? extends AEntity> MESE_CLASS = Mese.class;

    protected static Class<? extends AEntity> SECOLO_CLASS = Secolo.class;

    protected static Class<?> MESE_LOGIC_CLASS = MeseService.class;

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    AReflectionService service;

    private List<Field> listaFields;

    private AEntity CLASSE_UNO;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        service.text = text;
        service.array = array;
        service.logger = logger;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        ottenutoField = null;
        listaFields = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Fields pubblici della entity")
    void getFields() {
        previstoIntero = 7;
        listaFields = service.getFields(UTENTE_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(UTENTE_CLASS, "getFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 4;
        listaFields = service.getFields(COMPANY_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(COMPANY_CLASS, "getFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 5;
        listaFields = service.getFields(MESE_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(MESE_CLASS, "getFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 4;
        listaFields = service.getFields(SECOLO_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(SECOLO_CLASS, "getFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());
    }


    @Test
    @Order(2)
    @DisplayName("2 - Fields pubblici della entity e delle superclassi")
    void getAllFields() {
        previstoIntero = 9;
        listaFields = service.getAllFields(UTENTE_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(UTENTE_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 5;
        listaFields = service.getAllFields(COMPANY_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(COMPANY_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 6;
        listaFields = service.getAllFields(MESE_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(MESE_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 5;
        listaFields = service.getAllFields(SECOLO_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(SECOLO_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());
    }


    @Test
    @Order(3)
    @DisplayName("3 - Nomi dei fields pubblici della entity")
    void getFieldsName() {
        previstoIntero = 7;
        listaStr = service.getFieldsName(UTENTE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(UTENTE_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 4;
        listaStr = service.getFieldsName(COMPANY_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(COMPANY_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 5;
        listaStr = service.getFieldsName(MESE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(MESE_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 4;
        listaStr = service.getFieldsName(SECOLO_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(SECOLO_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());
    }


    @Test
    @Order(4)
    @DisplayName("4 - Nomi dei fields pubblici della entity e delle superclassi")
    void getAllFieldsName() {
        previstoIntero = 9;
        listaStr = service.getAllFieldsName(UTENTE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(UTENTE_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 5;
        listaStr = service.getAllFieldsName(COMPANY_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(COMPANY_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 6;
        listaStr = service.getAllFieldsName(MESE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(MESE_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 5;
        listaStr = service.getAllFieldsName(SECOLO_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(SECOLO_CLASS, "getFields", listaStr);
        assertEquals(previstoIntero, listaStr.size());
    }


    @Test
    @Order(5)
    @DisplayName("4 - Singolo field di una classe")
    void getField() {
        System.out.println("Singolo field di una classe");
        System.out.println("");

        sorgente = FlowCost.FIELD_CODE;
        ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        Assertions.assertNull(ottenutoField);
        System.out.println("Non esiste " + UTENTE_CLASS.getSimpleName()  +PUNTO+ sorgente);

        sorgente = FIELD_COMPANY;
        ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + UTENTE_CLASS.getSimpleName() + PUNTO+ sorgente);

        sorgente = FIELD_NOTE;
        ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + UTENTE_CLASS.getSimpleName() + PUNTO+ sorgente);

        sorgente = FlowCost.FIELD_ORDINE;
        ottenutoField = service.getField(MESE_CLASS, sorgente);
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + MESE_CLASS.getSimpleName() + PUNTO+ sorgente);

        sorgente = FIELD_NOTE;
        ottenutoField = service.getField(MESE_LOGIC_CLASS, sorgente);
        Assertions.assertNull(ottenutoField);
        System.out.println("Non esiste " + MESE_LOGIC_CLASS.getSimpleName() + PUNTO+ sorgente);
    }


    private void printField(Class<? extends AEntity> clazz, String methodName, List<Field> lista) {
        int pos = 1;
        System.out.println("Scandagliando " + clazz.getSimpleName() + " col metodo " + methodName + " trovo " + lista.size() + " fields");
        for (Field field : lista) {
            System.out.println((pos++) + FlowCost.SEP + field.getName());
        }
        System.out.println("");
    }


    private void printName(Class<? extends AEntity> clazz, String methodName, List<String> lista) {
        int pos = 1;
        System.out.println("Scandagliando " + clazz.getSimpleName() + " col metodo " + methodName + " trovo " + lista.size() + " fields");
        for (String nome : lista) {
            System.out.println((pos++) + FlowCost.SEP + nome);
        }
        System.out.println("");
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