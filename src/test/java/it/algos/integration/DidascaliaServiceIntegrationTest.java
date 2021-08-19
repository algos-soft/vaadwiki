package it.algos.integration;

import it.algos.test.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit.jupiter.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 15-ago-2021
 * Time: 18:11
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Didascalia service - Elaborazione delle didascalie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DidascaliaServiceIntegrationTest extends WTest {


    @Autowired
    DidascaliaService service;

    @Autowired
    BioService bioService;

    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        Assertions.assertNotNull(service);
        Assertions.assertNotNull(bioService);
    }


    @BeforeEach
    void setUpEach() {
        super.setUp();
        wrap = null;
        bio = null;
    }


    @Test
    @Order(1)
    @DisplayName(PAGINA_UNO)
    void getNomeCognome() {
        sorgente = PAGINA_UNO;
        bio = bioService.downloadBio(sorgente);
        assertNotNull(bio);
        didascalia = service.getNomeCognome(bio);
        printNomeCognome(bio, didascalia);
    }

    @Test
    @Order(2)
    @DisplayName(PAGINA_DUE)
    void getNomeCognome2() {
        sorgente = PAGINA_DUE;
        bio = bioService.downloadBio(sorgente);
        assertNotNull(bio);
        didascalia = service.getNomeCognome(bio);
        printNomeCognome(bio, didascalia);
    }

    @Test
    @Order(3)
    @DisplayName(PAGINA_TRE)
    void getNomeCognome3() {
        sorgente = PAGINA_TRE;
        bio = bioService.downloadBio(sorgente);
        assertNotNull(bio);
        didascalia = service.getNomeCognome(bio);
        printNomeCognome(bio, didascalia);
    }

    @Test
    @Order(4)
    @DisplayName(PAGINA_QUATTRO)
    void getNomeCognome4() {
        sorgente = PAGINA_QUATTRO;
        bio = bioService.downloadBio(sorgente);
        assertNotNull(bio);
        didascalia = service.getNomeCognome(bio);
        printNomeCognome(bio, didascalia);
    }

    @Test
    @Order(5)
    @DisplayName(PAGINA_CINQUE)
    void getNomeCognome5() {
        sorgente = PAGINA_CINQUE;
        bio = bioService.downloadBio(sorgente);
        assertNotNull(bio);
        didascalia = service.getNomeCognome(bio);
        printNomeCognome(bio, didascalia);
    }

    @Test
    @Order(6)
    @DisplayName(PAGINA_DISAMBIGUA)
    void getNomeCognome6() {
        sorgente = PAGINA_DISAMBIGUA;
        bio = bioService.downloadBio(sorgente);
        assertNull(bio);
    }

    @Test
    @Order(7)
    @DisplayName(PAGINA_REDIRECT)
    void getNomeCognome7() {
        sorgente = PAGINA_REDIRECT;
        bio = bioService.downloadBio(sorgente);
        assertNull(bio);
    }

    protected void printNomeCognome(final Bio bio, final String didascalia) {
        printDidascalia("nomeCognome", bio, didascalia);
    }

    protected void printDidascalia(final String tipologia, final Bio bio, final String didascalia) {
        System.out.println(String.format("Didascalia (%s) della voce %s: %s", tipologia, bio.wikiTitle, didascalia));
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
