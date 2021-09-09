package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.wiki.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 25-lug-2021
 * Time: 22:18
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("QueryOnerose - Alcune query molto lunghe.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryOneroseTest extends WTest {

    private static final String CATEGORIA_BIO = "BioBot";

    private static final String CAT_2020 = "Morti nel 2020";

    private static final String CAT_1435 = "Nati nel 1435";

//    private static final List<Integer> PAGINE = List.of(10, 100, 1000, 10000);
    private static final List<Integer> PAGINE = List.of(10, 100);

    private List<Long> listaPageIds;

    private List<WrapBio> listaWrapBio;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--abilita il bot
        queryLogin.urlRequest();
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
    @DisplayName("1 - queryCat. Legge tutti i pageIds della categoria BioBot (per intero)")
    void queryCat() {
        System.out.println("1 - Legge tutti i pageIds della categoria BioBot (per intero)");
        queryType = queryCat.getClass().getSimpleName();

        sorgente = CATEGORIA_BIO;
        sorgente = CAT_2020; //@todo provvisorio
        sorgente = CAT_1435; //@todo provvisorio

        previsto = JSON_SUCCESS;
        ottenutoRisultato = queryCat.urlRequest(sorgente);
        assertTrue(ottenutoRisultato.isValido());
        assertEquals(previsto, ottenutoRisultato.getCodeMessage());
        printRisultato(ottenutoRisultato, queryType);
        System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
        print10long(ottenutoRisultato.getLista());
        listaPageIds = ottenutoRisultato.getLista();
        System.out.println(String.format("Ho messo da parte una listaPageIds di %s elementi", listaPageIds.size()));
    }

    @Test
    @Order(2)
    @DisplayName("2 - queryTimestamp. Legge i MiniWrap (timestamp) dai pageIds della categoria BioBot")
    void queryTimestamp() {
        System.out.println("2 - Legge i MiniWrap (timestamp) dai pageIds della categoria BioBot");
        queryType = queryTimestamp.getClass().getSimpleName();

        if (listaPageIds != null) {
            for (int sub : PAGINE) {
                sorgenteArrayLong = listaPageIds.subList(0, Math.min(sub, listaPageIds.size()));
                inizio = System.currentTimeMillis();
                previsto = JSON_SUCCESS;
                ottenutoRisultato = queryTimestamp.urlRequest(sorgenteArrayLong);
                assertTrue(ottenutoRisultato.isValido());
                assertEquals(previsto, ottenutoRisultato.getCodeMessage());
                System.out.println(VUOTA);
                printRisultato(ottenutoRisultato, queryType);
                System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
                print10Mini(ottenutoRisultato.getLista());
            }
        }
    }


    @Test
    @Order(3)
    @DisplayName("3 - queryPages. Legge i WrapBio dai pageIds (elaborati) della categoria BioBot")
    void queryPages() {
        System.out.println("3 - Legge i WrapBio dai pageIds (elaborati) della categoria BioBot");
        queryType = queryPages.getClass().getSimpleName();

        if (listaPageIds != null) {
            listaWrapBio = new ArrayList<>();
            for (int sub : PAGINE) {
                sorgenteArrayLong = listaPageIds.subList(0, Math.min(sub, listaPageIds.size()));
                inizio = System.currentTimeMillis();
                previsto = JSON_SUCCESS;
                ottenutoRisultato = queryPages.urlRequest(sorgenteArrayLong);
                listaWrapBio.addAll(ottenutoRisultato.getLista());
                assertTrue(ottenutoRisultato.isValido());
                assertEquals(previsto, ottenutoRisultato.getCodeMessage());
                System.out.println(VUOTA);
                printRisultato(ottenutoRisultato, queryType);
                System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
                print10Bio(ottenutoRisultato.getLista());
            }
            System.out.println(String.format("Ho messo da parte una listaWrapBio di %s elementi", listaWrapBio.size()));
        }
    }

    @Test
    @Order(4)
    @DisplayName("4 - Elabora e registra la listaWrapBio ricavata della categoria BioBot")
    void pippoz() {
        System.out.println("4 - Elabora e registra la listaWrapBio ricavata della categoria BioBot");
        queryType = bioService.getClass().getSimpleName();
         List<WrapBio> listaWrapBioTmp;

        if (listaWrapBio != null) {
            for (int sub : PAGINE) {
                listaWrapBioTmp = listaWrapBio.subList(0, Math.min(sub, listaWrapBio.size()));
                inizio = System.currentTimeMillis();
                bioService.creaElaboraListaBio(listaWrapBioTmp);
                System.out.println(VUOTA);
                System.out.println(String.format("Risultato ottenuto in esattamente %s", dateService.deltaTextEsatto(inizio)));
            }
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