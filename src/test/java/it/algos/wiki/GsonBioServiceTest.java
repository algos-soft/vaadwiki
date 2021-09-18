package it.algos.wiki;

import it.algos.test.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 13-set-2021
 * Time: 08:56
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("Gson service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GsonBioServiceTest extends WTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private GsonService service;

    private Class bioClazz = Bio.class;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = gSonService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
        bio = null;
    }

    @Test
    @Order(1)
    @DisplayName("Primo test")
    void getLabelHost() {
    }

    @Test
    @Order(2)
    @DisplayName("2 - crea una entityBean (Bio) da mongoDb con keyId")
    void findById() {
        System.out.println("2 - crea una entityBean (Bio) da mongoDb con keyId");
        FlowVar.typeSerializing = AETypeSerializing.gson;

        sorgente = "63624";
        try {
            bio = bioService.findById(sorgente);
            assertNotNull(entityBean);
            System.out.println(String.format("Creazione di un bean di classe %s dal titolo %s", bioClazz.getSimpleName(), bio.wikiTitle));
        } catch (Exception unErrore) {
            System.out.println(String.format("Errore %s per la bio con keyId %s", unErrore.getCause().getMessage(),unErrore.getMessage()));
            assertNull(entityBean);
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
    @AfterEach
    void tearDownAll() {
    }

}