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

import java.lang.reflect.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 05-set-2020
 * Time: 16:22
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("ReflectionService - Reflexion dei fields e delle classi.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReflectionServiceTest extends ATest {

    protected static Class<? extends AEntity> UTENTE_CLASS = Utente.class;

    protected static Class<? extends AEntity> COMPANY_CLASS = Company.class;

    protected static Class<? extends AEntity> MESE_CLASS = Mese.class;

    protected static Class<? extends AEntity> SECOLO_CLASS = Secolo.class;

    protected static Class<?> MESE_LOGIC_CLASS = MeseService.class;


    private List<Field> listaFields;

    private AEntity CLASSE_UNO;


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ReflectionService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = reflectionService;
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
    @DisplayName("1 - Nomi dei fields pubblici della entity")
    void getFieldsName() {
        System.out.println("1 - Nomi dei fields pubblici della entity");
        System.out.println(VUOTA);

        previstoIntero = 7;
        listaStr = service.getFieldsName(UTENTE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(UTENTE_CLASS, "getFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 4;
        listaStr = service.getFieldsName(COMPANY_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(COMPANY_CLASS, "getFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 5;
        listaStr = service.getFieldsName(MESE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(MESE_CLASS, "getFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 5;
        listaStr = service.getFieldsName(SECOLO_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(SECOLO_CLASS, "getFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 2;
        listaStr = service.getFieldsName(VIA_ENTITY_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(SECOLO_CLASS, "getFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());
    }


    @Test
    @Order(2)
    @DisplayName("2 - Nomi dei fields pubblici della entity e delle superClassi")
    void getAllFieldsName() {
        System.out.println("2 - Nomi dei fields pubblici della entity e delle superClassi");
        System.out.println(VUOTA);

        previstoIntero = 12;
        listaStr = service.getAllFieldsName(UTENTE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(UTENTE_CLASS, "getAllFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 8;
        listaStr = service.getAllFieldsName(COMPANY_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(COMPANY_CLASS, "getAllFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 7;
        listaStr = service.getAllFieldsName(MESE_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(MESE_CLASS, "getAllFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 7;
        listaStr = service.getAllFieldsName(SECOLO_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(SECOLO_CLASS, "getAllFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 6;
        listaStr = service.getAllFieldsName(VIA_ENTITY_CLASS);
        Assertions.assertNotNull(listaStr);
        printName(SECOLO_CLASS, "getAllFieldsName", listaStr);
        assertEquals(previstoIntero, listaStr.size());
    }

    @Test
    @Order(3)
    @DisplayName("3 - Fields pubblici della entity")
    void getFields() {
        System.out.println("3 - Fields pubblici della entity");
        System.out.println(VUOTA);

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

        previstoIntero = 5;
        listaFields = service.getFields(SECOLO_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(SECOLO_CLASS, "getFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 2;
        listaFields = service.getFields(VIA_ENTITY_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(SECOLO_CLASS, "getFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());
    }


    @Test
    @Order(4)
    @DisplayName("4 - Fields pubblici della entity e delle superClassi")
    void getAllFields() {
        System.out.println("4 - Fields pubblici della entity e delle superClassi");
        System.out.println(VUOTA);

        previstoIntero = 12;
        listaFields = service.getAllFields(UTENTE_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(UTENTE_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 8;
        listaFields = service.getAllFields(COMPANY_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(COMPANY_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 7;
        listaFields = service.getAllFields(MESE_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(MESE_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 7;
        listaFields = service.getAllFields(SECOLO_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(SECOLO_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());

        previstoIntero = 6;
        listaFields = service.getAllFields(VIA_ENTITY_CLASS);
        Assertions.assertNotNull(listaFields);
        printField(SECOLO_CLASS, "getAllFields", listaFields);
        assertEquals(previstoIntero, listaFields.size());
    }


    @Test
    @Order(5)
    @DisplayName("5 - Singolo field di una classe")
    void getField() {
        System.out.println("5 - Singolo field di una classe");
        System.out.println(VUOTA);

        sorgente = FlowCost.FIELD_CODE;
        ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        Assertions.assertNull(ottenutoField);
        System.out.println("Non esiste " + UTENTE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FIELD_COMPANY;
        ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + UTENTE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FIELD_NOTE;
        ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + UTENTE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FlowCost.FIELD_ORDINE;
        ottenutoField = service.getField(MESE_CLASS, sorgente);
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + MESE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FIELD_NOTE;
        ottenutoField = service.getField(MESE_LOGIC_CLASS, sorgente);
        Assertions.assertNull(ottenutoField);
        System.out.println("Non esiste " + MESE_LOGIC_CLASS.getSimpleName() + PUNTO + sorgente);
    }

    @Test
    @Order(6)
    @DisplayName("6 - getListStaticFieldsName")
    void getStaticFields() {
        System.out.println("6 - getListStaticFieldsName");
        System.out.println(VUOTA);

        clazz = FlowVar.class;
        listaStr = service.getListStaticFieldsName(clazz);
        printListaCostanti(clazz, listaStr);
    }


    @Test
    @Order(7)
    @DisplayName("7 - getMapStaticFieldsValue")
    void getMapStaticFieldsValue() {
        System.out.println("7 - getMapStaticFieldsValue");
        System.out.println(VUOTA);

        FlowVar.projectNameDirectoryIdea = "Valore_Di_Prova_Che_Verra_Cancellato_Al_Termine_Del_Test";

        clazz = FlowVar.class;
        mappa = service.getMapStaticFieldsValue(clazz);
        printMappaCostanti(clazz, mappa);
    }


    private void printField(Class<? extends AEntity> clazz, String methodName, List<Field> lista) {
        int pos = 1;
        System.out.println("Scandagliando " + clazz.getSimpleName() + " col metodo " + methodName + " trovo " + lista.size() + " fields");
        for (Field field : lista) {
            System.out.println((pos++) + SEP + field.getName());
        }
        System.out.println("");
    }

    private void printName(final Class<? extends AEntity> clazz, final String methodName, final List<String> lista) {
        int pos = 1;
        System.out.println(String.format("Scandagliando %s col metodo %s trovo %d fields", clazz.getSimpleName(), methodName, lista.size()));
        for (String nome : lista) {
            System.out.println((pos++) + SEP + nome);
        }
        System.out.println(VUOTA);
    }

    private void printListaCostanti(final Class clazz, final List<String> lista) {
        int pos = 1;
        System.out.println(String.format("Scandagliando %s trovo %d variabili statiche", clazz.getSimpleName(), lista.size()));
        for (String nome : lista) {
            System.out.println((pos++) + SEP + nome);
        }
        System.out.println(VUOTA);
    }

    private void printMappaCostanti(final Class clazz, final Map<String, Object> mappa) {
        int pos = 1;
        System.out.println(String.format("Scandagliando %s trovo %d variabili statiche", clazz.getSimpleName(), mappa.size()));
        for (String key : mappa.keySet()) {
            System.out.print((pos++) + PARENTESI_TONDA_END + SPAZIO);
            System.out.print(key);
            System.out.print(UGUALE_SPAZIATO);
            System.out.println(mappa.get(key));
        }
        System.out.println(VUOTA);
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