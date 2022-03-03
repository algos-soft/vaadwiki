package it.algos.integration;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.upload.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.support.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 17-feb-2022
 * Time: 15:37
 */
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllIntegration")
@DisplayName("Test per l'upload delle di Attività")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UploadAttivitaIntegrationTest extends WTest {

    public static final String WIKI_TITLE_DEBUG = Upload.WIKI_TITLE_DEBUG;

    /**
     * The App context.
     */
    @Autowired
    protected GenericApplicationContext appContext;


    private UploadAttivita istanza;

    private Attivita attivita;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        FlowVar.typeSerializing = AETypeSerializing.spring;
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void wFixRiferimentiIncrociati() {
        super.wFixRiferimentiIncrociati();

        attivitaService.array = arrayService;
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        istanza = null;
        attivita = null;
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(1)
    @DisplayName("1 - Crea (senza upload) una istanza uploadAttivita da Attivita")
        //--attivita
        //--AETypeAttivita
        //--flag booleano upload (non usato)
    void creaIstanzaAttivita(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("1 - Crea una istanza uploadAttivita per l'attività (singolare) '%s'", attivita.singolare));
            System.out.println("Un upload con type=AETypeAttivita.singolare non ha senso.");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("1 - Crea una istanza uploadAttivita per l'attività (singolare) '%s' da cui risalire a (plurale) '%s'", attivita.singolare, attivita.plurale));
            System.out.println("Per costruire una istanza uploadAttivita, uso una entity Attivita senza altri parametri");
            istanza = appContext.getBean(UploadAttivita.class, attivita);
            assertNotNull(istanza);
            ottenuto = istanza.getTestoConParagrafi();
            System.out.println(String.format("Testo con paragrafi per l'attività (plurale) '%s'", attivita.plurale));
            System.out.println(String.format("Ci sono %d didascalie nella pagina", istanza.getNumVoci()));
            System.out.println(VUOTA);
            System.out.println(ottenuto);
        }
    }


    @Test
    @Order(2)
    @DisplayName("2 - Upload di una sottoPagina specifica (Britannici)")
    void uploadSottopagina() {
        Map<String, List<String>> mappaSub;
        attivita = attivitaAccademico;
        sorgente2 = "Britannici";
        sorgente = WIKI_TITLE_DEBUG + AETypeLista.attivita.getPrefix() + textService.primaMaiuscola(attivita.plurale) + SLASH + textService.primaMaiuscola(sorgente2);

        istanza = appContext.getBean(UploadAttivita.class, attivita);
        assertNotNull(istanza);
        mappaSub = istanza.getMappaDue().get(sorgente2);
        istanza = appContext.getBean(UploadAttivita.class, attivita, AETypePagina.sottoPagina, sorgente, sorgente2, mappaSub);
        assertNotNull(istanza);

        //--upload effettivo
        if (false) {
            ottenutoRisultato = istanza.upload();
            assertNotNull(ottenutoRisultato);
            System.out.println(String.format("2 - Upload della sottoPagina '%s' estratta da '%s'", sorgente2, sorgente));
            printRisultato(ottenutoRisultato, "UploadSottopagina");
        }
        else {
            ottenuto = istanza.getTestoPagina();
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("2 - Testo completo di upload della sottoPagina '%s' estratta da '%s'", sorgente2, sorgente));
            System.out.println(String.format("Ci sono %d didascalie nella pagina", istanza.getNumVoci()));
            System.out.println(VUOTA);
            System.out.println(ottenuto);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(3)
    @DisplayName("3 - Preview testo pagina completo")
        //--attivita
        //--AETypeAttivita
        //--flag booleano upload (non usato)
    void preview(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("3 - Preview testo pagina completo di upload per l'attività (singolare) '%s'", attivita.singolare));
            System.out.println("Un upload con type=AETypeAttivita.singolare non ha senso.");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("3 - Preview testo pagina completo di upload per l'attività (singolare) '%s' da cui risalire a (plurale) '%s'", attivita.singolare, attivita.plurale));
            istanza = appContext.getBean(UploadAttivita.class, attivita);
            assertNotNull(istanza);
            ottenuto = istanza.getTestoPagina();
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("Testo completo di upload della pagina di attività (plurale) '%s'", attivita.plurale));
            System.out.println(String.format("Ci sono %d didascalie nella pagina", istanza.getNumVoci()));
            System.out.println(VUOTA);
            System.out.println(ottenuto);
        }
    }

    @Test
    @Order(4)
    @DisplayName("4 - Upload di una pagina completa specifica (Accademico) con sottoPagine (tre)")
    void uploadPagina() {
        attivita = attivitaAccademico;
        sorgente = WIKI_TITLE_DEBUG + AETypeLista.attivita.getPrefix() + textService.primaMaiuscola(attivita.plurale);
        istanza = appContext.getBean(UploadAttivita.class, attivita, AETypePagina.paginaPrincipale, sorgente, VUOTA, null);
        assertNotNull(istanza);

        //--upload effettivo
        if (true) {
            ottenutoRisultato = istanza.upload();
            assertNotNull(ottenutoRisultato);
            System.out.println(String.format("Upload effettuato per l'attività '%s'", attivita.plurale));
            printRisultato(ottenutoRisultato, "Upload");
        }
        else {
            ottenuto = istanza.getTestoPagina();
            assertTrue(textService.isValid(ottenuto));
            System.out.println(String.format("Testo completo di upload per l'attività '%s'", attivita.plurale));
            System.out.println(String.format("Ci sono %d didascalie nella pagina", istanza.getNumVoci()));
            System.out.println(VUOTA);
            System.out.println(ottenuto);
        }
    }

    //    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(5)
    @DisplayName("5 - Upload su pagina di servizio")
    //--attivita
    //--AETypeAttivita
    //--flag booleano upload
    void uploadPagina2(final Attivita attivita, final ListaAttivita.AETypeAttivita type, final boolean upload) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("5 - Upload su pagina di servizio dell'attività (singolare) '%s'", attivita.singolare));
            System.out.println("Un upload con type=AETypeAttivita.singolare non ha senso.");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            if (upload) {
                System.out.println(String.format("5 - Upload su pagina di servizio dell'attività (singolare) '%s' da cui risalire a (plurale) '%s'", attivita.singolare, attivita.plurale));
                istanza = appContext.getBean(UploadAttivita.class, attivita);
                ottenutoRisultato = istanza.upload();
                assertNotNull(ottenutoRisultato);
                System.out.println(String.format("Upload effettuato per l'attività (plurale) '%s'", attivita.plurale));
                printRisultato(ottenutoRisultato, "UploadAttivita");
            }
            else {
                System.out.println(String.format("5 - Upload su pagina di servizio dell'attività (singolare) '%s'", attivita.plurale));
                System.out.println(String.format("Flag di upload falso per un'attività troppo lunga"));
            }
        }
    }


    //    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(5)
    @DisplayName("5 - Upload pagina (di test) completa")
    //--attivita
    //--AETypeAttivita
    void uploadTest(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("5 - Upload pagina (di test) completa per l'attività (singolare) '%s'", attivita.singolare));
            System.out.println("Un upload con type=AETypeAttivita.singolare non ha senso.");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("5 - Upload pagina (di test) completa per l'attività (singolare) '%s' da cui risalire a (plurale) '%s'", attivita.singolare, attivita.plurale));
            appContext.getBean(UploadAttivita.class, attivita).uploadTest();

            System.out.println(String.format("Vai sul browser a controllare la pagina '%s'", attivita.plurale));
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
