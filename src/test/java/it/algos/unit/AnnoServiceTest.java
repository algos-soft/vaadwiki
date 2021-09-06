package it.algos.unit;

import it.algos.test.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 20-ago-2021
 * Time: 07:02
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Anno service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnnoServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    private AnnoService service;

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
    }


    @Test
    @Order(1)
    @DisplayName("1 - findById")
    void findById() {
        sorgente = "1946dc";
        try {
            entityBean = service.findById(sorgente);
        } catch (Exception unErrore) {
        }
        assertNull(entityBean);

        sorgente = "1946";
        try {
            entityBean = service.findById(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(entityBean);

        sorgente = "847a.c.";
        try {
            entityBean = service.findById(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(entityBean);
    }


    @Test
    @Order(2)
    @DisplayName("2 - findByProperty")
    void findByKey() {
        sorgente = "847a.c.";
        try {
            entityBean = service.findByProperty( sorgente2, sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNull(entityBean);

        sorgente = "847 a.C.";
        sorgente2 = "titolo";
        try {
            entityBean = service.findByProperty( sorgente2, sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
    }


    @Test
    @Order(3)
    @DisplayName("3 - findByKey")
    void findByKey3() {
        sorgente = "847a.c.";
        try {
            entityBean = service.findByKey( sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNull(entityBean);

        sorgente = "847 a.C.";
        try {
            entityBean = service.findByKey( sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
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