package it.algos.integration;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadwiki.*;
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
    @DisplayName("1 - Crea una istanza uploadAttivita da Attivita")
        //--attivita
        //--AETypeAttivita
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


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(2)
    @DisplayName("2 - Preview testo pagina completo")
        //--attivita
        //--AETypeAttivita
    void preview(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("2 - Preview testo pagina completo di upload per l'attività (singolare) '%s'", attivita.singolare));
            System.out.println("Un upload con type=AETypeAttivita.singolare non ha senso.");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("2 - Preview testo pagina completo di upload per l'attività (singolare) '%s' da cui risalire a (plurale) '%s'", attivita.singolare, attivita.plurale));
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



//    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(3)
    @DisplayName("3 - Upload pagina (di test) completa")
        //--attivita
        //--AETypeAttivita
    void uploadTest(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("3 - Upload pagina (di test) completa per l'attività (singolare) '%s'", attivita.singolare));
            System.out.println("Un upload con type=AETypeAttivita.singolare non ha senso.");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("3 - Upload pagina (di test) completa per l'attività (singolare) '%s' da cui risalire a (plurale) '%s'", attivita.singolare, attivita.plurale));
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