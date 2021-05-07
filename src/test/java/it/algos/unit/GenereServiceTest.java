package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import it.algos.vaadwiki.backend.packages.genere.*;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

/**
 * Project wikibio
 * Created by Algos
 * User: gac
 * Date: mar, 06-apr-2021
 * Time: 14:44
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("GenereServiceTest")
@DisplayName("Test modulo Genere")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GenereServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    GenereService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        service.text = text;
        service.wiki = wiki;
        service.array = array;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("1 - downloadModuloGenereNonValido")
    void downloadModuloGenereNonValido() {
        ottenutoBooleano = service.downloadModulo(VUOTA);
        Assert.assertFalse(ottenutoBooleano);

        sorgente = "http://www.algos.it/hellogac.html";
        ottenutoBooleano = service.downloadModulo(sorgente);
        Assert.assertFalse(ottenutoBooleano);

        sorgente = "Sarmato";
        ottenutoBooleano = service.downloadModulo(sorgente);
        Assert.assertFalse(ottenutoBooleano);
    }

    @Test
    @Order(2)
    @DisplayName("2 - downloadModuloGenere")
    void downloadModuloGenere() {
        int cicloMax = 10;
        int ciclo = 0;
        sorgente = WikiService.PATH_MODULO_GENERE;
        sorgente2 = "agronomo";
        previsto = "\"agronomi\",\"M\"";
        String singolare = VUOTA;
        String pluraliGrezzi = VUOTA;
        String pluraleMaschile = VUOTA;
        String pluraleFemminile = VUOTA;
        String tagM = "M";
        String tagF = "F";

        Map<String, String> mappa = wiki.leggeMappaModulo(sorgente);
        if (mappa != null && mappa.size() > 0) {
            System.out.println("Entities create nella collection");
            System.out.println(VUOTA);
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                if (ciclo <= cicloMax) {
                    singolare = entry.getKey();
                    pluraliGrezzi = entry.getValue();

                    pluraleMaschile = service.estraeMaschile(pluraliGrezzi);
                    pluraleFemminile = service.estraeFemminile(pluraliGrezzi);

                    if (text.isValid(pluraleMaschile)) {
                        ciclo++;
                        print(ciclo, tagM, singolare, pluraleMaschile);
                    }
                    if (text.isValid(pluraleFemminile)) {
                        ciclo++;
                        print(ciclo, tagF, singolare, pluraleFemminile);
                    }
                }
                else {
                    break;
                }
            }
        }

        ottenuto = mappa.get(sorgente2);
        Assert.assertEquals(previsto, ottenuto);
    }


    /**
     * Controllo il numero dei caratteri <br>
     * singolare         ->  @Size(min = 3, max = 50) <br>
     * plurale maschile  ->  @Size(min = 3, max = 50) <br>
     * plurale femminile ->  @Size(min = 3, max = 50) <br>
     */
    @Test
    @Order(3)
    @DisplayName("3 - checkNumeroCaratteri")
    void checkNumeroCaratteri() {
        int maxTest = 30; //accettabili, li mostro per avere un'idea della lunghezza
        int maxMongo = 50; //errore non accettabile
        int minTest = 5; //accettabili, li mostro per avere un'idea della lunghezza
        int minMongo = 2; //errore non accettabile
        sorgente = WikiService.PATH_MODULO_GENERE;
        String singolare = VUOTA;
        String pluraliGrezzi = VUOTA;
        String pluraleMaschile = VUOTA;
        String pluraleFemminile = VUOTA;

        Map<String, String> mappa = wiki.leggeMappaModulo(sorgente);
        if (mappa != null && mappa.size() > 0) {
            System.out.println("Controllo dei valori più lunghi");
            System.out.println(VUOTA);
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                singolare = entry.getKey();
                pluraliGrezzi = entry.getValue();

                pluraleMaschile = service.estraeMaschile(pluraliGrezzi);
                pluraleFemminile = service.estraeFemminile(pluraliGrezzi);

                if (text.isValid(singolare) && singolare.length() > maxTest) {
                    System.out.println("Singolare " + "(" + singolare.length() + ") " + singolare + " è più lungo di " + maxTest);
                }
                if (text.isValid(pluraleMaschile) && pluraleMaschile.length() > maxTest) {
                    System.out.println("Maschi " + "(" + pluraleMaschile.length() + ") " + pluraleMaschile + " è più lungo di " + maxTest);
                }
                if (text.isValid(pluraleFemminile) && pluraleFemminile.length() > maxTest) {
                    System.out.println("Femmine " + "(" + pluraleFemminile.length() + ") " + pluraleFemminile + " è più lungo di " + maxTest);
                }

                if (text.isValid(singolare) && singolare.length() > maxMongo) {
                    System.out.println("ERRORE - Singolare " + "(" + singolare.length() + ") " + singolare + " è più lungo di " + maxMongo);
                }
                if (text.isValid(pluraleMaschile) && pluraleMaschile.length() > maxMongo) {
                    System.out.println("ERRORE - Maschi " + "(" + pluraleMaschile.length() + ") " + pluraleMaschile + " è più lungo di " + maxMongo);
                }
                if (text.isValid(pluraleFemminile) && pluraleFemminile.length() > maxMongo) {
                    System.out.println("ERRORE - Femmine " + "(" + pluraleFemminile.length() + ") " + pluraleFemminile + " è più lungo di " + maxMongo);
                }
            }
        }

        if (mappa != null && mappa.size() > 0) {
            System.out.println(VUOTA);
            System.out.println(VUOTA);
            System.out.println("Controllo dei valori più corti");
            System.out.println(VUOTA);
            for (Map.Entry<String, String> entry : mappa.entrySet()) {
                singolare = entry.getKey();
                pluraliGrezzi = entry.getValue();

                pluraleMaschile = service.estraeMaschile(pluraliGrezzi);
                pluraleFemminile = service.estraeFemminile(pluraliGrezzi);

                if (text.isValid(singolare) && singolare.length() < minTest) {
                    System.out.println("Singolare " + "(" + singolare.length() + ") " + singolare + " è più corto di " + minTest);
                }
                if (text.isValid(pluraleMaschile) && pluraleMaschile.length() < minTest) {
                    System.out.println("Maschi " + "(" + pluraleMaschile.length() + ") " + pluraleMaschile + " è più corto di " + minTest);
                }
                if (text.isValid(pluraleFemminile) && pluraleFemminile.length() < minTest) {
                    System.out.println("Femmine " + "(" + pluraleFemminile.length() + ") " + pluraleFemminile + " è più corto di " + minTest);
                }

                if (text.isValid(singolare) && singolare.length() < minMongo) {
                    System.out.println("ERRORE - Singolare " + "(" + singolare.length() + ") " + singolare + " è più corto di " + minMongo);
                }
                if (text.isValid(pluraleMaschile) && pluraleMaschile.length() < minMongo) {
                    System.out.println("ERRORE - Maschi " + "(" + pluraleMaschile.length() + ") " + pluraleMaschile + " è più corto di " + minMongo);
                }
                if (text.isValid(pluraleFemminile) && pluraleFemminile.length() < minMongo) {
                    System.out.println("ERRORE - Femmine " + "(" + pluraleFemminile.length() + ") " + pluraleFemminile + " è più corto di " + minMongo);
                }
            }
        }

    }

    private void print(int ciclo, String genere, String singolare, String plurale) {
        System.out.println(ciclo + " (" + genere + ") " + singolare + " -> " + plurale);
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