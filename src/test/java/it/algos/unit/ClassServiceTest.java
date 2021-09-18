package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 16-mag-2021
 * Time: 17:41
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("ClassService - Utility di Class e path.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClassServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ClassService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = classService;
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
    @DisplayName("1 - Test 'a freddo' (senza service)")
    void first() {
        Class clazz = VIA_ENTITY_CLASS;
        String canonicalName = clazz.getCanonicalName();
        assertTrue(textService.isValid(canonicalName));
        System.out.println(canonicalName);
        Class clazz2 = null;

        try {
            clazz2 = Class.forName(canonicalName);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        assertNotNull(clazz2);
        System.out.println(clazz2.getSimpleName());
        System.out.println(clazz2.getName());
        System.out.println(clazz2.getCanonicalName());
    }

    @Test
    @Order(2)
    @DisplayName("2 - getNameFromPath")
    void getNameFromPath() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadwiki/src/main/java/it/algos/vaadflow14/backend/packages/anagrafica/via/Via.java";

        ottenuto = service.getNameFromPath(VUOTA);
        assertTrue(textService.isEmpty(ottenuto));

        previsto = "it/algos/vaadflow14/backend/packages/anagrafica/via/Via";
        ottenuto = service.getNameFromPath(sorgente);
        assertEquals(previsto, ottenuto);
        System.out.println(sorgente);
        System.out.println(previsto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - getClazzFromCanonicalName")
    void getClazzFromCanonicalName() {
        sorgente = VUOTA;
        clazz = null;
        try {
            clazz = service.getClazzFromCanonicalName(VUOTA);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);

        sorgente = VIA_ENTITY_CLASS.getCanonicalName();
        clazz = null;
        try {
            clazz = service.getClazzFromCanonicalName(sorgente);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);

        sorgente = sorgente + JAVA_SUFFIX;
        clazz = null;
        try {
            clazz = service.getClazzFromCanonicalName(sorgente);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);
    }

    @Test
    @Order(4)
    @DisplayName("4 - getClazzFromSimpleName")
    void getClazzFromSimpleName() {
        System.out.println("il progetto corrente viene simulato regolando (provvisoriamente) la property statica FlowVar.projectNameDirectoryIdea");
        System.out.println(VUOTA);

        sorgente = VUOTA;
        clazz = null;
        try {
            clazz = service.getClazzFromSimpleName(VUOTA);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);

        sorgente = "Via";
        clazz = null;
        try {
            clazz = service.getClazzFromSimpleName(sorgente);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);

        sorgente = "Via" + JAVA_SUFFIX;
        clazz = null;
        try {
            clazz = service.getClazzFromSimpleName(sorgente);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);

        sorgente = "via";
        clazz = null;
        try {
            clazz = service.getClazzFromSimpleName(sorgente);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);

        sorgente = "Bolla";
        clazz = null;
        try {
            clazz = service.getClazzFromSimpleName(sorgente);
        } catch (AlgosException unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
            System.out.println(String.format(unErrore.getStack()));
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);
    }


    @Test
    @Order(5)
    @DisplayName("5 - getClazzFromPath")
    void getClazzFromPath() {
        sorgente = "/Users/gac/Documents/IdeaProjects/operativi/vaadwiki/src/main/java/it/algos/vaadflow14/backend/packages/anagrafica/via/Via.java";

        clazz = null;
        try {
            clazz = service.getClazzFromPath(VUOTA);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        assertNull(clazz);

        clazz = null;
        try {
            clazz = service.getClazzFromPath(sorgente);
        } catch (Exception unErrore) {
            System.out.println(String.format(unErrore.getMessage()));
        }
        printClazz(sorgente, clazz);
    }

    @Test
    @Order(6)
    @DisplayName("6 - getEntityFromClazz")
    void getEntityFromClazz() {
        clazz = Via.class;
        entityBean = service.getEntityFromClazz(clazz);
        assertNotNull(entityBean);
        System.out.println(entityBean);
    }

    @Test
    @Order(7)
    @DisplayName("7 - getProjectName")
    void getProjectName() {
        ottenuto = service.getProjectName();
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("Nome del progetto corrente: %s", ottenuto));
    }

    void printClazz(final String sorgente, final Class clazz) {
        System.out.println("Classe trovata");

        System.out.print("Sorgente");
        System.out.print(FORWARD);
        System.out.println(sorgente);

        if (clazz != null) {
            System.out.print("Name");
            System.out.print(FORWARD);
            System.out.println(clazz.getName());

            System.out.print("SimpleName");
            System.out.print(FORWARD);
            System.out.println(clazz.getSimpleName());

            System.out.print("CanonicalName");
            System.out.print(FORWARD);
            System.out.println(clazz.getCanonicalName());
        }
        else {
            System.out.print("Non esiste la classe");
            System.out.print(FORWARD);
            System.out.println(sorgente);
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