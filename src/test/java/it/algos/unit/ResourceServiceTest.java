package it.algos.unit;

import com.vaadin.flow.server.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 24-set-2020
 * Time: 21:13
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("ResourceService - Risorse esterne.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourceServiceTest extends ATest {


    private Map<String, List<String>> mappa;


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ResourceService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = resourceService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        mappa = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Legge nella directory 'frontend'")
    void leggeFrontend() {
        previsto = ".login-information";

        sorgente = "styles.shared-styles.css";
        ottenuto = service.leggeFrontend(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "styles/shared-styles.css";
        ottenuto = service.leggeFrontend(sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
    }


    @Test
    @Order(2)
    @DisplayName("2 - Legge nella directory META-INF")
    void leggeMetaInf() {
        sorgente = "rainbow.png";
        ottenuto = service.leggeMetaInf(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "img.rainbow.png";
        ottenuto = service.leggeMetaInf(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "img/rainbow.png";
        ottenuto = service.leggeMetaInf(sorgente);
        assertTrue(textService.isValid(ottenuto));

        sorgente = "bandiere/at.png";
        ottenuto = service.leggeMetaInf(sorgente);
        assertTrue(textService.isValid(ottenuto));
    }


    @Test
    @Order(3)
    @DisplayName("3 - Legge i bytes[]")
    void getBytes() {
        sorgente = "rainbow.png";
        bytes = service.getBytes(sorgente);
        assertNull(bytes);

        sorgente = "img/rainbow.png";
        bytes = service.getBytes(sorgente);
        assertNotNull(bytes);

        sorgente = "src/main/resources/META-INF/resources/img/rainbow.png";
        bytes = service.getBytes(sorgente);
        assertNotNull(bytes);

        sorgente = "bandiere/ca.png";
        bytes = service.getBytes(sorgente);
        assertNotNull(bytes);

        sorgente = "src/main/resources/META-INF/resources/bandiere/ca.png";
        bytes = service.getBytes(sorgente);
        assertNotNull(bytes);
    }


    @Test
    @Order(4)
    @DisplayName("4 - Legge le risorse")
    void getSrc() {
        StreamResource resource;

        sorgente = "rainbow.png";
        ottenuto = service.getSrc(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "src/main/resources/META-INF/resources/img/" + sorgente;
        ottenuto = service.getSrc(sorgente);
        assertNotNull(ottenuto);

        sorgente = "ca.png";
        sorgente = "src/main/resources/META-INF/resources/bandiere/" + sorgente;
        ottenuto = service.getSrc(sorgente);
        assertNotNull(ottenuto);
    }


    @Test
    @Order(5)
    @DisplayName("5 - Legge un file nella directory 'config'")
    void leggeConfig() {
        previsto = "gac=aggiungere";

        sorgente = "config.password.txt";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "/config.password.txt";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "/config/password.txt";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "config/password.txt";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "password.txt";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        sorgente = "at.png";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

        sorgente = "africa";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

        sorgente = "regioni";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

    }


    @Test
    @Order(6)
    @DisplayName("6 - Legge una lista dalla directory 'config'")
    void leggeListaConfig() {
        sorgente = "regioni";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

        listaStr = service.leggeListaConfig(sorgente, true);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("lista CON titoli");
        System.out.println(VUOTA);
        printVuota(listaStr);

        listaStr = service.leggeListaConfig(sorgente, false);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("lista SENZA titoli");
        System.out.println(VUOTA);
        printVuota(listaStr);

        listaStr = service.leggeListaConfig(sorgente);
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("lista default");
        System.out.println(VUOTA);
        printVuota(listaStr);
    }


    @Test
    @Order(7)
    @DisplayName("7 - Legge una mappa dalla directory 'config'")
    void leggeMappaConfig() {
        sorgente = "regioni";
        ottenuto = service.leggeConfig(sorgente);
        assertTrue(textService.isValid(ottenuto));

        mappa = service.leggeMappaConfig(sorgente);
        assertNotNull(mappa);
        printMappa(mappa);
    }

    //    @Test
    //    @Order(6)
    //    @DisplayName("6 - Costruisce una Image da un file")
    //    void getImage() {
    //        Image image;
    //
    //        sorgente = "rainbow.png";
    //        image = service.getImage(sorgente);
    //        assertNull(image);
    //
    ////        sorgente = "src/main/resources/META-INF/resources/img/"  + sorgente;
    ////        image = service.getImage(sorgente);
    ////        assertNotNull(image);
    //
    //        sorgente="ca.png";
    //        sorgente = "src/main/resources/META-INF/resources/bandiere/"  + sorgente;
    //        image = service.getImage(sorgente);
    //        assertNotNull(image);
    //    }

    ;


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