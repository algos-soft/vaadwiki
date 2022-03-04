package it.algos.unit;

import it.algos.simple.*;
import it.algos.test.*;
import it.algos.vaadflow14.backend.application.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.packages.security.utente.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

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
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SimpleApplication.class})
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

    //--da regolare per mostrare errori previsti oppure nasconderli per velocizzare
    //--da usare SOLO come controllo per errori previsti
    private boolean flagRisultatiEsattiObbligatori = true;


    //--classe
    //--numero fields classe
    //--numero fields superClasse
    protected static Stream<Arguments> CLAZZ_FIELDS() {
        return Stream.of(
                Arguments.of((Class) null, 0, 0),
                Arguments.of(Via.class, 2, 6),
                Arguments.of(AIType.class, 0, 0),
                Arguments.of(Utente.class, 7, 12),
                Arguments.of(LogicList.class, 0, 0),
                Arguments.of(Company.class, 4, 8),
                Arguments.of(Mese.class, 5, 7),
                Arguments.of(Secolo.class, 5, 7)
        );
    }


    //--classe
    //--keyPropertyValue
    //--valida
    protected static Stream<Arguments> CLAZZ_KEY_ID() {
        return Stream.of(
                Arguments.of((Class) null, VUOTA, false),
                Arguments.of(Utente.class, VUOTA, false),
                Arguments.of(Mese.class, null, false),
                Arguments.of(Mese.class, VUOTA, false),
                Arguments.of(Mese.class, "termidoro", false),
                Arguments.of(Giorno.class, "2agosto", true),
                Arguments.of(Giorno.class, "2 agosto", true),
                Arguments.of(Mese.class, "marzo", true),
                Arguments.of(Mese.class, "Marzo", true),
                Arguments.of(Mese.class, "marzo esatto", false),
                Arguments.of(Via.class, "piazza", true)
        );
    }


    //--classe
    //--propertyName
    //--esiste nella classe
    //--esiste nelle superClassi
    protected static Stream<Arguments> CLAZZ_ESISTE_FIELD() {
        return Stream.of(
                Arguments.of((Class) null, VUOTA, false, false),
                Arguments.of(Utente.class, VUOTA, false, false),
                Arguments.of(Mese.class, null, false, false),
                Arguments.of(Mese.class, "ordine", true, true),
                Arguments.of(Mese.class, "mese", true, true),
                Arguments.of(Mese.class, "reset", false, true),
                Arguments.of(Mese.class, "marzo esatto", false, false)
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

        //--reindirizzo l'istanza della superclasse
        service = reflectionService;

        //--property statica utilizzata nel test
        FlowVar.typeSerializing = AETypeSerializing.spring;
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


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FIELDS")
    @Order(1)
    @DisplayName("1 - Nomi dei fields pubblici della entity e delle superClassi")
    void getFieldsName(final Class clazz, final int fieldClasse, final int fieldsSuperclassi) {
        String clazzName = clazz != null ? clazz.getSimpleName() : "(vuota)";
        System.out.println(String.format("Classe %s", clazzName));

        System.out.println(VUOTA);
        System.out.println(String.format("Nomi dei fields pubblici diretti della classe %s", clazzName));
        try {
            listaStr = service.getFieldsName(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printName(clazz, "getFieldsName", listaStr);
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(fieldClasse, listaStr != null ? listaStr.size() : 0);
        }

        System.out.println(VUOTA);
        System.out.println(String.format("Nomi dei fields pubblici della classe %s e delle sue superClassi", clazzName));
        try {
            listaStr = service.getAllFieldsName(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printName(clazz, "getAllFieldsName", listaStr);
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(fieldsSuperclassi, listaStr != null ? listaStr.size() : 0);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FIELDS")
    @Order(2)
    @DisplayName("2 - Fields pubblici della entity e delle superClassi")
    void getFields(final Class clazz, final int fieldClasse, final int fieldsSuperclassi) {
        String clazzName = clazz != null ? clazz.getSimpleName() : "(vuota)";
        System.out.println(String.format("Classe %s", clazzName));

        System.out.println(VUOTA);
        System.out.println(String.format("Fields pubblici diretti della classe %s", clazzName));
        try {
            listaFields = service.getFields(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printField(clazz, "getFieldsName", listaFields);
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(fieldClasse, listaFields != null ? listaFields.size() : 0);
        }

        System.out.println(VUOTA);
        System.out.println(String.format("Fields pubblici della classe %s e delle sue superClassi", clazzName));
        try {
            listaFields = service.getAllFields(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printField(clazz, "getAllFieldsName", listaFields);
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(fieldsSuperclassi, listaFields != null ? listaFields.size() : 0);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_ESISTE_FIELD")
    @Order(3)
    @DisplayName("3 - Esistenza di un field nella classe o nelle superClassi")
    void isEsiste(final Class clazz, final String propertyName, final boolean esisteClasse, final boolean esisteSuperclassi) {
        String clazzName = clazz != null ? clazz.getSimpleName() : "(vuota)";
        System.out.println(String.format("Classe %s", clazzName));

        System.out.println(VUOTA);
        System.out.println(String.format("Esistenza nella classe %s del field %s", clazzName, propertyName));
        try {
            ottenutoBooleano = service.isEsisteFieldOnClass(clazz, propertyName);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(esisteClasse, ottenutoBooleano);
        }
        printEsiste(clazz, propertyName, ottenutoBooleano);

        System.out.println(VUOTA);
        System.out.println(String.format("Esistenza nella classe %s e superClassi del field %s", clazzName, propertyName));
        try {
            ottenutoBooleano = service.isEsisteFieldOnSuperClass(clazz, propertyName);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(esisteSuperclassi, ottenutoBooleano);
        }
        printEsiste(clazz, propertyName, ottenutoBooleano);
    }

    @Test
    @Order(5)
    @DisplayName("5 - Singolo field di una classe")
    void getField() {
        System.out.println("5 - Singolo field di una classe");
        System.out.println(VUOTA);

        sorgente = FlowCost.FIELD_CODE;
        ottenutoField = null;
        try {
            ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        Assertions.assertNull(ottenutoField);
        System.out.println("Non esiste " + UTENTE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FIELD_COMPANY;
        ottenutoField = null;
        try {
            ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + UTENTE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FIELD_NOTE;
        ottenutoField = null;
        try {
            ottenutoField = service.getField(UTENTE_CLASS, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + UTENTE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FlowCost.FIELD_ORDINE;
        ottenutoField = null;
        try {
            ottenutoField = service.getField(MESE_CLASS, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        Assertions.assertNotNull(ottenutoField);
        System.out.println("Trovato " + MESE_CLASS.getSimpleName() + PUNTO + sorgente);

        sorgente = FIELD_NOTE;
        ottenutoField = null;
        try {
            ottenutoField = service.getField(MESE_LOGIC_CLASS, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
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


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(8)
    @DisplayName("8 - Mappa delle property di una entity")
    void getMapFieldsValue(final Class clazz, final Serializable keyPropertyValue, final boolean valida) {
        try {
            entityBean = mongoService.find(clazz, keyPropertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (valida) {
            assertNotNull(entityBean);
            try {
                mappa = service.getMappaEntity(entityBean);
            } catch (AlgosException unErrore) {
                printError(unErrore);
            }
            assertTrue(mappa.size() > 0);
            printMappaEntity(entityBean, mappa);
        }
        else {
            assertNull(entityBean);
        }
    }


    private void printField(Class<? extends AEntity> clazz, String methodName, List<Field> lista) {
        int pos = 1;
        if (clazz == null || lista == null) {
            return;
        }

        System.out.println("Scandagliando " + clazz.getSimpleName() + " col metodo " + methodName + " trovo " + lista.size() + " fields");
        for (Field field : lista) {
            System.out.println((pos++) + SEP + field.getName());
        }
        System.out.println("");
    }

    private void printName(final Class<? extends AEntity> clazz, final String methodName, final List<String> lista) {
        int pos = 1;
        if (clazz == null || lista == null) {
            return;
        }

        System.out.println(String.format("Scandagliando %s col metodo %s trovo %d fields", clazz.getSimpleName(), methodName, lista.size()));
        for (String nome : lista) {
            System.out.println((pos++) + SEP + nome);
        }
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

    private void printMappaEntity(final AEntity entityBean, final Map<String, Object> mappa) {
        int pos = 1;
        if (mappa == null) {
            System.out.println(String.format("La mappa della entityBean %s non esiste", entityBean));
            return;
        }

        System.out.println(String.format("La entityBean %s ha %d property", entityBean, mappa.size()));
        for (String key : mappa.keySet()) {
            System.out.print((pos++) + PARENTESI_TONDA_END + SPAZIO);
            System.out.print(key);
            System.out.print(UGUALE_SPAZIATO);
            System.out.println(mappa.get(key));
        }
        System.out.println(VUOTA);
    }

    private void printEsiste(final Class clazz, final String propertyName, final boolean esiste) {
        if (clazz == null) {
            return;
        }

        if (esiste) {
            System.out.println(String.format("Nella classe %s esiste il field %s", clazz.getSimpleName(), propertyName));
        }
        else {
            System.out.println(String.format("Nella classe %s non esiste il field %s", clazz.getSimpleName(), propertyName));
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