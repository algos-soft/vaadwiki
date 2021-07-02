package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.wiki.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 10-mag-2021
 * Time: 14:07
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("WikiBotServiceTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiBotServiceTest extends ATest {

    private static final String CAT_INESISTENTE = "Nati nel 3435";

    private static final String CAT_1435 = "Nati nel 1435";

    private static final String CAT_1591 = "Nati nel 1591";

    private static final String CAT_1935 = "Nati nel 1935";

    private static final String CAT_1713 = "Nati nel 1713";

    private static final String CAT_ROMANI = "Personaggi della storia romana";

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    AWikiBotService service;


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
        service.array = array;
        service.web = web;
        service.wikiApi = wikiApi;
        wikiApi.array = array;
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
    @DisplayName("1 - legge (come user) un template")
    public void leggePage() {
        sorgente = "Guido Rossi";
        previsto = "{{Bio";
        WrapPage wrap;

        wrap = service.leggePage(sorgente);
        assertEquals(AETypePage.testoConTmpl, wrap.getType());
        assertTrue(wrap.getTmpl().startsWith(previsto));

        System.out.println("1 - Legge il template Bio della pagina.");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Legge (come user) una SINGOLA pagina dal server wiki");
        System.out.println("La pagina viene richiesta dal TITLE");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        System.out.println("Sorgente restituito in formato visibile/leggibile");

        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        super.printWrap(wrap);
    }

    @Test
    @Order(2)
    @DisplayName("2 - legge una serie di wrapper di dati con una API action=query di Mediawiki")
    public void leggePages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<WrapPage> wrapLista;
        previstoIntero = 5;

        wrapLista = service.leggePages(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("2 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Legge (come user) una SERIE di pagine dal server wiki");
        System.out.println("Le pagine vengono richiesta dal PAGEIDs");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (WrapPage wrap : wrapLista) {
            this.printWrap(wrap);
        }
    }

    @Test
    @Order(3)
    @DisplayName("3 - Recupera (come user) 'lastModifica' di una serie di pageid")
    public void fixPages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<MiniWrap> wrapLista;
        previstoIntero = 5;

        wrapLista = service.fixPages(null);
        assertNull(wrapLista);

        wrapLista = service.fixPages(VUOTA);
        assertNull(wrapLista);

        wrapLista = service.fixPages(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("3 - Recupera (come user) 'lastModifica' di una serie di pageid");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest  pageid e timestamp");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (MiniWrap wrap : wrapLista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }
    }


    @Test
    @Order(4)
    @DisplayName("4 - Recupera (come user) 'lastModifica' di una categoria")
    public void fixPages2() {
        List<MiniWrap> wrapLista;

        sorgente = CAT_1435;
        previstoIntero = 33;
        sorgente2 = wikiApi.getPageidsCat(sorgente);
        assertTrue(text.isValid(sorgente2));

        wrapLista = service.fixPages(sorgente2);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("4 - Recupera (come user) 'lastModifica' di una categoria");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest 'pageid' e 'timestamp'");
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));

        System.out.println(VUOTA);
        System.out.println(String.format("La categoria è: %s", sorgente));
        System.out.println(String.format("Le pagine wiki recuperate sono: %s", wrapLista.size()));
        for (MiniWrap wrap : wrapLista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }

        sorgente = CAT_1435;
        previstoIntero = 33;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));


        sorgente = CAT_1591;
        previstoIntero = 67;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));


        sorgente = CAT_1713;
        previstoIntero = 104;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));


        sorgente = CAT_1935;
        previstoIntero = 1987;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));
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