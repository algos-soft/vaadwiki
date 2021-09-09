package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 07-set-2021
 * Time: 15:34
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test BioUtility")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BioUtilityTest extends WTest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    BioUtility utilityService;

    private LinkedHashMap<String, String> linkedMappa;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(utilityService);
        Assertions.assertNotNull(utilityService);

        utilityService.text = textService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        linkedMappa = null;
    }

    @Test
    @Order(1)
    @DisplayName("1 - estraeMappa")
    void estraeMappa() {
        linkedMappa = utilityService.estraeMappa(TMPL_UNO);
        printMappa(linkedMappa);

        linkedMappa = utilityService.estraeMappa(TMPL_DUE);
        printMappa(linkedMappa);

        linkedMappa = utilityService.estraeMappa(TMPL_TRE);
        printMappa(linkedMappa);
    }

    @Test
    @Order(2)
    @DisplayName("2 - seleziona campi")
    void getCampi() {
        ArrayList<ParBio> lista;

        lista = ParBio.getValues();
        printLista(lista, bioDue, "all");

        lista = ParBio.getCampiSignificativi();
        printLista(lista, bioDue, "significativi");

        lista = ParBio.getCampiNormali();
        printLista(lista, bioDue, "normali");

        lista = ParBio.getCampiObbligatori();
        printLista(lista, bioDue, "obbligatori");
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

    private void printMappa(final LinkedHashMap<String, String> mappa) {
        System.out.println(VUOTA);
        System.out.println(String.format("%s parametri validi (il minimo sono 11)", mappa.size()));
        System.out.println(VUOTA);
        for (String key : mappa.keySet()) {
            System.out.println(String.format("'%s' -> '%s'", key, mappa.get(key)));
        }
        System.out.println(VUOTA);
    }

    private void printLista(final ArrayList<ParBio> lista, final Bio bio, final String titolo) {
        System.out.println(VUOTA);
        System.out.println(String.format("%s parametri %s", lista.size(), titolo));
        System.out.println(VUOTA);
        for (ParBio par : lista) {
            System.out.println(String.format("'%s' -> '%s'", par.getTag(), par.getValue(bio) != null ? par.getValue(bio) : VUOTA));
        }
        System.out.println(VUOTA);
    }

}