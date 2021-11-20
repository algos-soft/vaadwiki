package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.geografica.continente.*;
import it.algos.vaadflow14.backend.packages.geografica.stato.*;
import it.algos.vaadflow14.backend.packages.security.utente.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

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
public class ClassServiceTest extends MongoTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ClassService service;

    //--clazzSorgente
    //--clazzPrevista
    protected static Stream<Arguments> CLAZZ_ENTITY() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(LogicList.class, null),
                Arguments.of(Utente.class, Utente.class),
                Arguments.of(Mese.class, Mese.class),
                Arguments.of(MeseService.class, Mese.class),
                Arguments.of(Giorno.class, Giorno.class),
                Arguments.of(GiornoLogicList.class, Giorno.class),
                Arguments.of(Via.class, Via.class),
                Arguments.of(ViaLogicList.class, Via.class),
                Arguments.of(AIType.class, null),
                Arguments.of(Company.class, Company.class),
                Arguments.of(Stato.class, Stato.class),
                Arguments.of(Continente.class, Continente.class)
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


    @ParameterizedTest
    @MethodSource(value = "SIMPLE")
    @Order(2)
    @DisplayName("2 - clazz and canonicalName from simpleName")
    void getClazzFromSimpleName(final String simpleName, final boolean esistePackage) {
        clazz = null;
        sorgente = simpleName;
        try {
            clazz = service.getClazzFromSimpleName(sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertFalse(esistePackage && clazz == null);

        printClazz(sorgente, clazz);
    }

    @ParameterizedTest
    @MethodSource(value = "SIMPLE")
    @Order(3)
    @DisplayName("3 - esistenza di una clazz from simpleName")
    void isEsisteFromSimpleName(final String simpleName, final boolean esistePackage) {
        clazz = null;
        sorgente = simpleName;
        try {
            ottenutoBooleano = service.isEsiste(sorgente);
            System.out.println(String.format("La classe %s esiste", sorgente));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertFalse(esistePackage && !ottenutoBooleano);
    }


    @ParameterizedTest
    @MethodSource(value = "PATH")
    @EmptySource
    @Order(4)
    @DisplayName("4 - clazz and canonicalName from pathName")
    void getClazzFromPathName(String pathName) {
        clazz = null;
        sorgente = pathName;
        try {
            clazz = service.getClazzFromPath(sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printClazz(sorgente, clazz);
    }

    @ParameterizedTest
    @MethodSource(value = "CANONICAL")
    @EmptySource
    @Order(5)
    @DisplayName("5 - clazz from canonicalName")
    void getClazzFromCanonicalName(String canonicalName) {
        clazz = null;
        sorgente = canonicalName;
        try {
            clazz = service.getClazzFromCanonicalName(sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printClazz(sorgente, clazz);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(6)
    @DisplayName("6 - getEntityFromClazz")
    void getEntityFromClazz(Class clazz) {
        entityBean = null;
        try {
            entityBean = service.getEntityFromClazz(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printEntityBeanFromClazz("New", clazz, entityBean);
    }


    @Test
    @Order(7)
    @DisplayName("7- getProjectName")
    void getProjectName() {
        ottenuto = service.getProjectName();
        assertTrue(textService.isValid(ottenuto));
        System.out.println(String.format("Nome del progetto corrente: %s", ottenuto));
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_ENTITY")
    @Order(8)
    @DisplayName("8 - getClazzEntityFromClazz")
        //--clazzSorgente
        //--clazzPrevista
    void getEntityClazzFromClazz(final Class clazzSorgente, final Class clazzPrevista) {
        String message;
        clazz = null;
        try {
            clazz = service.getEntityClazzFromClazz(clazzSorgente);
            System.out.print("Origine: ");
            message = clazzSorgente != null ? clazzSorgente.getSimpleName() : "(null)";
            System.out.println(message);
            System.out.print("AEntity: ");
            message = clazz != null ? clazz.getSimpleName() : "(null)";
            System.out.println(message);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertEquals(clazzPrevista, clazz);

    }

    void printEntityBean(final AEntity entityBean) {
        if (entityBean != null) {
            System.out.println("EntityBean");
            System.out.print("KeyID");
            System.out.print(FORWARD);
            System.out.println(entityBean.getId());
        }
        else {
            System.out.print("Non esiste una entityBean");
        }
        System.out.println(VUOTA);
    }

    void printClazz(final String sorgente, final Class clazz) {
        if (clazz != null) {
            System.out.println("Classe trovata");
            System.out.print("Sorgente");
            System.out.print(FORWARD);
            System.out.println(sorgente);

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