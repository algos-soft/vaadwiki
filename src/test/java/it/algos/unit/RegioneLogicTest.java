package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 30-set-2020
 * Time: 20:56
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("RegioneLogicTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegioneLogicTest extends ATest {

    private static final String ISO = "ISO 3166-2:";


    private List<String> listaAlfaDue;

    private List<List<String>> listaGrezza;

    private List<WrapDueStringhe> listaWrap;

    private List<WrapTreStringhe> listaWrapTre;

    private WrapDueStringhe dueStringhe;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        creazioneLista();
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        listaGrezza = null;
        listaWrap = null;
        listaWrapTre = null;
        dueStringhe = null;
    }


    /**
     * Creazioni di servizio per essere sicuri che ci siano tutti i files/directories richiesti <br>
     */
    private void creazioneLista() {
        String[] parti = null;
        ottenuto = resourceService.leggeConfig("3166-2");
        parti = ottenuto.split(A_CAPO);
        if (parti != null && parti.length > 0) {
            listaAlfaDue = new ArrayList<>();
            for (String riga : parti) {
                riga = textService.estrae(riga, DOPPIE_QUADRE_INI, DOPPIE_QUADRE_END);
                riga = textService.levaTestoPrimaDi(riga, PIPE);
                listaAlfaDue.add(riga);
            }
        }
    }


    //    @Test
    @Order(1)
    @DisplayName("1 - legge testo completo table")
    void leggeTable() {
        int max = 2;
        String alfaDue;

        for (int k = 0; k < max; k++) {
            alfaDue = listaAlfaDue.get(k);
            sorgente = ISO + alfaDue;
            try {
                ottenuto = wikiApiService.leggeTable(sorgente);
            } catch (Exception unErrore) {
                System.out.println(VUOTA);
                System.out.println(unErrore.getMessage());
            }
            assertNotNull(ottenuto);
            System.out.println(VUOTA);
            System.out.println("***" + alfaDue + "***");
            System.out.println(ottenuto.substring(0, 80));
        }
    }


    //    @Test
    @Order(2)
    @DisplayName("2 - legge tutte le righe VALIDE di una table")
    void getTable() {
        int max = 15;
        String alfaDue;

        for (int k = 0; k < max; k++) {
            alfaDue = listaAlfaDue.get(k);
            sorgente = ISO + alfaDue;
            try {
                listaGrezza = wikiApiService.getTable(sorgente);
            } catch (Exception unErrore) {
                System.out.println(VUOTA);
                System.out.println(unErrore.getMessage());
                continue;
            }
            assertNotNull(listaGrezza);
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println("*** " + alfaDue + " ***");
            System.out.println("Ci sono " + listaGrezza.size() + " elementi");
            printList(listaGrezza);
        }
    }


    //    @Test
    @Order(3)
    @DisplayName("3 - legge valori VALIDI per le regioni")
    void getRegioni() {
        int max = 20;
        String alfaDue;

        for (int k = 16; k < max; k++) {
            alfaDue = listaAlfaDue.get(k);
            sorgente = ISO + alfaDue;
            try {
                listaWrap = geograficService.getRegioni(sorgente);
            } catch (Exception unErrore) {
                System.out.println(VUOTA);
                System.out.println(unErrore.getMessage());
                continue;
            }
            assertNotNull(listaWrap);
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println("*** " + alfaDue + " ***");
            System.out.println("Ci sono " + listaWrap.size() + " elementi");
            printWrap(listaWrap);
        }
    }


    @Test
    @Order(4)
    @DisplayName("3 - legge i titoli delle tabelle regioni")
    void getTitoli() {
        int max = 30;
        String alfaDue;

        for (int k = 28; k < max; k++) {
            alfaDue = listaAlfaDue.get(k);
            sorgente = ISO + alfaDue;
            try {
                listaWrap = geograficService.getRegioni(sorgente);
            } catch (Exception unErrore) {
                System.out.println(VUOTA);
                System.out.println(unErrore.getMessage());
                continue;
            }
            assertNotNull(listaWrap);
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println("*** titoli " + alfaDue + " ***");
            System.out.println("Ci sono " + listaWrap.size() + " elementi");
            if (listaWrap.get(0) != null) {
                System.out.println(listaWrap.get(0).getPrima() + SEP + listaWrap.get(0).getSeconda());
            }
            else {
                System.out.println("Mancano i titoli");
            }
            System.out.println(listaWrap.get(1).getPrima() + SEP + listaWrap.get(1).getSeconda());
        }
    }

    //@todo Superato

    //    @Test
    //    @Order(3)
    //    @DisplayName("3 - confronto prima riga")
    //    void getTable2() {
    //        int max = 3;
    //        String alfaDue;
    //
    //        for (int k = 0; k < max; k++) {
    //            alfaDue = listaAlfaDue.get(k);
    //            sorgente = ISO + alfaDue;
    //            try {
    //                listaGrezza = service.getTable(sorgente, 1);
    //                if (array.isValid(listaGrezza)) {
    //                    System.out.println(listaGrezza.get(0));
    //                    System.out.println(listaGrezza.get(1));
    //                    System.out.println(VUOTA);
    //                } else {
    //                    System.out.println("La pagina wiki " + sorgente + " non contiene nessuna wikitable");
    //                }
    //            } catch (Exception unErrore) {
    //                //                System.out.println("Non ho trovato la pagina "+sorgente);
    //            }
    //        }
    //    }

    //    private void printWrap(List<WrapDueStringhe> listaWrap) {
    //        System.out.println("********");
    //        if (array.isAllValid(listaWrap)) {
    //            for (WrapDueStringhe wrap : listaWrap) {
    //                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda());
    //            }
    //        }
    //    }


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